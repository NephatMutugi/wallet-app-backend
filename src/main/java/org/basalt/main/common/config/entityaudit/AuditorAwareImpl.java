package org.basalt.main.common.config.entityaudit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @ Author Nephat Muchiri
 * Date 13/04/2024
 */
@Service
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        String auditor = "";
        return Optional.of(auditor);
    }
}
