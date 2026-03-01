# CatDogWatShong

실사용 흐름(회원가입 → 로그인 → 스토어 탐색/구매 + 커뮤니티 활동)까지 구현한  
`Spring Boot + Thymeleaf + PostgreSQL` 기반 하이브리드 웹(커머스 + 커뮤니티) 포트폴리오 프로젝트입니다.

## Service Status
- 현재는 PostgreSQL 기준으로 로컬/클라우드 재배포가 가능하도록 구성했습니다.
- 무료 호스팅 후보(Render/Koyeb 등)에 맞춰 DB 의존성을 경량화했습니다.

## Previous Live URL
- **서비스 주소: `http://158.180.70.210:8080`**
- **헬스체크: `http://158.180.70.210:8080/api/health`**

## Why This Project
- 서버 사이드 렌더링(SSR) 구조에서 `스토어 도메인`과 `커뮤니티 도메인`을 한 서비스 안에서 함께 운영하는 경험을 보여주기 위해 만들었습니다.
- 단순 CRUD가 아니라, 실제 사용자 시나리오 중심으로 기능을 통합했습니다.
- Docker Compose로 `앱 + PostgreSQL`을 함께 띄워, 로컬/서버 재현성을 높였습니다.

## Core Features
- 회원
- 이메일 인증 기반 가입 활성화
- 로그인 실패 카운트/차단 처리
- USER/SELLER/ADMIN 권한 분리

- 상품
- 카테고리 기반 조회
- 상품 등록(SELLER/ADMIN)
- 메인 진열(신상품/인기상품)

- 장바구니/주문
- 수량 증감, 선택 삭제, 총액 계산
- 주문 생성 및 주문 내역 확인
- 배송지 선택 기반 결제 단계 진입

- 리뷰
- 주문 상품 단위 리뷰 작성
- 주문내역에서 리뷰 작성 링크 제공

- 커뮤니티
- 자유/공지/Q&A/이미지 게시판 운영
- 댓글/추천/신고 흐름
- 사용자 참여형 콘텐츠 탐색(목록/읽기/검색)

- 운영 API
- 서버 상태 확인용 `/api/health` 제공

## Tech Stack
- Backend: Java 21, Spring Boot 3.3.x, Spring Security 6, Spring Data JPA, QueryDSL (Jakarta)
- Frontend: Thymeleaf, jQuery, Bootstrap, HTML/CSS
- DB: PostgreSQL 16
- Infra/DevOps: Docker, Docker Compose, Render Blueprint 대응

## Deployment
PostgreSQL 기준으로 `앱 + DB`를 Compose로 함께 올리거나, Render 같은 호스팅에 앱/DB를 분리 배포할 수 있습니다.

```bash
cp .env.example .env
# .env 값 수정 (DB/메일 계정 등)

docker compose up -d --build
```

접속:
- `http://158.180.70.210:8080` (위 Live URL 참고)

## Local Build (IntelliJ)
- Project SDK / Maven JDK를 `Java 21`로 맞춘 뒤 빌드하세요.
- Annotation Processing을 활성화해야 QueryDSL Q-class가 생성됩니다.

```bash
./mvnw -DskipTests clean compile
```

## Environment Variables
- `POSTGRES_DB`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`
- `SPRING_DATASOURCE_URL` (외부 DB 사용 시)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_MAIL_USERNAME`
- `SPRING_MAIL_PASSWORD`
- `APP_BASE_URL`
- `RESOURCE_PATH` (default: `file:/app/upload/`)
- `APP_UPLOAD_PRODUCTIMAGE_DIR` (default: `/app/upload/productimage`)

## Render Deployment
- 루트의 `render.yaml`을 사용하면 웹 서비스와 PostgreSQL을 함께 생성할 수 있습니다.
- 기본 흐름: GitHub 저장소 연결 -> Render에서 Blueprint 배포 -> 민감값만 입력
- 무료 플랜은 슬립/용량 제한이 있으므로 포트폴리오 공개용으로 적합합니다.
- Render 무료 웹 서비스는 SMTP `587` 아웃바운드가 제한되므로, 가입 메일 기능은 별도 메일 API/허용 포트 대응이 필요합니다.
- Render 무료 PostgreSQL은 공식 문서 기준 30일 후 만료되므로 장기 운영용으로는 부적합합니다.

## Portfolio Highlights (Recruiter Focus)
- 하이브리드 서비스 설계: 스토어와 커뮤니티를 단일 인증/권한 체계로 통합
- 도메인 연결 능력: 인증/인가, 주문, 리뷰, 게시판 기능을 하나의 서비스 흐름으로 구현
- 운영 이슈 대응: 배포 환경(OS/경로/권한/포트/DB) 문제를 코드/설정으로 해결
- 배포 재현성: Docker Compose 기반으로 동일한 실행 환경 제공
- 제품 감각: 메인 진열, 주문/리뷰 UX, 접근 메시지(인증/권한) 개선

## Notes
- 포트폴리오 목적의 MVP입니다.
- 결제는 테스트/연동 단계 기준이며, 상용 PG 운영 전 검증과 보안 설정이 필요합니다.
