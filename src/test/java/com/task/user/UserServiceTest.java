package com.task.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.net.URI;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private ObjectMapper mockMapper;

	private User buildUser(Integer id) {
		Address expectedAddress = new Address("street", "suite", "city", "zipcode");
		Company expectedCompany = new Company("companyName", "catchPhrase", "bs");
		return new User(id, "name", "username", "email", expectedAddress, "phone", "website", expectedCompany);
	}

	@Test
	public void testGetUsers_IsSuccessfull() throws Exception {
		User[] userArray = { buildUser(1), buildUser(2), buildUser(3) };

		Mockito.when(restTemplate.getForEntity(URI.create("https://jsonplaceholder.typicode.com/users"), User[].class))
				.thenReturn(ResponseEntity.ok(userArray));

		assertEquals(Arrays.asList(userArray), userService.getUsers());
	}

	@Test
	public void testGetUserById_IsSuccessfull() throws Exception {
		User expectedUser = buildUser(1);
		Mockito.when(restTemplate.getForEntity(URI.create("https://jsonplaceholder.typicode.com/users/1"), User.class))
				.thenReturn(ResponseEntity.ok(expectedUser));

		assertEquals(expectedUser, userService.getUserById(1));
	}

	@Test
	public void testGetUsersByName_IsSuccessfull() throws Exception {
		User[] userArray = { buildUser(1), buildUser(2), buildUser(3) };

		Mockito.when(restTemplate.getForEntity(URI.create("https://jsonplaceholder.typicode.com/users?name=name"), User[].class))
				.thenReturn(ResponseEntity.ok(userArray));

		assertEquals(Arrays.asList(userArray), userService.getUsersByName("name"));
	}

	@Test
	public void testCreateUser_IsSuccessfull() throws Exception {
		User expectedUser = buildUser(1);
		Mockito.when(restTemplate.postForEntity(URI.create("https://jsonplaceholder.typicode.com/users"), expectedUser,
				User.class)).thenReturn(ResponseEntity.ok(expectedUser));
		assertEquals(expectedUser, userService.createUser(expectedUser));
	}

	@Test
	public void testUpdateUser_IsSuccessfull() throws Exception {
		User expectedUser = buildUser(1);
		userService.updateUser(expectedUser);
		verify(restTemplate).put(URI.create("https://jsonplaceholder.typicode.com/users/1"), expectedUser);
	}

	@Test
	public void testDeleteUserById_IsSuccessfull() throws Exception {

		userService.deleteUserById(1);
		verify(restTemplate).delete(URI.create("https://jsonplaceholder.typicode.com/users/1"));
	}

}
