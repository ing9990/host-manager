package com.attoresearchhostmanager.service;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.attoresearchhostmanager.AttoResearchHostManagerApplication.hostCache;

/**
 * @author Taewoo
 */


@Service
@RequiredArgsConstructor
@Slf4j
public class HostService {

    private final HostRepository hostRepository;

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

        if (hostRepository.existsByIp(ip))
            return DefaultResponseDtoEntity.of(HttpStatus.BAD_REQUEST, "Duplicated ip address. " + ip);

        return updateIpByName(name, ip) == 0 ? DefaultResponseDtoEntity.of(HttpStatus.NO_CONTENT, "No changes.") : DefaultResponseDtoEntity.ok("Host Modified successfully.", hostRepository.findHostByName(name));
    }


    public DefaultResponseDtoEntity deleteHost(String name) {
        log.info("호스트 삭제 요청: [" + name + "]");

        var col = hostRepository.deleteHostByName(name);
        return col == 1 ? DefaultResponseDtoEntity.ok("Host deleted successfully.") : DefaultResponseDtoEntity.ok("Host not found: " + name);
    }

    public DefaultResponseDtoEntity findAllHosts() {
        log.info("호스트 전체 조회");
        return DefaultResponseDtoEntity.ok("Hosts full lookup.", hostRepository.findAll());
    }

    public DefaultResponseDtoEntity findHostByName(String name) {
        var host = hostCache.get(name);

        if (host == null) throw new HostNotFoundException(name);

        return DefaultResponseDtoEntity.ok("Host lookup successful. ", host);
    }


    private Host requestDtoToHost(HostRequestDto hostRequestDto) {
        boolean isConnect = connectionTest(hostRequestDto.getIp());

        return Host.builder().name(hostRequestDto.getHostName()).ip(hostRequestDto.getIp()).alive(isConnect ? Host.AliveStatus.Connected : Host.AliveStatus.Disconnected).lastConnection(isConnect ? LocalDateTime.now() : null).createAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
    }

    private int updateIpByName(String name, String ip) {
        return hostRepository.updateIpByName(name, ip, LocalDateTime.now());
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

    @Transactional(readOnly = true)
    public List<Host> findAll() {
        return hostRepository.findAll();
    }

    @Transactional
    public void updateAlive(String k, boolean b) {
        int col = hostRepository.updateAliveById(k, b ? Host.AliveStatus.Connected : Host.AliveStatus.Disconnected);

        if (col >= 1) {
            updateLastAliveNow(k, LocalDateTime.now());
        }
    }

    public void updateUpdatedAtNow(String name, LocalDateTime time) {
        hostRepository.updateUpdatedAtNow(name, time);
    }

    public void updateLastAliveNow(String name, LocalDateTime time) {
        hostRepository.updateLastAlive(name, time);
    }


    public void test() {
        var remain = 100 - hostRepository.count();

        for (int i = 100; i < remain + 100; i++) {
            var addr = i + "192." + i + "." + i + ".38";
            log.info("Ip address: " + addr);
            hostRepository.save(requestDtoToHost(new HostRequestDto("host" + i, addr)));
        }
    }

    public long getCount() {
        return hostRepository.count();
    }
}
