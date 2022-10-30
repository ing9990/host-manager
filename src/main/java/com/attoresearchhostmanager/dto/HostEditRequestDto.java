package com.attoresearchhostmanager.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author Taewoo
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostEditRequestDto {

    @NotEmpty(message = "Host name is empty")
    private String hostName;

    @NotEmpty(message = "Ip Address is empty.")
    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
            message = "Invalid ip address.")
    private String ip;
}
