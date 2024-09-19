package com.example.demo.services;

import com.example.demo.models.Competition;
import com.example.demo.models.Idea;
import com.example.demo.models.IdeaSelection;
import com.example.demo.models.Vote;
import com.example.demo.repositories.CompetitionRepository;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final IdeaRepository ideaRepository;
    private final CompetitionRepository competitionRepository;
    private final IdeaSelectionService ideaSelectionService;

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

    public List<Idea> getWinningIdeas(Integer competitionId) {
        List<Idea> winningIdeaList = new ArrayList<>();
        Optional<Competition> competitionOpt = competitionRepository.findById(competitionId);

        // Check if competition exists
        if (competitionOpt.isEmpty()) {
            return winningIdeaList; // or throw an exception if competition must exist
        }

        Competition competition = competitionOpt.get();
        HashMap<Integer, Integer> ideasWithPointsCalculated = getAllPoints();

        List<Map.Entry<Integer, Integer>> sortedIdeas = new ArrayList<>(ideasWithPointsCalculated.entrySet());
        sortedIdeas.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Loop through the sorted list and add the corresponding ideas to the winningIdeaList
        for (int i = 0; i < Math.min(competition.getAmountOfWinners(), sortedIdeas.size()); i++) {
            Integer ideaId = sortedIdeas.get(i).getKey();
            ideaRepository.findById(ideaId).ifPresent(winningIdeaList::add);
        }

        return winningIdeaList;
    }

    public void addWinningIdeasToIdeaSelectionRepository(Integer competitionId) {
        List<Idea> winningIdeaList = getWinningIdeas(competitionId);
        Optional<Competition> competitionOpt = competitionRepository.findById(competitionId);

        if (competitionOpt.isEmpty()) {
            return; // or throw an exception if competition must exist
        }

        Competition competition = competitionOpt.get();

        for (Idea idea : winningIdeaList) {
            IdeaSelection selectedIdea = new IdeaSelection(idea.getCreatedAt());
            selectedIdea.setCompetition(competition);
            selectedIdea.setIdea(idea);

            ideaSelectionService.addIdeaSelection(selectedIdea);
        }
    }
}
