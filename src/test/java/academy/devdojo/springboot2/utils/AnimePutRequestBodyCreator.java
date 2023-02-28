package academy.devdojo.springboot2.utils;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {

	public static AnimePutRequestBody createAnimeToBeSaved() {
		Anime anime = AnimeCreator.createValidAnime();
		return AnimePutRequestBody.builder()
				.id(anime.getId())
				.name(anime.getName())
				.build();
	}
}
