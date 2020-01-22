package com.project.twitter.Service;

import com.project.twitter.Model.Comments;
import com.project.twitter.Repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CommentsService {

    private CommentsRepository commentsRepository;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }


    public void addComment(Comments comment){
        commentsRepository.save(comment);
    }


    public List<Comments> getCommentsByPostCommentsIdOrderByIdDesc(Long postId){
        return commentsRepository.getCommentsByPostCommentsIdOrderByIdDesc(postId).orElse(new LinkedList<>());
    }


    public void deleteComment(Comments comment){
        commentsRepository.delete(comment);
    }



}
