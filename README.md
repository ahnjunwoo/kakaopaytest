## 카카오페이 서버개발 사전과제

> 카카오페이 뿌리기 기능 구현하기

### 개발환경

- Java 1.11
- Spring Boot 2.3.1.RELEASE
- JPA
- Lombok
- Gradle
- Swagger
- AWS RDS(MYSQL)

#### 실행방법
```
./gradlew bootRun
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

##모델링
![image](https://raw.githubusercontent.com/ahnjunwoo/kakaopaytest/master/src/main/resources/model.png)

## 엔티티
![image](https://raw.githubusercontent.com/ahnjunwoo/kakaopaytest/master/src/main/resources/received_completed_info.png)


#### user
- 사용자 정보
#### room
- 대화방 정보
#### user_room
- 사용자와 대화방 다대다 관계 연결
#### seed
- 뿌리기 모델을 관리하는 테이블
- 요청/받기/조회시 사용
### received_completed_info
- 뿌리기에 대한 받기 완료된 정보를 관리


### 핵심 문제해결 전략
- 공통 해결 전략
  - swagger 설정하여 API 현황 체크
  - 각각의 모델을 파악하여 aggregate 분리
  - 응답값 공통 처리
  - 응답코드 공통 처리
  
- [x] 돈 뿌리기 API 
  - 뿌리기시 분배로직을 도메인 레벨에서 구현하도록 설계 받기 완료된 정보는 뿌리기라는 도메인과
  함께 항상 동작하기때문에 같은 범위로 잡았다.
  - 분배 금액은 랜덤하게 분배 되도록 했으며 이부분은 자바 Math.random()이용하였다.
  - 토큰은 아파치 API를 사용하여 숫자+영대소문자를 이용하여 예측 불가능 하게 구현하였다.
  - 받기 완료된 정보는 중복이 되서는 안되니 LIST가 아닌 SET으로 구현하였다.
  
- [x] 돈 받기 API 
  - 돈받기시 벨리데이션 체크는 도메인 레벨에서 구현하였다. 서비스 레이어에서 할 경우 로직이 복잡도가 증가한다.
  - 랜덤 하게 분배되어진 정보를 가지고 분배가 되지 않는 첫번째 데이터를 조회하여 분배금을 할당되도록 구현하였다.
  받기가 완료된 경우 받기 완료 정보 컬럼의 유저아이디 값이 null이 아닌 받은 유저 아이디로 갱신되어진다.

- [x] 조회 API 
  - 자기 자신만이 조회되고 7일동안만 조회되도록 구현하였다.
  
  
  
 


  

  