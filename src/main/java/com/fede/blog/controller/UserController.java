package com.fede.blog.controller;

import com.fede.blog.dto.UserDto;
import com.fede.blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
    @PutMapping(value = "/edit")
    public ResponseEntity<?> editUser(UserDto userDto){
        userService.editUser(userDto);
        return new ResponseEntity<>(OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping(value = "/ban/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> banUser(@PathVariable(name = "username") String username){
        userService.banUser(username);
        return new ResponseEntity<>(OK);
    }


}
