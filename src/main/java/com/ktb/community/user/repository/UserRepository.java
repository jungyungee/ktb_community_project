package com.ktb.community.user.repository;

import com.ktb.community.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 유저 엔티티를 DB에 실제로 저장, 조회하는 기능
public interface UserRepository extends JpaRepository<User, Long>{
    // 조회, 저장, 삭제 등은 기본은 JpaRepository에 정의되어 있는 것을 상속받아서 쓴다.
    // SELECT, FIND, CREATE, UPDATE 등 직접 정의할 필요가 없다!!

    // Query Method
    // 중복 조회를 위한 메서드 정의 (이 역시 구현은 JPA가)
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmail(String email); //결과가 없을 수도 있음
}
