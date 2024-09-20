package com.example.demo.dto.general;

import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.dto.output.OutputUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {
    Integer id;
    OutputUserDTO user;
    OutputIdeaDTO idea;
    VoteTypeDTO voteTypeDTO;
}
