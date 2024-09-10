package com.example.demo.VotePackage;

import com.example.demo.IdeaPackage.Idea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void deleteVote(Long id) {
        boolean exists = voteRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Vote with Id " + id + " does not exist");
        } else {
            voteRepository.deleteById(id);
        }
    }

    @Transactional
    public void updateVote(Long id,
                           Long user_id,
                           Long idea_id,
                           Long voteType_id) {
        Vote vote = voteRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Vote with Id " + id + " does not exist."));
        vote.setUser_id(user_id);
        vote.setVote_type_id(voteType_id);
        vote.setIdea_id(idea_id);


    }
}
