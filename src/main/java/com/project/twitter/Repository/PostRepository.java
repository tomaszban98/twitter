package com.project.twitter.Repository;

import com.project.twitter.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    Optional<List<Post>> getPostsByUserIdOrderByIdDesc(@Param("user_id") Long user_id);

}
