package com.attoresearchhostmanager.service;

import com.attoresearchhostmanager.domain.Host;
import com.attoresearchhostmanager.dto.DefaultResponseDtoEntity;
import com.attoresearchhostmanager.dto.HostEditRequestDto;
import com.attoresearchhostmanager.dto.HostRequestDto;
import com.attoresearchhostmanager.exception.HostNotFoundException;
import com.attoresearchhostmanager.repository.HostRepository;
import com.attoresearchhostmanager.service.scheduler.PingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Taewoo
 */


@Service
@RequiredArgsConstructor
@Slf4j
public class HostService {

    private final HostRepository hostRepository;
    private final PingService pingService;

    @Value("${inet.timeout}")
    private int timeout;

    @Transactional(readOnly = true)
    public DefaultResponseDtoEntity findAllHosts() {
        pingTest();
        var hosts = findAll();

        return hosts.size() != 0 ? DefaultResponseDtoEntity.ok("Hosts full lookup.", hosts)
                : DefaultResponseDtoEntity.ok("Hosts is empty.", hosts);
    }

    @Transactional(readOnly = true)
    public DefaultResponseDtoEntity findHostByName(String name) {
        var host = hostRepository.findHostByName(name).orElseThrow(() -> new HostNotFoundException(name));

        return DefaultResponseDtoEntity.ok("Host lookup successful. ", host);
    }

    @Transactional
    public DefaultResponseDtoEntity addHost(HostRequestDto hostRequestDto) {
        log.info("호스트 등록 요청: [" + hostRequestDto.getHostName() + "]");

        var host = requestDtoToHost(hostRequestDto);

        return saveHost(host) == null ? DefaultResponseDtoEntity.of(HttpStatus.OK, "More than 100 hosts are connected.") : DefaultResponseDtoEntity.of(HttpStatus.CREATED, "Host registered successfully.");
    }

    @Transactional
    public DefaultResponseDtoEntity editHost(HostEditRequestDto hostEditRequestDto) {
        log.info("호스트 수정 요청: [" + hostEditRequestDto.getHostName() + "]");

        var host = getHostByHostName(hostEditRequestDto.getHostName());
        var ip = hostEditRequestDto.getIp();

        if (hostRepository.existsByIp(ip))
            return DefaultResponseDtoEntity.of(HttpStatus.BAD_REQUEST, "Duplicated ip address. " + ip);

        return updateIpByName(host.getName(), ip) == 0 ?
                DefaultResponseDtoEntity.of(HttpStatus.OK, "No changes.") :
                DefaultResponseDtoEntity.of(HttpStatus.CREATED, "Host Modified successfully.");
    }

    @Transactional
    public DefaultResponseDtoEntity deleteHost(String name) {
        log.info("호스트 삭제 요청: [" + name + "]");

        var col = hostRepository.deleteHostByName(name);

        return col == 1 ? DefaultResponseDtoEntity.ok("Host deleted successfully.") : DefaultResponseDtoEntity.of(HttpStatus.OK, "Host not found: " + name);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Host> findAll() {
        return hostRepository.findAll();
    }

    private Host requestDtoToHost(HostRequestDto hostRequestDto) {
        boolean isConnect = connectionTest(hostRequestDto.getIp());

        return Host.builder().name(hostRequestDto.getHostName()).ip(hostRequestDto.getIp()).alive(isConnect ? Host.AliveStatus.Connected : Host.AliveStatus.Disconnected).lastConnection(isConnect ? LocalDateTime.now() : null).createAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
    }

    private int updateIpByName(String name, String ip) {
        int changes = hostRepository.updateIpByName(name, ip);
        var host = getHostByHostName(name);

        if (changes != 0)
            updateUpdatedAtNow(name, LocalDateTime.now());

        pingService.pingTest(host);
        return changes;
    }

    private void pingTest() {
        findAll().forEach(pingService::pingTest);
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
        if (getCount() >= 100)
            return null;

        return hostRepository.save(host);
    }

    private Host getHostByHostName(String hostName) {
        return hostRepository.findHostByName(hostName).orElseThrow(() -> new HostNotFoundException(hostName));
    }

    private void updateUpdatedAtNow(String name, LocalDateTime time) {
        hostRepository.updateUpdatedAtNow(name, time);
    }

    private long getCount() {
        return hostRepository.count();
    }
}
