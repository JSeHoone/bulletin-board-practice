package com.sehoon.bulletinBoardMVC.controller;

import com.sehoon.bulletinBoardMVC.model.dto.PostCreateRequestDTO;
import com.sehoon.bulletinBoardMVC.model.dto.PostResponseDTO;
import com.sehoon.bulletinBoardMVC.model.entity.PostEntity;
import com.sehoon.bulletinBoardMVC.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/list")
    public String getPosts(@RequestAttribute("userId") Long userId,  Model model) {
        List<PostResponseDTO> posts = postService.showPosts(userId);

        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/detail")
    public String getPostDetails(@RequestParam("id") Long id, Model model) {
        PostResponseDTO post = postService.showPostDetail(id);

        model.addAttribute("post", post);
        return "posts/detail";
    }

    @GetMapping("/createPostView")
    public String createPostView() {
        return "posts/create";
    }

    @PostMapping("/create")
    public String createPost(@RequestAttribute("userId") Long userId,
                             @RequestParam String title,
                             @RequestParam String content) {
        postService.createPost(title, content, userId);

        return "redirect:/posts/list";
    }

    @PostMapping("/delete")
    public String deletePost(@RequestAttribute("userId") Long userId, @RequestParam("post-id") Long postId) {
        postService.removePost(userId, postId);

        return "redirect:/posts/list";
    }

    @GetMapping("/modifyView")
    public String modifyPostView(@RequestParam("post-id") Long postId, Model model) {
        PostResponseDTO post = postService.showPostDetail(postId);
        model.addAttribute("post", post);
        return "posts/modify";
    }

    @PostMapping("/modify")
    public String modifyPost(@RequestAttribute("userId") Long userId,
                             @RequestParam Long postId,
                             @RequestParam String title,
                             @RequestParam String content) {
        postService.modifyPost(userId, postId, title, content);

        return "redirect:/posts/detail?id="+postId;
    }



}
