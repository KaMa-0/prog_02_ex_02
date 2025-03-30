package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import okhttp3.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieAPI {
    private static final String URL = "https://prog2.fh-campuswien.ac.at/movies";
    private final Gson gson;

    public MovieAPI() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Movie.class, new MovieDeserializer())
                .create();
    }

    static class MovieDeserializer implements JsonDeserializer<Movie> {
        @Override
        public Movie deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject movieJson = json.getAsJsonObject();

            String id = getStringOrDefault(movieJson, "id", "");
            String title = getStringOrDefault(movieJson, "title", "");
            String description = getStringOrDefault(movieJson, "description", "");
            int releaseYear = getIntOrDefault(movieJson, "releaseYear", 0);
            int lengthInMinutes = getIntOrDefault(movieJson, "lengthInMinutes", 0);
            double rating = getDoubleOrDefault(movieJson, "rating", 0.0);

            List<Genre> genres = new ArrayList<>();
            if (movieJson.has("genres") && !movieJson.get("genres").isJsonNull()) {
                JsonElement genresElement = movieJson.get("genres");
                if (genresElement.isJsonArray()) {
                    genresElement.getAsJsonArray().forEach(genreElement -> {
                        try {
                            genres.add(Genre.valueOf(genreElement.getAsString()));
                        } catch (Exception e) {
                            System.err.println("Unknown genre: " + genreElement.getAsString());
                        }
                    });
                }
            }

            List<String> directors = getStringListOrDefault(movieJson, "directors");
            List<String> writers = getStringListOrDefault(movieJson, "writers");
            List<String> mainCast = getStringListOrDefault(movieJson, "mainCast");

            return new Movie(id, title, description, genres, releaseYear, lengthInMinutes,
                    directors, writers, mainCast, rating);
        }

        private String getStringOrDefault(JsonObject json, String key, String defaultValue) {
            return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsString() : defaultValue;
        }

        private int getIntOrDefault(JsonObject json, String key, int defaultValue) {
            return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsInt() : defaultValue;
        }

        private double getDoubleOrDefault(JsonObject json, String key, double defaultValue) {
            return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsDouble() : defaultValue;
        }

        private List<String> getStringListOrDefault(JsonObject json, String key) {
            List<String> result = new ArrayList<>();
            if (json.has(key) && !json.get(key).isJsonNull() && json.get(key).isJsonArray()) {
                json.get(key).getAsJsonArray().forEach(element -> {
                    if (!element.isJsonNull()) {
                        result.add(element.getAsString());
                    }
                });
            }
            return result;
        }
    }

    public List<Movie> getAllMovies(){
        return getMovies(null, null, null, null);
    }

    public List<Movie> getMovies(String query, Genre genre, Object releaseYear, Object ratingFrom){
        OkHttpClient client = new OkHttpClient();
        String requestURL = buildRequestURL(query, genre, releaseYear, ratingFrom);

        System.out.println("Requesting URL: " + requestURL); // Debug: show URL
        System.out.println("releaseYear type: " + (releaseYear != null ? releaseYear.getClass().getName() : "null")); // Debug: check releaseYear type

        Request request = new Request.Builder()
                .url(requestURL)
                .addHeader("User-Agent", "http.agent")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("API request failed: " + response);
                return new ArrayList<>();
            }

            String responseBody = response.body().string();
            System.out.println("Response received with length: " + responseBody.length());

            if (responseBody.length() < 100) {
                System.out.println("Full response: " + responseBody); // Show full response if short
            } else {
                System.out.println("Response preview: " + responseBody.substring(0, 100) + "...");
            }

            Movie[] movies = gson.fromJson(responseBody, Movie[].class);
            System.out.println("Parsed " + movies.length + " movies");
            return Arrays.asList(movies);
        } catch (Exception e) {
            System.err.println("[ERROR] Request Execution failed: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String buildRequestURL(String query, Genre genre, Object releaseYear, Object ratingFrom) {
        StringBuilder urlBuilder = new StringBuilder(URL);
        boolean hasParam = false;

        if (query != null) {
            urlBuilder.append(hasParam ? "&" : "?").append("query=").append(query);
            hasParam = true;
        }

        if (genre != null) {
            urlBuilder.append(hasParam ? "&" : "?").append("genre=").append(genre);
            hasParam = true;
        }

        if (releaseYear != null) {
            // Debug the releaseYear value and type
            System.out.println("Adding releaseYear: " + releaseYear + " (" + releaseYear.getClass().getName() + ")");
            urlBuilder.append(hasParam ? "&" : "?").append("releaseYear=").append(releaseYear);
            hasParam = true;
        }

        if (ratingFrom != null) {
            urlBuilder.append(hasParam ? "&" : "?").append("ratingFrom=").append(ratingFrom);
        }

        return urlBuilder.toString();
    }
}