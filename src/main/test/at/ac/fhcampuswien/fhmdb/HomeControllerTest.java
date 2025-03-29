package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
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
    }

    @Test
    void at_initialization_allMovies_and_observableMovies_should_be_filled_and_equal() {
        homeController.initializeState();
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    @Test
    void if_not_yet_sorted_sort_is_applied_in_ascending_order() {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.NONE;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION)),
                new Movie(
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE)),
                new Movie(
                        "Puss in Boots",
                        "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                        Arrays.asList(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION)),
                new Movie(
                        "The Usual Suspects",
                        "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                        Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY)),
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY))

        );

        assertEquals(expected, homeController.observableMovies);

    }

    @Test
    void if_last_sort_ascending_next_sort_should_be_descending() {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.ASCENDING;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY)),
                new Movie(
                        "The Usual Suspects",
                        "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                        Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY)),
                new Movie(
                        "Puss in Boots",
                        "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                        Arrays.asList(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION)),
                new Movie(
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE)),
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION))
        );

        assertEquals(expected, homeController.observableMovies);
    }

    @Test
    void if_last_sort_descending_next_sort_should_be_ascending() {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.DESCENDING;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION)),
                new Movie(
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE)),
                new Movie(
                        "Puss in Boots",
                        "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                        Arrays.asList(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION)),
                new Movie(
                        "The Usual Suspects",
                        "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                        Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY)),
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY))

        );

        assertEquals(expected, homeController.observableMovies);

    }

    @Test
    void query_filter_matches_with_lower_and_uppercase_letters(){
        // given
        homeController.initializeState();
        String query = "IfE";

        // when
        List<Movie> actual = homeController.filterByQuery(homeController.observableMovies, query);

        // then
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE)),
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY))
        );

        assertEquals(expected, actual);
    }

    @Test
    void query_filter_with_null_movie_list_throws_exception(){
        // given
        homeController.initializeState();
        String query = "IfE";

        // when and then
        assertThrows(IllegalArgumentException.class, () -> homeController.filterByQuery(null, query));
    }

    @Test
    void query_filter_with_null_value_returns_unfiltered_list() {
        // given
        homeController.initializeState();
        String query = null;

        // when
        List<Movie> actual = homeController.filterByQuery(homeController.observableMovies, query);


        // then
        assertEquals(homeController.observableMovies, actual);
    }

    @Test
    void genre_filter_with_null_value_returns_unfiltered_list() {
        // given
        homeController.initializeState();
        Genre genre = null;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(homeController.observableMovies, actual);
    }

    @Test
    void genre_filter_returns_all_movies_containing_given_genre() {
        // given
        homeController.initializeState();
        Genre genre = Genre.DRAMA;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(4, actual.size());
    }

    @Test
    void no_filtering_ui_if_empty_query_or_no_genre_is_set() {
        // given
        homeController.initializeState();

        // when
        homeController.applyAllFilters("", null);

        // then
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    @Test
    void get_most_popular_actor_should_return_the_actor_that_appears_most() {
        // given
        List<Movie> movies = List.of(
                new Movie("Movie A", "", List.of("Actor A", "Actor B")),
                new Movie("Movie B", "", List.of("Actor A", "Actor C")),
                new Movie("Movie C", "", List.of("Actor A", "Actor D"))
        );

    }

    // ** new tests ** //


    @Test
    void get_most_popular_actor_should_return_the_actor_that_appears_most() {
        // given
        List<Movie> movies = List.of(
                new Movie("Movie A", "", List.of("Actor A", "Actor B")),
                new Movie("Movie B", "", List.of("Actor A", "Actor C")),
                new Movie("Movie C", "", List.of("Actor A", "Actor D"))
        );

        @Test
        void get_most_popular_actor_should_return_null_for_empty_movie_list() {
            // Testet Verhalten bei leerer Liste.
            // Es gibt keine Schauspieler – daher sollte null zurückgegeben werden.

            List<Movie> movies = List.of();

            String result = homeController.getMostPopularActor(movies);

            assertNull(result);
        }


        // when
        String result = homeController.getMostPopularActor(movies);

        // then
        assertEquals("Actor A", result);
    }

    @Test
    void getLongestMovieTitle_returns_correct_length() {
        // given
        List<Movie> movies = List.of(
                new Movie("Short", "", List.of()),
                new Movie("A very very long title indeed", "", List.of()),
                new Movie("Medium title", "", List.of())
        );

        // when
        int result = homeController.getLongestMovieTitle(movies);

        // then
        assertEquals(31, result);
    }

    @Test
    void countMoviesFrom_returns_correct_count() { // zählt korrekt wieviele filme von einem bestimmten regisseur sind
        // given
        List<Movie> movies = List.of(
                new Movie("Film A", "", List.of(), "John Doe", 2020, 7.5),
                new Movie("Film B", "", List.of(), "Jane Smith", 2021, 8.1),
                new Movie("Film C", "", List.of(), "John Doe", 2019, 6.8)
        );

        // when
        long result = homeController.countMoviesFrom(movies, "John Doe");

        // then
        assertEquals(2, result);
    }

    @Test
    void getMoviesBetweenYears_filters_movies_correctly() {
        // given
        List<Movie> movies = List.of(
                new Movie("Old Movie", "", List.of(), "Director", 1995, 6.0),
                new Movie("Modern Movie", "", List.of(), "Director", 2010, 7.5),
                new Movie("New Movie", "", List.of(), "Director", 2023, 8.2)
        );

        // when
        List<Movie> result = homeController.getMoviesBetweenYears(movies, 2000, 2023);

        // then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> m.getReleaseYear() >= 2000 && m.getReleaseYear() <= 2023));
    }

    @Test
    void getAllMovies_returns_movie_list_from_api() throws IOException {
        // given
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
                .setBody("[{\"title\":\"Test Movie\",\"mainCast\":[\"Actor X\"],\"releaseYear\":2020}]")
                .addHeader("Content-Type", "application/json"));
        server.start();
        MovieAPI movieAPI = new MovieAPI(server.url("/").toString());

        // when
        List<Movie> movies = movieAPI.getAllMovies();

        // then
        assertEquals(1, movies.size());
        assertEquals("Test Movie", movies.get(0).getTitle());

        server.shutdown();
    }

    @Test
    void movieApi_builds_correct_url_with_parameters() {
        // given
        MovieAPI api = new MovieAPI("https://example.com");

        // when
        String url = api.buildUrl("Matrix", "ACTION", 2000, 8.0);

        // then
        assertEquals("https://example.com/movies?query=Matrix&genre=ACTION&releaseYear=2000&ratingFrom=8.0", url);
    }

    @Test
    void movieApi_throws_exception_on_403() {
        // given
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(403));
        server.start();
        MovieAPI api = new MovieAPI(server.url("/").toString());

        // when & then
        assertThrows(IOException.class, api::getAllMovies);

        server.shutdown();
    }


}


}