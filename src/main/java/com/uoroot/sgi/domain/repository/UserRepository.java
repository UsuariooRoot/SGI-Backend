package com.uoroot.sgi.domain.repository;

import com.uoroot.sgi.domain.model.User;

public interface UserRepository {

    User findByUsername(String username);

    boolean existsByUsername(String username);

    User save(User user);

}
