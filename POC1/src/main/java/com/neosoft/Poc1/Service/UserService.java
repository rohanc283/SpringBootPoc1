package com.neosoft.Poc1.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.neosoft.Poc1.Model.User;
import com.neosoft.Poc1.Repository.UsersRepository;

@Component
public class UserService {
	
	@Autowired
	private UsersRepository accountRepository;
	
	
	public List<User> getSoftUsers(){
		return accountRepository.findAllDeletedFalse();
	}
	
	public List<User> getAllUsers(){
		return accountRepository.findAll();
	}
	
	public User addUsers(User user) {
		return accountRepository.save(user);
	}

	public void deleteUsersById(int id) {
		accountRepository.deleteById(id);
		
	}

	public Optional<User> getUsersById(int id) {
		// TODO Auto-generated method stub
		return accountRepository.findById(id);
	}

	public User update(User user) {
		// TODO Auto-generated method stub
		return accountRepository.save(user);
	}

	public List<User> getUsersByName(String name) {
		// TODO Auto-generated method stub
		return accountRepository.findByName(name);
	}

	public List<User> getUsersBySurName(String surname) {
		// TODO Auto-generated method stub
		return accountRepository.findBySurname(surname);
	}

	public List<User> getUsersByPincode(long pincode) {
		// TODO Auto-generated method stub
		return accountRepository.findByPincode(pincode);
	}
	
	
	public List<User> sortByDoj() {
		
	 return accountRepository.findAll().stream().sorted((o1, o2) -> o1.getDoj().
				compareTo(o2.getDoj())).collect(Collectors.toList());
		
//		Comparator<User> byNameComparator = Comparator.comparing(User::getName);
//		return users.stream().sorted(byNameComparator).collect(Collectors.toList());
	}
	
	public List<User> sortByDob() {
		
//		 return accountRepository.findAll().stream().sorted((o1, o2) -> o1.getDoj().
//					compareTo(o2.getDoj())).collect(Collectors.toList());
			
			Comparator<User> byDobComparator = Comparator.comparing(User::getDob);
			return  accountRepository.findAll().stream().sorted(byDobComparator).collect(Collectors.toList());
		}

	public void softdeleteUsersById(int id,boolean value) {
		accountRepository.softDelete(id,value);
	}

	

}
