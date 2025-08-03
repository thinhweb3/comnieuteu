/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.dao.impl;

import comnieu.dao.EmployeeDAO;
import comnieu.entity.Employee;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;

import java.util.List;

/**
 * DAO implementation for Employee
 * 
 * @author Admin
 */
public class EmployeeDAOImpl implements EmployeeDAO {

    final String insertSql = """
        INSERT INTO Employee (FullName, Gender, BirthDate, Phone, Username, Password, Position, Role)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    final String updateSql = """
        UPDATE Employee
        SET FullName = ?, Gender = ?, BirthDate = ?, Phone = ?, Username = ?, Password = ?, Position = ?, Role = ?
        WHERE Id = ?
    """;

    final String deleteSql = "DELETE FROM Employee WHERE Id = ?";
    final String findAllSql = "SELECT * FROM Employee";
    final String findByIdSql = "SELECT * FROM Employee WHERE Id = ?";
    final String findByUsernameSql = "SELECT * FROM Employee WHERE Username = ?";
    final String findByRoleSql = "SELECT * FROM Employee WHERE Role = ?";

    @Override
    public Employee create(Employee entity) {
        Object[] values = {
            entity.getFullName(),
            entity.getGender(),
            entity.getBirthDate(),
            entity.getPhone(),
            entity.getUsername(),
            entity.getPassword(),
            entity.getPosition(),
            entity.getRole()
        };
        int id = (int) XJdbc.executeInsert(insertSql, values);
        entity.setId(id);
        return entity;
    }

    @Override
    public void update(Employee entity) {
        Object[] values = {
            entity.getFullName(),
            entity.getGender(),
            entity.getBirthDate(),
            entity.getPhone(),
            entity.getUsername(),
            entity.getPassword(),
            entity.getPosition(),
            entity.getRole(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Employee> findAll() {
        return XQuery.getBeanList(Employee.class, findAllSql);
    }

    @Override
    public Employee findById(Integer id) {
        return XQuery.getSingleBean(Employee.class, findByIdSql, id);
    }

    @Override
    public Employee findByUsername(String username) {
        return XQuery.getSingleBean(Employee.class, findByUsernameSql, username);
    }

    @Override
    public List<Employee> findByRole(Integer role) {
        return XQuery.getBeanList(Employee.class, findByRoleSql, role);
    }
}
