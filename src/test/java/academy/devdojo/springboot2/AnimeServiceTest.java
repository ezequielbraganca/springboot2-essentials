package academy.devdojo.springboot2;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.utils.AnimeCreator;

@ExtendWith(SpringExtension.class)
public class AnimeServiceTest {

	@InjectMocks
	private AnimeService animeService;
	
	@Mock
	private AnimeRepository animeRepository;
	
	@BeforeEach
	void setup() {
		PageImpl<Anime> animesPage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
			.thenReturn(animesPage);
		
		BDDMockito.when(animeRepository.findAll())
			.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyLong()))
			.thenReturn(Optional.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepository.findByName(ArgumentMatchers.anyString()))
			.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepository.save(ArgumentMatchers.any(Anime.class)))
			.thenReturn(AnimeCreator.createValidAnime());
		
		
		BDDMockito.doNothing().when(animeRepository).delete(ArgumentMatchers.any(Anime.class));
	}

	@Test
	@DisplayName("findAll Return List Of Animes Inside PageObject When Successful")
	void findAllReturnListOfAnimesInsidePageObjectWhenSuccessful() {
		String expectedName = AnimeCreator.createValidAnime().getName();
		
		Page<Anime> animesPage = animeService.findAll(PageRequest.of(1, 1));
		
		Assertions.assertThat(animesPage).isNotNull();
		Assertions.assertThat(animesPage.toList())
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(animesPage.toList().get(0).getName()).isEqualTo(expectedName);
	}
	
	
	@Test
	@DisplayName("listAll Return List Of Animes When Successful")
	void findAllReturnListOfAnimesWhenSuccessful() {
		String expectedName = AnimeCreator.createValidAnime().getName();
		
		List<Anime> animes = animeService.findAll();
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("findByIdOrThrowBadRequestException Return Anime When Successful")
	void findByIdOrThrowBadRequestExceptionReturnAnimeWhenSuccessful() {
		Long expectedId = AnimeCreator.createValidAnime().getId();
		
		Anime anime = animeService.findByIdOrThrowBadRequestException(1l);
		
		Assertions.assertThat(anime).isNotNull();

		Assertions.assertThat(anime.getId())
			.isNotNull()
			.isEqualTo(expectedId);
	}
	
	@Test
	@DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when anime is not found")
	void findByIdOrThrowBadRequestExceptionThrowsBadRequestExceptionWhenAnimeIsNotFound() {
		
		BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyLong()))
		.thenReturn(Optional.empty());
		
		Assertions.assertThatExceptionOfType(BadRequestException.class)
			.isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(1l));
		
		
	}
	
	@Test
	@DisplayName("findByName Return List Of Animes WhenSuccessful")
	void findByNameReturnListOfAnimesWhenSuccessful() {
		
		String expectedName = AnimeCreator.createValidAnime().getName();
		
		List<Anime> animes = animeService.findByName("name");
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isNotEmpty()
			.hasSize(1);
		
		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("findByName Return empty List Of Animes When name is not found")
	void findByNameReturnEmptyListOfAnimesWhenNameIsNotFound() {
		
		BDDMockito.when(animeRepository.findByName(ArgumentMatchers.anyString()))
		.thenReturn(Collections.emptyList());
		
		List<Anime> animes = animeService.findByName("name");
		
		Assertions.assertThat(animes)
			.isNotNull()
			.isEmpty();

	}
	
//	@Test
//	@DisplayName("save Return Anime When Successful")
//	void saveReturnAnimeWhenSuccessful() {
//		
//		Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimeToBeSaved());
//		
//		Assertions.assertThat(anime)
//			.isNotNull()
//			.isEqualTo(AnimeCreator.createValidAnime());
//	}
	
//	@Test
//	@DisplayName("replace update Anime When Successful")
//	void replaceUpdateAnimeWhenSuccessful() {
//		
//		Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createAnimeToBeSaved()))
//				.doesNotThrowAnyException();
//		
//		
//	}
	
	@Test
	@DisplayName("delete remove Anime When Successful")
	void deleteRemoveAnimeWhenSuccessful() {
		
		Assertions.assertThatCode(() -> animeService.delete(1l))
				.doesNotThrowAnyException();
		
	
	}
}
