package com.movieflix.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.movieflix.entities.User;
import com.movieflix.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserServices {
	@Autowired
	UserRepository userRepo;
	@Autowired
    private JavaMailSender mailSender;
	@Override
	public String addUser(User user) {
		// TODO Auto-generated method stub
		userRepo.save(user);
		return null;
	}
	@Override
	public boolean exitsEmail(String email) {
		// TODO Auto-generated method stub
		if(userRepo.findByEmail(email)==null) {
			return false;
		}
		else {
			return true;
		}
	}
	@Override
	public boolean checkUser(String email, String password) {
		// TODO Auto-generated method stub
		User user=userRepo.findByEmail(email);
		if(user==null) {
			return false;
		}
		if(user.getPassword().equals(password)) {
			return true;
		}
		else {
			return false;
		}
	}
	@Override
	public List<User> viewUser() {
		// TODO Auto-generated method stub
		return userRepo.findAll();
	}
	@Override
	public User getUser(String email) {
		// TODO Auto-generated method stub
		return userRepo.findByEmail(email);
	}
	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		userRepo.save(user);
	}
	@Override
	public void deleteUser(String email) {
		// TODO Auto-generated method stub
		User user=userRepo.findByEmail(email);
		userRepo.deleteById(user.getId());
	}
	private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
	@Override
    public String sendOtp(String email) {
        if (exitsEmail(email)) {
            User user = userRepo.findByEmail(email);
            String otp = generateOtp();
            user.setOtp(otp);
            userRepo.save(user);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password Reset OTP");
            message.setText("Your OTP for password reset is: " + otp);
            mailSender.send(message);

            return "OTP sent to your email";
        } else {
            return "Email not found";
        }
    }
	@Override
	public String resetPassword(String email, String otp, String newPassword) {
		// TODO Auto-generated method stub
		User user = userRepo.findByEmail(email);
        if (otp.equals(user.getOtp())) {
            user.setPassword(newPassword);  // Ensure you encode the password if needed
            user.setOtp(null);  // Clear the OTP
            userRepo.save(user);
            return "Password reset successfully";
        } else {
            return "Invalid OTP";
        }
	}
}
