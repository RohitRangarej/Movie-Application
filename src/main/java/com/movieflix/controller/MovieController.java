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
public class MovieController {
	@Autowired
	MovieServices movieServ;
	@Autowired
	UserServices userServ;
	
	@PostMapping("addmovie")
	public String addMovie(@ModelAttribute Movie movie, Model model, HttpSession session) {
		movieServ.addMovie(movie);
		String email=session.getAttribute("email").toString();
		User user=userServ.getUser(email);
		model.addAttribute("email", user.getEmail());
		model.addAttribute("password", user.getPassword());
		return"movieaddsuccess";
	}
	@GetMapping("viewmovie")
	public String viewMovie(Model model, HttpSession session) {
		String email=session.getAttribute("email").toString();
		User user=userServ.getUser(email);
		model.addAttribute("email", user.getEmail());
		model.addAttribute("password", user.getPassword());
		model.addAttribute("movies", movieServ.viewMovie());
		return "viewmovie";
	}
	@PostMapping("updatemovie")
	public String forMovieUpdate(@RequestParam int movie_id, Model model) {
		Movie movie=movieServ.getMovie(movie_id);
		String[] allGenres= {"SciFi","Horror","Thriller","Slice of life","Comedy","Action","Fantasy","Romance","Sports","Adventure","Mythology"};
		List<String> unCheckedGenres=new ArrayList<>();
		if(movie.getMovieGenre() == null || movie.getMovieGenre().trim().isEmpty()) {
			for(int i=0;i<allGenres.length;i++) {
				unCheckedGenres.add(allGenres[i]);
			}
			model.addAttribute("movie", movie);
			model.addAttribute("unCheckedGenres", unCheckedGenres);
			return "movieupdate";
		}
		else {
			String[] checkedGenres=movie.getMovieGenre().replaceAll(", ", ",").split(",");
			for(int i=0;i<allGenres.length;i++) {
				for(int j=0;j<checkedGenres.length;j++) {
					if(allGenres[i].equals(checkedGenres[j])) {
						break;
					}
					if(j==checkedGenres.length-1) {
						unCheckedGenres.add(allGenres[i]);
					}
				}
			}
			model.addAttribute("movie", movie);
			model.addAttribute("checkedGenres", checkedGenres);
			model.addAttribute("unCheckedGenres", unCheckedGenres);
			return "movieupdate";
		}
	}
	@PutMapping("updatedmovie")
	public String movieUpdate(@RequestParam int id, @ModelAttribute Movie movie) {
		movie.setMovie_id(id);
		movieServ.addMovie(movie);
		return "redirect:/viewmovie";
	}
	@DeleteMapping("deletemovie")
	public String movieDelete(@RequestParam int movie_id,RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", "The movie with ID-"+movie_id+" has been deleted successfully.");
		movieServ.deleteMovie(movie_id);
		return "redirect:/viewmovie";
	}
}
