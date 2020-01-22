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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    /*  @RequestMapping("/")
      public String index(Authentication authentication){

          return authentication.getName();
      }*/
    @RequestMapping(value = {"/", "/index"})
    public ModelAndView index(HttpSession session, Authentication authentication) {
        Map<String, Object> model = new HashMap<String, Object>();


        String login = authentication.getName();
        User user;
        user = userService.getUserByLogin(login);

        if (session.getAttribute("id") != null && user.getName().equals(userService.getOne((long) session.getAttribute("id")).getName())) {
            List<Post> post = postService.getPostsByUserId(Long.parseLong(session.getAttribute("id").toString()));
            user = userService.getOne((long) session.getAttribute("id"));

            model.put("loggedUser", user);
            model.put("user", user);
            model.put("posts", post);

            return new ModelAndView("index", "model", model);
        } else {
            user = userService.getUserByLogin(login);
            List<Post> post = postService.getPostsByUserId(user.getId());
            session.setAttribute("id", user.getId());
            model.put("loggedUser", user);
            model.put("user", user);
            model.put("posts", post);

            return new ModelAndView("index", "model", model);
        }


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


}
