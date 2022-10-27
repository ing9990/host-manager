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
Controller에서 Body를 받을 때 유효성 검증을 거치도록 했습니다.

- 1. Host, Ip가 빈 값이거나 중복일 경우
- 2. format에 맞지 않는 Ip일 경우
```

3. REST API

```
Path: {URI}/api/v1/hosts
- GET: 모든 호스트 조회
- POST: 호스트 등록 requestBody(name,ip) 
- PUT: 호스트 정보 수정 (name,ip)

Path: {URI}/api/v1/hosts/{name}
- GET: 호스트 단건 조회
- DELETE: name을 사용한 호스트 삭제

Response Status
200: 조회, 성공
201: 호스트가 등록됨.
204: 수정 및 삭제 과정에서 변경사항이 없음.

400: name, ip가 중복된 경우
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

5. API 테스트

- GET http://localhost:7777/api/v1/hosts : 호스트 전체 조회 (178ms)
    - ```json {
    "호스트1": {
        "ip": "192.168.150.38",
        "alive": "Disconnected",
        "lastConnection": "2022-10-27T23:36:01.646728",
        "createAt": "2022-10-27T15:57:05.932314",
        "updatedAt": "2022-10-27T23:33:28.641142"
    },
    "구글": {
        "ip": "142.250.196.100",
        "alive": "Connected",
        "lastConnection": "2022-10-27T23:36:01.739568",
        "createAt": "2022-10-27T23:34:32.065159",
        "updatedAt": "2022-10-27T23:34:32.065159"
    },
    "호스트2": {
        "ip": "192.188.235.181",
        "alive": "Disconnected",
        "lastConnection": "2022-10-27T23:36:02.643894",
        "createAt": "2022-10-27T00:50:26.707965",
        "updatedAt": "2022-10-27T23:33:25.648022"
    },
    "네이버": {
        "ip": "180.182.54.1",
        "alive": "Connected",
        "lastConnection": "2022-10-27T23:36:02.677568",
        "createAt": "2022-10-27T16:38:57.587999",
        "updatedAt": "2022-10-27T23:33:29.683564"
    }

}


<hr/>

## 고민한 부분

**1. 호스트에 연결이 끊겼을 경우 서버에서 알아채는 방법**

```
1. Socket으로 모든 호스트와 통신하며 연결 끊김을 감지한다.
 - Host는 REST-API로 post를 통해 등록 될텐데 소켓연결이 가능한가?
 - 장점은 HandShake 이후 연결이 끊기면 바로 알 수 있음.
 
2. Scheduler를 사용해 지속적으로 호스트의 연결을 확인한다. (polling)
 - 서버에서 매 초마다 요청을 보내면 비효율적이지 않을까?

내가 선택한 방법 (2)
```

**2. 100개의 호스트가 모두 Unreachable 상태일 때 1초안에 응답하는 방법**

```
1. 1초 안에 모든 호스트로 요청을 보낼 수 있도록 Timeout을 짧게 줄인다.
2. Socket 연결이 끊길 때 DB에 Host를 update 시킨다.
3. timeout을 기다리지 않고 다음 Host에게 ping을 보내는 방법을 찾는다.

모든 호스트마다 InetAddress.isReachable()의 timeout을 기다리면 비효율적임.
```

<hr/>

### 10/27

```
기본적인 CRUD 개발 완료 
Socket config 작성 완료

고민
- REST 기반에 웹 소켓을 사용할 수 있는가?
- 성능저하 없이 실시간으로 연결끊김을 감지하면서 isReachable()을 사용하라?
- isReachable()의 timeout은?? 그럼 병렬처리인가?? timeout을 짧게??
```

### 10/28

```
- 소켓 로직 제거
- Scheduler를 사용한 방법으로 확정.

timeout을 기다리지 않고 다음 호스트로 Ping을 보내는 방식

fix: Disconnect -> Connect일 경우 lastConnection update.
```

