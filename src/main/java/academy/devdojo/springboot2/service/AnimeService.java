package academy.devdojo.springboot2.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimeService {

	private final AnimeRepository animeRepository;

	public List<Anime> findAll() {
		return animeRepository.findAll();
	}

	public Page<Anime> findAll(Pageable pageable) {
		return animeRepository.findAll(pageable);
	}
	
	public List<Anime> findByName(String name) {
		return animeRepository.findByName(name);
	}

	public Anime findByIdOrThrowBadRequestException(long id) {
		return animeRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Anime not found"));

	}

	@Transactional
	public Anime save(AnimePostRequestBody animeRequestBody)  {
		return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animeRequestBody));
	}

	public void delete(long id) {
		animeRepository.delete(findByIdOrThrowBadRequestException(id));
	}

	public void replace(AnimePutRequestBody animePutRequestBody) {
		Anime savedAnime =  findByIdOrThrowBadRequestException(animePutRequestBody.getId());
		Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
		anime.setId(savedAnime.getId());
		animeRepository.save(anime);
		
	}
}
