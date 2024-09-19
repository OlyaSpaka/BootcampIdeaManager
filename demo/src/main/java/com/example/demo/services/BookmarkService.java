package com.example.demo.services;

import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.mapper.interf.IdeaMapperInt;
import com.example.demo.models.Bookmark;
import com.example.demo.models.User;
import com.example.demo.models.Idea;
import com.example.demo.repositories.BookmarkRepository;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;
    private final IdeaMapperInt ideaMapperInt;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository, IdeaRepository ideaRepository, IdeaMapperInt ideaMapperInt) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.ideaRepository = ideaRepository;
        this.ideaMapperInt = ideaMapperInt;
    }

    public void addBookmark(Integer ideaId, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new IllegalArgumentException("Idea not found with ID: " + ideaId));
        Bookmark bookmark = new Bookmark();

        bookmark.setIdea(idea);
        bookmark.setUser(user);

        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void deleteBookmark(Integer ideaId, Integer userId) {
        bookmarkRepository.deleteByIdeaIdAndUserId(ideaId, userId);
    }

    public List<Bookmark> getUserBookmarks(Integer userId) {
        return bookmarkRepository.findByUserId(userId);
    }

    public List<OutputIdeaDTO> getUserBookmarkedIdeas(Integer userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);
        return bookmarks.stream()
                .map(Bookmark::getIdea)
                .map(ideaMapperInt::map)
                .collect(Collectors.toList());
    }

    public Map<Integer, Boolean> getBookmarkStatusMap(User currentUser, List<OutputIdeaDTO> ideas) {
        List<Bookmark> userBookmarks = getUserBookmarks(currentUser.getId());

        Set<Integer> bookmarkedIdeaIds = userBookmarks.stream()
                .map(bookmark -> bookmark.getIdea().getId())
                .collect(Collectors.toSet());

        Map<Integer, Boolean> bookmarkStatusMap = new HashMap<>();

        for (OutputIdeaDTO idea : ideas) {
            boolean isBookmarked = bookmarkedIdeaIds.contains(idea.getId());
            bookmarkStatusMap.put(idea.getId(), isBookmarked);
        }

        return bookmarkStatusMap;
    }
}
