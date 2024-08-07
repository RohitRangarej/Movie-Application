package com.movieflix.services;

import java.util.List;

import com.movieflix.entities.User;

public interface UserServices {
	public String addUser(User user);
	public boolean exitsEmail(String email);
	public boolean checkUser(String email, String password);
	public List<User> viewUser();
	public User getUser(String email);
	public void updateUser(User user);
	public void deleteUser(String email);
	public String sendOtp(String email);
	public String resetPassword(String email, String otp, String newPassword);
}
