package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private static HomeController homeController;
    @BeforeAll
    static void init() {
        homeController = new HomeController();
        homeController.initializeState();
    }

    @BeforeEach
    void prepareDummyList() {
        List<Movie> dummyMovies = Arrays.asList(
                new Movie("5",  // "Comedy Chaos" (alphabetically first)
                        "Comedy Chaos",
                        "Mismatched roommates inherit a failing pet hotel.",
                        Arrays.asList(Genre.COMEDY),
                        2020,
                        92,
                        Arrays.asList("Dir. C"),
                        Arrays.asList("Writer C", "Writer Shared"),  // Shared with movies 2 & 4
                        Arrays.asList("Actor C", "Actor Shared"),  // Shared with movies 2,3,4
                        3.5),
                new Movie("1",  // "Dummy-A" (second)
                        "Dummy-A",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION),
                        2000,
                        120,
                        Arrays.asList("Dir. A", "Dir. B"),
                        Arrays.asList("Writer A", "Writer B", "Writer C"),
                        Arrays.asList("Actor A", "Actor C"),
                        4.0),

                new Movie("2",  // "Galactic Odyssey" (third)
                        "Galactic Odyssey",
                        "A team of astronauts embarks on a perilous journey across the galaxy.",
                        Arrays.asList(Genre.SCIENCE_FICTION, Genre.ADVENTURE),
                        2022,
                        135,
                        Arrays.asList("Dir. X", "Dir. Shared"),  // Shared with movie 3
                        Arrays.asList("Writer Shared", "Writer X"),  // Shared with movies 4 & 5
                        Arrays.asList("Actor Shared", "Actor Y", "Actor Z"),  // Shared with movies 3,4,5
                        4.5),

                new Movie("3",  // "Midnight Detective" (fourth)
                        "Midnight Detective",
                        "A private investigator takes on a mysterious case in 1940s LA.",
                        Arrays.asList(Genre.CRIME, Genre.THRILLER),
                        2019,
                        98,
                        Arrays.asList("Dir. Shared", "Dir. M"),  // Shared with movie 2
                        Arrays.asList("Writer M", "Writer N"),
                        Arrays.asList("Actor M", "Actor Shared"),  // Shared with movies 2,4,5
                        3.8),

                new Movie("4",  // "The Last Kingdom" (last, due to "The")
                        "The Last Kingdom",
                        "A warrior rises to power in medieval England.",
                        Arrays.asList(Genre.ADVENTURE, Genre.ACTION),
                        2021,
                        142,
                        Arrays.asList("Dir. H", "Dir. I"),
                        Arrays.asList("Writer Shared", "Writer H"),  // Shared with movies 2 & 5
                        Arrays.asList("Actor H", "Actor I", "Actor Shared"),  // Shared with movies 2,3,5
                        4.2)
        );
        homeController.setMovies(dummyMovies);
    }

    @Test
    void at_initialization_allMovies_and_observableMovies_should_be_filled_and_equal() {
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    @Test
    void if_not_yet_sorted_sort_is_applied_in_ascending_order() {
        // given
        homeController.sortedState = SortedState.NONE;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Arrays.asList(
                new Movie("5",  // "Comedy Chaos" (alphabetically first)
                        "Comedy Chaos",
                        "Mismatched roommates inherit a failing pet hotel.",
                        Arrays.asList(Genre.COMEDY),
                        2020,
                        92,
                        Arrays.asList("Dir. C"),
                        Arrays.asList("Writer C", "Writer Shared"),  // Shared with movies 2 & 4
                        Arrays.asList("Actor C", "Actor Shared"),  // Shared with movies 2,3,4
                        3.5),
                new Movie("1",  // "Dummy-A" (second)
                        "Dummy-A",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION),
                        2000,
                        120,
                        Arrays.asList("Dir. A", "Dir. B"),
                        Arrays.asList("Writer A", "Writer B", "Writer C"),
                        Arrays.asList("Actor A", "Actor C"),
                        4.0),
                new Movie("2",  // "Galactic Odyssey" (third)
                        "Galactic Odyssey",
                        "A team of astronauts embarks on a perilous journey across the galaxy.",
                        Arrays.asList(Genre.SCIENCE_FICTION, Genre.ADVENTURE),
                        2022,
                        135,
                        Arrays.asList("Dir. X", "Dir. Shared"),  // Shared with movie 3
                        Arrays.asList("Writer Shared", "Writer X"),  // Shared with movies 4 & 5
                        Arrays.asList("Actor Shared", "Actor Y", "Actor Z"),  // Shared with movies 3,4,5
                        4.5),
                new Movie("3",  // "Midnight Detective" (fourth)
                        "Midnight Detective",
                        "A private investigator takes on a mysterious case in 1940s LA.",
                        Arrays.asList(Genre.CRIME, Genre.THRILLER),
                        2019,
                        98,
                        Arrays.asList("Dir. Shared", "Dir. M"),  // Shared with movie 2
                        Arrays.asList("Writer M", "Writer N"),
                        Arrays.asList("Actor M", "Actor Shared"),  // Shared with movies 2,4,5
                        3.8),
                new Movie("4",  // "The Last Kingdom" (last, due to "The")
                        "The Last Kingdom",
                        "A warrior rises to power in medieval England.",
                        Arrays.asList(Genre.ADVENTURE, Genre.ACTION),
                        2021,
                        142,
                        Arrays.asList("Dir. H", "Dir. I"),
                        Arrays.asList("Writer Shared", "Writer H"),  // Shared with movies 2 & 5
                        Arrays.asList("Actor H", "Actor I", "Actor Shared"),  // Shared with movies 2,3,5
                        4.2)
        );

        assertEquals(expected, homeController.observableMovies);

    }

    @Test
    void if_last_sort_ascending_next_sort_should_be_descending() {
        // given
        homeController.sortedState = SortedState.ASCENDING;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Arrays.asList(
                new Movie("4",  // "The Last Kingdom" (first in descending order)
                        "The Last Kingdom",
                        "A warrior rises to power in medieval England.",
                        Arrays.asList(Genre.ADVENTURE, Genre.ACTION),
                        2021,
                        142,
                        Arrays.asList("Dir. H", "Dir. I"),
                        Arrays.asList("Writer Shared", "Writer H"),  // Shared with movies 2 & 5
                        Arrays.asList("Actor H", "Actor I", "Actor Shared"),  // Shared with movies 2,3,5
                        4.2),
                new Movie("3",  // "Midnight Detective" (second)
                        "Midnight Detective",
                        "A private investigator takes on a mysterious case in 1940s LA.",
                        Arrays.asList(Genre.CRIME, Genre.THRILLER),
                        2019,
                        98,
                        Arrays.asList("Dir. Shared", "Dir. M"),  // Shared with movie 2
                        Arrays.asList("Writer M", "Writer N"),
                        Arrays.asList("Actor M", "Actor Shared"),  // Shared with movies 2,4,5
                        3.8),
                new Movie("2",  // "Galactic Odyssey" (third)
                        "Galactic Odyssey",
                        "A team of astronauts embarks on a perilous journey across the galaxy.",
                        Arrays.asList(Genre.SCIENCE_FICTION, Genre.ADVENTURE),
                        2022,
                        135,
                        Arrays.asList("Dir. X", "Dir. Shared"),  // Shared with movie 3
                        Arrays.asList("Writer Shared", "Writer X"),  // Shared with movies 4 & 5
                        Arrays.asList("Actor Shared", "Actor Y", "Actor Z"),  // Shared with movies 3,4,5
                        4.5),
                new Movie("1",  // "Dummy-A" (fourth)
                        "Dummy-A",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION),
                        2000,
                        120,
                        Arrays.asList("Dir. A", "Dir. B"),
                        Arrays.asList("Writer A", "Writer B", "Writer C"),
                        Arrays.asList("Actor A", "Actor C"),
                        4.0),
                new Movie("5",  // "Comedy Chaos" (last in descending order)
                        "Comedy Chaos",
                        "Mismatched roommates inherit a failing pet hotel.",
                        Arrays.asList(Genre.COMEDY),
                        2020,
                        92,
                        Arrays.asList("Dir. C"),
                        Arrays.asList("Writer C", "Writer Shared"),  // Shared with movies 2 & 4
                        Arrays.asList("Actor C", "Actor Shared"),  // Shared with movies 2,3,4
                        3.5)
        );

        assertEquals(expected.get(0).getTitle(), homeController.observableMovies.get(0).getTitle());
        assertEquals(expected.get(1).getTitle(), homeController.observableMovies.get(1).getTitle());
        assertEquals(expected.get(2).getTitle(), homeController.observableMovies.get(2).getTitle());
        assertEquals(expected.get(3).getTitle(), homeController.observableMovies.get(3).getTitle());
        assertEquals(expected.get(4).getTitle(), homeController.observableMovies.get(4).getTitle());
    }

    @Test
    void if_last_sort_descending_next_sort_should_be_ascending() {
        // given
        homeController.sortedState = SortedState.DESCENDING;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Arrays.asList(
                new Movie("5",  // "Comedy Chaos" (alphabetically first)
                        "Comedy Chaos",
                        "Mismatched roommates inherit a failing pet hotel.",
                        Arrays.asList(Genre.COMEDY),
                        2020,
                        92,
                        Arrays.asList("Dir. C"),
                        Arrays.asList("Writer C", "Writer Shared"),  // Shared with movies 2 & 4
                        Arrays.asList("Actor C", "Actor Shared"),  // Shared with movies 2,3,4
                        3.5),

                new Movie("1",  // "Dummy-A" (second)
                        "Dummy-A",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION),
                        2000,
                        120,
                        Arrays.asList("Dir. A", "Dir. B"),
                        Arrays.asList("Writer A", "Writer B", "Writer C"),
                        Arrays.asList("Actor A", "Actor C"),
                        4.0),

                new Movie("2",  // "Galactic Odyssey" (third)
                        "Galactic Odyssey",
                        "A team of astronauts embarks on a perilous journey across the galaxy.",
                        Arrays.asList(Genre.SCIENCE_FICTION, Genre.ADVENTURE),
                        2022,
                        135,
                        Arrays.asList("Dir. X", "Dir. Shared"),  // Shared with movie 3
                        Arrays.asList("Writer Shared", "Writer X"),  // Shared with movies 4 & 5
                        Arrays.asList("Actor Shared", "Actor Y", "Actor Z"),  // Shared with movies 3,4,5
                        4.5),

                new Movie("3",  // "Midnight Detective" (fourth)
                        "Midnight Detective",
                        "A private investigator takes on a mysterious case in 1940s LA.",
                        Arrays.asList(Genre.CRIME, Genre.THRILLER),
                        2019,
                        98,
                        Arrays.asList("Dir. Shared", "Dir. M"),  // Shared with movie 2
                        Arrays.asList("Writer M", "Writer N"),
                        Arrays.asList("Actor M", "Actor Shared"),  // Shared with movies 2,4,5
                        3.8),

                new Movie("4",  // "The Last Kingdom" (last, ignores "The" in sorting)
                        "The Last Kingdom",
                        "A warrior rises to power in medieval England.",
                        Arrays.asList(Genre.ADVENTURE, Genre.ACTION),
                        2021,
                        142,
                        Arrays.asList("Dir. H", "Dir. I"),
                        Arrays.asList("Writer Shared", "Writer H"),  // Shared with movies 2 & 5
                        Arrays.asList("Actor H", "Actor I", "Actor Shared"),  // Shared with movies 2,3,5
                        4.2)
        );

        assertEquals(expected.get(0).getTitle(), homeController.observableMovies.get(0).getTitle());
        assertEquals(expected.get(1).getTitle(), homeController.observableMovies.get(1).getTitle());
        assertEquals(expected.get(2).getTitle(), homeController.observableMovies.get(2).getTitle());
        assertEquals(expected.get(3).getTitle(), homeController.observableMovies.get(3).getTitle());
        assertEquals(expected.get(4).getTitle(), homeController.observableMovies.get(4).getTitle());
    }

    @Test
    void query_filter_matches_with_lower_and_uppercase_letters(){
        // given
        String query = "KiNgDoM";

        // when
        List<Movie> actual = homeController.filterByQuery(homeController.observableMovies, query);

        // then
        List<Movie> expected = Arrays.asList(
            new Movie("4",
                "The Last Kingdom",
                "A warrior rises to power in medieval England.",
                Arrays.asList(Genre.ADVENTURE, Genre.ACTION),
                2021,
                142,
                Arrays.asList("Dir. H", "Dir. I"),
                Arrays.asList("Writer Shared", "Writer H"),
                Arrays.asList("Actor H", "Actor I", "Actor Shared"),
                4.2)
        );

        assertEquals(expected, actual);
    }

    @Test
    void query_filter_with_null_movie_list_throws_exception(){
        // given
        String query = "IfE";

        // when and then
        assertThrows(IllegalArgumentException.class, () -> homeController.filterByQuery(null, query));
    }

    @Test
    void query_filter_with_null_value_returns_unfiltered_list() {
        // given
        String query = null;

        // when
        List<Movie> actual = homeController.filterByQuery(homeController.observableMovies, query);


        // then
        assertEquals(homeController.observableMovies, actual);
    }

    @Test
    void genre_filter_with_null_value_returns_unfiltered_list() {
        // given
        Genre genre = null;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(homeController.observableMovies, actual);
    }

    @Test
    void genre_filter_returns_all_movies_containing_given_genre() {
        // given
        Genre genre = Genre.DRAMA;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(1, actual.size());
    }

    @Test
    void no_filtering_ui_if_empty_query_or_no_genre_is_set() {
        // given

        // when
        homeController.applyAllFilters("", null);

        // then
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    // ** new tests ** //

    @Test
    void get_most_popular_actor_should_return_the_actor_that_appears_most() {
        // when
        String result = homeController.getMostPopularActor();

        // then
        assertEquals("Actor Shared", result);
    }

    @Test
    void get_most_popular_actor_should_return_null_for_empty_movie_list() {
        // given
        List<Movie> emptyList = Arrays.asList();
        homeController.setMovies(emptyList);

        // when
        String result = homeController.getMostPopularActor();

        // then
        assertNull(result);
    }
    //new-----
    @Test
    void getMostPopularActor_handles_tie_correctly() {
        // given
        List<Movie> tieMovies = Arrays.asList(
                new Movie("1", "Movie 1", "", Arrays.asList(Genre.ACTION), 2020, 90,
                        Arrays.asList("Dir A"), Arrays.asList(), Arrays.asList("Actor X", "Actor Y"), 5.0),
                new Movie("2", "Movie 2", "", Arrays.asList(Genre.ACTION), 2020, 90,
                        Arrays.asList("Dir B"), Arrays.asList(), Arrays.asList("Actor X", "Actor Z"), 5.0),
                new Movie("3", "Movie 3", "", Arrays.asList(Genre.ACTION), 2020, 90,
                        Arrays.asList("Dir C"), Arrays.asList(), Arrays.asList("Actor Y", "Actor Z"), 5.0)
        );
        homeController.setMovies(tieMovies);

        // when
        String result = homeController.getMostPopularActor();

        // then
        assertTrue(result.equals("Actor X") || result.equals("Actor Y") || result.equals("Actor Z"));
    }

    @Test
    void getLongestMovieTitle_returns_correct_length() {
        // when
        int result = homeController.getLongestMovieTitle();

        // then
        assertEquals(18, result);
    }
    //new-----
    @Test
    void getLongestMovieTitle_with_same_length_titles() {
        // given
        List<Movie> moviesWithSameLengthTitles = Arrays.asList(
                new Movie("1", "12345678901234", "", Arrays.asList(Genre.ACTION), 2020, 90,
                        Arrays.asList(), Arrays.asList(), Arrays.asList(), 5.0),
                new Movie("2", "ABCDEFGHIJKLMN", "", Arrays.asList(Genre.ACTION), 2020, 90,
                        Arrays.asList(), Arrays.asList(), Arrays.asList(), 5.0)
        );
        homeController.setMovies(moviesWithSameLengthTitles);

        // when
        int result = homeController.getLongestMovieTitle();

        // then
        assertEquals(14, result);
    }

    @Test
    void countMoviesFrom_returns_correct_count() {
        // when
        long result = homeController.countMoviesFrom("Dir. Shared");

        // then
        assertEquals(2, result);
    }
    //new----
    @Test
    void countMoviesFrom_with_null_director_handled_properly() {
        // when
        long result = homeController.countMoviesFrom(null);
        // then
        assertEquals(0, result); // Or verify appropriate exception is thrown
    }
    //new-----
    @Test
    void countMoviesFrom_with_nonexistent_director_returns_zero() {
        // when
        long result = homeController.countMoviesFrom("Nonexistent Director");
        // then
        assertEquals(0, result);
    }


    @Test
    void getMoviesBetweenYears_filters_movies_correctly() {
        // when
        List<Movie> result = homeController.getMoviesBetweenYears(2020, 2022);

        // then
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(m -> m.getReleaseYear() >= 2000 && m.getReleaseYear() <= 2023));
    }
    //new-----
    @Test
    void getMoviesBetweenYears_with_exact_year_boundaries() {
        // when
        List<Movie> result = homeController.getMoviesBetweenYears(2022, 2022);
        // then
        assertEquals(1, result.size());
        assertEquals("Galactic Odyssey", result.get(0).getTitle());
    }
    //new-----
    @Test
    void getMoviesBetweenYears_with_invalid_range_returns_empty_list() {
        // when
        List<Movie> result = homeController.getMoviesBetweenYears(2025, 2023);
        // then
        assertEquals(0, result.size());
    }
}
