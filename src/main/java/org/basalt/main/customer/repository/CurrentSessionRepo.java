package org.basalt.main.customer.repository;

import org.basalt.main.customer.model.CurrentUserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 17/04/2024
 */
public interface CurrentSessionRepo extends JpaRepository<CurrentUserSession, UUID> {
    CurrentUserSession findByToken(String token);

    @Query("FROM CurrentUserSession a WHERE a.userId=?1")
    CurrentUserSession findByUserId(UUID userId);

    CurrentUserSession findCurrentUserSessionByUserId(UUID userId);
}
