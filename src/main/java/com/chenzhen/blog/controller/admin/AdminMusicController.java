package com.chenzhen.blog.controller.admin;

import com.chenzhen.blog.entity.pojo.Music;
import com.chenzhen.blog.entity.query.BaseQuery;
import com.chenzhen.blog.service.MusicService;
import com.chenzhen.blog.util.R;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
@CrossOrigin
@RequestMapping("/admin/musics")
public class AdminMusicController {

    @Value("${upload.directory}")
    private String UPLOAD_DIR;

    @Autowired
    private MusicService musicService;

    @GetMapping("")
    public String music(){
        return "admin/musics";
    }

    @ResponseBody
    @GetMapping("/page")
    public R page(BaseQuery query){
        return R.success().data("page",musicService.pageMusic(query));
    }

    @ResponseBody
    @PostMapping()
    public R save(@RequestBody @Valid Music music) {
        return musicService.save(music) ? R.success() : R.error("新增失败");
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        Music music = musicService.getById(id);
        //删除音乐
        File file = new File(UPLOAD_DIR + music.getFileName());
        if (file.exists()){
            file.delete();
        }

        return musicService.removeById(id) ? R.success() : R.error("删除失败");
    }

    @PostMapping("/upload")
    @ResponseBody
    public R upload(MultipartFile file) throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        //创建一个目录对象
        File dir = new File(UPLOAD_DIR);
        //判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在，需要创建
            dir.mkdirs();
        }

        //获取原始文件名
        String filename = file.getOriginalFilename();
        File filepath = new File(UPLOAD_DIR + filename);
        try {
            //将临时文件转存到指定位置
            file.transferTo(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //读取音乐
        AudioFile audioFile = AudioFileIO.read(filepath);
        Tag tag = audioFile.getTag();
        //获取歌手名
        String artist = tag.getFirst(FieldKey.ARTIST);
        //获取歌名
        String title = tag.getFirst(FieldKey.TITLE);

        Music music = new Music();
        music.setFileName(filename);
        music.setFilePath("/music/download/" + filename);
        music.setCoverPath("/music/cover/" + filename);
        music.setArtist(artist);
        music.setTitle(title);

        return musicService.save(music) ? R.success() : R.error("新增失败");
    }


}
