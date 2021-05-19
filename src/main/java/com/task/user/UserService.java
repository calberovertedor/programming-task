package com.task.user;

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
public class UserService {

	private static final String ERROR_MESSAGE = "Error sending User API request. Request body is empty.";

	private static final String API_URL = "https://jsonplaceholder.typicode.com";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Gets a list of Users from the JSON Placeholder API.
	 *
	 * @return the User list
	 * @throws ApiResponseNotValidException if the request body is empty
	 */
	public List<User> getUsers() throws ApiResponseNotValidException {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/users").build().toUri();

		ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);

		if (response.hasBody()) {
			return Arrays.asList(response.getBody());
		} else {
			throw new ApiResponseNotValidException(ERROR_MESSAGE);
		}
	}

	/**
	 * Gets the User with the given ID.
	 *
	 * @param userId the User ID
	 * @return the User with the given ID
	 * @throws ApiResponseNotValidException if the request body is empty
	 */
	public User getUserById(final Integer userId) throws ApiResponseNotValidException {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/users/{userId}").build(userId);

		ResponseEntity<User> response = restTemplate.getForEntity(uri, User.class);

		if (response.hasBody()) {
			return response.getBody();
		} else {
			throw new ApiResponseNotValidException(ERROR_MESSAGE);
		}

	}

	/**
	 * Gets the Users with the given name.
	 *
	 * @param name the name
	 * @return the User with the given name
	 * @throws ApiResponseNotValidException if the request body is empty
	 */
	public List<User> getUsersByName(final String name) throws ApiResponseNotValidException {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/users").queryParam("name", name).build().toUri();

		ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);

		if (response.hasBody()) {
			return Arrays.asList(response.getBody());
		} else {
			throw new ApiResponseNotValidException(ERROR_MESSAGE);
		}

	}

	/**
	 * Creates a User.
	 *
	 * @param user the User
	 * @return the created User
	 * @throws ApiResponseNotValidException if the request body is empty
	 */
	public User createUser(final User user) throws ApiResponseNotValidException {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/users").build().toUri();

		ResponseEntity<User> response = restTemplate.postForEntity(uri, user, User.class);

		if (response.hasBody()) {
			return user;
		} else {
			throw new ApiResponseNotValidException(ERROR_MESSAGE);
		}

	}

	/**
	 * Updates a User.
	 *
	 * @param user the User
	 */
	public void updateUser(final User user) {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/users/{id}").build(user.getId());

		restTemplate.put(uri, user);

	}

	/**
	 * Deletes a User.
	 *
	 * @param userId the user ID
	 */
	public void deleteUserById(final Integer userId) {

		URI uri = UriComponentsBuilder.fromHttpUrl(API_URL).path("/users/{userId}").build(userId);

		restTemplate.delete(uri);

	}

	/**
	 * Gets the User with the given ID and saves it in JSON and XML format.
	 *
	 * @param userID the user ID
	 * @return the saved User
	 * @throws ApiResponseNotValidException if the request body is empty
	 * @throws SaveFileException            if the files are not saved correctly
	 */
	public User getAndSaveUserById(final Integer userId) throws ApiResponseNotValidException, SaveFileException {

		User user = getUserById(userId);

		saveUserToJsonFile(user);

		saveUserToXmlFile(user);

		return user;
	}

	/**
	 * Saves the User to a JSON file.
	 *
	 * @param user the User
	 * @throws SaveFileException the Save File exception
	 */
	private void saveUserToJsonFile(final User user) throws SaveFileException {

		try {

			Path path = Paths.get("src/main/resources/users/json/" + user.getId() + ".json");
			Files.writeString(path, objectMapper.writeValueAsString(user));
			log.info("Saved User with ID: {} to JSON file", user.getId());
		} catch (IOException e) {
			throw new SaveFileException("Error saving User JSON file", e);

		}
	}

	/**
	 * Saves the User to a XML file.
	 *
	 * @param user the User
	 * @throws SaveFileException the Save File exception
	 */
	private void saveUserToXmlFile(final User user) throws SaveFileException {
		try {

			JAXBContext context = JAXBContext.newInstance(User.class);
			Marshaller mar = context.createMarshaller();
			mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			mar.marshal(user, new File("src/main/resources/users/xml/" + user.getId() + ".xml"));
			log.info("Saved User with ID: {} to XML file", user.getId());
		} catch (JAXBException e) {
			throw new SaveFileException("Error saving User XML file", e);
		}
	}
}
