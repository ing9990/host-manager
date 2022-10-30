# 호스트들의 Alive 상태 체크 및 모니터링 API 서버 개발

<hr/>

0. ✔️

```
- 조회 시 호스트마다 isReachable()의 timeout을 100ms로 고정했습니다.
- ORM은 JPA를 사용했습니다. 
```

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

5. TEST

```
# 호스트 전체 조회 (100개)
GET http://localhost:7777/api/v1/hosts
- 200 OK  19ms
- IP 수정 후 곧 바로 Host 조회 시 Alive 상태 실시간 감지 O
```

```
{
    "message": "Hosts full lookup.",
    "data": [
        {
            "name": "호스트1",
            "ip": "20.200.245.242",
            "alive": "Disconnected",
            "lastConnection": "2022-10-30T20:36:19.672305",
            "createAt": "2022-10-30T19:40:24.420101",
            "updatedAt": "2022-10-30T20:36:19.623623"
        },
        {
            "name": "host100",
            "ip": "180.182.54.1",
            "alive": "Connected",
            "lastConnection": "2022-10-30T20:36:36.366355",
            "createAt": "2022-10-30T20:17:09.422515",
            "updatedAt": "2022-10-30T20:36:33.923488"
        },
        {
            "name": "host101",
            "ip": "192.101.101.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:10.415793",
            "updatedAt": "2022-10-30T20:17:10.415793"
        },
        {
            "name": "host102",
            "ip": "192.102.102.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:11.408159",
            "updatedAt": "2022-10-30T20:17:11.408159"
        },
        {
            "name": "host103",
            "ip": "192.103.103.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:12.421857",
            "updatedAt": "2022-10-30T20:17:12.422965"
        },
        {
            "name": "host104",
            "ip": "192.104.104.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:13.420301",
            "updatedAt": "2022-10-30T20:17:13.420301"
        },
        {
            "name": "host105",
            "ip": "192.105.105.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:14.418188",
            "updatedAt": "2022-10-30T20:17:14.419225"
        },
        {
            "name": "host106",
            "ip": "192.106.106.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:15.417479",
            "updatedAt": "2022-10-30T20:17:15.417479"
        },
        {
            "name": "host107",
            "ip": "192.107.107.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:16.413784",
            "updatedAt": "2022-10-30T20:17:16.413784"
        },
        {
            "name": "host108",
            "ip": "192.108.108.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:17.414171",
            "updatedAt": "2022-10-30T20:17:17.414171"
        },
        {
            "name": "host109",
            "ip": "192.109.109.38",
            "alive": "Disconnected",
            "lastConnection": "2022-10-30T20:31:27.15187",
            "createAt": "2022-10-30T20:17:17.72655",
            "updatedAt": "2022-10-30T20:17:17.72655"
        },
        {
            "name": "host110",
            "ip": "192.110.110.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:18.411293",
            "updatedAt": "2022-10-30T20:17:18.411293"
        },
        {
            "name": "host111",
            "ip": "192.111.111.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:19.408273",
            "updatedAt": "2022-10-30T20:17:19.408273"
        },
        {
            "name": "host112",
            "ip": "192.112.112.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:20.418142",
            "updatedAt": "2022-10-30T20:17:20.418142"
        },
        {
            "name": "host113",
            "ip": "192.113.113.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:21.413814",
            "updatedAt": "2022-10-30T20:17:21.413814"
        },
        {
            "name": "host114",
            "ip": "192.114.114.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:22.415714",
            "updatedAt": "2022-10-30T20:17:22.415714"
        },
        {
            "name": "host115",
            "ip": "192.115.115.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:23.408433",
            "updatedAt": "2022-10-30T20:17:23.408433"
        },
        {
            "name": "host116",
            "ip": "192.116.116.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:24.416879",
            "updatedAt": "2022-10-30T20:17:24.416879"
        },
        {
            "name": "host117",
            "ip": "192.117.117.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:25.414932",
            "updatedAt": "2022-10-30T20:17:25.414932"
        },
        {
            "name": "host118",
            "ip": "192.118.118.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:26.42112",
            "updatedAt": "2022-10-30T20:17:26.42112"
        },
        {
            "name": "host119",
            "ip": "192.119.119.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:27.415233",
            "updatedAt": "2022-10-30T20:17:27.415233"
        },
        {
            "name": "host120",
            "ip": "192.120.120.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:28.419455",
            "updatedAt": "2022-10-30T20:17:28.419455"
        },
        {
            "name": "host121",
            "ip": "192.121.121.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:29.417521",
            "updatedAt": "2022-10-30T20:17:29.417521"
        },
        {
            "name": "host122",
            "ip": "192.122.122.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:30.407461",
            "updatedAt": "2022-10-30T20:17:30.407461"
        },
        {
            "name": "host123",
            "ip": "192.123.123.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:31.419902",
            "updatedAt": "2022-10-30T20:17:31.419902"
        },
        {
            "name": "host124",
            "ip": "192.124.124.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:32.410854",
            "updatedAt": "2022-10-30T20:17:32.410854"
        },
        {
            "name": "host125",
            "ip": "192.125.125.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:33.419037",
            "updatedAt": "2022-10-30T20:17:33.419037"
        },
        {
            "name": "host126",
            "ip": "192.126.126.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:34.414902",
            "updatedAt": "2022-10-30T20:17:34.414902"
        },
        {
            "name": "host127",
            "ip": "192.127.127.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:35.421824",
            "updatedAt": "2022-10-30T20:17:35.422321"
        },
        {
            "name": "host128",
            "ip": "192.128.128.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:36.408353",
            "updatedAt": "2022-10-30T20:17:36.408353"
        },
        {
            "name": "host129",
            "ip": "192.129.129.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:37.417545",
            "updatedAt": "2022-10-30T20:17:37.417545"
        },
        {
            "name": "host130",
            "ip": "192.130.130.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:38.41241",
            "updatedAt": "2022-10-30T20:17:38.41241"
        },
        {
            "name": "host131",
            "ip": "192.131.131.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:39.418898",
            "updatedAt": "2022-10-30T20:17:39.418898"
        },
        {
            "name": "host132",
            "ip": "192.132.132.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:40.415351",
            "updatedAt": "2022-10-30T20:17:40.415351"
        },
        {
            "name": "host133",
            "ip": "192.133.133.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:41.408665",
            "updatedAt": "2022-10-30T20:17:41.408665"
        },
        {
            "name": "host134",
            "ip": "192.134.134.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:42.415747",
            "updatedAt": "2022-10-30T20:17:42.415747"
        },
        {
            "name": "host135",
            "ip": "192.135.135.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:43.410362",
            "updatedAt": "2022-10-30T20:17:43.410362"
        },
        {
            "name": "host136",
            "ip": "192.136.136.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:44.408639",
            "updatedAt": "2022-10-30T20:17:44.408639"
        },
        {
            "name": "host137",
            "ip": "192.137.137.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:45.418239",
            "updatedAt": "2022-10-30T20:17:45.418239"
        },
        {
            "name": "host138",
            "ip": "192.138.138.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:46.416349",
            "updatedAt": "2022-10-30T20:17:46.416855"
        },
        {
            "name": "host139",
            "ip": "192.139.139.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:47.41071",
            "updatedAt": "2022-10-30T20:17:47.41071"
        },
        {
            "name": "host140",
            "ip": "192.140.140.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:48.421485",
            "updatedAt": "2022-10-30T20:17:48.421485"
        },
        {
            "name": "host141",
            "ip": "192.141.141.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:49.42088",
            "updatedAt": "2022-10-30T20:17:49.421383"
        },
        {
            "name": "host142",
            "ip": "192.142.142.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:50.414337",
            "updatedAt": "2022-10-30T20:17:50.414337"
        },
        {
            "name": "host143",
            "ip": "192.143.143.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:51.421576",
            "updatedAt": "2022-10-30T20:17:51.421576"
        },
        {
            "name": "host144",
            "ip": "192.144.144.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:52.413128",
            "updatedAt": "2022-10-30T20:17:52.413128"
        },
        {
            "name": "host145",
            "ip": "192.145.145.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:53.410968",
            "updatedAt": "2022-10-30T20:17:53.410968"
        },
        {
            "name": "host146",
            "ip": "192.146.146.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:54.408382",
            "updatedAt": "2022-10-30T20:17:54.408382"
        },
        {
            "name": "host147",
            "ip": "192.147.147.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:55.41167",
            "updatedAt": "2022-10-30T20:17:55.41167"
        },
        {
            "name": "host148",
            "ip": "192.148.148.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:56.421626",
            "updatedAt": "2022-10-30T20:17:56.421626"
        },
        {
            "name": "host149",
            "ip": "192.149.149.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:57.416151",
            "updatedAt": "2022-10-30T20:17:57.416698"
        },
        {
            "name": "host150",
            "ip": "192.150.150.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:58.411729",
            "updatedAt": "2022-10-30T20:17:58.411729"
        },
        {
            "name": "host151",
            "ip": "192.151.151.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:17:59.410222",
            "updatedAt": "2022-10-30T20:17:59.410222"
        },
        {
            "name": "host152",
            "ip": "192.152.152.38",
            "alive": "Disconnected",
            "lastConnection": "2022-10-30T20:31:27.084703",
            "createAt": "2022-10-30T20:17:59.638085",
            "updatedAt": "2022-10-30T20:17:59.638085"
        },
        {
            "name": "host153",
            "ip": "192.153.153.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:00.42111",
            "updatedAt": "2022-10-30T20:18:00.42111"
        },
        {
            "name": "host154",
            "ip": "192.154.154.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:01.416297",
            "updatedAt": "2022-10-30T20:18:01.416297"
        },
        {
            "name": "host155",
            "ip": "192.155.155.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:02.412216",
            "updatedAt": "2022-10-30T20:18:02.412216"
        },
        {
            "name": "host156",
            "ip": "192.156.156.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:03.419442",
            "updatedAt": "2022-10-30T20:18:03.419442"
        },
        {
            "name": "host157",
            "ip": "192.157.157.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:04.412684",
            "updatedAt": "2022-10-30T20:18:04.412684"
        },
        {
            "name": "host158",
            "ip": "192.158.158.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:05.419882",
            "updatedAt": "2022-10-30T20:18:05.419882"
        },
        {
            "name": "host159",
            "ip": "192.159.159.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:06.414984",
            "updatedAt": "2022-10-30T20:18:06.414984"
        },
        {
            "name": "host160",
            "ip": "192.160.160.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:07.409667",
            "updatedAt": "2022-10-30T20:18:07.409667"
        },
        {
            "name": "host161",
            "ip": "192.161.161.38",
            "alive": "Disconnected",
            "lastConnection": "2022-10-30T20:31:27.024071",
            "createAt": "2022-10-30T20:18:07.581061",
            "updatedAt": "2022-10-30T20:18:07.581561"
        },
        {
            "name": "host162",
            "ip": "192.162.162.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:08.411811",
            "updatedAt": "2022-10-30T20:18:08.411811"
        },
        {
            "name": "host163",
            "ip": "192.163.163.38",
            "alive": "Disconnected",
            "lastConnection": "2022-10-30T20:31:27.04485",
            "createAt": "2022-10-30T20:18:08.689856",
            "updatedAt": "2022-10-30T20:18:08.689856"
        },
        {
            "name": "host164",
            "ip": "192.164.164.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:09.419822",
            "updatedAt": "2022-10-30T20:18:09.419822"
        },
        {
            "name": "host165",
            "ip": "192.165.165.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:10.415252",
            "updatedAt": "2022-10-30T20:18:10.415252"
        },
        {
            "name": "host166",
            "ip": "192.166.166.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:11.411397",
            "updatedAt": "2022-10-30T20:18:11.411397"
        },
        {
            "name": "host167",
            "ip": "192.167.167.38",
            "alive": "Disconnected",
            "lastConnection": "2022-10-30T20:30:26.870558",
            "createAt": "2022-10-30T20:18:11.73391",
            "updatedAt": "2022-10-30T20:18:11.73391"
        },
        {
            "name": "host168",
            "ip": "192.168.168.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:12.418702",
            "updatedAt": "2022-10-30T20:18:12.418702"
        },
        {
            "name": "host169",
            "ip": "192.169.169.38",
            "alive": "Disconnected",
            "lastConnection": "2022-10-30T20:31:27.04485",
            "createAt": "2022-10-30T20:18:12.686889",
            "updatedAt": "2022-10-30T20:18:12.686889"
        },
        {
            "name": "host170",
            "ip": "192.170.170.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:13.411064",
            "updatedAt": "2022-10-30T20:18:13.411064"
        },
        {
            "name": "host171",
            "ip": "192.171.171.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:14.420327",
            "updatedAt": "2022-10-30T20:18:14.420327"
        },
        {
            "name": "host172",
            "ip": "192.172.172.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:15.411536",
            "updatedAt": "2022-10-30T20:18:15.411536"
        },
        {
            "name": "host173",
            "ip": "192.173.173.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:16.419815",
            "updatedAt": "2022-10-30T20:18:16.419815"
        },
        {
            "name": "host174",
            "ip": "192.174.174.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:17.413332",
            "updatedAt": "2022-10-30T20:18:17.413332"
        },
        {
            "name": "host175",
            "ip": "192.175.175.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:18.409637",
            "updatedAt": "2022-10-30T20:18:18.409637"
        },
        {
            "name": "host176",
            "ip": "192.176.176.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:19.418591",
            "updatedAt": "2022-10-30T20:18:19.418591"
        },
        {
            "name": "host177",
            "ip": "192.177.177.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:20.413121",
            "updatedAt": "2022-10-30T20:18:20.413622"
        },
        {
            "name": "host178",
            "ip": "192.178.178.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:21.421522",
            "updatedAt": "2022-10-30T20:18:21.421522"
        },
        {
            "name": "host179",
            "ip": "192.179.179.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:22.416673",
            "updatedAt": "2022-10-30T20:18:22.416673"
        },
        {
            "name": "host180",
            "ip": "192.180.180.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:23.423387",
            "updatedAt": "2022-10-30T20:18:23.423387"
        },
        {
            "name": "host181",
            "ip": "192.181.181.38",
            "alive": "Disconnected",
            "lastConnection": "2022-10-30T20:31:27.101286",
            "createAt": "2022-10-30T20:18:23.749285",
            "updatedAt": "2022-10-30T20:18:23.749285"
        },
        {
            "name": "host182",
            "ip": "192.182.182.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:24.410546",
            "updatedAt": "2022-10-30T20:18:24.410546"
        },
        {
            "name": "host183",
            "ip": "192.183.183.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:25.409741",
            "updatedAt": "2022-10-30T20:18:25.409741"
        },
        {
            "name": "host184",
            "ip": "192.184.184.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:26.421985",
            "updatedAt": "2022-10-30T20:18:26.421985"
        },
        {
            "name": "host185",
            "ip": "192.185.185.38",
            "alive": "Disconnected",
            "lastConnection": "2022-10-30T20:31:27.084703",
            "createAt": "2022-10-30T20:18:26.715203",
            "updatedAt": "2022-10-30T20:18:26.715203"
        },
        {
            "name": "host186",
            "ip": "192.186.186.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:27.408738",
            "updatedAt": "2022-10-30T20:18:27.408738"
        },
        {
            "name": "host187",
            "ip": "192.187.187.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:28.420467",
            "updatedAt": "2022-10-30T20:18:28.420467"
        },
        {
            "name": "host188",
            "ip": "192.188.188.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:29.411104",
            "updatedAt": "2022-10-30T20:18:29.411104"
        },
        {
            "name": "host189",
            "ip": "192.189.189.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:30.422597",
            "updatedAt": "2022-10-30T20:18:30.422597"
        },
        {
            "name": "host190",
            "ip": "192.190.190.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:31.416186",
            "updatedAt": "2022-10-30T20:18:31.416186"
        },
        {
            "name": "host191",
            "ip": "192.191.191.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:32.421445",
            "updatedAt": "2022-10-30T20:18:32.421445"
        },
        {
            "name": "host192",
            "ip": "192.192.192.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:33.416278",
            "updatedAt": "2022-10-30T20:18:33.416278"
        },
        {
            "name": "host193",
            "ip": "192.193.193.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:34.40818",
            "updatedAt": "2022-10-30T20:18:34.40818"
        },
        {
            "name": "host194",
            "ip": "192.194.194.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:35.418616",
            "updatedAt": "2022-10-30T20:18:35.418616"
        },
        {
            "name": "host195",
            "ip": "192.195.195.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:36.415796",
            "updatedAt": "2022-10-30T20:18:36.415796"
        },
        {
            "name": "host196",
            "ip": "192.196.196.38",
            "alive": "Disconnected",
            "lastConnection": "2022-10-30T20:31:22.050445",
            "createAt": "2022-10-30T20:18:36.858624",
            "updatedAt": "2022-10-30T20:18:36.858624"
        },
        {
            "name": "host197",
            "ip": "192.197.197.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:37.419678",
            "updatedAt": "2022-10-30T20:18:37.42018"
        },
        {
            "name": "host198",
            "ip": "192.198.198.38",
            "alive": "Disconnected",
            "lastConnection": null,
            "createAt": "2022-10-30T20:18:38.413904",
            "updatedAt": "2022-10-30T20:18:38.413904"
        }
    ],
    "httpStatus": "OK"
}
```