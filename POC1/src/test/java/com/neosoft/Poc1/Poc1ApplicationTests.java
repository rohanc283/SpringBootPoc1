package com.neosoft.Poc1;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;

import com.neosoft.Poc1.Controller.UserContoller;
import com.neosoft.Poc1.Model.User;
import com.neosoft.Poc1.Repository.UsersRepository;
import com.neosoft.Poc1.Service.InvalidRequestException;
import com.neosoft.Poc1.Service.UserService;

@WebMvcTest(UserContoller.class)
public class Poc1ApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;
	// ObjectMapper provides functionality for reading and writing JSON,
	// either to and from basic POJOs

	@MockBean
	UserService userService;

	@MockBean
	UsersRepository usersRepository;

	User RECORD_1 = new User(1, "Kajal", "Chaudhary", (long) 401209, new Date(2021 - 8 - 11), new Date(2003 - 12 - 10),
			"kajal@gmail.com", "kajal", (long) 832972048,false);
	User RECORD_2 = new User(2, "Rohan", "Chaudhary", (long) 401209, new Date(2021 - 8 - 11), new Date(2000 - 11 - 23),
			"Rohanc283@gmail.com", "Rohan", (long) 832972048,false);
	User RECORD_3 = new User(3, "Hero", "Chaudhary", (long) 401209, new Date(2022 - 8 - 11), new Date(2021 - 11 - 23),
			"hero@gmail.com", "hero", (long) 832972048, false);

	@Test
	void contextLoads() {
	}

	@Test
	public void getAllRecords_success() throws Exception {
		List<User> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

		Mockito.when(userService.getAllUsers()).thenReturn(records);
		// When findAll called then ready with records (No DB calls)
		mockMvc.perform(MockMvcRequestBuilders.get("/allusers").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) // 200
				.andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[2].name", is("Hero")));
	}
	
	@Test
	public void getAllSoftRecords_success() throws Exception {
		List<User> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

		Mockito.when(userService.getSoftUsers()).thenReturn(records);
		// When findAll called then ready with records (No DB calls)
		mockMvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) // 200
				.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	public void createRecord_success() throws Exception {
		User record = User.builder().id(5).name("John").surname("Doe").pincode((long) 401280)
				.doj(new Date(2021 - 12 - 11)).dob(new Date(1998 - 8 - 15)).email("john@gmail.com").password("doe")
				.phoneno((long) 80804454).build();

		Mockito.when(userService.addUsers(record)).thenReturn(record);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/add/users")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(record));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("John")));
	}

	@Test
	public void deleteUsersById_success() throws Exception {

		Mockito.when(userService.getUsersById(RECORD_2.getId())).thenReturn(Optional.of(RECORD_2));

		mockMvc.perform(MockMvcRequestBuilders.delete("/delete/users/2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void deleteSoftUsersById_success() throws Exception {

		Mockito.when(userService.getUsersById(RECORD_2.getId())).thenReturn(Optional.of(RECORD_2));

		mockMvc.perform(MockMvcRequestBuilders.delete("/delete/softusers/2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteUsersById_notFound() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.delete("/delete/users/8").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidRequestException))
				.andExpect(result -> assertEquals("User with ID 8 does not exist.",
						result.getResolvedException().getMessage()));
	}
	
	@Test
	public void deleteSoftUsersById_notFound() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.delete("/delete/softusers/8").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidRequestException))
				.andExpect(result -> assertEquals("User with ID 8 does not exist.",
						result.getResolvedException().getMessage()));
	}

	@Test
	public void getPatientByPincode_success() throws Exception {
		List<User> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
		Mockito.when(userService.getUsersByPincode(RECORD_1.getPincode())).thenReturn(records);

		mockMvc.perform(MockMvcRequestBuilders.get("/usersbypincode/401209").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[0].pincode", is(401209)));
	}

	@Test
	public void getUsersByName_success() throws Exception {
		List<User> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
		Mockito.when(userService.getUsersByName(RECORD_1.getName())).thenReturn(records);

		mockMvc.perform(MockMvcRequestBuilders.get("/usersbyname/Kajal").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[0].name", is("Kajal")));
	}

	@Test
	public void getUsersBySurname_success() throws Exception {
		List<User> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
		Mockito.when(userService.getUsersBySurName(RECORD_1.getSurname())).thenReturn(records);

		mockMvc.perform(MockMvcRequestBuilders.get("/usersbysurname/Chaudhary").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[0].surname", is("Chaudhary")));
	}

	@Test
	public void getUsersBySortByDoj() throws Exception {
		List<User> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
		Mockito.when(userService.sortByDoj()).thenReturn(records);

		mockMvc.perform(MockMvcRequestBuilders.get("/sort/sortbyDoj").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[0].name", is("Kajal")));
	}

	@Test
	public void getUsersBySortByDob() throws Exception {
		List<User> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
		Mockito.when(userService.sortByDob()).thenReturn(records);

		mockMvc.perform(MockMvcRequestBuilders.get("/sort/sortbyDob").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[2].id", is(3)));

	}

	@Test
	public void updateUsersRecord_success() throws Exception {
		User record = User.builder().id(1).name("John").surname("Doe").pincode((long) 401280)
				.doj(new Date(2021 - 12 - 11)).dob(new Date(1998 - 8 - 15)).email("john@gmail.com").password("doe")
				.phoneno((long) 80804454).build();

		Mockito.when(userService.getUsersById(RECORD_1.getId())).thenReturn(Optional.of(RECORD_1));

		Mockito.when(userService.update(record)).thenReturn(record);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/update/users/1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(record));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("John")));
	}

	@Test
	public void updateUsersRecord_recordNotFound() throws Exception {
		User updatedRecord = User.builder().id(5).name("John").surname("Doe").pincode((long) 401280)
				.doj(new Date(2021 - 12 - 11)).dob(new Date(1998 - 8 - 15)).email("john@gmail.com").password("doe")
				.phoneno((long) 80804454).build();

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/update/users/5")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(updatedRecord));

		mockMvc.perform(mockRequest).andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidRequestException))
				.andExpect(result -> assertEquals("User with ID 5 does not exist.",
						result.getResolvedException().getMessage()));
	}

}
