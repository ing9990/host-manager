package com.attoresearchhostmanager.dto;

/**
 * @author Taewoo
 */


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostsResponseDto {
    private String hostName;
    private String ip;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
