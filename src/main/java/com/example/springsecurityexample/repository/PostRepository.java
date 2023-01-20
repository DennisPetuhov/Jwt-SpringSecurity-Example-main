package com.example.springsecurityexample.repository;

import com.example.springsecurityexample.model.Post;

import com.example.springsecurityexample.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findListByUserProfileId (Long userProfileId);
    List<Post> findListByUserProfile (UserProfile userProfile);
    Post getPostById (Long id);

}
