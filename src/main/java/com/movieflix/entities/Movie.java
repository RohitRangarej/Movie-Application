package com.movieflix.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int movie_id;
	String movieName;
	@Column(length = 2000)
	String movieLink;
	String movieGenre;
	String movieCast;
	String movieDirector;
	public Movie() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Movie(int movie_id, String movieName, String movieLink, String movieGenre, String movieCast,
			String movieDirector) {
		super();
		this.movie_id = movie_id;
		this.movieName = movieName;
		this.movieLink = movieLink;
		this.movieGenre = movieGenre;
		this.movieCast = movieCast;
		this.movieDirector = movieDirector;
	}
	public int getMovie_id() {
		return movie_id;
	}
	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public String getMovieLink() {
		return movieLink;
	}
	public void setMovieLink(String movieLink) {
		this.movieLink = movieLink;
	}
	public String getMovieGenre() {
		return movieGenre;
	}
	public void setMovieGenre(String movieGenre) {
		this.movieGenre = movieGenre;
	}
	public String getMovieCast() {
		return movieCast;
	}
	public void setMovieCast(String movieCast) {
		this.movieCast = movieCast;
	}
	public String getMovieDirector() {
		return movieDirector;
	}
	public void setMovieDirector(String movieDirector) {
		this.movieDirector = movieDirector;
	}
	@Override
	public String toString() {
		return "Movie [movie_id=" + movie_id + ", movieName=" + movieName + ", movieLink=" + movieLink + ", movieGenre="
				+ movieGenre + ", movieCast=" + movieCast + ", movieDirector=" + movieDirector + "]";
	}
	
}
