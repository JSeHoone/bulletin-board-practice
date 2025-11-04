package com.sehoon.bulletinBoardMVC.service;

import com.sehoon.bulletinBoardMVC.model.dto.PostResponseDTO;
import com.sehoon.bulletinBoardMVC.model.entity.PostEntity;
import com.sehoon.bulletinBoardMVC.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostResponseDTO> showPosts(Long userId) {
        List<PostEntity> posts = postRepository.findByUserId(userId);

        List<PostResponseDTO> postResponseDTOs = posts.stream().
                map(post -> new PostResponseDTO(post.getPostId(), post.getTitle(), post.getContent(), post.getCreateAt())).
                toList();

        return postResponseDTOs;
    }

    public PostResponseDTO showPostDetail(Long postId) {
        PostEntity post = postRepository.findById(postId).orElse(null);
        PostResponseDTO response =  new PostResponseDTO(post.getPostId(), post.getTitle(), post.getContent(), post.getCreateAt());
        return response;
    }

    public PostEntity createPost(String title, String content, Long userId) {
        PostEntity newPost = PostEntity.createPostEntity(title, content, userId);
        postRepository.save(newPost);

        return newPost;
    }

    @Transactional
    public void removePost(Long userId, Long postId) {
        postRepository.deleteByUserIdAndPostId(userId, postId);
    }

    @Transactional
    public void modifyPost(Long userId, Long postId, String title, String content) {
        PostEntity post = postRepository.findByUserIdAndPostId(userId, postId);

        post.setTitle(title);
        post.setContent(content);

    }
}
