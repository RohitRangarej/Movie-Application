package com.movieflix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class NavController {
	@GetMapping("map-register")
	public String mapRegister() {
		return "register";
	}
	@GetMapping("map-login")
	public String mapLogin() {
		return "login";
	}
	@GetMapping("map-addmovie")
	public String addMovie() {
		return "addmovie";
	}
	@GetMapping("adminhome")
	public String adminhome(HttpSession session) {
		if(session.getAttribute("email").equals("admin@gmail.com")) {
			return "adminhome";
		}
		else {
			return "redirect:/";
		}
	}
	@GetMapping("map-terms")
	public String terms() {
		return "terms";
	}
	@GetMapping("map-privacy")
	public String privacy() {
		return "privacy";
	}
	@GetMapping("reset-password")
	public String resetPassword() {
		return "/reset-password";
	}
}
