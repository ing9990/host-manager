package com.attoresearchhostmanager.service.Job;

import com.attoresearchhostmanager.service.HostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;

import static com.attoresearchhostmanager.AttoResearchHostManagerApplication.hostCache;

/**
 * @author Taewoo
 */


@Component
@Slf4j
@RequiredArgsConstructor
public class PingScheduler {

    private final HostService hostService;

    @Value("${inet.timeout}")
    private int timeout;

    @Scheduled(fixedDelay = 1000 * 2)
    public void cacheScheduler() {
        if (hostCache.size() != hostService.getCount())
            hostCache.clear();

        log.info("호스트 " + hostCache.size() + "건 관리 중");

        hostService.findAll().forEach(host -> {
            hostCache.put(host.getName(), host);
        });
    }

    @Scheduled(fixedDelay = 1000 * 2)
    public void pingToHostsScheduler() {
        hostCache.forEach((k, v) -> {
            try {
                hostService.updateAlive(k, InetAddress.getByName(v.getIp()).isReachable(timeout));
            } catch (IOException e) {
                hostService.updateAlive(k, false);
            }
        });
    }
}
