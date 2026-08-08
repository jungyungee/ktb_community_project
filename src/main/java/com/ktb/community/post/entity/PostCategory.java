package com.ktb.community.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "post_categories")
// 게시글 종류의 코드와 화면 표시 이름을 저장하는 엔티티
public class PostCategory {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 프론트와 API에서 사용하는 고유 식별 코드
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    // 화면에 표시할 게시판 이름
    @Column(nullable = false, length = 20)
    private String name;

    // 초기 카테고리 데이터 생성용 생성자
    public PostCategory(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
