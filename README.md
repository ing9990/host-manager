# 호스트들의 Alive 상태 체크 및 모니터링 API 서버 개발

<hr/>

## 패턴

- Response

```
Controller와 Advice의 Response는 ResponseEntity<T>로 통일하였습니다.
Service -> Controller의 Response는 DefaultResponseDtoEntity로 통일했습니다.
```

- Validation

```
Controller에서 Body를 받을 때 유효성 검증을 거치도록 했습니다.

- Host, Ip가 빈 값이거나 중복일 경우
- format에 맞지 않는 Ip일 경우
```

<hr/>

## 고민한 부분

- 호스트에 연결이 끊겼을 경우 서버에서 알아채는 방법

```
- Socket으로 모든 호스트와 통신하며 연결 끊김을 감지한다.
- Scheduler를 사용해 지속적으로 호스트의 연결을 확인한다.

Host는 REST-API로 post를 통해 등록 될텐데 소켓연결이 가능한가?
Scheduler를 사용해 계속 요청을 한다면 비효율적임.
```

- 호스트마다 InetAddress.isReachable()의 timeout을 기다려야 하는데 100개의 호스트가 모두 Unreachable 상태일 때 1초안에 응답하는 방법

```
- 1초 안에 모든 호스트로 요청을 보낼 수 있도록 Timeout을 짧게 줄인다. (쉬운 방법)
- Socket 연결이 끊길 때 DB에 Host를 update 시킨다.
- 병렬처리 (Spring Batch)

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



