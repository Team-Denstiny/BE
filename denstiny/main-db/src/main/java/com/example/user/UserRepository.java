package com.example.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByNickName(String nickname);

    Optional<UserEntity> findFirstByEmailAndPassword(String email, String password);

    UserEntity findByResourceId(String resourceId);
    // 닉네임 중복 검증
    boolean existsByNickName(String nickName);
}
