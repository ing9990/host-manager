package com.attoresearchhostmanager.dto;

/**
 * @author Taewoo
 */


import com.attoresearchhostmanager.exception.valid.DuplicatedHostConstraint;
import com.attoresearchhostmanager.exception.valid.DuplicatedIpConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostRequestDto {

    @DuplicatedHostConstraint(message = "Duplicate host name.")
    @NotEmpty(message = "Host name is empty")
    private String hostName;

    @DuplicatedIpConstraint(message = "Duplicate ip address.")
    @NotEmpty(message = "Ip Address is empty.")
    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
            message = "Invalid ip address.")
    private String ip;
}
