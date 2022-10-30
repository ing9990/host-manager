package com.attoresearchhostmanager.service.scheduler;

import com.attoresearchhostmanager.service.HostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        hostService.findAll().forEach(hostService::pingTest);
    }
}
