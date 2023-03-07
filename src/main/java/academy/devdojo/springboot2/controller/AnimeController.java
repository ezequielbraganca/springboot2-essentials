package academy.devdojo.springboot2.controller;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("animes")
@RequiredArgsConstructor
@Log4j2
public class AnimeController {
	
	//private final DateUtil dateUtil;
	private final AnimeService animeService;
	
	@GetMapping(path = "/all")
	public ResponseEntity<List<Anime>> list(){
		//log.info(dateUtil.formatLocalDateToDatabaseStyle(LocalDateTime.now()));
		return ResponseEntity.ok(animeService.findAll());
	}

	@GetMapping
	@Operation(summary = "List all animes Paginated",
				description = "the default size is 20",
				tags = {"anime"})
	public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable){
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
	
	@GetMapping(path = "/id-auth/{id}")
	public ResponseEntity<Anime> getByIdAuth(@PathVariable long id, @AuthenticationPrincipal UserDetails userAuth){
		log.info("UserAuth: {}", userAuth);
		return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
	}
	
	@PostMapping(path = "/admin")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
		return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
	}
	
	@DeleteMapping(path = "/admin/{id}")
	//@PreAuthorize("hasRole('ADMIN')")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Successful Operation"),
		@ApiResponse(responseCode = "400", description = "When Anime does not exists in database")
	})
	public ResponseEntity<Void> delete(@PathVariable long id){
		animeService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping(path = "/admin")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody){
		animeService.replace(animePutRequestBody);
		return new ResponseEntity<>( HttpStatus.NO_CONTENT);
	}
}
