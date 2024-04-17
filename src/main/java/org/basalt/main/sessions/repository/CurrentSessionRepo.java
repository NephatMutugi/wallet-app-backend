package org.basalt.main.sessions.repository;

import org.basalt.main.sessions.model.CurrentUserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface CurrentSessionRepo extends JpaRepository<CurrentUserSession, UUID> {
    CurrentUserSession findByUuid(String uuid);

    @Query("FROM CurrentUserSession a WHERE a.userId=?1")
    Optional<CurrentUserSession> findByUserId(UUID userId);
}
