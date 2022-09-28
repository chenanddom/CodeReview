package com.itdom.dao;

import com.itdom.po.Comment;
//import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository /*extends MongoRepository<Comment,String>*/ {


    public Comment findCommentByArticleid(String articleid);

}
