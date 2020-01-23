package com.project.twitter.Repository;

import com.project.twitter.Model.Comments;
import com.project.twitter.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comments,Long> {
    Optional<List<Comments>> getCommentsByPostCommentsIdOrderByIdDesc(@Param("post_id") Long post_id);
void deleteCommentsByPostComments(@Param("post_comments") Post post);


}
//getCommentsByPostIdOrderByIdDesc