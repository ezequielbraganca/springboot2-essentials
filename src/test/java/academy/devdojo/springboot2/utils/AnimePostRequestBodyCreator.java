package academy.devdojo.springboot2.utils;

import academy.devdojo.springboot2.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {

	public static AnimePostRequestBody createAnimeToBeSaved() {
		return AnimePostRequestBody.builder()
				.name(AnimeCreator.createAnimeToBeSaved().getName())
				.build();
	}
}
