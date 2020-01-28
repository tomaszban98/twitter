package com.project.twitter.Controllers;

import com.project.twitter.Model.Post;
import com.project.twitter.Model.User;
import com.project.twitter.Service.PostService;
import com.project.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

@Controller
public class IndexController {
    private static final String LOCATION = "src/main/resources/static/img";
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

    @PostMapping("/addImg")
    public String postAddCampaign(@RequestPart(name = "image") MultipartFile file,HttpSession session) {

        User user = userService.getOne(Long.parseLong(session.getAttribute("id").toString()));

        String imageName = setRandomFileName(file.getOriginalFilename());

        upload(file, imageName);
       user.setImage(imageName);
userService.saveUser(user);

        return "redirect:/index";
    }

    private String setRandomFileName(String spl) {
        char[] str = new char[100];

        for (int i = 0; i < 7; i++) {
            str[i] = (char) (((int) (Math.random() * 26)) + (int) 'a');
        }


        String[] splitArray = spl.split("\\.");

        String text = new String(str, 0, 7);
        return text + '.' + splitArray[1].toLowerCase();
    }

    public void upload(MultipartFile file, String fileName) {

        if (!new File(LOCATION).exists()) {
            new File(LOCATION).mkdir();
        }
        Path root = Paths.get(LOCATION);
        try {
            if (fileName.contains("..")) {
                throw new Exception();
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
