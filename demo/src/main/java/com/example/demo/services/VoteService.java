package com.example.demo.services;

import com.example.demo.models.Idea;
import com.example.demo.models.Vote;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final IdeaRepository ideaRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, IdeaRepository ideaRepository) {
        this.voteRepository = voteRepository;
        this.ideaRepository = ideaRepository;
    }

    public void addVote(Vote vote) {
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

  /*  @Transactional
    public void updateVote(Integer id,
                           Integer user_id,
                           Integer idea_id,
                           Integer voteType_id) {
        Vote vote = voteRepository.findById(id).orElseThrow(() -> new IllegalStateException(
              "Vote with Id " + id + " does not exist."));
        vote.setUser(user_id);
        vote.setVoteType(voteType_id);
        vote.setIdea_id(idea_id);


    }*/

    //Calculates total vote points of specific ideas.
    public int calculatePoints(Integer id) {
        List<Vote> voteList = voteRepository.findByIdeaId(id);
        int points = 0;
        for (Vote vote : voteList) {
            points = points + vote.getVoteType().getPoints();
        }
        return points;
    }

    public HashMap<Integer, Integer> getAllPoints() {
        List<Idea> ideaList = ideaRepository.findAll();
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (Idea idea : ideaList) {
            hashMap.put(idea.getId(), calculatePoints(idea.getId()));
        }
        return hashMap;

    }
}
