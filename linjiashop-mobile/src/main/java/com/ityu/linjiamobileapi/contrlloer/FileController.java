package com.ityu.linjiamobileapi.contrlloer;


import com.ityu.bean.entity.shop.ShopUser;
import com.ityu.bean.entity.system.FileInfo;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.shop.Base64File;
import com.ityu.service.shop.ShopUserService;
import com.ityu.service.system.FileService;
import com.ityu.utils.CryptUtil;
import com.ityu.utils.HttpUtil;
import com.ityu.utils.Maps;
import com.ityu.utils.StringUtil;
import com.ityu.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/file")
public class FileController extends BaseController {
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileService fileService;
    @Autowired
    private ShopUserService shopUserService;

    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Object upload(@RequestPart("file") MultipartFile multipartFile) {

        try {
            FileInfo fileInfo = fileService.upload(multipartFile);
            return Rets.success(fileInfo);
        } catch (Exception e) {
            logger.error("上传文件异常", e);
            return Rets.failure("上传文件失败");
        }
    }

    @RequestMapping(value = "upload/base64", method = RequestMethod.POST)
    public Object uploadUploadFileBase64(@RequestBody Base64File base64File) {

        try {
            FileInfo fileInfo = fileService.upload(base64File);
            ShopUser user = shopUserService.getCurrentUser();
            user.setAvatar(String.valueOf(fileInfo.getRealFileName()));
            shopUserService.update(user);
            return Rets.success(user);
        } catch (Exception e) {
            logger.error("上传文件异常", e);
            return Rets.failure("上传文件失败");
        }
    }

    /**
     * 下载文件
     *
     * @param fileName
     * @param fileName
     */
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public void download(@RequestParam(value = "fileName", required = false) String fileName,
                         @RequestParam(value = "idFile", required = false) Long idFile) {
        FileInfo fileInfo = null;
        if (StringUtil.isNotEmpty(fileName)) {
            fileInfo = fileService.getByName(fileName);
        }
        if (idFile != null) {
            fileInfo = fileService.get(idFile);
        }
        downloadFile(fileInfo);
    }

    public void downloadFile(FileInfo fileInfo) {
        String fileName = fileInfo.getOriginalFileName();
        HttpServletResponse response = HttpUtil.getResponse();
        response.setContentType("application/x-download");
        try {
            fileName = new String(fileName.getBytes(), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int length = 1024;
        byte[] buffer = new byte[length];
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        OutputStream os = null;
        try {
            File file = new File(fileInfo.getAblatePath());
            os = response.getOutputStream();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer,0,i);
                buffer = new byte[length];
                i = bis.read(buffer);
            }

        } catch (Exception e) {
            logger.error("download error", e);
        } finally {
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                logger.error("close inputstream error", e);
            }
        }
    }


    /**
     * 获取base64图片数据
     *
     * @param fileName
     * @return
     */
    @RequestMapping(value = "getImgBase64", method = RequestMethod.GET)
    public Object getImgBase64(@RequestParam("idFile") String fileName) {

        FileInfo fileInfo = fileService.getByName(fileName);
        FileInputStream fis = null;
        try {
            File file = new File(fileInfo.getAblatePath());
            byte[] bytes = new byte[(int) file.length()];
            fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytes);
            String base64 = CryptUtil.encodeBASE64(bytes);
            return Rets.success(Maps.newHashMap("imgContent", base64));
        } catch (Exception e) {
            logger.error("get img error", e);
            return Rets.failure("获取图片异常");
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
                logger.error("close getImgBase64 error", e);
            }
        }

    }

    /**
     * 获取图片流
     *
     * @param response
     * @param fileName
     */
    @RequestMapping(value = "getImgStream", method = RequestMethod.GET)
    public void getImgStream(HttpServletResponse response,
                             @RequestParam("idFile") String fileName) {
        FileInfo fileInfo = fileService.getByName(fileName);
        FileInputStream fis = null;
        response.setContentType("image/" + fileInfo.getRealFileName().split("\\.")[1]);
        try {
            OutputStream out = response.getOutputStream();
            File file = new File(fileInfo.getAblatePath());
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            logger.error("getImgStream error", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("close getImgStream error", e);
                }
            }
        }
    }
}
