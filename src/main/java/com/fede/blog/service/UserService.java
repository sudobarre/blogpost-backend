package com.fede.blog.service;

import com.fede.blog.dto.UserDto;
import com.fede.blog.mapper.PostMapper;
import com.fede.blog.model.User;
import com.fede.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    public void editUser(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + userDto.getUsername()));
        user.setRoles(userDto.getRoles());
        user.setBio(userDto.getBio());
    }

    public void banUser(String username) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + username));
            user.setEnabled(false);
            //since this is transactional i dont need to save it.
    }

}
