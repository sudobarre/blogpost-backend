package com.fede.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    //for real-time display of views
    private Integer viewCount;
}
