package com.attoresearchhostmanager.repository;

import com.attoresearchhostmanager.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Taewoo
 */

public interface HostRepository extends JpaRepository<Host, Long> {

    @Query("select (count(h) > 0) from Host h where h.name = ?1")
    boolean existsByName(String name);

    @Query("select (count(h) > 0) from Host h where h.ip = ?1")
    boolean existsByIp(String ip);

    @Query("select h from Host h where h.name = ?1")
    Optional<Host> findHostByName(String name);

    @Transactional
    @Modifying
    @Query("update Host h set h.ip = ?2 where h.name = ?1")
    void updateIpByName(String name, String ip);

    @Transactional
    @Modifying
    @Query("delete from Host h where h.name = ?1")
    int deleteHostByName(String name);
}
