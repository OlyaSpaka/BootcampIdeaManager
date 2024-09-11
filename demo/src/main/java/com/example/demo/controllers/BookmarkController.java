package com.example.demo.controllers;

import com.example.demo.models.Bookmark;
import com.example.demo.services.BookmarkService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "Bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }
    @PostMapping
    public void addBookmark(@RequestBody Bookmark bookmark){
        bookmarkService.addBookmark(bookmark);
    }

    @DeleteMapping(path = "{bookmarkId}")
    public void deleteBookmark(@PathVariable("bookmarkId") Integer id) {
        bookmarkService.deleteBookmark(id);
    }
}
