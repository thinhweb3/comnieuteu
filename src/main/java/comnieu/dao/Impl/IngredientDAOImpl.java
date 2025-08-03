package comnieu.dao.impl;

import comnieu.dao.IngredientDAO;
import comnieu.entity.Ingredient;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;

import java.util.List;

/**
 * DAO implementation cho Ingredient
 * 
 * @author Admin
 */
public class IngredientDAOImpl implements IngredientDAO {

    final String insertSql = """
        INSERT INTO Ingredient (Name, Unit, Status, SupplierId)
        VALUES (?, ?, ?, ?)
    """;

    final String updateSql = """
        UPDATE Ingredient
        SET Name = ?, Unit = ?, Status = ?, SupplierId = ?
        WHERE Id = ?
    """;

    final String deleteSql = "DELETE FROM Ingredient WHERE Id = ?";
    final String findAllSql = "SELECT * FROM Ingredient";
    final String findByIdSql = "SELECT * FROM Ingredient WHERE Id = ?";
    final String findBySupplierIdSql = "SELECT * FROM Ingredient WHERE SupplierId = ?";
    final String findByStatusSql = "SELECT * FROM Ingredient WHERE Status = ?";

    @Override
    public Ingredient create(Ingredient entity) {
        Object[] values = {
            entity.getName(),
            entity.getUnit(),
            entity.getStatus(),
            entity.getSupplierId()
        };
        int id = (int) XJdbc.executeInsert(insertSql, values);
        entity.setId(id);
        return entity;
    }

    @Override
    public void update(Ingredient entity) {
        Object[] values = {
            entity.getName(),
            entity.getUnit(),
            entity.getStatus(),
            entity.getSupplierId(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Ingredient> findAll() {
        return XQuery.getBeanList(Ingredient.class, findAllSql);
    }

    @Override
    public Ingredient findById(Integer id) {
        return XQuery.getSingleBean(Ingredient.class, findByIdSql, id);
    }

    @Override
    public List<Ingredient> findBySupplierId(Integer supplierId) {
        return XQuery.getBeanList(Ingredient.class, findBySupplierIdSql, supplierId);
    }

    @Override
    public List<Ingredient> findByStatus(Integer status) {
        return XQuery.getBeanList(Ingredient.class, findByStatusSql, status);
    }
}
