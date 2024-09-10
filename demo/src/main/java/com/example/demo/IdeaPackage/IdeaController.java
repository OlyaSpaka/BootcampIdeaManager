package com.example.demo.IdeaPackage;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "Idea")
public class IdeaController {
    private final IdeaService ideaService;


    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }
@GetMapping
public String show(){
        return "working";
}
    @PostMapping
    public void addIdea(@RequestBody Idea idea){
        ideaService.addNewIdea(idea);
    }

    @DeleteMapping(path = "{ideaId}")
    public void deleteIdea(@PathVariable("ideaId") Long id) {
        ideaService.deleteIdea(id);
    }

    @PutMapping(path = "{ideaId}/idea")
    public void updateIdea(@PathVariable("ideaId") Long id,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) String key_features,
                           @RequestParam(required = false) String references) {
        ideaService.updateIdea(id, description,title,key_features,references);

    }

}
