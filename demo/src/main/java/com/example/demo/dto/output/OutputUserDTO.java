package com.example.demo.dto.output;

import com.example.demo.dto.general.BookmarkDTO;
import com.example.demo.dto.general.CommentDTO;
import com.example.demo.dto.general.RoleDTO;
import com.example.demo.dto.general.VoteDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class OutputUserDTO {
    Integer id;
    String username;
    Set<CommentDTO> comments;
    Set<BookmarkDTO> bookmarks;
    Set<VoteDTO> votes;
    Set<OutputIdeaDTO> ideas;
    Set<RoleDTO> roles;
    //votes, bookmarks, whatever
    public OutputUserDTO() {
    }
    public OutputUserDTO(String username) {
        this.username = username;
    }

    public OutputUserDTO(Integer id, String username) {
        this.id = id;
        this.username = username;
    }
}