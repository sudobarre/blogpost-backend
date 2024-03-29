package com.fede.blog.controller;

import com.fede.blog.dto.VoteDto;
import com.fede.blog.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vote/")
@AllArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> vote(@RequestBody VoteDto voteDto) {
        int updatedVoteCount = voteService.vote(voteDto);
        return ResponseEntity.ok(updatedVoteCount);
    }
}
