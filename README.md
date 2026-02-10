# catdogwatshong

Spring Boot(2.5.4) + Thymeleaf 기반 쇼핑몰 프로젝트입니다.

## 가장 빠른 배포(추천)
Oracle 의존성이 있으므로 `앱 + Oracle XE`를 Docker Compose로 같이 띄우는 방식이 가장 빠릅니다.

### 1) 로컬에서 배포 패키지 준비
```bash
cp .env.example .env
# .env 값 수정(비밀번호, 메일 계정)
```

### 2) 서버 준비
- 가장 빠름/저렴: `Oracle Cloud Always Free VM`
- AWS를 꼭 쓸 경우: `Lightsail(최저 플랜)`

서버에 Docker/Compose 설치 후 프로젝트 업로드:
```bash
git clone <your-repo-url>
cd catdogwatshong
cp .env.example .env
# .env 값 수정
```

### 3) 실행
```bash
docker compose up -d --build
```

### 4) 접속
- `http://서버공인IP:8080`

## 운영 시 필수 보안 작업
- 기존 `application.properties`에 노출된 메일 비밀번호는 즉시 변경(회전)하세요.
- 보안그룹/방화벽에서 외부 공개는 `80/443`만 허용하고, `1521`은 내부망만 허용하세요.
- 가능하면 Nginx + Let's Encrypt(HTTPS)까지 설정하세요.

## 환경변수
주요 환경변수는 아래와 같습니다.
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_MAIL_USERNAME`
- `SPRING_MAIL_PASSWORD`
- `RESOURCE_PATH` (기본값 `file:/app/upload/`)
