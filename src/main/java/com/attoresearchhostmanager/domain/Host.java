package com.attoresearchhostmanager.domain;

/**
 * @author Taewoo
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Host {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOST_ID")
    @JsonIgnore
    private Long id;

    @Column(name = "HOST_NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "HOST_IP", unique = true, nullable = false)
    private String ip;

    @Column(name = "HOST_ALIVE")
    @Enumerated(EnumType.STRING)
    private AliveStatus alive;

    public enum AliveStatus {
        Disconnected, Connected
    }

    @Column(name = "HOST_LAST_CONNECTION")
    private LocalDateTime lastConnection;

    @Column(name = "HOST_CREATED", updatable = false)
    private LocalDateTime createAt;

    @Column(name = "HOST_UPDATED")
    private LocalDateTime updatedAt;
}











