package com.itdom.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParser;
import com.itdom.dao.CommentRepository;
import com.itdom.po.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;


    public void testAdd(){
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString().replace("-",""));
        comment.setArticleid("10005");
        comment.setContent("测试内容，可以忽略");
        comment.setLikenum(8848);
        comment.setNickname("张三");
        Comment ret = commentRepository.insert(comment);
        System.out.println(JSON.toJSONString(ret));
    }

    public void testDelete(String id){
        if (commentRepository.existsById(id)){
            commentRepository.deleteById(id);
            System.out.println("删除成功");
            return;
        }
        System.out.println("删除失败");
        return;
    }


    public void testUpdate(String articleid) {
        Comment comment = commentRepository.findCommentByArticleid(articleid);
        comment.setState("1");
        Comment ret = commentRepository.save(comment);
        System.out.println(JSON.toJSONString(ret));


    }

    public void testSearchComment(Integer page,Integer pageSize) {
        Page<Comment> comments = commentRepository.findAll(PageRequest.of(page - 1, pageSize));
        System.out.println(JSON.toJSONString(comments));
    }
}
