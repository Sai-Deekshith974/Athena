package com.athena.service;

import com.athena.exception.http.IdOfResourceNotFoundException;
import com.athena.exception.http.ResourceNotDeletable;
import com.athena.model.Publication;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Tommy on 2017/10/1.
 */
public interface PublicationService<T extends Publication, PK extends Serializable> {
    T get(PK pk);

    List<T> get(List<PK> pks);

    T update(T t) throws IdOfResourceNotFoundException;

    @Transactional
    List<T> update(List<T> ts) throws IdOfResourceNotFoundException;

    void delete(T t) throws IdOfResourceNotFoundException, ResourceNotDeletable;

    @Transactional
    void delete(List<T> ts) throws IdOfResourceNotFoundException, ResourceNotDeletable;

    void add(T t);

    void add(List<T> ts);

}