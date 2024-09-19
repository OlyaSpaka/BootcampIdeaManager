package com.example.demo.services;

import com.example.demo.dto.GroupAssignmentsDTO;
import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserSelectionService {
    private final UserSelectionPrioritiesRepository userSelectionPriorityRepository;
    private final UserSelectionResultRepository userSelectionResultRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;
    private final IdeaSelectionRepository ideaSelectionRepository;
    private final UserSelectionPrioritiesRepository userSelectionPrioritiesRepository;

    @Autowired
    public UserSelectionService(UserSelectionPrioritiesRepository userSelectionPriorityRepository,
                                UserRepository userRepository,
                                IdeaRepository ideaRepository,
                                UserSelectionResultRepository userSelectionResultRepository,
                                IdeaSelectionRepository ideaSelectionRepository,
                                UserSelectionPrioritiesRepository userSelectionPrioritiesRepository) {
        this.userSelectionPriorityRepository = userSelectionPriorityRepository;
        this.userRepository = userRepository;
        this.ideaRepository = ideaRepository;
        this.userSelectionResultRepository = userSelectionResultRepository;
        this.ideaSelectionRepository = ideaSelectionRepository;
        this.userSelectionPrioritiesRepository = userSelectionPrioritiesRepository;
    }

    public void saveUserSelections(Integer userId, Map<Integer, Integer> priorities, Timestamp submissionTimestamp) {
        User user = userRepository.findById(userId).orElseThrow();

        var ideaSelections = ideaRepository.findIdeaSelections(priorities.keySet())
                .stream()
                .collect(Collectors.toMap(ideaSelection -> ideaSelection.getIdea().getId(), Function.identity()));
        priorities.forEach((ideaId, priority) -> {
            IdeaSelection ideaSelection = ideaSelections.get(ideaId);
            UserSelectionPriorities userSelectionPriority = new UserSelectionPriorities();
            userSelectionPriority.setUser(user);
            userSelectionPriority.setIdeaSelection(ideaSelection);
            userSelectionPriority.setPriority(priority);
            userSelectionPriority.setSubmissionTime(submissionTimestamp);
            userSelectionPriorityRepository.save(userSelectionPriority);
        });
        onSave();
    }

    private void onSave() {
        Integer amountOfUsersToVote = userRepository.countUsers();
        Integer amountOfUsersVoted = userSelectionPrioritiesRepository.countUsersWithPriorities();
        if (Objects.equals(amountOfUsersToVote, amountOfUsersVoted)) {
            assignUsersToGroups();
        }
    }

    public void assignUsersToGroups() {
        Map<Idea, List<User>> ideaToUsers = new HashMap<>();
        List<UserSelectionPriorities> allPriorities = userSelectionPrioritiesRepository.findAll();
        List<User> allVotingUsers = userRepository.findAllWithUserRole();
        List<UserSelectionPriorities> skippedPriorities = new ArrayList<>(); // todo: ???
        int maxGroupSize = getMaxUsersPerGroup();

        allPriorities.sort(Comparator.comparing(UserSelectionPriorities::getPriority)
                .thenComparing(UserSelectionPriorities::getSubmissionTime));

        for (UserSelectionPriorities selectedPriority : allPriorities) {
            User userOfPriority = selectedPriority.getUser();
            if (!allVotingUsers.contains(userOfPriority)) {
                continue;
            }
            Idea ideaOfPriority = selectedPriority.getIdeaSelection().getIdea();
            ideaToUsers.putIfAbsent(ideaOfPriority, new ArrayList<>());
            List<User> usersForIdea = ideaToUsers.get(ideaOfPriority);

            if (usersForIdea.size() >= maxGroupSize) {
                skippedPriorities.add(selectedPriority);
            } else {
                usersForIdea.add(userOfPriority);
                allVotingUsers.remove(userOfPriority);
            }
        }
        saveGroupAssignmentsToDatabase(ideaToUsers);
    }


    private void saveGroupAssignmentsToDatabase(Map<Idea, List<User>> groupAssignments) {
        groupAssignments.forEach((idea, users) -> {
            Integer ideaSelectionId = idea.getId(); // todo: ideaId?
            users.forEach(user -> userSelectionResultRepository.save(
                    new UserSelectionResult(new UserSelectionResult.UserSelectionResultId(user.getId(), ideaSelectionId))
            ));
        });
    }

    public List<GroupAssignmentsDTO> getGroupAssignments() {
        List<UserSelectionResult> userSelectionResults = userSelectionResultRepository.findAll();
        Map<Idea, List<User>> groupAssignments = new HashMap<>();

        for (UserSelectionResult result : userSelectionResults) {
            UserSelectionResult.UserSelectionResultId id = result.getId();
            int userId = id.getUserId();
            int ideaSelectionId = id.getIdeaSelectionId();
            Idea idea = ideaRepository.getReferenceById(ideaSelectionId);
            User user = userRepository.getReferenceById(userId);
            groupAssignments.computeIfAbsent(idea, k -> new ArrayList<>()).add(user);
        }
        return groupAssignments.entrySet().stream()
                .map(entry -> new GroupAssignmentsDTO(
                        entry.getKey().getTitle(), entry.getValue().stream().map(User::getUsername).toList())
                ).toList();
    }

    private int getMaxUsersPerGroup() {
        List<IdeaSelection> ideaSelections = ideaSelectionRepository.findAll();
        return userRepository.countUsers() / ideaSelections.size();
    }
}
