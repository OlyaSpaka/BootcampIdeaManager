package com.example.demo.services;

import com.example.demo.models.Competition;
import com.example.demo.models.Idea;
import com.example.demo.models.IdeaSelection;
import com.example.demo.models.Vote;
import com.example.demo.repositories.CompetitionRepository;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.IdeaSelectionRepository;
import com.example.demo.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final IdeaRepository ideaRepository;
    private final CompetitionRepository competitionRepository;
    private IdeaSelectionService ideaSelectionService;

    @Autowired
    public VoteService(VoteRepository voteRepository, IdeaRepository ideaRepository, CompetitionRepository competitionRepository, IdeaSelectionService ideaSelectionService) {
        this.voteRepository = voteRepository;
        this.ideaRepository = ideaRepository;
        this.competitionRepository = competitionRepository;
        this.ideaSelectionService = ideaSelectionService;
    }

    public void addVote(Vote vote) {
        // Check if the user is not voting on their own idea
        if (!Objects.equals(vote.getUser().getId(), vote.getIdea().getUser().getId())) {

            // Check if the user has already voted on the same idea
            Optional<Vote> existingVote = voteRepository.findByUserIdAndIdeaId(vote.getUser().getId(), vote.getIdea().getId());

            if (existingVote.isPresent()) {
                // User has already voted on this idea, do not allow voting twice
                throw new IllegalStateException("User has already voted on this idea.");
            } else {
                // If the user has not voted on the idea, save the new vote
                voteRepository.save(vote);
            }
        } else {
            // If the user is trying to vote on their own idea, throw an exception or handle the case
            throw new IllegalStateException("User cannot vote on their own idea.");
        }
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
    public List<Integer> getIdeaPoints(Integer competitionId){
        List<Integer> pointList = new ArrayList<>();
        List<Idea> ideaList = ideaRepository.findByCompetitionId(competitionId);
        for (Idea idea : ideaList) {
            pointList.add(calculatePoints(idea.getId()));
        }
        return pointList;
    }
    public HashMap<Integer, Integer> getAllPoints(Integer competitionId) {
        List<Idea> ideaList = ideaRepository.findByCompetitionId(competitionId);
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (Idea idea : ideaList) {
            hashMap.put(idea.getId(), calculatePoints(idea.getId()));
        }
        return hashMap;

    }

    public List<Idea> getWinningIdeas(Integer competitionId) {
        List<Idea> winningIdeaList = new ArrayList<>();
        Optional<Competition> competition = competitionRepository.findById(competitionId);
        HashMap<Integer, Integer> ideasWithPointsCalculated = getAllPoints(competitionId);

        List<Map.Entry<Integer, Integer>> sortedIdeas = new ArrayList<>(ideasWithPointsCalculated.entrySet());
        sortedIdeas.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Loop through the sorted list and add the corresponding ideas to the winningIdeaList
        for (int i = 0; i < Math.min(competition.get().getAmountOfWinners(), sortedIdeas.size()); i++) {
            Integer ideaId = sortedIdeas.get(i).getKey();
            winningIdeaList.add(ideaRepository.findById(ideaId).orElse(null));
        }

        return winningIdeaList;
    }

    public void addWinningIdeasToIdeaSelectionRepository(Integer competitionId) {
        if(ideaSelectionService.getSelectedIdeas(competitionId).isEmpty()) {
            List<Idea> winningIdeaList = getWinningIdeas(competitionId);
            for (Idea idea : winningIdeaList) {
                IdeaSelection selectedIdea = new IdeaSelection(idea.getCreatedAt());
                selectedIdea.setCompetition(competitionRepository.findById(competitionId).get());
                selectedIdea.setIdea(idea);

                ideaSelectionService.addIdeaSelection(selectedIdea);
            }
        }

    }
}
