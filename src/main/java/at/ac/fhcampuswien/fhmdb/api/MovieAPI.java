package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import okhttp3.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieAPI {
    private final String URL = "http://prog2.fh-campuswien.ac.at/swagger-ui/index.html#/movie-controller/getMovies";
    private final String DELIM = "&";

    public MovieAPI() {}

    public List<Movie> getAllMovies(){
        return getMovies(null, null, null, null);
    }

    public List<Movie> getMovies(String query, Genre genre, Object releaseYear, Object ratingFrom){
        OkHttpClient client = new OkHttpClient();
        String requestURL = buildRequestURL(query, genre, releaseYear, ratingFrom);

        Request request = new Request.Builder()
                .url(requestURL)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "http.agent")
                .build();

        // Try-Catch needed for handling execute() IO Exception
        try (Response response = client.newCall(request).execute()) {
            Gson gson = new Gson();
            Movie[] movies = gson.fromJson(response.body().string(), Movie[].class);
            System.out.println(Arrays.asList(movies));
            return Arrays.asList(movies);
        } catch (Exception e) {
            System.err.println("[ERROR] Request Execution failed: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    private String buildRequestURL(String query, Genre genre, Object releaseYear, Object ratingFrom) {
        String requestURL = URL;

        // If parameter for specific request given -> build specific request URL, otherwise: all movies
        if (query != null || genre != null || releaseYear != null || ratingFrom != null) {
            requestURL += "?"; // create specific request
            if (query != null) {
                requestURL += "query="+query+DELIM;
            }
            if (genre != null) {
                requestURL += "genre="+genre+DELIM;
            }
            if (releaseYear != null) {
                requestURL += "releaseYear="+releaseYear+DELIM;
            }
            if (ratingFrom != null) {
                requestURL += "ratingFrom="+ratingFrom+DELIM;
            }
        }

        return requestURL;
    }
}
