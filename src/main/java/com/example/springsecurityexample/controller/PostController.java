package com.example.springsecurityexample.controller;

import com.example.springsecurityexample.model.Post;
import com.example.springsecurityexample.model.User;
import com.example.springsecurityexample.model.UserProfile;
import com.example.springsecurityexample.repository.PostRepository;
import com.example.springsecurityexample.repository.UserProfileRepository;
import com.example.springsecurityexample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserProfileRepository userProfileRepository;

    @PostMapping()
    public Post createPost(@RequestBody Post post) {
        post.setUserProfile(getUserProfile());
        return postRepository.save(post);
    }
    @GetMapping()
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }
    @GetMapping("/byuserprofile")
    public List<Post> getPostByUserProfile(){
        UserProfile userProfile= getUserProfile();
       return postRepository.findListByUserProfile(userProfile);
    }

    private UserProfile getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // те ли за кого выдаем себя(прошли -идем дальше)

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        System.out.println("User principal name =" + userPrincipal.getUsername());
        User user = userRepository.findByUsername(userPrincipal.getUsername()).get();
        UserProfile userProfile = userProfileRepository.findUserProfileByUser(user);
        return userProfile;
    }
}
