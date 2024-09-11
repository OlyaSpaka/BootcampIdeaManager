package com.example.demo;

import com.example.demo.models.Competition;
import com.example.demo.repositories.CompetitionRepository;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Create and save a competition
        Competition competition = new Competition("Spring Boot Competition", "A competition for Spring Boot apps", new Date(), new Date());
//        competitionRepository.save(competition);

        // Create and save a user
//        User user = new User("john_doe", "email@g.com", "JohnDoe");
//        userRepository.save(user);

//        User user = userRepository.findById(47).get();
//        Role role = roleRepository.findById(3).get();
//        user.addRole(role);
//        roleRepository.save(role);

        // Create and save an idea
//        Idea idea = new Idea(competition, user, "A cool idea", "Cool Idea Title", "Key features", "References", new Date(), "image.png");
//        competition.addIdea(idea);

//        for (int i = 30; i<=39; i++){
//            competitionRepository.deleteById(i);
//        }
//        ideaRepository.save(idea);
//        competitionRepository.save(competition);
//
//        // Fetch and print the idea
//        Idea fetchedIdea = ideaRepository.findById(idea.getId()).orElse(null);
//        if (fetchedIdea != null) {
//            System.out.println("Fetched Idea: " + fetchedIdea);
//        } else {
//            System.out.println("Idea not found.");
//        }
//
//        // Fetch and print the competition with associated ideas
//        Competition fetchedCompetition = competitionRepository.findById(competition.getId()).orElse(null);
//        if (fetchedCompetition != null) {
//            System.out.println("Fetched Competition: " + fetchedCompetition);
//            // Initialize ideas collection
//            System.out.println("Ideas in Competition: " + fetchedCompetition.getIdeas());
//        } else {
//            System.out.println("Competition not found.");
//        }
    }
}