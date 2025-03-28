package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    //New ComboBoxes
    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingComboBox;

    public List<Movie> allMovies;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    protected SortedState sortedState;


    private final MovieAPI movieAPI = new MovieAPI(); //Adding MovieAPI object to call methods in MovieAPI class

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();
        initializeLayout();
    }

    public void initializeState() {

        allMovies = movieAPI.getAllMovies(); // We call all the films from API
        observableMovies.clear();
        observableMovies.addAll(allMovies);
        sortedState = SortedState.NONE;
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());

        // Genre ComboBox
        Object[] genres = Genre.values();
        genreComboBox.getItems().add("No filter");
        genreComboBox.getItems().addAll(genres);
        genreComboBox.setPromptText("Filter by Genre");

        //Release Year ComboBox
        releaseYearComboBox.getItems().add("No Filter"); //"No Filter" option
        int earliestYear = java.time.Year.now().getValue();
        // Someone need to check this method pls
        for (Movie movie : allMovies) { //With this method we can find the lowest year in our movie list. So we can start with the lowest value
            int year = movie.getReleaseYear();
            if (year > 0 && year < earliestYear) {
                earliestYear = year;
            }
        }
        // Adding years from lowest to today

        int currentYear = java.time.Year.now().getValue();
        for (int year = earliestYear; year <= currentYear; year++) {
            releaseYearComboBox.getItems().add(year);
        }
        releaseYearComboBox.setPromptText("Release Year");

        // Rating ComboBox
        ratingComboBox.getItems().add("No Filter"); // "No filter" option
        for (double rating = 1.0; rating <= 9.0; rating++) {
            ratingComboBox.getItems().add(rating);
        }
        ratingComboBox.setPromptText("Min Rating");
    }

    public void sortMovies(){
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }

    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query){
        if(query == null || query.isEmpty()) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase())
                )
                .toList();
    }

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre){
        if(genre == null) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie -> movie.getGenres().contains(genre))
                .toList();
    }

    public void applyAllFilters(String searchQuery, Object genre) {
        List<Movie> filteredMovies = allMovies;

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim();
        Genre genre = null;
        Object releaseYear = null;
        Object rating = null;

        // genre selection
        Object selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
        if (selectedGenre != null && !selectedGenre.toString().equals("No filter")) {
            genre = Genre.valueOf(selectedGenre.toString());
        }

        // release year selection
        Object selectedReleaseYear = releaseYearComboBox.getSelectionModel().getSelectedItem();
        if (selectedReleaseYear != null && !selectedReleaseYear.toString().equals("No filter")) {
            releaseYear = releaseYearComboBox.getSelectionModel().getSelectedItem(); // Check this when the movie list from API comes
        }

        // rating selection
        Object selectedRating = ratingComboBox.getSelectionModel().getSelectedItem();
        if (selectedRating != null && !selectedRating.toString().equals("No filter")) {
            rating = ratingComboBox.getSelectionModel().getSelectedItem(); // Check this when the movie list from API comes
        }

        // Calling movies with filter from API
        List<Movie> filteredMovies;
        if (searchQuery.isEmpty()) {
            filteredMovies = movieAPI.getMovies(null, genre, releaseYear, rating);
        } else {
            filteredMovies = movieAPI.getMovies(searchQuery, genre, releaseYear, rating);
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
        sortMovies(sortedState);
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }
}