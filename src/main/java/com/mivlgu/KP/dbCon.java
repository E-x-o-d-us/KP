package com.mivlgu.KP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс для управления соединением с базой данных.
 * <p>
 * Обеспечивает создание, получение и корректное закрытие
 * соединения с базой данных SQLite, а также инициализацию
 * структуры базы данных при запуске приложения.
 * </p>
 *
 * Реализует шаблон Singleton для работы с соединением.
 *
 * @author Igor Builov
 * @version 1.0
 */
public class dbCon {

    /** Соединение с базой данных */
    private static Connection connection;
    private static final Logger logger =
            LoggerFactory.getLogger(dbCon.class);

    /**
     * Возвращает соединение с базой данных.
     * <p>
     * Если соединение отсутствует или было закрыто,
     * создаётся новое соединение с файлом базы данных.
     * </p>
     *
     * @return объект {@link Connection}
     * @throws SQLException при ошибке подключения к базе данных
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:sqlite:worker.db";
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }

    /**
     * Инициализирует структуру базы данных.
     * <p>
     * Выполняет SQL-запрос создания таблицы работников,
     * если она ещё не существует.
     * SQL-запрос загружается из конфигурационного файла.
     * </p>
     */
    public static void initDatabase() {
        logger.info("Инициализация базы данных");
        String sql = LabApplication.getProperty()
                .getProperty("sql.create_table");
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            logger.info("Таблица успешно создана или уже существует");
        } catch (SQLException e) {
            logger.error("Ошибка при создании таблицы", e);
        }
    }

    /**
     * Корректно завершает работу с базой данных.
     * Закрывает активное соединение при завершении приложения.
     *
     * @throws SQLException при ошибке закрытия соединения
     */
    public static void stopConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}

