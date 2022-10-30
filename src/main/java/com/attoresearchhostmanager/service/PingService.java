package com.attoresearchhostmanager.service;

import com.attoresearchhostmanager.domain.Host;
import com.attoresearchhostmanager.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;

/**
 * @author Taewoo
 */


@Service
@RequiredArgsConstructor
@Slf4j
public class PingService {

    private final HostRepository hostRepository;

    @Value("${inet.timeout}")
    private static int timeout;

    @Async
    public void pingTest(Host host) {
        log.info("PingTest: " + host.getName() + " : " + host.getAlive());
        try {
            updateAlive(host.getName(), InetAddress.getByName(host.getIp()).isReachable(timeout));
        } catch (IOException e) {
            updateAlive(host.getName(), false);
        }
    }

    public void updateAlive(String k, boolean b) {
        hostRepository.updateAliveById(k, b ? Host.AliveStatus.Connected : Host.AliveStatus.Disconnected);

        var host = hostRepository.getByName(k);

        if (host.getAlive() == Host.AliveStatus.Connected)
            updateLastAliveNow(k, LocalDateTime.now());
    }

    public void updateLastAliveNow(String name, LocalDateTime time) {
        hostRepository.updateLastAlive(name, time);
    }

}
