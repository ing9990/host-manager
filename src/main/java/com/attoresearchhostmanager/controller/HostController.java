package com.attoresearchhostmanager.controller;

import com.attoresearchhostmanager.dto.HostEditRequestDto;
import com.attoresearchhostmanager.dto.HostRequestDto;
import com.attoresearchhostmanager.service.HostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Taewoo
 */


@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@RequestMapping("/api/v1/hosts")
public class HostController {

    private final HostService hostService;

    @GetMapping()
    public ResponseEntity<?> findAllHosts() {
        log.info("hello");

        return ResponseEntity.ok().body(hostService.findAllHosts());
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> findHostByName(@PathVariable String name) {
        return ResponseEntity.ok().body(hostService.findHostByName(name));
    }

    @PostMapping("")
    public ResponseEntity<?> addHost(@Valid @RequestBody HostRequestDto hostRequestDto) {
        var res = hostService.addHost(hostRequestDto);

        return ResponseEntity.status(res.getHttpStatus()).body(res);
    }

    // 데이터베이스에 host를 100개 채우는 API
    @PostMapping("/test")
    public void test() {
        log.info("test");
        hostService.test();
    }

    @PutMapping("")
    public ResponseEntity<?> editHost(@Valid @RequestBody HostEditRequestDto hostEditRequestDto) {
        var res = hostService.editHost(hostEditRequestDto);

        return ResponseEntity.status(res.getHttpStatus()).body(res);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteHost(@PathVariable String name) {
        var res = hostService.deleteHost(name);

        return ResponseEntity.status(res.getHttpStatus()).body(res);
    }

}
