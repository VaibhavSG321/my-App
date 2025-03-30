package com.chatApplication.Authentication.repo;

import com.chatApplication.Authentication.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);
    Users findByUniqueID(String uniqueID);
}
