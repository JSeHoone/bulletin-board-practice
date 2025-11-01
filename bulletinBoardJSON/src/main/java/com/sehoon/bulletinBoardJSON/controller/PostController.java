package com.sehoon.bulletinBoardJSON.controller;

import com.sehoon.bulletinBoardJSON.model.dto.PostResponseDTO;
import com.sehoon.bulletinBoardJSON.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public ResponseEntity<List<PostResponseDTO>> getPosts() {
        List<PostResponseDTO> responseData = postService.showPosts();

        return ResponseEntity.ok(responseData);
    }
}
