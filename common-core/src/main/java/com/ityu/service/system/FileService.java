package com.ityu.service.system;


import com.ityu.bean.constant.CfgKey;
import com.ityu.bean.constant.cache.Cache;
import com.ityu.bean.constant.cache.CacheKey;

import com.ityu.bean.entity.system.FileInfo;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.bean.vo.shop.Base64File;
import com.ityu.cache.ConfigCache;

import com.ityu.dao.system.FileInfoRepository;
import com.ityu.security.UserService;
import com.ityu.service.base.BaseService;
import com.ityu.utils.XlsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class FileService extends BaseService<FileInfo,Long, FileInfoRepository> {
    @Autowired
    private ConfigCache configCache;
    @Autowired
    private FileInfoRepository fileInfoRepository;
    /**
     * 文件上传
     * @param multipartFile
     * @return
     */
    public FileInfo upload(MultipartFile multipartFile){
        String uuid = UUID.randomUUID().toString();
        String realFileName =   uuid +"."+ multipartFile.getOriginalFilename().split("\\.")[1];
        try {

            File file = new File(configCache.get(CfgKey.SYSTEM_FILE_UPLOAD_PATH)+ File.separator+realFileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            multipartFile.transferTo(file);
            return save(multipartFile.getOriginalFilename(),file);
        } catch (Exception e) {
            e.printStackTrace();
             return null;
        }
    }

    /**
     * 根据模板创建excel文件
     * @param template excel模板
     * @param fileName 导出的文件名称
     * @param data  excel中填充的数据
     * @return
     */
    public FileInfo createExcel(String template, String fileName, Map<String, Object> data){
        FileOutputStream outputStream = null;
        File file = new File(configCache.get(CfgKey.SYSTEM_FILE_UPLOAD_PATH) + File.separator+UUID.randomUUID().toString()+".xlsx");
        try {

            // 定义输出类型
            outputStream =new FileOutputStream(file);

            JxlsHelper jxlsHelper = JxlsHelper.getInstance();
            String templateFile = getClass().getClassLoader().getResource(template).getPath();
            InputStream is = new BufferedInputStream(new FileInputStream(templateFile));

            Transformer transformer = jxlsHelper.createTransformer(is, outputStream);
            Context context = new Context();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                context.putVar(entry.getKey(), entry.getValue());
            }

            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> funcs = new HashMap<String, Object>(4);
            funcs.put("utils", new XlsUtils());
            JexlEngine customJexlEngine = new JexlBuilder().namespaces(funcs).create();
            evaluator.setJexlEngine(customJexlEngine);
            jxlsHelper.processTemplate(context, transformer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
            e.printStackTrace();
            }

        }
        return save(fileName,file);
    }
    /**
     * 创建文件
     * @param originalFileName
     * @param file
     * @return
     */
    public FileInfo save(String originalFileName,File file){
        try {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setCreateBy(UserService.me().getTokenFromRequest().getId()+"");
            fileInfo.setOriginalFileName(originalFileName);
            fileInfo.setRealFileName(file.getName());
            insert(fileInfo);
            return fileInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Cacheable(value = Cache.APPLICATION, key = "'" + CacheKey.FILE_INFO + "'+#id")
    public FileInfo get(Long id){
        FileInfo fileInfo = fileInfoRepository.getOne(id);
        fileInfo.setAblatePath(configCache.get(CfgKey.SYSTEM_FILE_UPLOAD_PATH) + File.separator+fileInfo.getRealFileName());
        return fileInfo;
    }

    public FileInfo getByName(String fileName) {
        FileInfo fileInfo =  get(SearchFilter.build("realFileName",fileName));
        fileInfo.setAblatePath(configCache.get(CfgKey.SYSTEM_FILE_UPLOAD_PATH) + File.separator+fileInfo.getRealFileName());
        return fileInfo;
    }

    /**
     * 文件上传
     * @param base64File
     * @return
     */
    public FileInfo upload(Base64File base64File){
        String uuid = UUID.randomUUID().toString();
        String originalFileName = base64File.getName();
        String realFileName =   uuid +"."+ originalFileName.split("\\.")[originalFileName.split("\\.").length-1];
        try {
            File file = new File(configCache.get(CfgKey.SYSTEM_FILE_UPLOAD_PATH) + File.separator+realFileName);
            if(base64ToFile(base64File.getBase64(),file)){
                return save(originalFileName,file);
            }

        } catch (Exception e) {
            log.error("保存文件异常",e);
        }
        return null;
    }

    private boolean base64ToFile(String base64, File file) {
        base64 = base64.substring(base64.indexOf(",")+1);
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        try {

            byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(base64);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            return true;
        } catch (Exception e) {
            log.error("base64转视频文件失败", e);
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error("关闭文件流bos失败", e);
                    return false;
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("关闭文件流fos失败", e);
                    return false;
                }
            }
        }
    }
}
