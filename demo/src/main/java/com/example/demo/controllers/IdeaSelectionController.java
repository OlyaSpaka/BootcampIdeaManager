package com.example.demo.controllers;

import com.example.demo.models.IdeaSelection;
import com.example.demo.services.IdeaSelectionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "IdeaSelection")
public class IdeaSelectionController {

    private final IdeaSelectionService ideaSelectionService;

    public IdeaSelectionController(IdeaSelectionService ideaSelectionService) {
        this.ideaSelectionService = ideaSelectionService;
    }
    @PostMapping
    public void addIdeaSelection(@RequestBody  IdeaSelection ideaSelection){
        ideaSelectionService.addIdeaSelection(ideaSelection);
    }
    @DeleteMapping(path = "{ideaSelectionId}")
    public void deleteIdeaSelection(@PathVariable("ideaSelectionId") Integer id){
        ideaSelectionService.deleteIdeaSelection(id);
    }

}
