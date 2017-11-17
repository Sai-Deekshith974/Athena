package com.athena.service;

import com.athena.exception.http.IdOfResourceNotFoundException;
import com.athena.exception.http.IllegalBorrowRequest;
import com.athena.exception.http.IllegalReturnRequest;
import com.athena.exception.http.ResourceNotDeletable;
import com.athena.model.Borrow;
import com.athena.model.CopyStatus;
import com.athena.model.SimpleCopy;
import com.athena.repository.jpa.BorrowRepository;
import com.athena.repository.jpa.copy.SimpleCopyRepository;
import com.athena.security.model.Account;
import com.athena.service.util.BorrowVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Created by Tommy on 2017/11/5.
 */
@Service
public class BorrowService implements ModelCRUDService<Borrow, String> {

    private SimpleCopyRepository simpleCopyRepository;
    private BorrowRepository borrowRepository;
    private BorrowVerificationService borrowVerificationService;


    @Autowired
    public BorrowService(BorrowRepository borrowRepository, BorrowVerificationService borrowVerificationService, SimpleCopyRepository simpleCopyRepository) {
        this.borrowRepository = borrowRepository;
        this.borrowVerificationService = borrowVerificationService;
        this.simpleCopyRepository = simpleCopyRepository;
    }

    @Override
    @Transactional
    public Borrow add(Borrow borrow) {
        //set status of copy
        SimpleCopy copy = borrow.getCopy();
        copy.setStatus(CopyStatus.CHECKED_OUT);
        borrow.setCopy(this.simpleCopyRepository.save(copy));
        return this.borrowRepository.save(borrow);
    }

    @Override
    public Borrow get(String id) throws IdOfResourceNotFoundException {
        Borrow borrow = this.borrowRepository.findOne(id);
        if (borrow == null) {
            throw new IdOfResourceNotFoundException();
        }
        return borrow;
    }

    @Override
    public Borrow update(Borrow borrow) throws IdOfResourceNotFoundException {
        if (!this.borrowRepository.exists(borrow.getId())) {
            throw new IdOfResourceNotFoundException();
        }
        this.borrowRepository.save(borrow);
        return borrow;
    }

    @Override
    public void delete(Borrow borrow) throws IdOfResourceNotFoundException, ResourceNotDeletable {
        Objects.requireNonNull(borrow);
        this.borrowRepository.delete(borrow);
    }

    public Borrow borrowCopy(Account account, SimpleCopy simpleCopy) throws IllegalBorrowRequest {
        Borrow borrow = new Borrow();
        borrow.setUser(account.getUser());
        borrow.setCopy(simpleCopy);

        if (borrowVerificationService.userCanBorrow(account) && borrowVerificationService.copyCanBorrow(simpleCopy)) {
            return this.add(borrow);
        }
        throw new IllegalBorrowRequest();
    }

    public Borrow returnCopy(String id, Account account) throws IllegalReturnRequest {
        return this.returnCopy(id, account, false);
    }

    @Transactional
    public Borrow returnCopy(String id, Account account, Boolean isSelfService) throws IllegalReturnRequest {
        //check if the account has the borrow
        Borrow borrow = this.borrowRepository.findOne(id);
        if (borrow == null) {
            //if the borrow doesn't exist
            throw new IllegalReturnRequest();
        }
        if (borrow.getUser() != account.getUser()) {
            //if the user does not has the borrow
            throw new IllegalReturnRequest();
        }
        return this.returnCopy(borrow, isSelfService);
    }

    private Borrow returnCopy(Borrow borrow, Boolean isSelfService) throws IllegalReturnRequest {
        Objects.requireNonNull(borrow);
        //check status
        if (borrowVerificationService.canReturn(borrow)) {
            return this.setReturnStatus(borrow, isSelfService);
        }
        throw new IllegalReturnRequest();
    }

    private Borrow setReturnStatus(Borrow borrow, boolean isSelfService) {
        Objects.requireNonNull(borrow);
        borrow.setEnable(false);
        if (isSelfService) {
            borrow.getCopy().setStatus(CopyStatus.WAIT_FOR_VERIFY);
        } else {
            borrow.getCopy().setStatus(CopyStatus.AVAILABLE);
        }
        borrow.setCopy(this.simpleCopyRepository.save(borrow.getCopy()));
        return borrow;
    }

}