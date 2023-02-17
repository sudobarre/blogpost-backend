package com.fede.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String url;
    private String description;
    private String userName;
    private String forumName;
    private Integer voteCount;
    private Integer commentCount;
    private String createdAt;
    private boolean upVote;
    private boolean downVote;
}
