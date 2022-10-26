package com.attoresearchhostmanager.service;

import com.attoresearchhostmanager.config.WebSocketHandler;
import com.attoresearchhostmanager.domain.Host;
import com.attoresearchhostmanager.dto.DefaultResponseDtoEntity;
import com.attoresearchhostmanager.dto.HostEditRequestDto;
import com.attoresearchhostmanager.dto.HostRequestDto;
import com.attoresearchhostmanager.exception.HostNotFoundException;
import com.attoresearchhostmanager.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

/**
 * @author Taewoo
 */


@Service
@RequiredArgsConstructor
@Slf4j
public class HostService {

    private final HostRepository hostRepository;
    private final WebSocketHandler webSocketHandler;

    @Value("${inet.timeout}")
    private int timeout;


    public DefaultResponseDtoEntity addHost(HostRequestDto hostRequestDto) {
        log.info("호스트 등록 요청: [" + hostRequestDto.getHostName() + "]");

        var host = requestDtoToHost(hostRequestDto);

        return saveHost(host) == null ? DefaultResponseDtoEntity.of(HttpStatus.NO_CONTENT, "More than 100 hosts are connected.") : DefaultResponseDtoEntity.of(HttpStatus.CREATED, "Host registered successfully.");
    }

    public DefaultResponseDtoEntity editHost(HostEditRequestDto hostEditRequestDto) {
        log.info("호스트 수정 요청: [" + hostEditRequestDto.getHostName() + "]");

        var name = hostEditRequestDto.getHostName();
        var ip = hostEditRequestDto.getIp();

        if (hostRepository.existsByIp(ip)) {
            return DefaultResponseDtoEntity.of(HttpStatus.BAD_REQUEST, "Duplicated ip address. " + ip);
        }

        updateIpByName(name, ip);
        return DefaultResponseDtoEntity.ok("Host Modified successfully.", hostRepository.findHostByName(name));
    }


    public DefaultResponseDtoEntity deleteHost(String name) {
        log.info("호스트 삭제 요청: [" + name + "]");

        var col = hostRepository.deleteHostByName(name);
        return col == 1 ? DefaultResponseDtoEntity.ok("Host deleted successfully.") : DefaultResponseDtoEntity.ok("Host not found: " + name);
    }


    private Host requestDtoToHost(HostRequestDto hostRequestDto) {
        boolean isConnect = connectionTest(hostRequestDto.getIp());

        return Host.builder().name(hostRequestDto.getHostName()).ip(hostRequestDto.getIp()).alive(isConnect ? Host.AliveStatus.Connected : Host.AliveStatus.Disconnected).lastConnection(isConnect ? LocalDateTime.now() : null).createAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
    }

    private void updateIpByName(String name, String ip) {
        hostRepository.updateIpByName(name, ip);
    }

    private boolean connectionTest(String hostName) {
        try {
            return InetAddress.getByName(hostName).isReachable(timeout);
        } catch (IOException e) {
            log.warn("연결 실패: [" + hostName + "]");
        }

        return false;
    }

    private Host saveHost(Host host) {
        if (hostRepository.count() >= 100) {
            log.warn("More than 100 hosts are connected.");
            return null;
        }

        return hostRepository.save(host);
    }

    private Host getHostByHostName(String hostName) {
        return hostRepository.findHostByName(hostName).orElseThrow(() -> new HostNotFoundException(hostName));
    }
}
