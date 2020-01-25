package com.project.twitter.Service;

import com.project.twitter.Model.Comments;
import com.project.twitter.Model.Post;
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


    public Comments getOne(Long id) {
        return commentsRepository.getOne(id);
    }

    public void deleteComments(List<Comments> comments) {
        commentsRepository.deleteInBatch(comments);
    }


    public void  del(Post post){
        commentsRepository.deleteCommentsByPostComments(post);
    }

    public List<Comments> getAll(){
        return commentsRepository.findAll();
    }

}
