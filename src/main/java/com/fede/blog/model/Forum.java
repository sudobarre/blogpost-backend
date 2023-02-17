package com.fede.blog.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Forum {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "forumId")
    private Long forumId;
    @NotBlank(message = "Forum name is required")
    private String name;
    @Column(columnDefinition = "varchar(255) default ''")
    private String description;
    @OneToMany(fetch = LAZY)
    private List<Post> posts;
    private Instant createdDate;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    //followerCount works the same as voting on comments/posts.
    private int followerCount = 0;
}