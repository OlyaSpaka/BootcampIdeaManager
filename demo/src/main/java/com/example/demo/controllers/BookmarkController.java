package com.example.demo.controllers;

import com.example.demo.dto.BookmarkRequestDTO;
import com.example.demo.models.Bookmark;
import com.example.demo.services.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addBookmark(@RequestBody BookmarkRequestDTO bookmarkRequestDTO) {
        bookmarkService.addBookmark(
                bookmarkRequestDTO.ideaId(),
                bookmarkRequestDTO.userId()
        );

        return ResponseEntity.ok("Bookmark saved successfully");
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteBookmark(@RequestBody BookmarkRequestDTO bookmarkRequestDTO) {
        System.out.println("Deleting bookmark = " + bookmarkRequestDTO);

        bookmarkService.deleteBookmark(
                bookmarkRequestDTO.ideaId(),
                bookmarkRequestDTO.userId()
        );

        return ResponseEntity.ok("Bookmark deleted successfully");
    }

    @GetMapping
    public List<Bookmark> getUserBookmarks(@RequestParam("userId") Integer userId) {
        return bookmarkService.getUserBookmarks(userId);
    }
}
