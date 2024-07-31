package com.chenzhen.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.pojo.Message;
import com.chenzhen.blog.entity.vo.MessageVO;
import com.chenzhen.blog.service.MessageService;
import com.chenzhen.blog.mapper.MessageMapper;
import com.chenzhen.blog.util.MailUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author MIS
* @description 针对表【t_message】的数据库操作Service实现
* @createDate 2022-09-11 18:21:11
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private MessageMapper messageMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public PageInfo<MessageVO> getMessageList(Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        PageHelper.orderBy("create_time desc");
        List<MessageVO> messageList = messageMapper.getMessageList();//获取根评论列表

        PageInfo<MessageVO> pageInfo = new PageInfo<>(messageList, 5);

        return pageInfo;
    }

    @Override
    public List<Message> getRecentMessage() {
        return messageMapper.getRecentMessage();
    }


}




