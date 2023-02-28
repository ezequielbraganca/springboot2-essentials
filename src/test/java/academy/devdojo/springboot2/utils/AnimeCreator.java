package academy.devdojo.springboot2.utils;

import academy.devdojo.springboot2.domain.Anime;

public class AnimeCreator {
	
	private static String animeName = "Dragon Ball Z";

	public static Anime createAnimeToBeSaved() {
		return Anime.builder()
				.name(animeName)
				.build();
	}
	
	public static Anime createValidAnime() {
		return Anime.builder()
				.id(1l)
				.name(animeName)
				.build();
	}
	
	public Anime createValidUpdateAnime() {
		return Anime.builder()
				.id(1l)
				.name(animeName)
				.build();
	}
}
