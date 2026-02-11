package com.example.btungdung.Service;  // ← Package phải là Service (viết hoa S)

import com.example.btungdung.Model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private List<Book> books = new ArrayList<>();

    // 1. Lấy tất cả sách
    public List<Book> getAllBooks() {
        return books;
    }

    // 2. Lấy sách theo ID
    public Book getBookById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // 3. Thêm sách mới
    public void addBook(Book book) {
        books.add(book);
    }

    // 4. Cập nhật thông tin sách
    public void updateBook(int id, Book updatedBook) {
        books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .ifPresent(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                });
    }

    // 5. Xóa sách theo ID
    public void deleteBook(int id) {
        books.removeIf(book -> book.getId() == id);
    }
}