package academy.devdojo.springboot2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import academy.devdojo.springboot2.domain.AnimeUser;

public interface AnimeUserRepository extends JpaRepository<AnimeUser, Long> {

	AnimeUser findByUsername(String username);
}
