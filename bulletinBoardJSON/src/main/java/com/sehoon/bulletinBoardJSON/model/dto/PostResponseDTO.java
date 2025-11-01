package com.sehoon.bulletinBoardJSON.model.dto;

import java.time.LocalDateTime;

public record PostResponseDTO(String title, String content, LocalDateTime createAt) {
}
