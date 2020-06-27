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


## Entity
![image](https://raw.githubusercontent.com/ahnjunwoo/kakaopaytest/master/src/main/resources/received_completed_info.png)

##### seed
- 




### 핵심 문제해결 전략
  
  
 


  

  