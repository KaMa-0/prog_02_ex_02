package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAPI {
    private final String URL = "https://prog2.fh-campuswien.ac.at/swagger-ui/index.html#/movie-controller/getMovies";

    public MovieAPI() {}

    public List<Movie> getAllMovies(){

        return new ArrayList<>();
    }
    public List<Movie> getMovies(String query, Genre genre, Object releaseYear, Object ratingFrom){
        return new ArrayList<>();
    }


}
