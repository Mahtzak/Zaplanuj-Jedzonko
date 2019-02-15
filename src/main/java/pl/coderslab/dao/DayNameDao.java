package pl.coderslab.dao;

import pl.coderslab.exception.NotFoundException;
import pl.coderslab.model.Book;
import pl.coderslab.model.DayName;
import pl.coderslab.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DayNameDao {
    // ZAPYTANIA SQL

    private static final String FIND_ALL_DAY_NAMES_QUERY = "SELECT * FROM day_name";



    /**
     * Return all days
     *
     * @return
     */
    public List<DayName> findAll() {
        List<DayName> daysList = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_DAY_NAMES_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                DayName dayToAdd = new DayName();
                dayToAdd.setId(resultSet.getInt("id"));
                dayToAdd.setName(resultSet.getString("name"));
                dayToAdd.setOrder(resultSet.getInt("order"));
                daysList.add(dayToAdd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daysList;

    }

}