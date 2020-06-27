## 카카오페이 서버개발 사전과제

> 카카오페이 뿌리기 기능 구현하기

### 개발환경

- Java 1.11
- Spring Boot 2.3.1.RELEASE
- JPA
- Lombok
- Gradle
- Swagger

#### 실행방법
```
#java -jar kakaopay-0.0.1-SNAPSHOT.jar
```

### API 명세(swagger : http://localhost:8080/swagger-ui.html)

#### 돈 뿌리기 API 

POST /v1/seeds
* 요청 헤더

| Key      | Value  | 설명                 |
| -------- | ------ | -------------------- |
| X-USER-ID | 1   | 유저 아이디 |
| X-ROOM-ID | room1  | 대화방 식별키 |
* 요청바디

```json
{
  "seedingAmount" : 100,
  "receivedPersonCount": 3
}
```

* 응답값

```json
{
  "code": 20000,
  "message": "success",
  "data": {
    "token": "o4m"
  }
}
```

* 에러

| 에러 코드       | 설명                                                         |
| :-----  | ------------------------------------------------------------ |
| 50000  | 시스템 에러      |


#### 받기 API

PUT /v1/seeds

* 요청 헤더

| Key      | Value  | 설명                 |
| -------- | ------ | -------------------- |
| X-USER-ID | 1   | 유저 아이디 |
| X-ROOM-ID | room1  | 대화방 식별키 |
* 요청 바디
```json
{
  "token" : "Hcw"
}
```

* 응답값

```json
{
  "code": 20000,
  "message": "success",
  "data": {
    "receivedAmount": 2
  }
}
```

* Error

| 에러 코드       | 설명                                                         |
| :-----  | ------------------------------------------------------------ |
| 40001  | 토큰이 존재하지않는 경우      |
| 40902  | 요청건에 대해 이미 받기가 완료된경우      |
| 40903 | 자신이 뿌리기한 건은 받을수 없는 예외 |
| 40904 | 동일한 대화방에 속한 사용자가 아닐경우 |
| 40905 | 유효시간 초과(10분) |
| 50000  | 시스템 에러      |

#### 조회 API

GET /v1/seeds

* 요청 헤더

| Key      | Value  | 설명                 |
| -------- | ------ | -------------------- |
| X-USER-ID | 1   | 유저 아이디 |
* 요청 파라미터

| Key      | Value  | 설명                 |
| -------- | ------ | -------------------- |
| token | Hcw   | 토큰 아이디 |
* 응답값

```json

{
  "code": 20000,
  "message": "success",
  "data": {
    "seedingAmount": 100,
    "receivedCompletedAmount": 3,
    "createdAt": "2020-06-27T11:04:51",
    "receivedCompletedInfos": [
      {
        "receivedUserId": null,
        "receivedAmount": 97
      },
      {
        "receivedUserId": "1",
        "receivedAmount": 2
      },
      {
        "receivedUserId": "3",
        "receivedAmount": 1
      }
    ]
  }
}
```

| 필드명 | 응답값                               | 설명                                                         |
| :----- | ------------------------------------ | ------------------------------------------------------------ |
| seedingAmount   | long                            | 뿌린금액      |
| receivedCompletedAmount | long | 받기 완료된 금액 |
| createdAt | LocaleDateTime | 뿌린시각|
| receivedCompletedInfos | list | 받기 완료된 정보(받은금액,받은사용자 아이디)|

* 에러

| 에러 코드       | 설명                                                         |
| :-----  | ------------------------------------------------------------ |
| 40001  | 토큰이 존재하지않는 경우      |
| 40906  | 자기가 요청한 토큰이 아닌경우      |
| 40907  | 뿌린건이 7일이 넘어간경우      |
| 50000  | 시스템 에러      |


### Entity
#### bank
- 은행의 기본 정보를 가지고 있음
```
bank_id : bigint (PK / GeneratedValue)
bank_name : varchar 
```
#### bank_finance
- 은행의 금융 현황을 가지고 있음 / 논리적으로 bank 테이블과 1:n 관계
```
bank_finance_id : bigint (PK / GeneratedValue)
bank_id : bigint (FK)
year : int 
month : int
amount : int
```
#### user
- 은행의 금융 현황을 가지고 있음 / 논리적으로 bank 테이블과 1:n 관계
```
user_id : bigint (PK / GeneratedValue)
user_name : varchar (UK)
user_password : varchar (Password encode)
```
* `DelegatingPasswordEncoder`를 사용한 Password 암호화 결과 
    * (username = "test",user_password = "1234) 
    
![image]()

### 핵심 문제해결 전략

- 공통 해결 전략
  - 스탠다드 언론 프로그램 개발 목적 
  - 다른 플랫폼(ex. MySQL) 에 종속 되지 않도록 구현한다. (ex. 내장형 DB 사용)
 
- [x] CSV -> DB 로 저장하는 API 개발 
  - 어플리케이션이 시작될때 로컬에 저장하고 있는 CSV 파일을 읽고 DB 에 로드한다.
    - 저장하고 있는 CSV 파일이 없다면 에러로그를 찍고 정상 구동에 실패 하도록 한다.
     
- [x] 주택금융 공급 금융기관(은행) 목록을 출력하는 API 를 개발

  - 최상위 Entity 가 각 은행의 목록을 가지고 있다.

- [x] 년도별 각 금융기관의 지원금액 합계를 출력하는 API 를 개발

  - bank_id 와 year 가 row 끼리 묶으면 된다.
  - Comparable 인터페이스를 구현해 연도별 정렬한다.

- [x] 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API 개발

  - Comparable 인터페이스를 구현해 비교한다.
  - Stream 식으로 비교

- [x] 전체 년도(2005~2016)에서 특정은행의 지원금액 평균 중에서 가장 작은 금액과 큰
  금액을 출력하는 API 개발

  - 각 년도별 비교하는 메소드 구현 

- [x] 특정 은행의 특정 달에 대해서 2018 년도 해당 달에 금융지원 금액을 예측하는 API
  개발

  - 정확도가 떨어지더라도 다른 분석 플랫폼에 의존적이지 않도록 구현을 하도록 하였다. 
  - 바로 적용 가능한 [java-timeseries](<https://github.com/signaflo/java-timeseries>) 오픈소스 라이브러리 사용
  - 차후 다른 모델이 사용될 수 있으므로 PredictFinance 인터페이스를 작성 후 Arima 모델을 사용하는 클래스로 구현
  - 향 후 파라메터 조정을 통한 정확도 조정 계획 

- [x] API 인증을 위해 JWT 사용

  - Account API 를 따로 분리 할 수 있도록 패키지화 
  - Resource API 는 Interceptor 를 구현해서 Authorization 헤더 검증 로직 구현
  
  
 


  

  