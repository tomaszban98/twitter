package com.project.twitter.Controllers;

import com.project.twitter.Model.Comments;
import com.project.twitter.Model.Post;
import com.project.twitter.Model.User;
import com.project.twitter.Service.CommentsService;
import com.project.twitter.Service.PostService;
import com.project.twitter.Service.UserService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.DateUtils;

import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentsService commentsService;


    @GetMapping("/admin")
    public ModelAndView getAdmin() {
        Map<String, Object> model = new HashMap<String, Object>();
        List<Post> postList=postService.getAllPosts();
        List<User> userList =userService.getAll();
        model.put("postList",postList);
        model.put("userList",userList);
        model.put("newUsers",getNewUsers());
        return new ModelAndView("admin/index","model",model);
    }


    @GetMapping("/admin/allPosts")
    public ModelAndView getAllPosts() {
        List<Post> postList = postService.getAllPosts();
        if (postList.isEmpty()) {
            postList = new LinkedList<>();
        }
        return new ModelAndView("admin/allPosts", "postList", postList);
    }

    @GetMapping("/admin/deletePost/{id}")
    public String getDeletePost(@PathVariable("id") Long id) {
        Post post = postService.getOne(id);
        postService.delete(post);
        return "redirect:/admin/allPosts";

    }

    @GetMapping("/admin/allComments")
    public ModelAndView getAllComments() {
        List<Comments> commentsList = commentsService.getAll();
        if (commentsList.isEmpty()) commentsList = new LinkedList<>();
        return new ModelAndView("admin/allComments", "commentsList", commentsList);
    }


    @GetMapping("/admin/deleteComment/{id}")
    public String getDeleteComment(@PathVariable("id") Long id) {
        Comments comments = commentsService.getOne(id);
        commentsService.deleteComment(comments);
        return "redirect:/admin/allComments";

    }


    @GetMapping("/admin/users")
    public ModelAndView getAllUsers() {
        List<User> userList = userService.getAll();
        if (userList.isEmpty()) userList = new LinkedList<>();
        return new ModelAndView("admin/users", "userList", userList);
    }

    @GetMapping("/admin/blockUser/{id}/{time}")
    public String getBlockUser(@PathVariable("id") Long id, @PathVariable("time") long time) {
        User user = userService.getOne(id);

        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        long curTimeInMs = System.currentTimeMillis();
        Date date = new Date(curTimeInMs + (time * ONE_MINUTE_IN_MILLIS));


        user.setBlockingDate(date);
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    List<User> getNewUsers() {
        List<User> list = userService.getAll();
        Collections.reverse(list);
        List<User> newUsers = new LinkedList<>();
        if (list.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                newUsers.add(list.get(i));
            }
            return newUsers;
        } else {
            return list;
        }
    }


}
