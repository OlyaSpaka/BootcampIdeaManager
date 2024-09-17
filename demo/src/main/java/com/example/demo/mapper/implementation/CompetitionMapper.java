package com.example.demo.mapper.implementation;

import com.example.demo.dto.general.CompetitionDTO;
import com.example.demo.mapper.interf.CompetitionMapperInt;
import com.example.demo.models.Competition;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompetitionMapper implements CompetitionMapperInt {
    @Override
    public Competition map(CompetitionDTO competitionDTO){
        if (competitionDTO == null) return null;

        Competition competition = new Competition();
        competition.setName(competitionDTO.getName());
        competition.setDescription(competitionDTO.getDescription());
        competition.setStartDate(competitionDTO.getStartDate());
        competition.setEndDate(competitionDTO.getEndDate());
        competition.setAmountOfWinners(competitionDTO.getAmountOfWinners());

        return competition;
    }

    @Override
    public CompetitionDTO map(Competition competition) {
        if (competition == null) return null;

        CompetitionDTO dto = new CompetitionDTO();
        dto.setId(competition.getId());
        dto.setName(competition.getName());
        dto.setStartDate(competition.getStartDate());
        dto.setEndDate(competition.getEndDate());
        dto.setAmountOfWinners(competition.getAmountOfWinners());
        return dto;
    }

    @Override
    public List<CompetitionDTO> map(List<Competition> competitionList) {
        List<CompetitionDTO> resultList = new ArrayList<>();
        for (Competition competition : competitionList){
            if (competition == null) continue;

            CompetitionDTO dto = new CompetitionDTO();
            dto.setId(competition.getId());
            dto.setName(competition.getName());
            dto.setStartDate(competition.getStartDate());
            dto.setEndDate(competition.getEndDate());
            dto.setAmountOfWinners(competition.getAmountOfWinners());

            resultList.add(dto);
        }
        return resultList;
    }
}
