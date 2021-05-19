package com.task.album;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.net.URI;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.exception.ApiResponseNotValidException;
import com.task.exception.SaveFileException;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

	@InjectMocks
	private AlbumService albumService;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private ObjectMapper mockMapper;

	@Test
	public void testGetAlbums_IsSuccessfull() throws ApiResponseNotValidException {
		Album[] albumArray = { new Album(1, 1, "mock title 1"), new Album(2, 2, "mock title 2") };
		Mockito.when(
				restTemplate.getForEntity(URI.create("https://jsonplaceholder.typicode.com/albums"), Album[].class))
				.thenReturn(ResponseEntity.ok(albumArray));

		assertEquals(Arrays.asList(albumArray), albumService.getAlbums());

	}

	@Test
	public void testGetAlbums_BodyIsEmpty_ExceptionIsThrown() {

		Mockito.when(
				restTemplate.getForEntity(URI.create("https://jsonplaceholder.typicode.com/albums"), Album[].class))
				.thenReturn(ResponseEntity.ok().build());

		assertThrows(ApiResponseNotValidException.class, () -> {
			albumService.getAlbums();
		});

	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 3, 5 })
	public void testGetAlbumById_IsSuccessfull(Integer albumId) throws ApiResponseNotValidException {

		Album expectedAlbum = new Album(1, albumId, "mock title");
		Mockito.when(restTemplate.getForEntity(URI.create("https://jsonplaceholder.typicode.com/albums/" + albumId),
				Album.class)).thenReturn(ResponseEntity.ok(expectedAlbum));

		assertEquals(expectedAlbum, albumService.getAlbumById(albumId));
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 3, 5 })
	public void testGetAlbumById_BodyIsEmpty_ExceptionIsThrown(Integer albumId) {

		Mockito.when(restTemplate.getForEntity(URI.create("https://jsonplaceholder.typicode.com/albums/" + albumId),
				Album.class)).thenReturn(ResponseEntity.ok().build());

		assertThrows(ApiResponseNotValidException.class, () -> {
			albumService.getAlbumById(albumId);
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "title1", "title2", "title2" })
	public void testGetAlbumsByTitle_IsSuccessfull(String title) throws ApiResponseNotValidException {

		Album[] albumArray = { new Album(1, 1, "mock title 1"), new Album(2, 2, "mock title 2") };
		Mockito.when(restTemplate.getForEntity(URI.create("https://jsonplaceholder.typicode.com/albums?title=" + title),
				Album[].class)).thenReturn(ResponseEntity.ok(albumArray));

		assertEquals(Arrays.asList(albumArray), albumService.getAlbumsByTitle(title));
	}

	@ParameterizedTest
	@ValueSource(strings = { "title1", "title2", "title2" })
	public void testGetAlbumsByTitle_BodyIsEmpty_ExceptionIsThrown(String title) {

		Mockito.when(restTemplate.getForEntity(URI.create("https://jsonplaceholder.typicode.com/albums?title=" + title),
				Album[].class)).thenReturn(ResponseEntity.ok().build());

		assertThrows(ApiResponseNotValidException.class, () -> {
			albumService.getAlbumsByTitle(title);
		});
	}

	@Test
	public void testCreateAlbum_IsSuccessfull() throws ApiResponseNotValidException {
		Album expectedAlbum = new Album(1, 1, "mock title");
		Mockito.when(restTemplate.postForEntity(URI.create("https://jsonplaceholder.typicode.com/albums"),
				expectedAlbum, Album.class)).thenReturn(ResponseEntity.ok(expectedAlbum));
		assertEquals(expectedAlbum, albumService.createAlbum(expectedAlbum));

	}

	@Test
	public void testCreateAlbum_BodyIsEmpty_ExceptionIsThrown() throws ApiResponseNotValidException {
		Album expectedAlbum = new Album(1, 1, "mock title");
		Mockito.when(restTemplate.postForEntity(URI.create("https://jsonplaceholder.typicode.com/albums"),
				expectedAlbum, Album.class)).thenReturn(ResponseEntity.ok().build());
		assertThrows(ApiResponseNotValidException.class, () -> {
			albumService.createAlbum(expectedAlbum);
		});

	}

	@Test
	public void testUpdateAlbum_IsSuccessfull() {
		Album expectedAlbum = new Album(1, 1, "mock title");
		albumService.updateAlbum(expectedAlbum);
		verify(restTemplate).put(URI.create("https://jsonplaceholder.typicode.com/albums/1"), expectedAlbum);

	}

	@Test
	public void testDeleteAlbumById_IsSuccessfull() {
		albumService.deleteAlbumById(1);
		verify(restTemplate).delete(URI.create("https://jsonplaceholder.typicode.com/albums/1"));

	}

	@Test
	public void testGetAndSaveAlbumById_IsSuccessfull()
			throws ApiResponseNotValidException, SaveFileException, JsonProcessingException {
		Album expectedAlbum = new Album(1, 11, "mock title");
		Mockito.when(
				restTemplate.getForEntity(URI.create("https://jsonplaceholder.typicode.com/albums/" + 11), Album.class))
				.thenReturn(ResponseEntity.ok(expectedAlbum));
		Mockito.when(mockMapper.writeValueAsString(expectedAlbum))
				.thenReturn("{\"userId\":2,\"id\":11,\"title\":\"quam nostrum impedit mollitia quod et dolor\"}");
		assertEquals(expectedAlbum, albumService.getAndSaveAlbumById(11));

	}

}
