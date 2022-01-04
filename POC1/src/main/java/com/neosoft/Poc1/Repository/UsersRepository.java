package com.neosoft.Poc1.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.neosoft.Poc1.Model.User;

public interface UsersRepository extends JpaRepository<User, Integer>{
	
	List<User> findByName(String firstname);
	
	List<User> findBySurname(String firstname);
	
	List<User> findByPincode(long pincode);

	
	
	@Transactional
	@Modifying
	@Query("SELECT u From User u  WHERE u.deleted='false'")
	List<User>  findAllDeletedFalse();

	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.deleted=:value WHERE u.id=:uid")
	void softDelete(int uid,boolean value);
	

}
