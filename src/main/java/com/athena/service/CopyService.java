package com.athena.service;

import com.athena.exception.BookNotFoundException;
import com.athena.exception.IdOfResourceNotFoundException;
import com.athena.model.Book;
import com.athena.model.BookCopy;
import com.athena.model.Copy;
import com.athena.model.CopyPK;
import com.athena.repository.jpa.BookCopyRepository;
import com.athena.repository.jpa.BookRepository;
import com.athena.repository.jpa.CopyRepository;
import com.athena.repository.jpa.JournalCopyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Tommy on 2017/8/24.
 */
@Service
public class CopyService {
    private final CopyRepository copyRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final JournalCopyRepository journalCopyRepository;

    @Autowired
    public CopyService(CopyRepository copyRepository, BookRepository bookRepository, BookCopyRepository bookCopyRepository, JournalCopyRepository journalCopyRepository) {
        this.copyRepository = copyRepository;
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.journalCopyRepository = journalCopyRepository;
    }

    public void saveCopies(Copy[] copies) {
        if (copies != null && copies.length > 0) {
            if (copies[0] instanceof BookCopy) {
                //todo: distinguish BookCopy and JournalCopy
            }
        }
    }

    public void saveCopies(Long[] copyPKList) {
        Map<Long, Integer> indexMap = new HashMap<>();
        List<Copy> copyList = new ArrayList<>();
//        for (Long isbn : copyPKList) {
//            Copy copy = new Copy();
//            if (indexMap.containsKey(isbn)) {
//                copy.setId(new CopyPK(isbn, indexMap.get(isbn) + 1));
//            } else {
//                indexMap.put(isbn, 0);
//                copy.setId(new CopyPK(isbn, indexMap.get(isbn) + 1));
//            }
//            copyList.add(copy);
//        }
        this.copyRepository.save(copyList);
    }

    public List<Copy> saveCopies(List<CopyPK> copyPKList) throws BookNotFoundException {
        List<Copy> copyList = new ArrayList<>();
        Map<Long, Set<CopyPK>> isbnCopyPK = this.divideCopyPKByIsbn(copyPKList);
//        for (Long isbn: isbnCopyPK.keySet()) {
//            Book book = this.bookRepository.findOne(isbn);
//            if(book == null){
//                throw new BookNotFoundException(isbn);
//            }
//            for (CopyPK copyPK : isbnCopyPK.get(isbn)) {
//                Copy copy = new Copy();
//                copy.setId(copyPK);
//                copy.setBook(book);
//                copyList.add(copy);
//            }
//        }
        this.copyRepository.save(copyList);
        return copyList;
    }


    private Map<Long, Set<CopyPK>> divideCopyPKByIsbn(List<CopyPK> copyPKList) {
        Map<Long, Set<CopyPK>> result = new HashMap<>();
        for (CopyPK copyPK : copyPKList) {
            Long isbn = copyPK.getIsbn();
            if (result.containsKey(isbn)) {
                // if the isbn has been saved
                Set<CopyPK> set = result.get(isbn);
                set.add(copyPK);
                result.put(isbn, set);
            } else {
                //if not
                Set<CopyPK> set = new HashSet<>();
                set.add(copyPK);
                result.put(isbn, set);
            }
        }
        return result;
    }

    public void removeCopies(List<Copy> copies) {
        this.copyRepository.delete(copies);
    }

    public Copy getCopy(Long isbn, Integer id) throws IdOfResourceNotFoundException {
//        Copy copy = this.copyRepository.findOne(new CopyPK(isbn, id));
        Copy copy = new Copy();
        if (copy == null) {
            throw new IdOfResourceNotFoundException();
        }
        return copy;
    }

    public List<Copy> getCopies(Long isbn) throws BookNotFoundException {
        Book book = this.bookRepository.findOne(isbn);
        if (book == null) {
            throw new BookNotFoundException(isbn);
        }

        return new ArrayList<Copy>();
    }
}
