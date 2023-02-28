package academy.devdojo.springboot2.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("animes")
@RequiredArgsConstructor
public class AnimeController {
	
	//private final DateUtil dateUtil;
	private final AnimeService animeService;
	
	@GetMapping(path = "/all")
	public ResponseEntity<List<Anime>> list(){
		//log.info(dateUtil.formatLocalDateToDatabaseStyle(LocalDateTime.now()));
		return ResponseEntity.ok(animeService.findAll());
	}

	@GetMapping
	public ResponseEntity<Page<Anime>> list(Pageable pageable){
		//log.info(dateUtil.formatLocalDateToDatabaseStyle(LocalDateTime.now()));
		return ResponseEntity.ok(animeService.findAll(pageable));
	}
	
	
	
	@GetMapping(path = "/find")
	public ResponseEntity<List<Anime>> findByName(@RequestParam String name){
		//log.info(dateUtil.formatLocalDateToDatabaseStyle(LocalDateTime.now()));
		return ResponseEntity.ok(animeService.findByName(name));
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Anime> getById(@PathVariable long id){
		//log.info(dateUtil.formatLocalDateToDatabaseStyle(LocalDateTime.now()));
		return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
	}
	
	@PostMapping
	public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
		return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id){
		animeService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping
	public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody){
		animeService.replace(animePutRequestBody);
		return new ResponseEntity<>( HttpStatus.NO_CONTENT);
	}
}
