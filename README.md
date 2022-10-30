# 호스트들의 Alive 상태 체크 및 모니터링 API 서버 개발

<hr/>

## EndPoint

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

- 연결된 호스트가 100개 이상일 때
```json
{
    "message": "More than 100 hosts are connected.",
    "httpStatus": "OK"
}
```

- 단건 조회 성공
```json
{
    "message": "Host lookup successful. ",
    "data": {
        "name": "네이버",
        "ip": "180.182.54.1",
        "alive": "Connected",
        "lastConnection": "2022-10-31T02:54:24.104128",
        "createAt": "2022-10-30T20:17:09.422515",
        "updatedAt": "2022-10-31T02:23:53.540409"
    },
    "httpStatus": "OK"
}
```

- 단건 조회 실패
```json
{
    "timestamp": "2022-10-30T17:55:09.844+00:00",
    "status": 404,
    "error": "Not Found",
    "path": "/api/v1/hosts/asd"
}
```



## DDL

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

```
release/v2 브랜치는 Scheduler를 사용하지 않은 버전입니다.
```
