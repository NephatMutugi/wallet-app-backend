package org.basalt.main.sessions.model;

import jakarta.persistence.*;
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
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "userId", nullable = false, unique = true)
	private UUID userId;
	private String uuid;
	
}
