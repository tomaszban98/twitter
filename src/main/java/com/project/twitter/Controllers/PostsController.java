package com.project.twitter.Controllers;

import com.project.twitter.Model.Comments;
import com.project.twitter.Model.Post;
import com.project.twitter.Model.User;
import com.project.twitter.Service.CommentsService;
import com.project.twitter.Service.PostService;
import com.project.twitter.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PostsController {

    @Autowired
    private UserService userService;


    @Autowired
    private PostService postService;


    @Autowired
    private CommentsService commentsService;


    @GetMapping("/editPost/{id}")
    public String getEditPost(@PathVariable("id") Long id, HttpSession session) {

        Map<String, Object> model = new HashMap<String, Object>();


        User user = userService.getOne((long) session.getAttribute("id"));
        Post post = postService.getOne(id);
        model.put("loggedUser", user);
        model.put("post", post);

        if (post.getUser().getId() != Long.parseLong(session.getAttribute("id").toString())) {

            return "redirect:/comment/" + id;
        }
        session.setAttribute("loggedUser", user);
        session.setAttribute("post", post);
        return "editPost";
    }


    @PostMapping("/editPost")
    public String editPost(String title, String content, String pId, HttpSession session) {
        Post post = postService.getOne(Long.parseLong(pId));
        if (post.getUser().getId() != Long.parseLong(session.getAttribute("id").toString())) {
            return "redirect:/comment/" + pId;
        }
        post.setTitle(title);
        post.setContent(content);
        postService.addPost(post);
        session.removeAttribute("loggedUser");
        session.removeAttribute("post");
        return "redirect:/comment/" + pId;
    }


    @GetMapping("/editComment/{id}")
    public String getEditComment(@PathVariable("id") Long id, HttpSession session) {
        Comments comment = commentsService.getOne(id);

        if (comment.getUserComments().getId() != Long.parseLong(session.getAttribute("id").toString())) {

            // return new ModelAndView("redirect: comment"+comment.getPostComments().getId());
            return "redirect:/comment/" + comment.getPostComments().getId();
        }
        session.setAttribute("comment", comment);
        return "editComment";
    }


    @PostMapping("/editComment")
    public String editComment(String content, String id, HttpSession session) {
        Comments comment = commentsService.getOne(Long.parseLong(id));

        if (comment.getUserComments().getId() != Long.parseLong(session.getAttribute("id").toString())) {
            session.removeAttribute("comment");
            return "redirect:/comment/" + comment.getPostComments().getId();

        }
        comment.setComment(content);
        commentsService.addComment(comment);
        session.removeAttribute("comment");
        return "redirect:/comment/" + comment.getPostComments().getId();
    }

    @GetMapping("/deleteComment/{id}")
    public String getDeleteComment(@PathVariable("id") Long id, HttpSession session) {
        Comments comment = commentsService.getOne(id);

        if (comment.getUserComments().getId() != Long.parseLong(session.getAttribute("id").toString())) {


            return "redirect:/comment/" + comment.getPostComments().getId();
        }

        commentsService.deleteComment(comment);
        return "redirect:/comment/" + comment.getPostComments().getId();
    }


    @GetMapping("/deletePost/{id}")
    public String getDeletePost(@PathVariable("id") Long id, HttpSession session) {
        Post post = postService.getOne(id);
List<Comments> comments=commentsService.getCommentsByPostCommentsIdOrderByIdDesc(id);
        if (post.getUser().getId() != Long.parseLong(session.getAttribute("id").toString())) {


            return "redirect:/comment/" + post.getId();
        }

      commentsService.deleteComments(comments);

        postService.delete(post);
        return "redirect:/index";
    }


}
