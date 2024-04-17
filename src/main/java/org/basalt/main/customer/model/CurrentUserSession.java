package org.basalt.main.customer.model;

import jakarta.persistence.*;
import lombok.*;
import org.basalt.main.common.config.entityaudit.EntityAudit;

import java.time.LocalDateTime;
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
	@Column(name = "token")
	private String token;
	private LocalDateTime localDateTime;
	
}
