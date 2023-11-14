package com.ityu.common.service.system;


import com.ityu.common.bean.constant.cache.Cache;
import com.ityu.common.bean.constant.cache.CacheKey;
import com.ityu.common.bean.entity.system.FileInfo;
import com.ityu.common.cache.ConfigCache;
import com.ityu.common.cache.TokenCache;
import com.ityu.common.dao.system.FileInfoRepository;
import com.ityu.common.enumeration.ConfigKeyEnum;
import com.ityu.common.service.BaseService;
import com.ityu.common.utils.HttpUtil;
import com.ityu.common.utils.JwtTokenUtil;
import com.ityu.common.utils.XlsUtils;
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
public class FileService extends BaseService<FileInfo, Long, FileInfoRepository> {
    @Autowired
    private ConfigCache configCache;
    @Autowired
    private FileInfoRepository fileInfoRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private TokenCache tokenCache;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     */
    public FileInfo upload(MultipartFile multipartFile) {
        String uuid = UUID.randomUUID().toString();
        String realFileName = uuid + "." + multipartFile.getOriginalFilename().split("\\.")[1];
        try {

            File file = new File(configCache.get(ConfigKeyEnum.SYSTEM_FILE_UPLOAD_PATH) + File.separator + realFileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            multipartFile.transferTo(file);
            return save(multipartFile.getOriginalFilename(), file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据模板创建excel文件
     *
     * @param template excel模板
     * @param fileName 导出的文件名称
     * @param data     excel中填充的数据
     * @return
     */
    public FileInfo createExcel(String template, String fileName, Map<String, Object> data) {
        FileOutputStream outputStream = null;
        File file = new File(configCache.get(ConfigKeyEnum.SYSTEM_FILE_UPLOAD_PATH) + File.separator + UUID.randomUUID().toString() + ".xlsx");
        try {

            // 定义输出类型
            outputStream = new FileOutputStream(file);

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
        return save(fileName, file);
    }

    /**
     * 创建文件
     *
     * @param originalFileName
     * @param file
     * @return
     */
    public FileInfo save(String originalFileName, File file) {
        try {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setCreateBy(HttpUtil.getUserId(jwtTokenUtil));
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
    public FileInfo get(Long id) {
        FileInfo fileInfo = fileInfoRepository.getOne(id);
        fileInfo.setAblatePath(configCache.get(ConfigKeyEnum.SYSTEM_FILE_UPLOAD_PATH) + File.separator + fileInfo.getRealFileName());
        return fileInfo;
    }
}
