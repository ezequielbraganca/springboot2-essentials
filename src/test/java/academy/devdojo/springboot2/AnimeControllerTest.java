package academy.devdojo.springboot2;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import academy.devdojo.springboot2.controller.AnimeController;
import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.utils.AnimeCreator;

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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
