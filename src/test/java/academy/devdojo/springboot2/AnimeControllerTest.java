package academy.devdojo.springboot2;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import academy.devdojo.springboot2.controller.AnimeController;
import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.utils.AnimeCreator;
import academy.devdojo.springboot2.utils.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.utils.AnimePutRequestBodyCreator;

@ExtendWith(SpringExtension.class)
public class AnimeControllerTest {
	
	@InjectMocks
	private AnimeController animeController;
	
	@Mock
	private AnimeService animeService;
	
	@BeforeEach
	void setup() {
		PageImpl<Anime> animesPage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeService.findAll(ArgumentMatchers.any()))
			.thenReturn(animesPage);
		
		BDDMockito.when(animeService.findAll())
			.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeService.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
			.thenReturn(AnimeCreator.createValidAnime());
		
		BDDMockito.when(animeService.findByName(ArgumentMatchers.anyString()))
			.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeService.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
		.thenReturn(AnimeCreator.createValidAnime());
		
		BDDMockito.doNothing().when(animeService).replace(ArgumentMatchers.any(AnimePutRequestBody.class));
		
		BDDMockito.doNothing().when(animeService).delete(ArgumentMatchers.anyLong());
	}

	@Test
	@DisplayName("list Return List Of Animes Inside PageObject When Successful")
	void listReturnListOfAnimesInsidePageObjectWhenSuccessful() {
		String expectedName = AnimeCreator.createValidAnime().getName();
		
		Page<Anime> animesPage = animeController.list(null).getBody();
		
		Assertions.assertThat(animesPage).isNotNull();
		Assertions.assertThat(animesPage.toList())
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(animesPage.toList().get(0).getName()).isEqualTo(expectedName);
	}
	
	
	@Test
	@DisplayName("listAll Return List Of Animes When Successful")
	void listAllReturnListOfAnimesWhenSuccessful() {
		String expectedName = AnimeCreator.createValidAnime().getName();
		
		List<Anime> animes = animeController.list().getBody();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("getById Return Anime When Successful")
	void getByIdReturnAnimeWhenSuccessful() {
		Long expectedId = AnimeCreator.createValidAnime().getId();
		
		Anime anime = animeController.getById(1l).getBody();
		
		Assertions.assertThat(anime).isNotNull();

		Assertions.assertThat(anime.getId())
			.isNotNull()
			.isEqualTo(expectedId);
	}
	
	@Test
	@DisplayName("findByName Return List Of Animes WhenSuccessful")
	void findByNameReturnListOfAnimesWhenSuccessful() {
		
		String expectedName = AnimeCreator.createValidAnime().getName();
		
		List<Anime> animes = animeController.findByName("name").getBody();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		
		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("findByName Return empty List Of Animes When name is not found")
	void findByNameReturnEmptyListOfAnimesWhenNameIsNotFound() {
		
		BDDMockito.when(animeService.findByName(ArgumentMatchers.anyString()))
		.thenReturn(Collections.emptyList());
		
		List<Anime> animes = animeController.findByName("name").getBody();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isEmpty();

	}
	
	@Test
	@DisplayName("save Return Anime When Successful")
	void saveReturnAnimeWhenSuccessful() {
		
		Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimeToBeSaved()).getBody();
		
		Assertions.assertThat(anime)
			.isNotNull()
			.isEqualTo(AnimeCreator.createValidAnime());
	}
	
	@Test
	@DisplayName("replace update Anime When Successful")
	void replaceUpdateAnimeWhenSuccessful() {
		
		Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.createAnimeToBeSaved()))
				.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createAnimeToBeSaved());
		
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("delete remove Anime When Successful")
	void deleteRemoveAnimeWhenSuccessful() {
		
		Assertions.assertThatCode(() -> animeController.delete(1l))
				.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = animeController.delete(1l);
		
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
}
