package com.project.twitter.Controllers;

import com.project.twitter.Model.Post;
import com.project.twitter.Model.User;
import com.project.twitter.Service.PostService;
import com.project.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @RequestMapping(value = {"/", "/index"})
    public ModelAndView index(HttpSession session, Authentication authentication) {

        Map<String, Object> model = new HashMap<String, Object>();
        String login = authentication.getName();
        User user = userService.getUserByLogin(login);
        List<Post> posts;
        Date date = new Date();

        posts = postService.getPostsByUserId(user.getId());
        session.setAttribute("id", user.getId());

        if (user.getBlockingDate() != null) {
           int a = user.getBlockingDate().compareTo(date);
            if (a > 0) {
                session.invalidate();
                return new ModelAndView("redirect:/logout", "user", user);
            }
        }

        model.put("newUsers", getNewUsers());
        model.put("user", user);
        model.put("posts", posts);
        return new ModelAndView("index", "model", model);
    }


    @GetMapping("/login")
    public String getLoginView() {

        return "login";
    }

    @GetMapping("/logout")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "login";
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

    @GetMapping("/message")
    public String message() {

        return "indexOld";

    }


}
