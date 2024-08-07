package com.movieflix.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.movieflix.entities.Movie;
import com.movieflix.entities.User;
import com.movieflix.services.MovieServices;
import com.movieflix.services.UserServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	UserServices userServ;
	@Autowired
	MovieServices movieServ;
	@PostMapping("register")
	public String addUsers(@ModelAttribute User user) {
		if(userServ.exitsEmail(user.getEmail())) {
			return "registerfailure";
		}
		else {
			userServ.addUser(user);
			return "registersuccess";
		}
	}
	@PostMapping("login")
	public String loginUser(@RequestParam String email, @RequestParam String password,HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		if(userServ.checkUser(email, password)) {
			session.setAttribute("email", email);
			if(email.equals("admin@gmail.com")) {
				return "adminhome";
			}
			else {
				for(String profile : userServ.getUser(email).toString().replaceAll("[\\[\\]]", "").split(",")) {
					if(profile.substring(1, profile.indexOf("=")).equals("address")) {
						if(profile.substring(profile.indexOf("=")+1).trim().replaceAll(" ", "").equals("")){
							continue;
						}
					}
					if(profile.contains("null")) {
						continue;
					}
					model.addAttribute(profile.substring(1, profile.indexOf("=")), profile.substring(profile.indexOf("=")+1));
				}
				List<Movie> previewMovies=new ArrayList<>();
				for(Movie movie : movieServ.viewMovie()) {
					if(movie.getMovieLink().contains("www.youtube.com")) {
						previewMovies.add(movie);
					}
				}
				for(Movie movie : previewMovies) {
					String link=movie.getMovieLink();
					link="https://img.youtube.com/vi/"+link.substring(68, link.indexOf("?"))+"/hqdefault.jpg";
					movie.setMovieLink(link);
				}
				model.addAttribute("previewMovies", previewMovies);
				if(userServ.getUser(email).isPremium()) {
					model.addAttribute("premium", "premium");
				}
				return "home";
			}
		}
		else {
			redirectAttributes.addFlashAttribute("login_failure", "login_failure");
        	return "redirect:/map-login";
		}
	}
	@GetMapping("viewuser")
	public String viewUser(Model model,HttpSession session) {
		List<User> userList=userServ.viewUser();
		String email=session.getAttribute("email").toString();
		User user=userServ.getUser(email);
		model.addAttribute("email", user.getEmail());
		model.addAttribute("password", user.getPassword());
		model.addAttribute("users", userList);
		return "viewuser";
	}
	@GetMapping("exploremovies")
	public String exploreMovies(Model model,HttpSession session) {
		String email=session.getAttribute("email").toString();
		User user=userServ.getUser(email);
		if(user.isPremium()) {
			model.addAttribute("email", user.getEmail());
			model.addAttribute("password", user.getPassword());
			model.addAttribute("movies",movieServ.viewMovie());
			return "viewmovieuser";
		}
		else {
			return "payment";
		}
	}
	@GetMapping("updateprofile")
	public String updateUser(HttpSession session, Model model) {
		User user=userServ.getUser(session.getAttribute("email").toString());
		if(user.getGender()==null) {
		}
		else if(user.getGender().equals("male")) {
			model.addAttribute("male", user.getGender());
		}
		else if(user.getGender().equals("female")) {
			model.addAttribute("female", user.getGender());
		}
		else {
			model.addAttribute("other", user.getGender());
		}
		model.addAttribute("user", user);
		return "userupdate";
	}
	@PutMapping("updateuser")
	public String updateUser(@ModelAttribute User user, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		User currUser=userServ.getUser(user.getEmail());
		user.setId(currUser.getId());
		user.setPremium(currUser.isPremium());
		userServ.updateUser(user);
		redirectAttributes.addFlashAttribute("message", "The information has been updated successfully.");
		return "redirect:/home";
	}
	@GetMapping("home")
	public String home(HttpSession session, Model model) {
		if(session.getAttribute("email")!=null) {
			String email=session.getAttribute("email").toString();
			if(userServ.getUser(email)==null) {
				return "redirect:/";
			}
			for(String profile : userServ.getUser(email).toString().replaceAll("[\\[\\]]", "").split(",")) {
				if(profile.substring(1, profile.indexOf("=")).equals("address")) {
					if(profile.substring(profile.indexOf("=")+1).trim().replaceAll(" ", "").equals("")){
						continue;
					}
				}
				if(profile.contains("null")) {
					continue;
				}
				model.addAttribute(profile.substring(1, profile.indexOf("=")), profile.substring(profile.indexOf("=")+1));
			}
			List<Movie> previewMovies=new ArrayList<>();
			for(Movie movie : movieServ.viewMovie()) {
				if(movie.getMovieLink().contains("www.youtube.com")) {
					previewMovies.add(movie);
				}
			}
			for(Movie movie : previewMovies) {
				String link=movie.getMovieLink();
				link="https://img.youtube.com/vi/"+link.substring(68, link.indexOf("?"))+"/hqdefault.jpg";
				movie.setMovieLink(link);
			}
			model.addAttribute("previewMovies", previewMovies);
			if(userServ.getUser(email).isPremium()) {
				model.addAttribute("premium", "premium");
			}
			return "home";
		}
		else {
			return "redirect:/";
		}
	}
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	@DeleteMapping("deleteuser")
	public String deleteUser(HttpSession session, RedirectAttributes redirectAttributes) {
		userServ.deleteUser(session.getAttribute("email").toString());
		redirectAttributes.addFlashAttribute("message","Your account has been deleted successfully.");
		return "redirect:/";
	}
	@GetMapping("admin-deleteuser")
	public String forDeleteUsers(Model model) {
		model.addAttribute("users", userServ.viewUser());
		return "deleteusers";
	}
	@DeleteMapping("deleteusers")
	public String deleteUsers(@RequestParam(value = "deletelist", required = false) String deletelist, RedirectAttributes redirectAttributes) {
		if(deletelist == null || deletelist.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "No accounts have been selected for deletion.");
			return "redirect:/adminhome";
		}
		deletelist=deletelist.replaceAll(" ", "");
		String emails[]=deletelist.split(",");
		for(String email : emails) {
			userServ.deleteUser(email);
		}
		redirectAttributes.addFlashAttribute("message", "The accounts have been deleted successfully.");
		return "redirect:/adminhome";
	}
	@GetMapping("admin-updateuser")
	public String forPremiumUpdate(Model model) {
		List<User> premiumUsers=new ArrayList<>();
		for(User user : userServ.viewUser()) {
			if(user.isPremium()) {
				premiumUsers.add(user);
			}
		}
		model.addAttribute("premiumUsers", premiumUsers);
		return "premiumupdate";
	}
	@PutMapping("updatedpremium")
	public String updatedPremium(@RequestParam(value = "removelist", required = false) String removelist, RedirectAttributes redirectAttributes) {
		if(removelist == null || removelist.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "No selected users to remove the premium.");
			return "redirect:/adminhome";
		}
		removelist=removelist.replaceAll(" ", "");
		String emails[]=removelist.split(",");
		for(String email : emails) {
			User user=userServ.getUser(email);
			user.setPremium(false);
			userServ.updateUser(user);
		}
		redirectAttributes.addFlashAttribute("message", "Selected users premium has been removed and the premium users list has been updated.");
		return "redirect:/adminhome";
	}
	@PostMapping("forgot-password")
    public String forgotPassword(@RequestParam String email, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if(userServ.sendOtp(email).equals("OTP sent to your email")) {
        	session.setAttribute("reset-password_email", email);
        	model.addAttribute("email", email);
        	return "reset-password";
        }
        else {
        	redirectAttributes.addFlashAttribute("forgot_password_message", "No account found for this email address!");
        	return "redirect:/map-login";
        }
    }
    @PostMapping("reset-password")
    public String resetPassword(@RequestParam String otp, @RequestParam String newPassword, HttpSession session, RedirectAttributes redirectAttributes) {
    	String email=session.getAttribute("reset-password_email").toString();
        if(userServ.resetPassword(email, otp, newPassword).equals("Password reset successfully")) {
        	redirectAttributes.addFlashAttribute("forgot_password_message", "The password has been changed successfully.");
        	return "redirect:/map-login";
        }
        else {
        	redirectAttributes.addFlashAttribute("message", "Wrong OTP, please enter the correct OTP!");
        	return "redirect:/reset-password";
        }
    }
}