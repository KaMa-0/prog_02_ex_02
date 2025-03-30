package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genre> genres;
    // Zusätzliche Attribute exercise 2
    private final String id;
    private final int releaseYear;
    private final int lengthInMinutes;
    private final List<String> directors;
    private final List<String> writers;
    private final List<String> mainCast;
    private final double rating;

    public Movie(String id, String title, String description, List<Genre> genres, int releaseYear, int lengthInMinutes, List<String> directors, List<String> writers, List<String> mainCast, double rating) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.id = id;
        this.releaseYear = releaseYear;
        this.lengthInMinutes = lengthInMinutes;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
        this.rating = rating;

    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Movie other)) {
            return false;
        }
        return this.title.equals(other.title) && this.description.equals(other.description) && this.genres.equals(other.genres);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getId() {
        return id;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getWriters() {
        return writers;
    }

    public List<String> getMainCast() {
        return mainCast;
    }

    public double getRating() {
        return rating;
    }

    // Dummy movies without API for implementation testing
    public static List<Movie> initializeMovies(){
        return Arrays.asList(
            new Movie("5",  // "Comedy Chaos" (alphabetically first)
                "Comedy Chaos (DUMMY MOVIE)",
                "Mismatched roommates inherit a failing pet hotel.",
                Arrays.asList(Genre.COMEDY),
                2020,
                92,
                Arrays.asList("Dir. C"),
                Arrays.asList("Writer C", "Writer Shared"),  // Shared with movies 2 & 4
                Arrays.asList("Actor C", "Actor Shared"),  // Shared with movies 2,3,4
                3.5),
            new Movie("1",  // "Dummy-A" (second)
                "Dummy-A (DUMMY MOVIE)",
                "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION),
                2000,
                120,
                Arrays.asList("Dir. A", "Dir. B"),
                Arrays.asList("Writer A", "Writer B", "Writer C"),
                Arrays.asList("Actor A", "Actor C"),
                4.0),

            new Movie("2",  // "Galactic Odyssey" (third)
                "Galactic Odyssey (DUMMY MOVIE)",
                "A team of astronauts embarks on a perilous journey across the galaxy.",
                Arrays.asList(Genre.SCIENCE_FICTION, Genre.ADVENTURE),
                2022,
                135,
                Arrays.asList("Dir. X", "Dir. Shared"),  // Shared with movie 3
                Arrays.asList("Writer Shared", "Writer X"),  // Shared with movies 4 & 5
                Arrays.asList("Actor Shared", "Actor Y", "Actor Z"),  // Shared with movies 3,4,5
                4.5),

            new Movie("3",  // "Midnight Detective" (fourth)
                "Midnight Detective (DUMMY MOVIE)",
                "A private investigator takes on a mysterious case in 1940s LA.",
                Arrays.asList(Genre.CRIME, Genre.THRILLER),
                2019,
                98,
                Arrays.asList("Dir. Shared", "Dir. M"),  // Shared with movie 2
                Arrays.asList("Writer M", "Writer N"),
                Arrays.asList("Actor M", "Actor Shared"),  // Shared with movies 2,4,5
                3.8),

            new Movie("4",  // "The Last Kingdom" (last, due to "The")
                "The Last Kingdom (DUMMY MOVIE)",
                "A warrior rises to power in medieval England.",
                Arrays.asList(Genre.ADVENTURE, Genre.ACTION),
                2021,
                142,
                Arrays.asList("Dir. H", "Dir. I"),
                Arrays.asList("Writer Shared", "Writer H"),  // Shared with movies 2 & 5
                Arrays.asList("Actor H", "Actor I", "Actor Shared"),  // Shared with movies 2,3,5
                4.2)
        );
    }
}
