
package com.fede.blog.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long commentId;
    @NotEmpty
    @Column(name="body", columnDefinition="text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    //@OnDelete(action= OnDeleteAction.CASCADE)
    private Post post;
    private Instant createdDate;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    private Integer voteCount = 0;
}