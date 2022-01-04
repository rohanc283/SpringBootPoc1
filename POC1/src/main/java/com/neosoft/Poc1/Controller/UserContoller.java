package com.neosoft.Poc1.Controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.neosoft.Poc1.Model.User;
import com.neosoft.Poc1.Service.InvalidRequestException;
import com.neosoft.Poc1.Service.UserService;

@RestController
public class UserContoller {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/users")
	public List<User> getSoftUsers(){
		return userService.getSoftUsers();
	}
	
	@GetMapping("/allusers")
	public List<User> getAllUsers(){
		return userService.getAllUsers();
	}
	
	@GetMapping("/usersbyname/{name}")
	public List<User> getUsersByName(@PathVariable String name){
		return userService.getUsersByName(name);
	}
	
	
	@GetMapping("/usersbysurname/{surname}")
	public List<User> getUsersBySurName(@PathVariable String surname){
		return userService.getUsersBySurName(surname);
	}
	
	@GetMapping("/usersbypincode/{pincode}")
	public List<User> getUsersByPincode(@PathVariable long pincode){
		return userService.getUsersByPincode(pincode);
	}
	
	@GetMapping("/sort/sortbyDob")
	public List<User> sortByDob( User user) {
		return userService.sortByDob();
	}
	
	@GetMapping("/sort/sortbyDoj")
	public List<User> sort( User user) {
		return userService.sortByDoj();
	}
	
	@PostMapping("/add/users")
	public User addUsers(@RequestBody User user){
		 return userService.addUsers(user);
	}
	
	
	@DeleteMapping("/delete/users/{id}")
	public void  delete(@PathVariable  int id) {
		 if (!userService.getUsersById(id).isPresent()) {
	            throw new InvalidRequestException("User with ID " + 
	            			id + " does not exist.");
	        }
	        userService.deleteUsersById(id);
	}
	
	@DeleteMapping("/delete/softusers/{id}")
	public void  softDelete(@PathVariable  int id) {
		 if (!userService.getUsersById(id).isPresent()) {
	            throw new InvalidRequestException("User with ID " + 
	            			id + " does not exist.");
	        }
		 boolean value= true;
	        userService.softdeleteUsersById(id,value);
	}
	
	@PutMapping("/update/users/{id}")
	public User updateInfo(@RequestBody User user , @PathVariable int id) {
		if(userService.getUsersById(id).isPresent()) {
			user.setId(id);
			return userService.update(user);
		}
		else {
			throw new InvalidRequestException("User with ID " + 
        			user.getId() + " does not exist.");
		}	
	}
	
}
