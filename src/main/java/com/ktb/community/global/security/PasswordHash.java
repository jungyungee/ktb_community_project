package com.ktb.community.global.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

// 스프링이 객체를 생성하고 관리하도록
// component 어노테이션을 붙임
@Component
// 비밀번호 해싱 및 검증 을 위한 컴포넌트
public class PasswordHash {

    // 비밀번호를 받아서 비밀번호와 랜덤 salt를 넣고 hash 함
    public String hash(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // 입력받은 비밀번호와 해시된 비밀번호를 비교해서 결과값 반환
    public boolean matches(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }
}
