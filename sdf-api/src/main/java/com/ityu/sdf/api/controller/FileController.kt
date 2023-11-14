package com.ityu.sdf.api.controller

import com.ityu.common.bean.entity.system.FileInfo
import com.ityu.common.bean.vo.front.Rets.failure
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.service.system.FileService
import com.ityu.common.utils.*
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.net.http.HttpRequest


@Api(tags = ["file"])
@Tag(name = "file", description = "文件操作")
@RestController
@RequestMapping("/file")
open class FileController {
    @Autowired
    private val fileService: FileService? = null


    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     */
    @PostMapping
    fun upload(@RequestPart("file") multipartFile: MultipartFile?): Any {
        return try {
            val fileInfo = fileService!!.upload(multipartFile)
            success(fileInfo)
        } catch (e: Exception) {
            logger.error("上传文件异常", e)
            failure("上传文件失败", null)
        }
    }

    /**
     * 上传文件
     *
     * @param files
     * @return
     */
    @PostMapping(value = ["uploadFiles"])
    fun uploadFiles(@RequestPart("files") multipartFiles: List<MultipartFile>?): Any {
        val pics = mutableListOf<FileInfo>()
        return try {
            multipartFiles?.forEach {
                val fileInfo = fileService!!.upload(it)
                pics.add(fileInfo);
            }
            success(pics)
        } catch (e: Exception) {
            logger.error("上传文件异常", e)
            failure("上传文件失败", null)
        }
    }

    /**
     * 下载文件
     *
     * @param idFile
     * @param fileName
     */
    @GetMapping(value = ["download"])
    fun download(
        @RequestParam("idFile") idFile: Long?,
        @RequestParam(value = "fileName", required = false) fileName: String?
    ) {
        var fileName = fileName
        val fileInfo = fileService!![idFile]
        fileName = if (StringUtil.isEmpty(fileName)) fileInfo.originalFileName else fileName
        val response = HttpUtil.getResponse()
        response.contentType = "application/x-download"
        try {
            fileName = String(fileName!!.toByteArray(), charset("ISO-8859-1"))
            response.setHeader("Content-Disposition", "attachment;filename=$fileName")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val length = 1024
        var buffer = ByteArray(length)
        var fis: FileInputStream? = null
        var bis: BufferedInputStream? = null
        var os: OutputStream? = null
        try {
            val file = File(fileInfo.ablatePath)
            os = response.outputStream
            fis = FileInputStream(file)
            bis = BufferedInputStream(fis)
            var i = bis.read(buffer)
            while (i != -1) {
                os.write(buffer, 0, i)
                buffer = ByteArray(length)
                i = bis.read(buffer)
            }
        } catch (e: Exception) {
            logger.error("download error", e)
        } finally {
            try {
                bis!!.close()
                fis!!.close()
            } catch (e: IOException) {
                logger.error("close inputstream error", e)
            }
        }
    }

    /**
     * 获取base64图片数据
     *
     * @param idFile
     * @return
     */
    @GetMapping(value = ["getImgBase64"])
    fun getImgBase64(@RequestParam("idFile") idFile: Long?): Any {
        val fileInfo = fileService!![idFile]
        var fis: FileInputStream? = null
        return try {
            val file = File(fileInfo.ablatePath)
            val bytes = ByteArray(file.length().toInt())
            fis = FileInputStream(file)
            val bis = BufferedInputStream(fis)
            bis.read(bytes)
            val base64 = CryptUtil.encodeBASE64(bytes)
            success(Maps.newHashMap("imgContent", base64))
        } catch (e: Exception) {
            logger.error("get img error", e)
            failure("获取图片异常", null)
        } finally {
            try {
                fis!!.close()
            } catch (e: Exception) {
                logger.error("close getImgBase64 error", e)
            }
        }
    }

    /**
     * 获取文件流
     *
     * @param response
     * @param idFile
     */
    @GetMapping(value = ["getImgStream"])
    fun getImgStream(
        response: HttpServletResponse,
        @RequestParam("idFile") idFile: Long?
    ) {
        val fileInfo = fileService!![idFile]
        var fis: FileInputStream? = null
        val suffix = "." + fileInfo.realFileName!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1]
        val contentType = ContentType.get(suffix)
        response.contentType = contentType
        try {
            val out: OutputStream = response.outputStream
            val file = File(fileInfo.ablatePath)
            fis = FileInputStream(file)
            val b = ByteArray(fis.available())
            fis.read(b)
            out.write(b)
            out.flush()
        } catch (e: Exception) {
            logger.error("getImgStream error", e)
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    logger.error("close getImgStream error", e)
                }
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FileController::class.java)
    }
}
