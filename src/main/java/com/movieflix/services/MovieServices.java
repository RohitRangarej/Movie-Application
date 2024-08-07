package com.movieflix.services;

import java.util.List;

import com.movieflix.entities.Movie;

public interface MovieServices {
	public String addMovie(Movie movie);
	public List<Movie> viewMovie();
	public Movie getMovie(int movie_id);
	public void deleteMovie(int movie_id);
}
