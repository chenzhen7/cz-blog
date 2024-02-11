package com.chenzhen.blog.service;

import com.chenzhen.blog.entity.pojo.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhen.blog.entity.vo.MessageVO;
import com.github.pagehelper.PageInfo;

/**
* @author MIS
* @description 针对表【t_message】的数据库操作Service
* @createDate 2022-09-11 18:21:11
*/
public interface MessageService extends IService<Message> {

    PageInfo<MessageVO> getMessageList(Integer pageNum);





}
