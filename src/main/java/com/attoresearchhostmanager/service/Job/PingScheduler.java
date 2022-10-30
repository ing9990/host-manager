package com.attoresearchhostmanager.service.Job;

import com.attoresearchhostmanager.service.HostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Taewoo
 */


@Service
@Slf4j
@RequiredArgsConstructor
public class PingScheduler {

    private final HostService hostService;

    @Scheduled(fixedDelay = 1000)
    public void pingToHostsScheduler() {
        hostService.findAll().forEach((host) -> {
            log.info("호스트 연결 검사: " + host.getName() + " : " + host.getIp());
            hostService.pingTest(host);
        });
    }
}
