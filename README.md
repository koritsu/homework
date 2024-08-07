
```markdown
# 템플릿

## 개요

이 애플리케이션은 브랜드를 관리하기 위해 설계된 Spring Boot 기반의 애플리케이션입니다. 
CRUD 작업을 위한 RESTful API를 제공하며, 애플리케이션의 견고성을 보장하기 위해 포괄적인 테스트를 포함하고 있습니다.

## 기능

- 브랜드 관리
- CRUD 작업을 위한 RESTful API 제공
- H2 데이터베이스 사용
- Mockito 및 Rest Assured를 사용한 포괄적인 단위 및 통합 테스트

## 요구 사항

- Java 17 이상
- Gradle


## 시작하기

### 저장소 클론

```bash
git clone https://github.com/koritsu/homework.git
cd homework
```

### 설정

다양한 환경을 위한 애플리케이션 속성을 구성합니다. 기본 구성은 H2 메모리 내 데이터베이스를 사용합니다.

#### `src/main/resources/application.properties`

```properties
# 테스트 환경용 데이터베이스 설정
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

# SQL 스크립트 위치
spring.datasource.schema=classpath:h2sql/schema.sql
spring.datasource.data=classpath:h2sql/data.sql

# H2 콘솔 설정 (개발용)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console


```

### 빌드 및 실행

Gradle을 사용하여 프로젝트를 빌드하고 애플리케이션을 실행합니다.

```bash
./gradlew clean build
./gradlew bootRun
```

애플리케이션이 실행된 후 브라우저에서 `http://localhost:20000`으로 접속할 수 있습니다.

### 테스트

테스트를 실행하여 애플리케이션의 기능을 검증합니다.

```bash
./gradlew test
```

### 브랜드 API

- **생성**: `POST /v1/brands`
- **목록 조회**: `GET /v1/brands`
- **조회**: `GET /v1/brands/{id}`
- **수정**: `PUT /v1/brands/{id}`
- **삭제**: `DELETE /v1/brands/{id}`

