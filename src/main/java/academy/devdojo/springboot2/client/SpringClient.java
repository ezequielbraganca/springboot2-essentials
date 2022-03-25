package academy.devdojo.springboot2.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpringClient {

	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Anime> entity = restTemplate.getForEntity("http://localhost:8080/animes/{1d}", Anime.class, 3);
		log.info(entity);
		
		Anime[] animes = restTemplate.getForObject("http://localhost:8080/animes/all", Anime[].class);
		log.info(Arrays.toString(animes));
		
		
		ResponseEntity<List<Anime>> listAnimes= restTemplate.exchange("http://localhost:8080/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {});
		log.info(listAnimes.getBody());
		
		Anime kingdon = Anime.builder().name("kingdon").build();
		Anime insertItem = restTemplate.postForObject("http://localhost:8080/animes", kingdon, Anime.class);
		log.info(insertItem);
	}
}
