package com.sehoon.bulletinBoardJSON.service;

import com.sehoon.bulletinBoardJSON.model.dto.PostCreateRequest;
import com.sehoon.bulletinBoardJSON.model.dto.PostResponseDTO;
import com.sehoon.bulletinBoardJSON.model.entity.PostEntity;
import com.sehoon.bulletinBoardJSON.repository.PostRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostResponseDTO> showPosts() {
        List<PostEntity> findPosts = postRepository.findAll();

        // convert to DTO
        List<PostResponseDTO> response = findPosts.
                stream().
                map(post -> new PostResponseDTO(post.getTitle(), post.getContent(), post.getCreateAt()))
                .toList();

        return response;
    }

    public PostResponseDTO showPostDetail(Long postId) {
        PostEntity findPost = postRepository.findById(postId).orElseThrow();

        PostResponseDTO response = new PostResponseDTO(findPost.getTitle(), findPost.getContent(), findPost.getCreateAt());

        return response;
    }

    public PostResponseDTO registerPost(PostCreateRequest request) {
        PostEntity newPost = PostEntity.createPostEntity(request.title(), request.content());

        PostEntity insertedPost = postRepository.save(newPost);

        PostResponseDTO response = new PostResponseDTO(insertedPost.getTitle(), insertedPost.getContent(), insertedPost.getCreateAt());
        return response;
    }
}
