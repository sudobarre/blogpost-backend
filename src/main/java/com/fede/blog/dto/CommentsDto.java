package com.fede.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsDto {
    private Long id;
    private String text;
    private String createdDate;
    private Long postId;
    private String userName;
    private Integer voteCount = 0;
}