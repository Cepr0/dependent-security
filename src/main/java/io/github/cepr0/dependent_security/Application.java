package io.github.cepr0.dependent_security;

import io.github.cepr0.dependent_security.model.Category;
import io.github.cepr0.dependent_security.model.Room;
import io.github.cepr0.dependent_security.model.User;
import io.github.cepr0.dependent_security.repo.BaseRepo;
import org.h2.tools.Server;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * @author Cepro, 2017-08-30
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server h2Server() throws SQLException {
		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
	}

	/**
	 * Populate initial data
	 */
	@Bean
	ApplicationRunner applicationRunner(BaseRepo repo) {
		return args -> {
			List<Room> rooms = repo.save(asList(
					new Room(1, "description1"),
					new Room(2, "description1"),
					new Room(3, "description1"),
					new Room(4, "description1"),
					new Room(5, "description1")
			));

			List<Category> categories = repo.save(asList(
					new Category("category1", rooms.subList(0, 2)),
					new Category("category2", rooms.subList(2, 5))
			));

			repo.save(asList(
					new User("user1", "123456", User.Role.ROLE_USER, singletonList(categories.get(0))),
					new User("user2", "123456", User.Role.ROLE_USER, singletonList(categories.get(1)))
			));
		};
	}
}
