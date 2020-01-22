package com.project.twitter.Controllers;

import com.project.twitter.Model.Post;
import com.project.twitter.Model.User;
import com.project.twitter.Service.CommentsService;
import com.project.twitter.Service.PostService;
import com.project.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class PostsController {

    @Autowired
    private UserService userService;


    @Autowired
    private PostService postService;


    @Autowired
    private CommentsService commentsService;

    @GetMapping("/editPost/{id}")
    public String getAddUsersView(@PathVariable("id") Long id, HttpSession session) {
            Post post=postService.getOne(id);
            if(post.getUser().getId()!=Long.parseLong(session.getAttribute("id").toString()))
            {
                return "index";
            }


        return "editPost";
    }
}
