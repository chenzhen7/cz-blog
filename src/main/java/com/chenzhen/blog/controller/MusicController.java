package com.chenzhen.blog.controller;

import com.chenzhen.blog.entity.annotation.BlogLog;
import com.chenzhen.blog.factories.CoverStrategyFactory;
import com.chenzhen.blog.strategies.CoverStrategy;
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

    @Value("${upload.directory.music}")
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
        // 获取文件扩展名
        String fileExtension = filename.substring(filename.lastIndexOf(".") + 1);
        // 获取封面下载策略
        CoverStrategy coverStrategy = CoverStrategyFactory.getCoverStrategy(fileExtension);
        // 调用策略
        coverStrategy.download(UPLOAD_DIR + filename,response);
    }
}
