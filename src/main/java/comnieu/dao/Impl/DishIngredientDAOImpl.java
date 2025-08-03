package comnieu.dao.impl;

import comnieu.dao.DishIngredientDAO;
import comnieu.entity.DishIngredient;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class DishIngredientDAOImpl implements DishIngredientDAO {

    final String insertSql = """
        INSERT INTO DishIngredient (DishId, IngredientId, Quantity, Unit)
        VALUES (?, ?, ?, ?)
    """;

    final String updateSql = """
        UPDATE DishIngredient
        SET Quantity = ?, Unit = ?
        WHERE DishId = ? AND IngredientId = ?
    """;

    final String deleteSql = "DELETE FROM DishIngredient WHERE DishId = ? AND IngredientId = ?";

    final String findAllSql = "SELECT * FROM DishIngredient";
    final String findByIdSql = "SELECT * FROM DishIngredient WHERE DishId = ? AND IngredientId = ?";
    final String findByDishIdSql = "SELECT * FROM DishIngredient WHERE DishId = ?";
    final String findByIngredientIdSql = "SELECT * FROM DishIngredient WHERE IngredientId = ?";

    @Override
    public DishIngredient create(DishIngredient entity) {
        Object[] values = {
            entity.getDishId(),
            entity.getIngredientId(),
            entity.getQuantity(),
            entity.getUnit()
        };
        XJdbc.executeUpdate(insertSql, values);
        return entity;
    }

    @Override
    public void update(DishIngredient entity) {
        Object[] values = {
            entity.getQuantity(),
            entity.getUnit(),
            entity.getDishId(),
            entity.getIngredientId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void delete(Integer dishId, Integer ingredientId) {
        XJdbc.executeUpdate(deleteSql, dishId, ingredientId);
    }

    @Override
    public List<DishIngredient> findAll() {
        return XQuery.getBeanList(DishIngredient.class, findAllSql);
    }

    @Override
    public DishIngredient findById(Integer dishId, Integer ingredientId) {
        return XQuery.getSingleBean(DishIngredient.class, findByIdSql, dishId, ingredientId);
    }

    @Override
    public List<DishIngredient> findByDishId(Integer dishId) {
        return XQuery.getBeanList(DishIngredient.class, findByDishIdSql, dishId);
    }

    @Override
    public List<DishIngredient> findByIngredientId(Integer ingredientId) {
        return XQuery.getBeanList(DishIngredient.class, findByIngredientIdSql, ingredientId);
    }
}
