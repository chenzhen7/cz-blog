package com.chenzhen.blog.strategies.Impl;

import cn.hutool.core.io.IoUtil;
import com.chenzhen.blog.factories.CoverStrategyFactory;
import com.chenzhen.blog.strategies.CoverStrategy;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;

/**
 * 基本的音频封面图片获取策略类
 */
@Service
public class BaseCoverStrategy implements CoverStrategy, InitializingBean {

    @Value("${default-cover-path}")
    private String DEFAULT_COVER_PATH;

    @Override
    public void download(String filepath, HttpServletResponse response)  {
        try{
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            //读取音乐
            AudioFile audioFile = AudioFileIO.read(new File(filepath));
            Tag tag = audioFile.getTag();
            //封面
            Artwork artwork = tag.getFirstArtwork();
            //如果获取不到封面则将本地路径classpath下的默认图片写入响应流
            if (artwork == null) {
                InputStream defaultImageStream = getClass().getResourceAsStream(DEFAULT_COVER_PATH);
                IoUtil.copy(defaultImageStream, outputStream);
            }
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

    @Override
    public void afterPropertiesSet() throws Exception {
        CoverStrategyFactory.register("mp3", this);
        CoverStrategyFactory.register("flac", this);
    }
}
