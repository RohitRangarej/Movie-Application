package com.movieflix.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieflix.entities.Movie;
import com.movieflix.repository.MovieRepository;

@Service
public class MovieServiceImplementaion implements MovieServices {
	@Autowired
	MovieRepository movieRepo;
	
	@Override
	public String addMovie(Movie movie) {
		// TODO Auto-generated method stub
		movieRepo.save(movie);
		return null;
	}

	@Override
	public List<Movie> viewMovie() {
		// TODO Auto-generated method stub
		return movieRepo.findAll();
	}

	@Override
	public Movie getMovie(int movie_id) {
		// TODO Auto-generated method stub
		return movieRepo.findById(movie_id).get();
	}

	@Override
	public void deleteMovie(int movie_id) {
		// TODO Auto-generated method stub
		movieRepo.deleteById(movie_id);
	}


}
