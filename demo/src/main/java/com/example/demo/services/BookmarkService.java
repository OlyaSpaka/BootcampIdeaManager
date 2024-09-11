package com.example.demo.services;

import com.example.demo.models.Bookmark;
import com.example.demo.repositories.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public void addBookmark(Bookmark bookmark){
        bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(Integer id){
        boolean exists = bookmarkRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Bookmark with Id " + id + " does not exist");
        } else {
            bookmarkRepository.deleteById(id);
        }
    }

}
