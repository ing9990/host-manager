package com.attoresearchhostmanager;

import com.attoresearchhostmanager.domain.Host;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class AttoResearchHostManagerApplication {

    public static Map<String, Host> hostCache = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(AttoResearchHostManagerApplication.class, args);
    }

}
