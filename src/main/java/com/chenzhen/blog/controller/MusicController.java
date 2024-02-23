package com.chenzhen.blog.controller;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

@Controller
@RequestMapping("/music")
public class MusicController {

    @Value("${upload.directory}")
    private String UPLOAD_DIR;

    /**
     * 文件下载
     * @param filename
     * @param response
     */
    @ResponseBody
    @GetMapping("/download/{filename}")
    public void download(@PathVariable String filename, HttpServletResponse response){
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(UPLOAD_DIR + filename);
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            //音乐
            response.setContentType("audio/mpeg");

            int len;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 封面下载
     */
    @ResponseBody
    @GetMapping("/cover/{filename}")
    public void cover(@PathVariable String filename, HttpServletResponse response){
        try {
            //读取音乐
            AudioFile audioFile = AudioFileIO.read(new File(UPLOAD_DIR + filename));
            Tag tag = audioFile.getTag();
            //封面
            Artwork artwork = tag.getFirstArtwork();
            if (artwork == null) {
                return;
            }
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            // 设置响应类型为图像类型
            response.setContentType(artwork.getMimeType());
            // 将封面图像写入响应输出流
            outputStream.write(artwork.getBinaryData());
            //关闭资源
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
