package com.project.twitter.Service;

import com.project.twitter.Model.Comments;
import com.project.twitter.Model.Post;
import com.project.twitter.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class PostService {

   private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {

        this.postRepository = postRepository;
    }

    public void addPost(Post post) {
        postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByUserId(Long user_id) {

        return postRepository.getPostsByUserIdOrderByIdDesc(user_id).orElse(new LinkedList<Post>());
    }

    public Post getOne(long id){
        return postRepository.getOne(id);
    }

    public void delete(Post post) {

        postRepository.delete(post);
    }






}
