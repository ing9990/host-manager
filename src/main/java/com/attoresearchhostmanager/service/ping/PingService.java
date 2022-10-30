package com.attoresearchhostmanager.service.ping;

import com.attoresearchhostmanager.domain.Host;
import com.attoresearchhostmanager.repository.HostRepository;
import com.attoresearchhostmanager.service.HostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;


/**
 * @author Taewoo
 */


@RequiredArgsConstructor
@Slf4j
public class PingService {

    private final HostService hostService;

    @Value("${inet.timeout}")
    private static int timeout;

    @Async(value = "hostExecutor")
    public void pingTest(Host host) {
        try {
            var isReachable = InetAddress.getByName(host.getIp()).isReachable(timeout);
            log.info(host.getName() + ": " + isReachable);
            hostService.updateAlive(host.getName(), isReachable);
        } catch (IOException e) {
            hostService.updateAlive(host.getName(), false);
        }
    }

}
