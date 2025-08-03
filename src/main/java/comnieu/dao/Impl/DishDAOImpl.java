/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.dao.impl;

import comnieu.dao.DishDAO;
import comnieu.entity.Dish;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;

import java.util.List;

/**
 *
 * @author Admin
 */
public class DishDAOImpl implements DishDAO {

    final String insertSql = """
        INSERT INTO Dish (Name, UnitPrice, Unit, Description, CategoryId, Status, Size)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    final String updateSql = """
        UPDATE Dish
        SET Name=?, UnitPrice=?, Unit=?, Description=?, CategoryId=?, Status=?, Size=?
        WHERE Id=?
    """;

    final String deleteSql = "DELETE FROM Dish WHERE Id=?";
    final String findAllSql = "SELECT * FROM Dish";
    final String findByIdSql = "SELECT * FROM Dish WHERE Id=?";
    final String findByCategorySql = "SELECT * FROM Dish WHERE CategoryId=?";

    @Override
    public Dish create(Dish entity) {
        Object[] values = {
            entity.getName(),
            entity.getUnitPrice(),
            entity.getUnit(),
            entity.getDescription(),
            entity.getCategoryId(),
            entity.getStatus(),
            entity.getSize()
        };
        int id = (int) XJdbc.executeInsert(insertSql, values);
        entity.setId(id);
        return entity;
    }

    @Override
    public void update(Dish entity) {
        Object[] values = {
            entity.getName(),
            entity.getUnitPrice(),
            entity.getUnit(),
            entity.getDescription(),
            entity.getCategoryId(),
            entity.getStatus(),
            entity.getSize(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Dish> findAll() {
        return XQuery.getBeanList(Dish.class, findAllSql);
    }

    @Override
    public Dish findById(Integer id) {
        return XQuery.getSingleBean(Dish.class, findByIdSql, id);
    }

    @Override
    public List<Dish> findByCategoryId(Integer categoryId) {
        return XQuery.getBeanList(Dish.class, findByCategorySql, categoryId);
    }
}
