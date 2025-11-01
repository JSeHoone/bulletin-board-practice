package com.sehoon.bulletinBoardJSON.service;

import com.sehoon.bulletinBoardJSON.model.dto.PostCreateRequest;
import com.sehoon.bulletinBoardJSON.model.dto.PostResponseDTO;
import com.sehoon.bulletinBoardJSON.model.dto.PostUpdateRequest;
import com.sehoon.bulletinBoardJSON.model.entity.PostEntity;
import com.sehoon.bulletinBoardJSON.repository.PostRepository;
import javax.naming.NameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> showPosts() {
        List<PostEntity> findPosts = postRepository.findAll();

        // convert to DTO
        List<PostResponseDTO> response = findPosts.
                stream().
                map(post -> new PostResponseDTO(post.getTitle(), post.getContent(), post.getCreateAt()))
                .toList();

        return response;
    }

    @Transactional(readOnly = true)
    public PostResponseDTO showPostDetail(Long postId) {
        // TODO 예외처리가 필요하네
        PostEntity findPost = postRepository.findById(postId).orElseThrow();

        PostResponseDTO response = new PostResponseDTO(findPost.getTitle(), findPost.getContent(), findPost.getCreateAt());

        return response;
    }

    @Transactional
    public PostResponseDTO registerPost(PostCreateRequest request) {
        PostEntity newPost = PostEntity.createPostEntity(request.title(), request.content());

        PostEntity insertedPost = postRepository.save(newPost);

        PostResponseDTO response = new PostResponseDTO(insertedPost.getTitle(), insertedPost.getContent(), insertedPost.getCreateAt());
        return response;
    }

    @Transactional
    public PostResponseDTO updatePost(Long postId, PostUpdateRequest request) {
        // TODO : 예외처리가 필요하다
        PostEntity findPost = postRepository.findById(postId).orElseThrow();

        findPost.update(request.title(), request.content());

        PostResponseDTO response = new PostResponseDTO(findPost.getTitle(),findPost.getContent(), findPost.getCreateAt());
        return response;
    }

    @Transactional
    public void removePost(Long postId) {
        // TODO : 응답이 500으로 나옴 !
        PostEntity findPost = postRepository.findById(postId).orElseThrow( () ->
                new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );

        postRepository.delete(findPost);
    }
}
