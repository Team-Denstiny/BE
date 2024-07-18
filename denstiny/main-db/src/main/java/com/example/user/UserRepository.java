package com.example.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByNickName(String nickname);

    Optional<UserEntity> findFirstByEmailAndPassword(String email, String password);
}
