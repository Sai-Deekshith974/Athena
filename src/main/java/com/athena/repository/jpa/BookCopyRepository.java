package com.athena.repository.jpa;

import com.athena.model.Book;
import com.athena.model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Tommy on 2017/8/30.
 */
@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findByBook(Book book);

    List<BookCopy> findByIdIsInAndBookIsNotNull(List<Long> idList);

    Set<BookCopy> findByBookAndBookIsNotNull(Book book);
}