package com.core.backend.data.repository;

import com.core.backend.data.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByNickname(String nickName);

    Optional<Users> findByAccountId(String accountId);
}
