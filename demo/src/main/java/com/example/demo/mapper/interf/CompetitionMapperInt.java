package com.example.demo.mapper.interf;

import com.example.demo.dto.general.CompetitionDTO;
import com.example.demo.models.Competition;

import java.util.List;

public interface CompetitionMapperInt {
    Competition map(CompetitionDTO competitionDTO);
    CompetitionDTO map(Competition competition);
    List<CompetitionDTO> map(List<Competition> competitionList);
}
