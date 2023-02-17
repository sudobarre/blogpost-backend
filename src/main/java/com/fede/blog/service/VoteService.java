package com.fede.blog.service;

import com.fede.blog.dto.VoteDto;
import com.fede.blog.exceptions.BlogException;
import com.fede.blog.exceptions.PostNotFoundException;
import com.fede.blog.model.Post;
import com.fede.blog.model.Vote;
import com.fede.blog.repository.PostRepository;
import com.fede.blog.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.fede.blog.model.VoteType.DOWNVOTE;
import static com.fede.blog.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new BlogException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        //could make it cleaner by declaring a variable to hold the amt of upvotes/downvotes.
        if (UPVOTE.equals(voteDto.getVoteType())) {
            if(voteByPostAndUser.isPresent() && DOWNVOTE.equals(voteByPostAndUser.get().getVoteType())){
                post.setVoteCount(post.getVoteCount() + 2);
            } else {
                post.setVoteCount(post.getVoteCount() + 1);
            }

        } else {
            if(voteByPostAndUser.isPresent() && UPVOTE.equals(voteByPostAndUser.get().getVoteType())){
                post.setVoteCount(post.getVoteCount() - 2);
            } else {
                post.setVoteCount(post.getVoteCount() - 1);
            }
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    //No need for a new file, its just 1 function only used here.
    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
