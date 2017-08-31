package io.github.cepr0.dependent_security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

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

	@Column(unique = true)
	private String name;

	private String password;

	private Role role;

	@OneToMany
	private List<Category> categories;

	public enum Role implements GrantedAuthority {
		ROLE_USER("User"), ROLE_ADMIN("Admin");

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
