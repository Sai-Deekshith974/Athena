package com.athena.repository.jpa;

import com.athena.model.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Tommy on 2017/6/9.
 */
@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {

}
