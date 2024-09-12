package com.example.demo.services;

import com.example.demo.models.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.repositories.VoteRepository;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public void addVote(Vote vote){
        voteRepository.save(vote);
    }
    public void deleteVote(Integer id) {
        boolean exists = voteRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Vote with Id " + id + " does not exist");
        } else {
            voteRepository.deleteById(id);
        }
    }

    @Transactional
    public void updateVote(Integer id,
                           Integer user_id,
                           Integer idea_id,
                           Integer voteType_id) {
//        Vote vote = voteRepository.findById(id).orElseThrow(() -> new IllegalStateException(
//                "Vote with Id " + id + " does not exist."));
//        vote.setUser(user_id);
//        vote.setVoteType(voteType_id);
//        vote.setIdea_id(idea_id);


    }
}
