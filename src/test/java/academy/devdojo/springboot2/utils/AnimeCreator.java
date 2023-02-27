package academy.devdojo.springboot2.utils;

import academy.devdojo.springboot2.domain.Anime;

public class AnimeCreator {

	public static Anime createAnimeToBeSaved() {
		return Anime.builder()
				.name("Dragon Ball Z")
				.build();
	}
	
	public static Anime createValidAnime() {
		return Anime.builder()
				.id(1l)
				.name("Ddragon Ball Z")
				.build();
	}
	
	public Anime createValidUpdateAnime() {
		return Anime.builder()
				.id(1l)
				.name("Ddragon Ball Z")
				.build();
	}
}
