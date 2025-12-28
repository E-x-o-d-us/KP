package com.mivlgu.KP;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Контроллер главного окна приложения.
 * <p>
 * Отвечает за управление пользовательским интерфейсом,
 * отображение данных о работниках в таблице,
 * обработку пользовательских действий и взаимодействие с DAO-слоем.
 * </p>
 *
 * Класс используется JavaFX при загрузке файла {@code lab-view.fxml}.
 *
 * @author Igor Builov
 * @version 1.0
 */
public class LabController implements Initializable {

    /** Объект доступа к данным */
    private Dao dao;

    /**
     * Конструктор контроллера.
     * Инициализирует объект DAO для работы с базой данных.
     */
    public LabController() {
        this.dao = new WorkerDao();
    }
    private static final Logger logger = LoggerFactory.getLogger(LabController.class);

    /** Список работников, отображаемых в таблице */
    private ObservableList<Worker> workers = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;
    /** Таблица работников */
    @FXML
    private TableView<Worker> workerTable;

    /** Колонка идентификатора */
    @FXML
    private TableColumn<Worker, Integer> idColumn;

    /** Колонка фамилии */
    @FXML
    private TableColumn<Worker, String> surnameColumn;

    /** Колонка имени */
    @FXML
    private TableColumn<Worker, String> nameColumn;

    /** Колонка отчества */
    @FXML
    private TableColumn<Worker, String> lastNameColumn;

    /** Колонка возраста */
    @FXML
    private TableColumn<Worker, Integer> ageColumn;

    /** Колонка города */
    @FXML
    private TableColumn<Worker, String> cityColumn;

    /** Колонка должности */
    @FXML
    private TableColumn<Worker, String> positionColumn;

    /** Метка для отображения служебных сообщений */
    @FXML
    private Label lblLog;

    /** Поле ввода поискового запроса */
    @FXML
    private TextField searchField;

    @FXML
    private ChoiceBox<String> languageBox;

    /**
     * Метод инициализации контроллера.
     * <p>
     * Вызывается автоматически после загрузки FXML-файла.
     * Загружает данные из базы данных, настраивает таблицу
     * и обработчики пользовательских действий.
     * </p>
     *
     * @param url путь к ресурсу
     * @param resourceBundle набор ресурсов приложения
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        readFromDB();
        workerTable.setItems(workers);

        idColumn.setCellValueFactory(item ->
                item.getValue().idProperty().asObject());
        surnameColumn.setCellValueFactory(item ->
                item.getValue().surnameProperty());
        nameColumn.setCellValueFactory(item ->
                item.getValue().nameProperty());
        lastNameColumn.setCellValueFactory(item ->
                item.getValue().lastnameProperty());
        ageColumn.setCellValueFactory(item ->
                item.getValue().ageProperty().asObject());
        cityColumn.setCellValueFactory(item ->
                item.getValue().cityProperty());
        positionColumn.setCellValueFactory(item ->
                item.getValue().positionProperty());

        workerTable.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Worker>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker>
                                                observableValue,
                                        Worker oldValue,
                                        Worker newValue) {
                        if (newValue != null) {
                            showWorker(newValue);
                        }
                    }
                });

        languageBox.getItems().addAll("Русский", "English");

        if (LabApplication.currentLocale.getLanguage().equals("ru")) {
            languageBox.setValue("Русский");
        } else {
            languageBox.setValue("English");
        }

        languageBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        changeLanguage(newVal);
                    }
                });
    }

    private void changeLanguage(String lang) {
        try {
            if ("Русский".equals(lang)) {
                LabApplication.currentLocale = new Locale("ru");
            } else {
                LabApplication.currentLocale = new Locale("en");
            }

            LabApplication.loadBundle();
            LabApplication.loadMainView();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Отображает информацию о выбранном работнике.
     *
     * @param worker выбранный работник
     */
    private void showWorker(Worker worker) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Работник ");
        stringBuilder.append(worker.getSurname());
        stringBuilder.append(" ");
        stringBuilder.append(worker.getName());
        stringBuilder.append(", должность ");
        stringBuilder.append(worker.getPosition());
        stringBuilder.append(".");
        lblLog.setText(stringBuilder.toString());
    }

    /**
     * Обработчик удаления выбранной строки из таблицы.
     * Удаляет запись из базы данных и интерфейса.
     */
    @FXML
    private void onClickDelete() {
        int selectedIndex = workerTable.getSelectionModel()
                .getSelectedIndex();
        dao.delete(workerTable.getItems().get(selectedIndex));
        workerTable.getItems().remove(selectedIndex);
        lblLog.setText("Строка удалена");
    }

    /**
     * Отображает диалоговое окно для добавления или редактирования работника.
     *
     * @param worker объект работника
     * @return {@code true}, если пользователь подтвердил ввод данных
     * @throws IOException при ошибке загрузки FXML
     */
    private boolean showDialog(Worker worker) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                LabApplication.class.getResource("dialog.fxml"),
                LabApplication.bundle
        );
        Parent page = loader.load();

        Stage addStage = new Stage();
        addStage.setTitle("Информация о работнике");
        addStage.initModality(Modality.WINDOW_MODAL);
        addStage.initOwner(LabApplication.getPrimaryStage());

        Scene scene = new Scene(page);
        addStage.setScene(scene);

        NewWorkerController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setStudent(worker);

        addStage.showAndWait();

        return controller.isOkClicked();
    }

    /**
     * Обработчик редактирования выбранного работника.
     *
     * @throws IOException при ошибке загрузки диалогового окна
     */
    @FXML
    private void onEdit() throws IOException {
        Worker selectedWorker = workerTable.getSelectionModel().getSelectedItem();
        if (selectedWorker != null) {
            if (showDialog(selectedWorker)) {
                dao.update(selectedWorker);
            }
        }
    }

    /**
     * Обработчик добавления нового работника.
     *
     * @throws IOException при ошибке загрузки диалогового окна
     */
    @FXML
    private void onAdd() throws IOException {
        Worker worker = new Worker();
        if (showDialog(worker)) {
            dao.save(worker);
            workers.add(worker);
        }
    }

    /**
     * Выполняет поиск работников по фамилии или названию группы.
     */
    @FXML
    private void onClickSearch() {
        String query = searchField.getText();

        if (query.isEmpty()) {
            workerTable.setItems(workers);
            return;
        }

        Collection<Worker> collection =
                dao.findBySurnameOrGroupName(query);

        ObservableList<Worker> filteredList =
                FXCollections.observableArrayList();
        filteredList.addAll(collection);

        workerTable.setItems(filteredList);
    }

    /**
     * Сбрасывает результаты поиска и обновляет таблицу.
     */
    @FXML
    private void onClickSbros() {
        readFromDB();
        workerTable.setItems(workers);
    }

    /**
     * Завершает работу приложения.
     *
     * @param actionEvent событие нажатия кнопки
     */
    @FXML
    private void onExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Загружает данные о работниках из базы данных.
     */
    private void readFromDB() {
        Collection<Worker> collection = dao.findAll();
        workers.clear();
        workers.addAll(collection);
    }

    /**
     * Отображает информационное сообщение пользователю.
     *
     * @param message текст сообщения
     */
    public void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Сообщение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

