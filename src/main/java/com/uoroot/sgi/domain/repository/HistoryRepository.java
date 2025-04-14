package com.uoroot.sgi.domain.repository;

import java.util.List;

import com.uoroot.sgi.domain.model.History;

public interface HistoryRepository {

    List<History> findAll();

    History findById(Long id);

}
