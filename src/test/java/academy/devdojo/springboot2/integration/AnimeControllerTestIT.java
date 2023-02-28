package academy.devdojo.springboot2.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.utils.AnimeCreator;
import academy.devdojo.springboot2.utils.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) //dropa e recria as tabelas para cada teste
class AnimeControllerTestIT {

	@Autowired
	private TestRestTemplate testRestTemplate;
	@LocalServerPort
	private int port;
	
	@Autowired
	private AnimeRepository animeRepository;
	
	@Test
	@DisplayName("list Return List Of Animes Inside PageObject When Successful")
	void listReturnListOfAnimesInsidePageObjectWhenSuccessful() {
		Anime anime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		String expectedName = anime.getName();
		
		PageableResponse<Anime> animesPage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();
		
		Assertions.assertThat(animesPage).isNotNull();
		Assertions.assertThat(animesPage.toList())
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(animesPage.toList().get(0).getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("listAll Return List Of Animes When Successful")
	void listAllReturnListOfAnimesWhenSuccessful() {
		Anime anime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		String expectedName = anime.getName();
		
		List<Anime> animes = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Anime>>() {}).getBody();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isNotEmpty();
		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("getById Return Anime When Successful")
	void getByIdReturnAnimeWhenSuccessful() {
		
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		long expectedId = savedAnime.getId();
		
		Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);
		
		Assertions.assertThat(anime)
			.isNotNull();
		
		Assertions.assertThat(anime).isEqualTo(savedAnime);
	}
	
	@Test
	@DisplayName("findByName Return List Of Animes WhenSuccessful")
	void findByNameReturnListOfAnimesWhenSuccessful() {
		
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		String expectedName = savedAnime.getName();
		
		String url = String.format("/animes/find?name=%s", expectedName);
		
		List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Anime>>() {}).getBody();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isNotEmpty();
		
		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("findByName Return empty List Of Animes When name is not found")
	void findByNameReturnEmptyListOfAnimesWhenNameIsNotFound() {
		
		animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		String url = String.format("/animes/find?name=%s", "inexistente");
		
		List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Anime>>() {}).getBody();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isEmpty();
		

	}
	
	@Test
	@DisplayName("save Return Anime When Successful")
	void saveReturnAnimeWhenSuccessful() {
		
		AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimeToBeSaved();
		
		ResponseEntity<Anime> response = testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);
		
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getId()).isNotNull();
	}
	
	@Test
	@DisplayName("replace update Anime When Successful")
	void replaceUpdateAnimeWhenSuccessful() {
		
		Anime animeSaved = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		animeSaved.setName("new Name");
		
		ResponseEntity<Void> response = testRestTemplate.exchange("/animes", HttpMethod.PUT,
				new HttpEntity<>(animeSaved) ,Void.class);
		
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Assertions.assertThat(animeSaved.getName()).isEqualTo(animeRepository.findById(animeSaved.getId()).get().getName());
	}
	
	@Test
	@DisplayName("delete remove Anime When Successful")
	void deleteRemoveAnimeWhenSuccessful() {
		
		Anime animeSaved = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		ResponseEntity<Void> response = testRestTemplate.exchange("/animes/{id}", HttpMethod.DELETE,
				null ,Void.class, animeSaved.getId());
		
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

	}
}
