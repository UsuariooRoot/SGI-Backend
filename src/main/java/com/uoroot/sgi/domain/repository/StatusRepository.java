package com.uoroot.sgi.domain.repository;

import java.util.List;

import com.uoroot.sgi.domain.model.Status;

public interface StatusRepository {

    List<Status> findAll();

    Status findById(Integer id);

    boolean existsById(Integer id);

}
