package com.example.kinopoisk.Controllers;

import com.example.kinopoisk.DAO.FilmDAO;
import com.example.kinopoisk.Entities.Film;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;


public class MainController {
    @FXML
    private TextField searchTitleField;
    @FXML
    private TextField searchDirectorField;
    @FXML
    private TextField searchYearField;
    @FXML
    private ListView<Film> filmList;

    private FilmDAO filmDAO = new FilmDAO();

    // Инициализация контроллера
    @FXML
    public void initialize() {
        // Настраиваем кастомный элемент списка
        filmList.setCellFactory(new Callback<ListView<Film>, ListCell<Film>>() {
            @Override
            public ListCell<Film> call(ListView<Film> param) {
                return new ListCell<Film>() {
                    @Override
                    protected void updateItem(Film film, boolean empty) {
                        super.updateItem(film, empty);
                        if (empty || film == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            // Форматируем вывод данных о фильме с переносом строки для синопсиса
                            setText(String.format(
                                    "Название: %s\nРежиссер: %s\nГод: %d\nРейтинг: %.1f\nСинопсис: %s",
                                    film.getTitle(), film.getDirector(), film.getYear(), film.getRating(),
                                    film.getSynopsis().replaceAll("(.{50})", "$1\n") // Перенос строки каждые 50 символов
                            ));
                        }
                    }
                };
            }
        });

        refreshFilmList();
    }

    // Обработка поиска
    @FXML
    public void handleSearch() {
        String title = searchTitleField.getText();
        String director = searchDirectorField.getText();
        Integer year = null;

        try {
            year = Integer.parseInt(searchYearField.getText());
        } catch (NumberFormatException e) {
            // Если год не введен или введен некорректно, игнорируем
        }

        List<Film> films = filmDAO.searchFilms(title, director, year);
        filmList.getItems().setAll(films);
    }

    // Обработка сброса поиска
    @FXML
    public void handleResetSearch() {
        searchTitleField.clear();
        searchDirectorField.clear();
        searchYearField.clear();
        refreshFilmList();
    }

    // Обработка добавления фильма
    @FXML
    public void handleAddFilm() {
        // Открываем диалоговое окно для ввода данных
        Film newFilm = showAddFilmDialog();
        if (newFilm != null) {
            filmDAO.addFilm(newFilm);
            refreshFilmList();
        }
    }

    // Обработка удаления фильма
    @FXML
    public void handleDeleteFilm() {
        Film selectedFilm = filmList.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            filmDAO.deleteFilm(selectedFilm.getId());
            refreshFilmList();
        } else {
            showAlert("Ошибка", "Фильм не выбран", "Пожалуйста, выберите фильм для удаления.");
        }
    }

    // Обновление списка фильмов
    private void refreshFilmList() {
        filmList.getItems().setAll(filmDAO.getAllFilms());
    }

    // Диалоговое окно для добавления фильма
    private Film showAddFilmDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавить фильм");
        dialog.setHeaderText("Введите данные фильма");

        // Создаем текстовые поля для ввода данных
        TextField titleField = new TextField();
        TextField directorField = new TextField();
        TextField yearField = new TextField();
        TextField ratingField = new TextField();
        TextField synopsisField = new TextField();

        // Добавляем поля в диалоговое окно
        dialog.getDialogPane().setContent(new VBox(10,
                new Label("Название:"), titleField,
                new Label("Режиссер:"), directorField,
                new Label("Год:"), yearField,
                new Label("Рейтинг:"), ratingField,
                new Label("Синопсис:"), synopsisField));

        // Показываем диалоговое окно и ждем ввода данных
        dialog.showAndWait();

        try {
            // Получаем данные из текстовых полей
            String title = titleField.getText();
            String director = directorField.getText();
            int year = Integer.parseInt(yearField.getText());
            double rating = Double.parseDouble(ratingField.getText());
            String synopsis = synopsisField.getText();

            // Создаем новый фильм
            return new Film(title, director, year, rating, synopsis);
        } catch (Exception e) {
            showAlert("Ошибка", "Некорректные данные", "Пожалуйста, введите корректные данные.");
            return null;
        }
    }

    // Показать сообщение об ошибке
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}