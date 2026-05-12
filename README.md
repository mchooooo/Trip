# TripAI

TripAI는 사용자의 여행 조건을 바탕으로 여행지를 추천해주는 백엔드 프로젝트입니다.
OpenAI API를 활용해 추천 결과와 추천 이유를 생성합니다.



## MVP

- 사용자가 자유 텍스트와 선택형 조건으로 여행 요청을 입력한다.
- 서버가 조건에 맞는 여행지 1곳과 추천 이유를 생성한다.
- 생성된 추천 결과를 DB에 저장한다.
- 저장된 추천 결과를 목록/단건으로 조회한다.

입력 예시는 아래와 같습니다.

- 추가 요청사항
- 동행 유형 (선택)
- 예산 수준 (선택)
- 여행 스타일 (선택)
- 여행 계절 (선택)

## Tech Stack

- Java 17
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- H2 Database
- Spring Validation
- Spring AI
- Swagger UI (`springdoc-openapi`)
- Lombok

## Features

- `POST /api/recommendations`
  - 자유 텍스트 요청과 선택형 조건을 받아 추천 결과를 생성하고 저장합니다.
- `GET /api/recommendations`
  - 저장된 추천 결과 목록을 조회합니다.
- `GET /api/recommendations/{id}`
  - 저장된 추천 결과 단건을 조회합니다.
- `/`
  - Thymeleaf 기반 입력 페이지에서 추천 생성 흐름을 바로 테스트할 수 있습니다.

## AI Provider Mode

이 프로젝트는 추천 생성 로직을 인터페이스로 분리해두었습니다.

- `mock`
  - 기본값입니다.
  - 실제 OpenAI를 호출하지 않고, 미리 정한 규칙으로 추천 결과를 반환합니다.
  - 결제 전, 프론트 연동 전, API 설계 단계에서 빠르게 개발하기 좋습니다.
- `openai`
  - Spring AI를 통해 OpenAI API를 실제 호출합니다.

관련 코드는 아래에 있습니다.

- [RecommendationAiClient.java](/Users/ch/study/side_project/tripAI/src/main/java/com/my/proj/tripai/recommendation/service/RecommendationAiClient.java)
- [MockRecommendationAiClient.java](/Users/ch/study/side_project/tripAI/src/main/java/com/my/proj/tripai/recommendation/service/MockRecommendationAiClient.java)
- [OpenAiRecommendationClient.java](/Users/ch/study/side_project/tripAI/src/main/java/com/my/proj/tripai/recommendation/service/OpenAiRecommendationClient.java)

## Project Structure

```text
src/main/java/com/my/proj/tripai
├── recommendation
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── repository
│   └── service
└── TripaiApplication.java
```

역할은 아래처럼 나뉩니다.

- `controller`
  - HTTP 요청/응답 처리
- `domain`
  - JPA 엔티티
- `dto`
  - 요청/응답 데이터 구조
- `repository`
  - DB 접근
- `service`
  - 추천 생성, 저장, 조회 비즈니스 로직

## Run

### 1. 기본 실행

기본 실행은 `mock` 모드입니다.

```bash
./gradlew bootRun
```

### 2. OpenAI 모드 실행

환경변수에 API 키를 넣고 `openai` 프로필로 실행합니다.

```bash
export OPENAI_API_KEY=your_api_key
./gradlew bootRun --args='--spring.profiles.active=openai'
```


## API Example

### Create Recommendation

`POST /api/recommendations`

Request

```json
{
  "userPrompt": "부모님 모시고 조용하고 걷기 편한 국내 여행지 추천해줘",
  "companionType": "가족",
  "budgetLevel": "중간",
  "travelStyle": "힐링",
  "season": "가을"
}
```

Response

```json
{
  "id": 1,
  "destination": "공주",
  "companionType": "가족",
  "budgetLevel": "중간",
  "travelStyle": "힐링",
  "season": "가을",
  "userPrompt": "부모님 모시고 조용하고 걷기 편한 국내 여행지 추천해줘",
  "promptSummary": "가족와 함께 중간 예산으로 힐링 여행을 가을에 가고 싶어 함. 추가 요청사항은 \"부모님 모시고 조용하고 걷기 편한 국내 여행지 추천해줘\"입니다.",
  "reason": "공주는 힐링 여행에 잘 맞고, 사용자가 남긴 추가 요청사항인 \"부모님 모시고 조용하고 걷기 편한 국내 여행지 추천해줘\"도 함께 반영하기 좋은 추천지입니다.",
  "createdAt": "2026-05-07T21:00:00"
}
```

선택형 조건 없이 자유 텍스트만으로도 요청할 수 있습니다.

```json
{
  "userPrompt": "서울에서 출발해서 1박 2일로 다녀오기 좋은 국내 여행지 추천해줘"
}
```

## Today Updates

- 자유 텍스트 입력 `userPrompt`를 추천 생성의 필수값으로 추가했습니다.
- `동행 유형`, `예산 수준`, `여행 스타일`, `여행 계절`은 선택 입력으로 변경했습니다.
- OpenAI 프롬프트가 자유 텍스트 요청을 반드시 반영하도록 확장했습니다.
- Thymeleaf 입력 페이지에서 자유 텍스트 기반 추천을 바로 테스트할 수 있게 수정했습니다.
- 추천 결과와 히스토리 화면에서 사용자가 입력한 추가 요청사항을 함께 확인할 수 있습니다.


## Test

```bash
./gradlew test
```

## Notes

- 현재 기본 `mock` 구현은 실제 AI 추론이 아니라 규칙 기반 예시 응답입니다.
- OpenAI 응답은 JSON 문자열 형식으로 받도록 프롬프트를 구성해두었습니다.
- OpenAI 호출 시 선택형 입력이 비어 있으면 내부적으로 `미입력`으로 정규화해 프롬프트에 전달합니다.
- MVP 단계이므로 인증/인가, 사용자 계정, 운영 DB, 외부 여행 데이터 연동은 아직 포함하지 않았습니다.

## Next Ideas

- 여행 지역 선호도 입력 추가
- 출발 지역, 이동 수단, 여행 기간 같은 세부 조건 추가
- 추천 결과에 지역, 한줄 요약, 여행 팁 필드 추가
