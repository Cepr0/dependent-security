package io.github.cepr0.dependent_security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @author Cepro, 2017-08-31
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@NaturalId
	private String name;

	private String password;

	@CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
	@ElementCollection
	private Set<Role> roles;

	@ManyToMany
	private List<Category> categories;

	public enum Role implements GrantedAuthority {
		ROLE_USER("USER"), ROLE_ADMIN("ADMIN");

		private String title;

		Role(String title) {
			this.title = title;
		}

		@Override
		public String toString() {
			return title;
		}

		@Override
		public String getAuthority() {
			return name();
		}
	}
}
