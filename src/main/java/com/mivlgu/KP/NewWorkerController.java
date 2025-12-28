package com.mivlgu.KP;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Контроллер диалогового окна добавления и редактирования работника.
 * <p>
 * Обеспечивает ввод и валидацию данных о работнике,
 * а также передачу введённых значений в объект {@link Worker}.
 * </p>
 *
 * Класс используется при загрузке FXML-файла {@code dialog.fxml}.
 *
 * @author Igor Builov
 * @version 1.0
 */
public class NewWorkerController {

    /** Диалоговое окно добавления/редактирования работника */
    @FXML
    private Stage dialogStage;

    /** Редактируемый объект работника */
    @FXML
    private Worker worker;

    /** Поле ввода фамилии */
    @FXML
    private TextField tfSurname;

    /** Поле ввода имени */
    @FXML
    private TextField tfName;

    /** Поле ввода отчества */
    @FXML
    private TextField tfLastname;

    /** Поле ввода возраста */
    @FXML
    private TextField tfAge;

    /** Поле ввода города */
    @FXML
    private TextField tfCity;

    /** Поле ввода должности */
    @FXML
    private TextField tfPosition;

    /** Флаг подтверждения ввода данных */
    private boolean okClicked = false;

    /**
     * Метод инициализации контроллера.
     * <p>
     * Вызывается автоматически после загрузки FXML.
     * Настраивает ограничения ввода и валидацию данных
     * для текстовых полей формы.
     * </p>
     */
    @FXML
    public void initialize() {
        setupTextOnly(tfName);
        setupTextOnly(tfSurname);
        setupTextOnly(tfLastname);
        setupCity(tfCity);
        setupGroup(tfPosition);
        setupAge(tfAge);
    }

    /**
     * Ограничивает ввод только буквенными символами.
     *
     * @param tf текстовое поле для ограничения ввода
     */
    private void setupTextOnly(TextField tf) {
        tf.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("[а-яА-ЯёЁa-zA-Z]*")) {
                tf.setText(oldValue);
            }
        });
    }

    /**
     * Ограничивает ввод символами, допустимыми для названия города.
     *
     * @param tf текстовое поле города
     */
    private void setupCity(TextField tf) {
        tf.textProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue.matches("[а-яА-Яa-zA-Z0-9-]*")) {
                tf.setText(oldValue);
            }
        });
    }

    /**
     * Ограничивает ввод символами, допустимыми для названия должности.
     *
     * @param tf текстовое поле должности
     */
    private void setupGroup(TextField tf) {
        tf.textProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue.matches("[а-яА-Яa-zA-Z0-9-]*")) {
                tf.setText(oldValue);
            }
        });
    }

    /**
     * Ограничивает ввод возраста только числовыми значениями
     * и проверяет допустимый диапазон.
     *
     * @param tf текстовое поле возраста
     */
    private void setupAge(TextField tf) {
        tf.textProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(oldValue);
                return;
            }
            if (newValue.isEmpty())
                return;

            int age = Integer.parseInt(newValue);
            if (age < 0 || age > 120) {
                showAlert("Возраст должен быть от 1 до 120");
                tf.setText(oldValue);
            }
        });
    }

    /**
     * Отображает предупреждающее сообщение об ошибке ввода данных.
     *
     * @param text текст сообщения
     */
    private void showAlert(String text) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setTitle("Ошибка ввода");
        a.setContentText(text);
        a.show();
    }

    /**
     * Устанавливает диалоговое окно контроллера.
     *
     * @param addStage диалоговое окно
     */
    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    /**
     * Устанавливает объект работника и заполняет поля формы
     * его текущими значениями.
     *
     * @param worker объект работника
     */
    public void setStudent(Worker worker) {
        this.worker = worker;
        tfSurname.setText(worker.getSurname());
        tfName.setText(worker.getName());
        tfLastname.setText(worker.getLastname());
        tfAge.setText(String.valueOf(worker.getAge()));
        tfCity.setText(worker.getCity());
        tfPosition.setText(worker.getPosition());
    }

    /**
     * Обработчик подтверждения ввода данных.
     * Сохраняет введённые значения в объект работника
     * и закрывает диалоговое окно.
     */
    @FXML
    private void onClickOk() {
        worker.setName(tfName.getText());
        worker.setSurname(tfSurname.getText());
        worker.setLastname(tfLastname.getText());
        worker.setAge(Integer.parseInt(tfAge.getText()));
        worker.setCity(tfCity.getText());
        worker.setPosition(tfPosition.getText());

        okClicked = true;
        dialogStage.close();
    }

    /**
     * Возвращает результат подтверждения ввода данных.
     *
     * @return {@code true}, если пользователь нажал кнопку «ОК»
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Обработчик отмены ввода данных.
     * Закрывает диалоговое окно без сохранения изменений.
     */
    @FXML
    private void onClickCancel() {
        dialogStage.close();
    }
}

