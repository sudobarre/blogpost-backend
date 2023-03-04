package com.fede.blog.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.*;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;

    @NotBlank(message = "Username is required")
    @Size(max=20)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(max=120)
    private String password;

    @Email
    @NotEmpty(message = "Email is required")
    @Size(max=100)
    private String email;

    private Instant created;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @Lob
    private String bio;

    private String imgUrl;

    @JoinTable(name = "USER_FRIENDS", joinColumns = {
            @JoinColumn(name = "ADDING_USER", referencedColumnName = "userId", nullable =   false)}, inverseJoinColumns = {
            @JoinColumn(name = "ADDED_USER", referencedColumnName = "userId", nullable = false)})
    @ManyToMany(fetch=EAGER)
    private Collection<User> friends;

    @ManyToMany(fetch=EAGER, mappedBy = "friends")
    private Collection<User> addUser;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<SavedPost> savedPosts;
}
