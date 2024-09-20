package com.example.demo.services;

import com.example.demo.dto.general.VoteDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.mapper.implementation.IdeaMapper;
import com.example.demo.mapper.implementation.VoteMapper;
import com.example.demo.models.*;
import com.example.demo.repositories.CompetitionRepository;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.VoteRepository;
import com.example.demo.repositories.VoteTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VoteService {
    @PersistenceContext
    private EntityManager entityManager;
    private final VoteRepository voteRepository;
    private final IdeaRepository ideaRepository;
    private final VoteTypeRepository voteTypeRepository;
    private final CompetitionRepository competitionRepository;
    private final IdeaSelectionService ideaSelectionService;
    private final VoteMapper voteMapper;
    private final IdeaMapper ideaMapper;
    private final VoteTypeService voteTypeService;

    @Autowired
    public VoteService(VoteRepository voteRepository, IdeaRepository ideaRepository, VoteTypeRepository voteTypeRepository, CompetitionRepository competitionRepository, IdeaSelectionService ideaSelectionService, VoteMapper voteMapper, IdeaMapper ideaMapper, VoteTypeService voteTypeService) {
        this.voteRepository = voteRepository;
        this.ideaRepository = ideaRepository;
        this.voteTypeRepository = voteTypeRepository;
        this.competitionRepository = competitionRepository;
        this.ideaSelectionService = ideaSelectionService;
        this.voteMapper = voteMapper;
        this.ideaMapper = ideaMapper;
        this.voteTypeService = voteTypeService;
    }


    public void addVote(VoteDTO voteDTO) {
        // Convert VoteDTO to Vote entity
        Vote newVote = voteMapper.map(voteDTO);

        // Check if the user is trying to vote on their own idea
        if (Objects.equals(newVote.getUser().getId(), newVote.getIdea().getUser().getId())) {
            throw new IllegalStateException("User cannot vote on their own idea.");
        }
        // Find existing vote by user and idea
        Optional<Vote> existingVoteOpt = voteRepository.findByUserIdAndIdeaId(
                newVote.getUser().getId(),
                newVote.getIdea().getId()
        );

        if (existingVoteOpt.isPresent()) {
            // If an existing vote is found, check if it needs to be updated

            throw new IllegalStateException("User has already voted on this idea with the same vote type.");

        } else {
            // No existing vote found, so save the new vote
            voteRepository.save(newVote);
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

    public List<Integer> getIdeaPoints(Integer competitionId) {
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
        Optional<Competition> competitionOpt = competitionRepository.findById(competitionId);

        // Check if competition exists
        if (competitionOpt.isEmpty()) {
            return winningIdeaList; // or throw an exception if competition must exist
        }

        Competition competition = competitionOpt.get();
        HashMap<Integer, Integer> ideasWithPointsCalculated = getAllPoints(1);

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

    public List<VoteDTO> findByUserId(Integer id) {
        List<VoteDTO> resultList = new ArrayList<>();
        List<Vote> votes = voteRepository.findByUserId(id);

        for (Vote v : votes) {
            VoteDTO voteDTO = voteMapper.map(v);
            resultList.add(voteDTO);
        }

        return resultList;
    }

    public List<OutputIdeaDTO> findIdeasByUserId(Integer id) {
        List<OutputIdeaDTO> resultList = new ArrayList<>();
        List<Idea> ideas = voteRepository.findByUserId(id)
                .stream().map(Vote::getIdea)
                .toList();

        for (Idea idea : ideas) {
            OutputIdeaDTO ideaDTO = ideaMapper.map(idea);
            resultList.add(ideaDTO);
        }


        return resultList;
    }

    public List<Vote> findUserVotes(Integer userId) {
        return voteRepository.findByUserId(userId);
    }

    public List<Vote> findVotesByUserIdAndIdeaId(Integer userId, Integer ideaId) {
        return voteRepository.findVotesByUserIdAndIdeaId(userId, ideaId);
    }
    public List<VoteType> voteTypesLeft(Integer userId){
        List<VoteType> voteTypes = voteTypeService.getVoteTypes();

        List<Vote> votes = findUserVotes(userId);

        for(Vote v : votes){
            voteTypes.remove(v.getVoteType());
        }
        return voteTypes;
    }
}
