package com.attoresearchhostmanager.service.scheduler;

import com.attoresearchhostmanager.domain.Host;
import com.attoresearchhostmanager.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;

/**
 * @author Taewoo
 */


@Service
@Slf4j
@RequiredArgsConstructor
public class PingService {

    private final HostRepository hostRepository;

    @Async(value = "pingExecutor")
    public void pingTest(Host host) {
        try {
            updateAlive(host.getName(), InetAddress.getByName(host.getIp()).isReachable(100));
        } catch (IOException e) {
            updateAlive(host.getName(), false);
        }
    }

    private void updateAlive(String k, boolean b) {
        hostRepository.updateAliveById(k, b ? Host.AliveStatus.Connected : Host.AliveStatus.Disconnected);
        var host = hostRepository.getByName(k);

        if (host.getAlive() == Host.AliveStatus.Connected)
            updateLastAliveNow(k, LocalDateTime.now());
    }

    private void updateLastAliveNow(String name, LocalDateTime time) {
        hostRepository.updateLastAlive(name, time);
    }
}
