package com.mivlgu.KP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Реализация DAO для работы с сущностью {@link Worker}.
 * <p>
 * Класс обеспечивает доступ к данным о работниках,
 * хранящимся в базе данных SQLite, и реализует
 * основные операции CRUD (создание, чтение, обновление, удаление).
 * </p>
 *
 * SQL-запросы загружаются из конфигурационного файла
 * {@code statements.properties}.
 *
 * @author Igor Builov
 * @version 1.0
 */
public class WorkerDao implements Dao<Worker, Integer> {

    private static final Logger logger =
            LoggerFactory.getLogger(WorkerDao.class);

    /**
     * Выполняет поиск работника по его идентификатору.
     *
     * @param id идентификатор работника
     * @return объект {@link Worker} или {@code null},
     * если работник не найден
     */
    @Override
    public Worker findById(Integer id) {
        String sql = LabApplication.getProperty()
                .getProperty("sql.find_by_id");

        try (PreparedStatement stmt =
                     dbCon.getConnection().prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Worker s = new Worker();
                    s.setId(rs.getInt("id"));
                    s.setName(rs.getString("name"));
                    s.setSurname(rs.getString("surname"));
                    s.setLastname(rs.getString("lastname"));
                    s.setAge(rs.getInt("age"));
                    s.setCity(rs.getString("city"));
                    s.setPosition(rs.getString("position"));
                    return s;
                }
            }

        } catch (SQLException e) {
            logger.error("Ошибка при поиске работника", e);
        }
        return null;
    }

    /**
     * Возвращает список всех работников из базы данных.
     *
     * @return коллекция работников
     */
    @Override
    public Collection<Worker> findAll() {
        List<Worker> list = null;
        ResultSet rs = null;

        String sql = LabApplication.getProperty()
                .getProperty("sql.find_all");

        try (PreparedStatement statement =
                     dbCon.getConnection().prepareStatement(sql)) {

            rs = statement.executeQuery();
            list = mapper(rs);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * Сохраняет нового работника в базе данных.
     * <p>
     * После успешного выполнения операции идентификатор
     * работника автоматически устанавливается.
     * </p>
     *
     * @param worker объект работника
     * @return сохранённый объект работника
     */
    @Override
    public Worker save(Worker worker) {
        logger.debug("Сохранение работника: {}", worker.getSurname());
        String sql = LabApplication.getProperty()
                .getProperty("sql.save");
        try (PreparedStatement statement =
                     dbCon.getConnection().prepareStatement(sql)) {

            statement.setString(1, worker.getName());
            statement.setString(2, worker.getSurname());
            statement.setString(3, worker.getLastname());
            statement.setInt(4, worker.getAge());
            statement.setString(5, worker.getCity());
            statement.setString(6, worker.getPosition());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                worker.setId(keys.getInt(1));
            }
            logger.info("Работник сохранён, id={}", worker.getId());
        } catch (SQLException e) {
            logger.error("Ошибка сохранения работника", e);
        }
        return worker;
    }

    /**
     * Обновляет данные работника в базе данных.
     *
     * @param worker объект работника с обновлёнными данными
     * @return обновлённый объект работника
     */
    @Override
    public Worker update(Worker worker) {
        logger.debug("Обновление данных работника: {}", worker.getId());
        String sql = LabApplication.getProperty()
                .getProperty("sql.update");

        try (PreparedStatement stmt =
                     dbCon.getConnection().prepareStatement(sql)) {

            stmt.setString(1, worker.getName());
            stmt.setString(2, worker.getSurname());
            stmt.setString(3, worker.getLastname());
            stmt.setInt(4, worker.getAge());
            stmt.setString(5, worker.getCity());
            stmt.setString(6, worker.getPosition());
            stmt.setInt(7, worker.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                logger.info("Работник с id={} , не найден", worker.getId());
            } else {
                logger.info("Работник с id={} , успешно обновлен", worker.getId());
            }
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении работника", e);
        }
        return worker;
    }

    /**
     * Удаляет работника из базы данных.
     *
     * @param entity объект работника
     */
    @Override
    public void delete(Worker entity) {
        if (entity != null) {
            deleteById(entity.getId());
        }
    }

    /**
     * Удаляет работника по его идентификатору.
     *
     * @param id идентификатор работника
     */
    @Override
    public void deleteById(Integer id) {
        String sql = LabApplication.getProperty()
                .getProperty("sql.deleteById");
        logger.info("Попытка удаления работника с id={}", id);

        try (PreparedStatement stmt =
                     dbCon.getConnection().prepareStatement(sql)) {

            stmt.setLong(1, id);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                logger.warn("Работник с id={} не найден", id);
            } else {
                logger.info("Работник с id={} успешно удалён", id);
            }

        } catch (SQLException e) {
            logger.error("Ошибка при удалении работника с id={}", id, e);
        }
    }

    /**
     * Преобразует результат SQL-запроса в список объектов {@link Worker}.
     *
     * @param rs результат выполнения SQL-запроса
     * @return список работников
     */
    protected List<Worker> mapper(ResultSet rs) {
        List<Worker> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(new Worker(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("lastname"),
                        rs.getInt("age"),
                        rs.getString("city"),
                        rs.getString("position")
                ));
            }
            logger.debug("Результат запроса преобразован в список. Количество записей: {}",
                    list.size());
        } catch (SQLException e) {
            logger.error("Ошибка при преобразовании ResultSet в список работников", e);
        }

        return list;
    }

    /**
     * Выполняет поиск работников по фамилии или названию группы.
     *
     * @param value поисковое значение
     * @return коллекция найденных работников
     */
    @Override
    public Collection<Worker> findBySurnameOrGroupName(String value) {

        String sql = LabApplication.getProperty()
                .getProperty("sql.findBySurnameOrGroupName");

        logger.info("Поиск работников по подстроке: '{}'", value);

        List<Worker> result = new ArrayList<>();

        try (PreparedStatement stmt =
                     dbCon.getConnection().prepareStatement(sql)) {

            String pattern = "%" + value + "%";

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                result = mapper(rs);
            }

            logger.debug("Поиск завершён. Найдено записей: {}", result.size());

        } catch (SQLException e) {
            logger.error("Ошибка поиска работников по значению: '{}'", value, e);
        }

        return result;
    }
}

