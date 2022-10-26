package com.attoresearchhostmanager.controller;

import com.attoresearchhostmanager.dto.HostEditRequestDto;
import com.attoresearchhostmanager.dto.HostRequestDto;
import com.attoresearchhostmanager.service.HostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Taewoo
 */


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hosts")
public class HostController {

    private final HostService hostService;

    @GetMapping("")
    public ResponseEntity<?> findAllHosts() {
        return null;
    }

    @PostMapping("")
    public ResponseEntity<?> addHost(@Valid @RequestBody HostRequestDto hostRequestDto) {
        var res = hostService.addHost(hostRequestDto);

        return ResponseEntity.status(res.getHttpStatus()).body(res);
    }

    @PutMapping("")
    public ResponseEntity<?> editHost(@Valid @RequestBody HostEditRequestDto hostEditRequestDto) {
        var res = hostService.editHost(hostEditRequestDto);

        return ResponseEntity.status(res.getHttpStatus())
                             .body(res);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteHost(@PathVariable String name) {
        var res = hostService.deleteHost(name);

        return ResponseEntity.status(res.getHttpStatus()).body(res);
    }

}
