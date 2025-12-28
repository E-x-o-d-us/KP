package com.mivlgu.KP;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Модельный класс, представляющий сущность «Работник».
 * <p>
 * Используется для хранения и отображения данных о работниках
 * в пользовательском интерфейсе JavaFX.
 * Все поля реализованы с использованием JavaFX-свойств
 * для поддержки двустороннего связывания данных.
 * </p>
 *
 * Класс используется в слоях Controller и DAO.
 *
 * @author Igor Builov
 * @version 1.0
 */
public class Worker {

    /** Идентификатор работника */
    private IntegerProperty id;

    /** Имя работника */
    private StringProperty name;

    /** Фамилия работника */
    private StringProperty surname;

    /** Отчество работника */
    private StringProperty lastname;

    /** Возраст работника */
    private IntegerProperty age;

    /** Город проживания */
    private StringProperty city;

    /** Должность работника */
    private StringProperty position;

    /**
     * Конструктор с параметрами.
     * Создаёт объект работника с заданными значениями полей.
     *
     * @param id идентификатор работника
     * @param name имя
     * @param surname фамилия
     * @param lastname отчество
     * @param age возраст
     * @param city город проживания
     * @param position должность
     */
    public Worker(int id, String name, String surname,
                  String lastname, int age,
                  String city, String position) {

        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.lastname = new SimpleStringProperty(lastname);
        this.age = new SimpleIntegerProperty(age);
        this.city = new SimpleStringProperty(city);
        this.position = new SimpleStringProperty(position);
    }

    /**
     * Конструктор по умолчанию.
     * Создаёт объект работника с пустыми значениями полей.
     */
    public Worker() {
        this(0, "", "", "", 0, "", "");
    }

    /**
     * Возвращает имя работника.
     *
     * @return имя работника
     */
    public String getName() {
        return name.get();
    }

    /**
     * Возвращает JavaFX-свойство имени.
     *
     * @return свойство имени
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Устанавливает имя работника.
     *
     * @param name имя работника
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Возвращает фамилию работника.
     *
     * @return фамилия работника
     */
    public String getSurname() {
        return surname.get();
    }

    /**
     * Возвращает JavaFX-свойство фамилии.
     *
     * @return свойство фамилии
     */
    public StringProperty surnameProperty() {
        return surname;
    }

    /**
     * Устанавливает фамилию работника.
     *
     * @param surname фамилия работника
     */
    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    /**
     * Возвращает отчество работника.
     *
     * @return отчество работника
     */
    public String getLastname() {
        return lastname.get();
    }

    /**
     * Возвращает JavaFX-свойство отчества.
     *
     * @return свойство отчества
     */
    public StringProperty lastnameProperty() {
        return lastname;
    }

    /**
     * Устанавливает отчество работника.
     *
     * @param lastname отчество работника
     */
    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    /**
     * Возвращает возраст работника.
     *
     * @return возраст
     */
    public int getAge() {
        return age.get();
    }

    /**
     * Возвращает JavaFX-свойство возраста.
     *
     * @return свойство возраста
     */
    public IntegerProperty ageProperty() {
        return age;
    }

    /**
     * Устанавливает возраст работника.
     *
     * @param age возраст
     */
    public void setAge(int age) {
        this.age.set(age);
    }

    /**
     * Возвращает город проживания работника.
     *
     * @return город проживания
     */
    public String getCity() {
        return city.get();
    }

    /**
     * Возвращает JavaFX-свойство города.
     *
     * @return свойство города
     */
    public StringProperty cityProperty() {
        return city;
    }

    /**
     * Устанавливает город проживания работника.
     *
     * @param city город проживания
     */
    public void setCity(String city) {
        this.city.set(city);
    }

    /**
     * Возвращает должность работника.
     *
     * @return должность
     */
    public String getPosition() {
        return position.get();
    }

    /**
     * Возвращает JavaFX-свойство должности.
     *
     * @return свойство должности
     */
    public StringProperty positionProperty() {
        return position;
    }

    /**
     * Устанавливает должность работника.
     *
     * @param position должность
     */
    public void setPosition(String position) {
        this.position.set(position);
    }

    /**
     * Возвращает идентификатор работника.
     *
     * @return идентификатор
     */
    public int getId() {
        return id.get();
    }

    /**
     * Возвращает JavaFX-свойство идентификатора.
     *
     * @return свойство идентификатора
     */
    public IntegerProperty idProperty() {
        return id;
    }

    /**
     * Устанавливает идентификатор работника.
     *
     * @param id идентификатор
     */
    public void setId(int id) {
        this.id.set(id);
    }
}

