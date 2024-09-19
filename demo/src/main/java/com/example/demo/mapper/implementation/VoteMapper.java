package com.example.demo.mapper.implementation;

import com.example.demo.dto.general.VoteDTO;
import com.example.demo.dto.general.VoteTypeDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.dto.output.OutputUserDTO;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.models.Vote;
import com.example.demo.models.VoteType;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.VoteTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class VoteMapper {
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;
    private final VoteTypeRepository voteTypeRepository;
    public VoteMapper(UserRepository userRepository, IdeaRepository ideaRepository, VoteTypeRepository voteTypeRepository) {
        this.userRepository = userRepository;
        this.ideaRepository = ideaRepository;
        this.voteTypeRepository = voteTypeRepository;
    }

    public Vote map(VoteDTO voteDTO){
        if (voteDTO == null) return null;

        User user = userRepository.findById(voteDTO.getUser().getId())
                .orElseThrow(IllegalArgumentException::new);
        Idea idea = ideaRepository.findById(voteDTO.getIdea().getId())
                .orElseThrow(IllegalArgumentException::new);
        VoteType voteType = voteTypeRepository.findById(voteDTO.getVoteTypeDTO().getId())
                .orElseThrow(IllegalArgumentException::new);

        Vote vote = new Vote();
        vote.setId(voteDTO.getId());
        voteType.addVote(vote);
        user.addVote(vote);
        idea.addVote(vote);

        return vote;
    }

    public VoteDTO map(Vote vote){
        if (vote == null) return null;

        VoteDTO voteDTO = new VoteDTO();

        OutputUserDTO userDTO = new OutputUserDTO();
        userDTO.setId(vote.getUser().getId());
        OutputIdeaDTO ideaDTO = new OutputIdeaDTO();
        ideaDTO.setId(vote.getIdea().getId());
        VoteTypeDTO voteTypeDTO = new VoteTypeDTO();
        voteTypeDTO.setId(vote.getVoteType().getId());

        voteDTO.setId(vote.getId());
        voteDTO.setVoteTypeDTO(voteTypeDTO);
        voteDTO.setUser(userDTO);
        voteDTO.setIdea(ideaDTO);

        return voteDTO;
    }
}
