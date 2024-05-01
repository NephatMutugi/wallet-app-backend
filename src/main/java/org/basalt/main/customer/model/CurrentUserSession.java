package org.basalt.main.customer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.basalt.main.common.config.entityaudit.EntityAudit;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentUserSession extends EntityAudit {

	@Id
	@Column(name = "userId", nullable = false, unique = true)
	private UUID userId;
	@Column(name = "token")
	private String token;
}
