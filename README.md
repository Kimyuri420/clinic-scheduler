# Clinic Scheduler

Java 17과 Spring Boot로 만드는 진료 일정·예약 API 개인 프로젝트입니다.

## 현재 구현한 기능

- `GET /api/demo/slots`: 09:00~18:00 진료 시간에서 12:00~13:00 점심시간을 제외한 30분 단위 슬롯 16개를 반환합니다.

## 로컬 실행 방법

```bat
.\mvnw.cmd spring-boot:run
```

서버를 실행한 뒤 브라우저에서 [예약 시간 조회 API](http://localhost:8080/api/demo/slots)를 열어 확인합니다.

## 다음에 구현할 기능

- 예약 등록과 저장
- 일정 변경 시 기존 예약과의 충돌 검사
