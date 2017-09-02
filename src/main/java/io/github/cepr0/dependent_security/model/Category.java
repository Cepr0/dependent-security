package io.github.cepr0.dependent_security.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @author Cepro, 2017-08-31
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

	@NaturalId
	private String name;

	@OneToMany(mappedBy = "category")
	private List<Room> rooms;

	public Category(String name) {
		this.name = name;
	}
}
