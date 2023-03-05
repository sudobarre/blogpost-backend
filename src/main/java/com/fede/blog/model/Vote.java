package com.fede.blog.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Vote {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long voteId;
    private VoteType voteType;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Post post;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "commentId", referencedColumnName = "commentId")
    private Comment comment;
    @ManyToOne
    @JoinColumn(name = "forumId", referencedColumnName = "forumId")
    private Forum forum;
}
