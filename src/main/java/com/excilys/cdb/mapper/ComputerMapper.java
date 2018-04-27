package main.java.com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.excilys.cdb.model.Computer;

public class ComputerMapper extends Mapper {

    @Override
    public Computer map(ResultSet resultSet) throws SQLException {
        Computer computer = new Computer();
        computer.setId(resultSet.getLong("id"));
        computer.setNom(resultSet.getString("name"));
        computer.setIntroduced(resultSet.getTimestamp("introduced") == null ? null
                : resultSet.getTimestamp("introduced").toLocalDateTime().toLocalDate());
        computer.setDiscontinued(resultSet.getTimestamp("discontinued") == null ? null
                : resultSet.getTimestamp("discontinued").toLocalDateTime().toLocalDate());
        return computer;
    }
}