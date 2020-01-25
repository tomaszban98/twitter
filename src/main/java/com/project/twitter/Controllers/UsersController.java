package com.project.twitter.Controllers;

import com.project.twitter.Model.Comments;
import com.project.twitter.Model.Post;
import com.project.twitter.Model.User;
import com.project.twitter.Service.CommentsService;
import com.project.twitter.Service.PostService;
import com.project.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UsersController {

    @Autowired
    private UserService userService;


    @Autowired
    private PostService postService;


    @Autowired
    private CommentsService commentsService;
    @Autowired
    private IndexController indexController;


    @GetMapping("/users")
    public ModelAndView getAllUsers(HttpSession session) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<User> users = userService.getAll();
        User user = userService.getOne(Long.parseLong(session.getAttribute("id").toString()));
        model.put("loggedUser", user);
        model.put("users", users);
        return new ModelAndView("users", "model", model);
    }

    @PostMapping("/register")
    public String addUser(@RequestParam String username, String name, String surname, String password) {
        if (userService.existsByLogin(username)) {
            return "redirect:/login?error=exists";
        }
        User user = new User();
        user.setLogin(username);
        user.setName(name);
        user.setSurname(surname);
        user.setPassword(password);
        user.setRole("ROLE_USER");
        user.setDescription("");

        userService.saveUser(user);

        return "redirect:/index";
    }


    @PostMapping("/addPost")
    public String addPost(@RequestParam long userId, String title, String content) {
        Date date = new Date();

        User user = userService.getOne(userId);
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        post.setAddTime(date);
        postService.addPost(post);
        return "redirect:/index";
    }

/*
    @PostMapping("/userDetails")
    public String userDetails(@ModelAttribute User user) {

        return "indexOld";
    }*/


    @GetMapping("/users/{id}")
    public ModelAndView getUserProfile(@PathVariable("id") Long id, HttpSession session) {
        Map<String, Object> model = new HashMap<String, Object>();
        List<Post> post;
        User user;
        User user2;
        if (id == session.getAttribute("id") || id == null) {
            post = postService.getPostsByUserId(Long.parseLong(session.getAttribute("id").toString()));
            user = userService.getOne(Long.parseLong(session.getAttribute("id").toString()));

            model.put("newUsers", indexController.getNewUsers());
            model.put("user", user);
            model.put("loggedUser", user);
            model.put("posts", post);

            return new ModelAndView("index", "model", model);
        }
        user2 = userService.getOne(Long.parseLong(session.getAttribute("id").toString()));

        post = postService.getPostsByUserId(id);
        user = userService.getOne(id);
        model.put("newUsers", indexController.getNewUsers());
        model.put("user", user);
        model.put("loggedUser", user2);
        model.put("posts", post);

        return new ModelAndView("userProfile", "model", model);

    }


    @GetMapping("/comment/{id}")
    public ModelAndView addcomment(@PathVariable("id") Long pId, HttpSession session) {
        Map<String, Object> model = new HashMap<String, Object>();


        User user = userService.getOne((long) session.getAttribute("id"));
        Post post = postService.getOne(pId);
        List<Comments> comments = commentsService.getCommentsByPostCommentsIdOrderByIdDesc(pId);
        model.put("loggedUser", user);
        model.put("post", post);
        model.put("comments", comments);


        return new ModelAndView("comment", "model", model);

    }

    @PostMapping("/comment")
    public String addcomments(@RequestParam String pId, String text, HttpSession session) {
        Comments comments = new Comments();
        comments.setPostComments(postService.getOne(Long.parseLong(pId)));
        comments.setUserComments(userService.getOne(Long.parseLong(session.getAttribute("id").toString())));
        comments.setComment(text);
        comments.setTime(new Date());
        commentsService.addComment(comments);

        return "redirect:/comment/" + pId;

    }


    @GetMapping("/settings")
    public ModelAndView settings(HttpSession session) {
        User user = userService.getOne((long) session.getAttribute("id"));

        return new ModelAndView("settings", "loggedUser", user);
    }


    @PostMapping("/addDesc")
    public String addDesc(@RequestParam String desc, HttpSession session) {
        User user = userService.getOne(Long.parseLong(session.getAttribute("id").toString()));
        user.setDescription(desc);
        userService.saveUser(user);
        userService.saveUser(user);
        return "redirect:/index";
    }


}
