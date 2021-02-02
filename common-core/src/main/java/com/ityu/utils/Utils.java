package com.ityu.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    /**
     * 得到网页中图片的地址
     */
    public static List<String> getImgStr(String htmlStr) {
        List<String> pics = new ArrayList<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }


    /**
     *
     * @param file 文件
     * @param path 文件存放路径
     * @param fileName 源文件名
     * @return
     */
    public static boolean upload(MultipartFile file, String path, String fileName){

        //使用原文件名
        String realPath = path + "/" +fileName;

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return true;
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }
    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName){
        return getUUID() + getSuffix(fileOriginName);
    }

    /**
     *
     * @param url
     * @param type
     * 0是图片
     * 1是网页
     * @return
     */
    public static String getUrl(String url, int type){
        if(StringUtils.isEmpty(url)){
            return url;
        }
        if(url.startsWith("http")){
            return  url;
        }
        switch (type) {
            case 0:
                //url = Constants.PICBASEURL+url;
                break;
            case 1:
                //url = Constants.HTMLBASEURL+url;
                break;
        }
        return url;
    }

    public static String getImageFromContent(String content) {
        List<String> imgStr = Utils.getImgStr(content);
        if(imgStr.size()>0){
            return imgStr.get(0);
        }else{
            return "";
        }
    }

}
