package com.example.demo.mapper.interf;

import com.example.demo.dto.input.InputIdeaDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.models.Idea;

import java.text.ParseException;

public interface IdeaMapperInt {
    Idea map(InputIdeaDTO inputIdeaDTO) throws IllegalArgumentException, ParseException;
    OutputIdeaDTO map(Idea Idea) throws IllegalArgumentException;
}
