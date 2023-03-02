package com.fede.blog.dto;

import com.fede.blog.model.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private List<Role> roles;
    private String bio;
}
