<div align="center">

# T R I P F E E D

**여행 정보 공유와 동행 모집을 위한 커뮤니티**

여행 정보, 후기, 질문을 공유하고  
일정에 맞는 동행을 찾을 수 있습니다.

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker&logoColor=white)](https://www.docker.com/)
[![GitHub Actions](https://img.shields.io/badge/CI-GitHub_Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white)](https://github.com/jungyungee/ktb_community_project/actions)
[![Argo CD](https://img.shields.io/badge/CD-Argo_CD-EF7B4D?style=flat-square&logo=argo&logoColor=white)](https://argo-cd.readthedocs.io/)

</div>

<div align="center">

<img width="1469" height="870" alt="TRIPFEED 메인 화면" src="https://github.com/user-attachments/assets/867de4a0-9223-407b-839d-eeccc5e73294" />

</div>


---

## Service Overview

**TRIPFEED**는 여행 정보를 공유하고 동행을 모집할 수 있는 커뮤니티 서비스입니다.

사용자는 여행 정보와 후기를 작성하고, 궁금한 내용을 질문하거나 여행 일정에 맞는 동행을 구할 수 있습니다.
특히 후기 페이지는 이미지 중심의 피드 형식으로 제공된다는 특징이 있습니다.

이 저장소는 서비스의 백엔드 API를 담당합니다. JWT 인증, 커서 기반 페이지네이션, 중복 조회 방지, 동시성을 고려한 카운트 처리와 S3 Presigned URL 이미지 업로드를 지원합니다.

### Community Boards

| 🗺️ 정보 | 📸 후기 | 💬 질문 | 🧳 동행 |
| :---: | :---: | :---: | :---: |
| 여행지 정보를 공유합니다 | 여행을 피드 형식으로 기록합니다 | 여행 관련 질문을 등록합니다 | 일정에 맞는 동행을 모집합니다 |

## Screenshots


| 메인 페이지 | 후기 피드 페이지 | 피드 게시글 상세 페이지 |
| :---: | :---: | :---: |
| <img width="1470" height="872" alt="스크린샷 2026-08-10 오전 2 11 21" src="https://github.com/user-attachments/assets/302adddf-89ce-46b1-b111-da7f0d8b4c62" /> | <img width="1470" height="871" alt="스크린샷 2026-08-10 오전 2 11 53" src="https://github.com/user-attachments/assets/8c5a0435-0824-4aa1-b5b3-366e4530c685" /> | <img width="1470" height="870" alt="스크린샷 2026-08-10 오전 2 11 40" src="https://github.com/user-attachments/assets/7d70682b-f0fb-4ee4-976b-b0b90cc0176d" />

## Demo

https://github.com/user-attachments/assets/dbd216d5-8020-4e57-ae1d-73f2d049f217

## Core Features

| 기능 | 설명 |
| --- | --- |
| **회원 및 인증** | 회원가입, 로그인, 로그아웃, 토큰 재발급, 프로필·비밀번호 관리 |
| **여행 게시판** | 정보·후기·질문·동행 카테고리별 게시글과 커서 기반 목록 조회 |
| **댓글** | 댓글 작성·조회·수정·삭제와 커서 기반 목록 조회 |
| **좋아요** | 게시글 좋아요 등록·취소를 하나의 토글 API로 처리 |
| **조회수** | 회원과 비회원을 구분하고 동일 사용자의 중복 조회를 방지 |
| **이미지** | S3 Presigned URL을 발급해 클라이언트가 스토리지에 직접 업로드 |
| **공통 응답** | 일관된 API 응답 포맷과 전역 예외 처리 제공 |

## Tech Stack

| Category | Technology |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4, Spring MVC |
| ORM / Query | Spring Data JPA, QueryDSL |
| Database | MySQL 8, Amazon RDS |
| Security | JWT, BCrypt |
| File Storage | Amazon S3 |
| Image Upload | S3 Presigned URL |
| Build | Gradle 9 |
| Infra | Docker, Kubernetes, Helm, Traefik |
| CI | GitHub Actions |
| CD | Argo CD |
| Image Registry | Docker Hub |


## ERD

<div align="center">

<img width="644" height="618" alt="TRIPFEED ERD" src="https://github.com/user-attachments/assets/f5112cd0-bd64-4b4b-a8f8-e61df5c1f6ec" />

</div>


## Infrastructure Architecture

<div align="center">

<img width="584" height="566" alt="TRIPFEED AWS 인프라 아키텍처" src="https://github.com/user-attachments/assets/9bfd1804-6f97-4ee0-94a1-7464f4cb35ea" />

</div>

프론트엔드는 Public Subnet의 EC2에서, 백엔드는 Private Subnet의 Kubernetes 클러스터에서 운영합니다. 사용자 요청은 Route 53과 Application Load Balancer를 거쳐 각 서비스로 전달되며, 데이터는 Amazon RDS에 저장합니다. 이미지는 S3 Presigned URL을 이용해 클라이언트에서 Amazon S3로 직접 업로드합니다.


## API Overview

<details>
<summary>전체 API 목록 보기</summary>

<br>

| Domain | Method | Endpoint | Description |
| --- | --- | --- | --- |
| Health | `GET` | `/health` | 서버 상태 확인 |
| Auth | `POST` | `/auth` | 로그인 |
| Auth | `POST` | `/auth/refresh` | 액세스 토큰 재발급 |
| Auth | `POST` | `/auth/logout` | 로그아웃 |
| User | `POST` | `/users` | 회원가입 |
| User | `GET` | `/users/me` | 내 정보 조회 |
| User | `PATCH` | `/users/me` | 내 정보 수정 |
| User | `PATCH` | `/users/me/password` | 비밀번호 변경 |
| User | `DELETE` | `/users/me` | 회원 탈퇴 |
| Post | `POST` | `/posts` | 게시글 작성 |
| Post | `GET` | `/posts` | 게시글 목록 조회 |
| Post | `GET` | `/posts/{postId}` | 게시글 상세 조회 |
| Post | `PATCH` | `/posts/{postId}` | 게시글 수정 |
| Post | `DELETE` | `/posts/{postId}` | 게시글 삭제 |
| Category | `GET` | `/post-categories` | 게시글 카테고리 조회 |
| Comment | `POST` | `/posts/{postId}/comments` | 댓글 작성 |
| Comment | `GET` | `/posts/{postId}/comments` | 댓글 목록 조회 |
| Comment | `PATCH` | `/comments/{commentId}` | 댓글 수정 |
| Comment | `DELETE` | `/comments/{commentId}` | 댓글 삭제 |
| Like | `POST` | `/posts/{postId}/likes` | 좋아요 등록·취소 |
| Image | `POST` | `/images/presigned-url` | S3 업로드 URL 발급 |

게시글 목록은 `cursor`와 `categoryCode`, 댓글 목록은 `cursor` 쿼리 파라미터를 지원합니다.

</details>

## Project Structure

백엔드 애플리케이션은 도메인 단위의 `controller → service → repository` 구조를 따릅니다. 공통 인증, 예외, 응답 처리는 `global` 영역에서 관리합니다.

```text
src/main/java/com/ktb/community
├── comment       # 댓글 도메인
├── global        # 인증, 예외, 공통 응답, 헬스 체크
├── image         # S3 Presigned URL
├── like          # 게시글 좋아요
├── post          # 게시글, 카테고리, 조회 이력
└── user          # 회원 및 인증

deploy
├── helm          # Kubernetes Helm Chart
└── traefik       # Ingress 설정
```

## Deployment

GitHub Actions를 통해 Docker 이미지 빌드와 헬스 체크를 수행하고, `dev` 브랜치 반영 시 이미지를 Docker Hub에 푸시합니다. Argo CD는 Helm 설정을 기준으로 Kubernetes 배포 상태를 동기화합니다.

```text
Pull Request / dev Push
          ↓
GitHub Actions (CI)
          ↓
 Build · Health Check
          ↓ dev push
 Docker Hub Image Push

Git Repository
      ↓ sync
Argo CD (CD)
      ↓
Kubernetes → Traefik
```
