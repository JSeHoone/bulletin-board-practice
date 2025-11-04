package com.sehoon.bulletinBoardMVC.repository;

import com.sehoon.bulletinBoardMVC.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findByUserId(Long userId);

    void deleteByUserIdAndPostId(Long userId,  Long postId);

    PostEntity findByUserIdAndPostId(Long userId,  Long postId);
}
