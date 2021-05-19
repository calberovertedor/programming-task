package com.task.user;

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
@RequestMapping("/users")
public class UserController {
	
	
	@Autowired
	private UserService userService;

	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> getUsers() throws ApiResponseNotValidException {

		return ResponseEntity.ok(userService.getUsers());
	}

	@GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUserById(@PathVariable(name = "userId") final Integer userId)
			throws ApiResponseNotValidException {
		return ResponseEntity.ok(userService.getUserById(userId));
	}
	
	@GetMapping(path = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> getUserByName(@RequestParam(name = "name") final String name)
			throws ApiResponseNotValidException {
		return ResponseEntity.ok(userService.getUsersByName(name));
	}

	@GetMapping(path = "/{userId}/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> saveUserById(@PathVariable(name = "userId") final Integer userId)
			throws ApiResponseNotValidException, SaveFileException {

		return ResponseEntity.ok(userService.getAndSaveUserById(userId));
	}

	@PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> createUser(@RequestBody final User user) throws ApiResponseNotValidException {

		return ResponseEntity.ok(userService.createUser(user));
	}

	@PutMapping("/update")
	public ResponseEntity<Void> updateUser(@RequestBody final User user){
		userService.updateUser(user);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{userId}/delete")
	public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") final Integer userId){
		userService.deleteUserById(userId);
		return ResponseEntity.ok().build();
	}

}
