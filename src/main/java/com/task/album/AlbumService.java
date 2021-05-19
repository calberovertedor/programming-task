package com.task.album;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.exception.ApiResponseNotValidException;
import com.task.exception.SaveFileException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AlbumService {

	private static final String ERROR_MESSAGE = "Error sending Album API request. Request body is empty.";

	private static final String API_URL = "https://jsonplaceholder.typicode.com";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Gets a list of Albums from the JSON Placeholder API.
	 *
	 * @return the Album list
	 * @throws ApiResponseNotValidException if the request body is empty
	 */
	public List<Album> getAlbums() throws ApiResponseNotValidException {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/albums").build().toUri();

		ResponseEntity<Album[]> response = restTemplate.getForEntity(uri, Album[].class);

		if (response.hasBody()) {
			return Arrays.asList(response.getBody());
		} else {
			throw new ApiResponseNotValidException(ERROR_MESSAGE);
		}
	}

	/**
	 * Gets the Album with the given ID.
	 *
	 * @param albumId the Album ID
	 * @return the Album with the given ID
	 * @throws ApiResponseNotValidException if the request body is empty
	 */
	public Album getAlbumById(final Integer albumId) throws ApiResponseNotValidException {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/albums/{albumId}").build(albumId);

		ResponseEntity<Album> response = restTemplate.getForEntity(uri, Album.class);

		if (response.hasBody()) {
			return response.getBody();
		} else {
			throw new ApiResponseNotValidException(ERROR_MESSAGE);
		}

	}

	/**
	 * Gets the Albums with the given title.
	 *
	 * @param title the title
	 * @return the Album with the given title
	 * @throws ApiResponseNotValidException if the request body is empty
	 */
	public List<Album> getAlbumsByTitle(final String title) throws ApiResponseNotValidException {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/albums").queryParam("title", title).build().toUri();

		ResponseEntity<Album[]> response = restTemplate.getForEntity(uri, Album[].class);

		if (response.hasBody()) {
			return Arrays.asList(response.getBody());
		} else {
			throw new ApiResponseNotValidException(ERROR_MESSAGE);
		}

	}

	/**
	 * Creates an Album.
	 *
	 * @param album the Album
	 * @return the created Album
	 * @throws ApiResponseNotValidException if the request body is empty
	 */
	public Album createAlbum(final Album album) throws ApiResponseNotValidException {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/albums").build().toUri();

		ResponseEntity<Album> response = restTemplate.postForEntity(uri, album, Album.class);

		if (response.hasBody()) {
			return album;
		} else {
			throw new ApiResponseNotValidException("Error sending create Album API request. Request body is empty.");
		}

	}

	/**
	 * Updates an Album.
	 *
	 * @param album the Album
	 */
	public void updateAlbum(final Album album) {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/albums/{id}").build(album.getId());

		restTemplate.put(uri, album);

	}

	/**
	 * Deletes an Album.
	 *
	 * @param albumId the album ID
	 */
	public void deleteAlbumById(final Integer albumId) {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/albums/{albumId}").build(albumId);

		restTemplate.delete(uri);

	}

	/**
	 * Gets the Album with the given ID and saves it in JSON and XML format.
	 *
	 * @param albumID the album ID
	 * @return the saved Album
	 * @throws ApiResponseNotValidException if the request body is empty
	 * @throws SaveFileException            if the files are not saved correctly
	 */
	public Album getAndSaveAlbumById(final Integer albumId) throws ApiResponseNotValidException, SaveFileException {

		Album album = getAlbumById(albumId);

		saveAlbumToJsonFile(album);

		saveAlbumToXmlFile(album);

		return album;
	}

	/**
	 * Saves the Album to a JSON file.
	 *
	 * @param album the Album
	 * @throws SaveFileException the Save File exception
	 */
	private void saveAlbumToJsonFile(final Album album) throws SaveFileException {

		try {

			Path path = Paths.get("src/main/resources/albums/json/" + album.getId() + ".json");
			Files.writeString(path, objectMapper.writeValueAsString(album));
			log.info("Saved Album with ID: {} to JSON file", album.getId());
		} catch (IOException e) {
			throw new SaveFileException("Error saving Album JSON file", e);

		}
	}

	/**
	 * Saves the Album to a XML file.
	 *
	 * @param album the Album
	 * @throws SaveFileException the Save File exception
	 */
	private void saveAlbumToXmlFile(final Album album) throws SaveFileException {
		try {

			JAXBContext context = JAXBContext.newInstance(Album.class);
			Marshaller mar = context.createMarshaller();
			mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			mar.marshal(album, new File("src/main/resources/albums/xml/" + album.getId() + ".xml"));
			log.info("Saved Album with ID: {} to XML file", album.getId());
		} catch (JAXBException e) {
			throw new SaveFileException("Error saving Album XML file", e);
		}
	}

}
