package com.sehoon.bulletinBoardMVC.model.dto;

import java.time.LocalDateTime;

public record PostResponseDTO(Long id, String title, String content, LocalDateTime createAt) {
}
