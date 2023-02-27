package academy.devdojo.springboot2;


import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.utils.AnimeCreator;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
public class AnimeRepositoryTest {
	
	@Autowired
	private  AnimeRepository animeRepository;

	@Test
	@DisplayName("Save persist Anime when Successful")
	void savePersistAnimeWhenSuccessful() {
		Anime animeToSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = animeRepository.save(animeToSaved);
		Assertions.assertThat(animeSaved).isNotNull();
		Assertions.assertThat(animeSaved.getId()).isNotNull();
		Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToSaved.getName());
	}
	
	@Test
	@DisplayName("Update persist Anime when Successful")
	void updatePersistAnimeWhenSuccessful() {
		Anime animeToSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = animeRepository.save(animeToSaved);
		
		animeSaved.setName("One Punch Man");
		
		Anime animeUpdate = animeRepository.save(animeSaved);
		
		Assertions.assertThat(animeUpdate).isNotNull();
		Assertions.assertThat(animeUpdate.getId()).isNotNull();
		
		Assertions.assertThat(animeUpdate.getName()).isEqualTo(animeSaved.getName());
	}
	
	@Test
	@DisplayName("Delete Anime when Successful")
	void deleteAnimeWhenSuccessful() {
		Anime animeToSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = animeRepository.save(animeToSaved);
		
		animeRepository.delete(animeSaved);
		
		Optional<Anime> animeDeletedOpt = this.animeRepository.findById(animeSaved.getId());
		
		Assertions.assertThat(animeDeletedOpt).isEmpty();
	}
	
	@Test
	@DisplayName("Find By Name Returns List Animes When Successful")
	void findByNameReturnsListAnimesWhenSuccessful() {
		Anime animeToSaved = AnimeCreator.createAnimeToBeSaved();
		Anime animeSaved = animeRepository.save(animeToSaved);
		
		List<Anime> animes = animeRepository.findByName(animeSaved.getName());
		
		Assertions.assertThat(animes)
			.isNotEmpty()
			.contains(animeSaved);
	}
	
	@Test
	@DisplayName("Find By Name Returns empty List when no anime is not found")
	void findByNameReturnsEmptyListAnimesWhenAnimeIsNotFound() {
		
		List<Anime> animes = animeRepository.findByName("Anime Inexistente");
		
		Assertions.assertThat(animes).isEmpty();
	}
	
	@Test
	@DisplayName("save Throws ConstraintViolationException When Name Is Empty")
	void saveThrowsConstraintViolationExceptionWhenNameIsEmpty() {
		Anime animeToSaved =  new Anime();

		//forma 1
//		Assertions.assertThatThrownBy(() -> animeRepository.save(animeToSaved))
//			.isInstanceOf(ConstraintViolationException.class);
		//forma 2
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> animeRepository.save(animeToSaved))
			.withMessageContaining("The anime name cannot be empty");
			
	}
	
}
