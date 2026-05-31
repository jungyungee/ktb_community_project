package com.ktb.community.user;

import org.springframework.data.jpa.repository.JpaRepository;

// 유저 엔티티를 DB에 실제로 저장, 조회하는 기능
public interface UserRepository extends JpaRepository<User, Long>{
    // 조회, 저장, 삭제 등은 기본은 JpaRepository에 정의되어 있는 것을 상속받아서 쓴다.
}
