package com.sehoon.bulletinBoardJSON.repository;

import com.sehoon.bulletinBoardJSON.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
