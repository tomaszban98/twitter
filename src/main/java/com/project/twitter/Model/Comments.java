package com.project.twitter.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date time;

    @Column(length = 500)
    private String comment;


    @ManyToOne
    private User userComments;


    @ManyToOne
    private Post postComments;


    public Long getId() {
        return id;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUserComments() {
        return userComments;
    }

    public void setUserComments(User userComments) {
        this.userComments = userComments;
    }

    public Post getPostComments() {
        return postComments;
    }

    public void setPostComments(Post postComments) {
        this.postComments = postComments;
    }
}
