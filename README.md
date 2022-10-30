# 호스트들의 Alive 상태 체크 및 모니터링 API 서버 개발

<hr/>

## 패턴

1. Response

```
Controller와 Advice의 Response는 ResponseEntity<T>로 통일하였습니다.
Service -> Controller의 Response는 DefaultResponseDtoEntity로 통일했습니다.
```

2. Validation

```
Controller에서 Body를 받을 때 어노테이션을 사용한 유효성 검증을 거치도록 했습니다.

DuplicatedHostConstraint
- 1. Host, Ip가 빈 값이거나 중복일 경우

DuplicatedIpConstraint
- 2. format에 맞지 않는 Ip일 경우
```

3. EndPoint

- [API 테스트 바로가기](https://documenter.getpostman.com/view/19080293/2s8YK4rmjm)

| EndPoint             | METHOD | Description | Param          | Required |
|----------------------|--------|-------------|----------------|----------|
| /api/v1/hosts        | GET    | 호스트 전체 조회   |                |          |
| /api/v1/hosts        | POST   | 호스트 등록      | Body(name, ip) | O        |
| /api/v1/hosts        | PUT    | 호스트 수정      | Body(name, ip) | O        |
| /api/v1/hosts/{name} | GET    | 호스트 단건 조회   | Path(name)     | O        |
| /api/v1/hosts/{name} | DELETE | 호스트 삭제      | Path(name)     | O        |

- 유효성 검사 성공 응답

```json
{
  "message": "Host registered successfully.",
  "httpStatus": "CREATED"
}
```

- 유효성 검사 실패 응답

```json

{
  "statusCode": 400,
  "requestUrl": "/api/v1/hosts",
  "errorList": [
    {
      "field": "hostName",
      "message": "Host name is empty",
      "path": "/api/v1/hosts"
    },
    {
      "field": "ip",
      "message": "Invalid ip address.",
      "path": "/api/v1/hosts"
    }
  ]
}
```

- HostNotFound 핸들링 (404)

```java

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class HostNotFoundException extends RuntimeException {
    public HostNotFoundException(String message) {
        super("Host not found: " + message);
    }
}
```

4. DDL

```mysql
create table host
(
    host_id              bigint auto_increment
        primary key,
    host_alive           varchar(255) null,
    host_created         datetime(6)  null,
    host_ip              varchar(255) not null,
    host_last_connection datetime(6)  null,
    host_name            varchar(255) not null,
    host_updated         datetime(6)  null,
    constraint "[제약조건 이름1]"
        unique (host_name),
    constraint "[제약조건 이름2]"
        unique (host_ip)
);
```

<hr/>

## 고민한 부분

**1. 호스트에 연결이 끊겼을 경우 서버에서 알아채는 방법**

```
@Scheduler
스케쥴러가 fixedDelay마다 데이터베이스에 저장된 Hosts를 대상으로 pingTest를 실행함.
 
@Async MultiThread
다중 스레드이기 때문에 timeout를 기다리는 동안 다른 Thread가 Job을 실행.
```

<hr/>

### 10/27

```
기본적인 CRUD 개발 완료 
Socket config 작성 완료
```

### 10/28

```
소켓 로직 제거
Scheduler를 사용한 방법으로 확정.

fix: Disconnect -> Connect일 경우 lastConnection update.
```

### 10/30

```
- Host 100개 무작위 등록 후 조회 테스트
- CORS 로직 추가
- @Async를 활용한 ThreadPool 사용
- Scheduler fixedDelay를 1000ms로 변경 

* Hosts Disconnected 98개 Connected 2개로 테스트 
- 조회 (132ms)
- 연결 끊김 시 실시간 감지 가능 (최대 1000ms)
```

