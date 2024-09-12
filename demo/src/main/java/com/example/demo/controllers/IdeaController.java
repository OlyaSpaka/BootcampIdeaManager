package com.example.demo.controllers;

import com.example.demo.models.Comment;
import com.example.demo.services.IdeaService;
import com.example.demo.models.Idea;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "Idea")
public class IdeaController {
    private final IdeaService ideaService;


    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }
    @PostMapping
    public void addIdea(@RequestBody Idea idea){
        ideaService.addNewIdea(idea);
    }

    @DeleteMapping(path = "{ideaId}")
    public void deleteIdea(@PathVariable("ideaId") Integer id) {
        ideaService.deleteIdea(id);
    }

    @PutMapping(path = "{ideaId}/idea")
    public void updateIdea(@PathVariable("ideaId") Integer id,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) String key_features,
                           @RequestParam(required = false) String references) {
        ideaService.updateName(id, description,title,key_features,references);

    }

    @GetMapping
    public List<Idea> showUserIdea(@RequestParam("userId")Integer userId){
        return ideaService.showUserIdea(userId);
    }

}
