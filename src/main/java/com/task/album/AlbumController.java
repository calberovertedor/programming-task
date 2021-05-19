package com.task.album;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import com.task.exception.ApiResponseNotValidException;
import com.task.exception.SaveFileException;

@RestController
@RequestMapping("/albums")
public class AlbumController {

	@Autowired
	private AlbumService albumService;

	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Album>> getAlbums() throws ApiResponseNotValidException {

		return ResponseEntity.ok(albumService.getAlbums());
	}

	@GetMapping(path = "/{albumId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Album> getAlbumById(@PathVariable(name = "albumId") final Integer albumId)
			throws ApiResponseNotValidException {
		return ResponseEntity.ok(albumService.getAlbumById(albumId));
	}

	@GetMapping(path = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Album>> getAlbumByTitle(@RequestParam(name = "title") final String title)
			throws ApiResponseNotValidException {
		return ResponseEntity.ok(albumService.getAlbumsByTitle(title));
	}

	@GetMapping(path = "/{albumId}/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Album> saveAlbumById(@PathVariable(name = "albumId") final Integer albumId)
			throws ApiResponseNotValidException, SaveFileException {

		return ResponseEntity.ok(albumService.getAndSaveAlbumById(albumId));
	}

	@PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Album> createAlbum(@RequestBody final Album album) throws ApiResponseNotValidException {

		return ResponseEntity.ok(albumService.createAlbum(album));
	}

	@PutMapping("/update")
	public ResponseEntity<Void> updateAlbum(@RequestBody final Album album) {
		albumService.updateAlbum(album);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{albumId}/delete")
	public ResponseEntity<Void> deleteAlbum(@PathVariable(name = "albumId") final Integer albumId) {
		albumService.deleteAlbumById(albumId);
		return ResponseEntity.ok().build();
	}
}
