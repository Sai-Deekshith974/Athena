package com.athena.service;

import com.athena.exception.http.IdOfResourceNotFoundException;
import com.athena.exception.http.IllegalEntityAttributeException;
import com.athena.model.Publication;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tommy on 2017/10/1.
 */
public interface PublicationService<T extends Publication, K extends Serializable> extends ModelCRUDService<T, K> {

    List<T> get(Iterable<K> ks);

    @Transactional
    default List<T> update(Iterable<T> ts) throws IdOfResourceNotFoundException, IllegalEntityAttributeException {
        List<T> result = new ArrayList<>();
        for (T t : ts) {
            result.add(this.update(t));
        }
        return result;
    }

    List<T> add(Iterable<T> ts);
}
