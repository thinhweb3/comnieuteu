package comnieu.dao.impl;

import comnieu.dao.ImportDetailDAO;
import comnieu.entity.ImportDetail;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;

import java.util.List;

/**
 * DAO implementation for ImportDetail
 * 
 * @author Admin
 */
public class ImportDetailDAOImpl implements ImportDetailDAO {

    final String insertSql = """
        INSERT INTO ImportDetail (ImportReceiptId, IngredientId, Quantity, Unit, UnitPrice)
        VALUES (?, ?, ?, ?, ?)
    """;

    final String updateSql = """
        UPDATE ImportDetail
        SET ImportReceiptId = ?, IngredientId = ?, Quantity = ?, Unit = ?, UnitPrice = ?
        WHERE Id = ?
    """;

    final String deleteSql = "DELETE FROM ImportDetail WHERE Id = ?";
    final String findAllSql = "SELECT * FROM ImportDetail";
    final String findByIdSql = "SELECT * FROM ImportDetail WHERE Id = ?";
    final String findByImportReceiptIdSql = "SELECT * FROM ImportDetail WHERE ImportReceiptId = ?";
    final String findByIngredientIdSql = "SELECT * FROM ImportDetail WHERE IngredientId = ?";

    @Override
    public ImportDetail create(ImportDetail entity) {
        Object[] values = {
            entity.getImportReceiptId(),
            entity.getIngredientId(),
            entity.getQuantity(),
            entity.getUnit(),
            entity.getUnitPrice()
        };
        long id = XJdbc.executeInsert(insertSql, values);
        entity.setId(id);
        return entity;
    }

    @Override
    public void update(ImportDetail entity) {
        Object[] values = {
            entity.getImportReceiptId(),
            entity.getIngredientId(),
            entity.getQuantity(),
            entity.getUnit(),
            entity.getUnitPrice(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Long id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<ImportDetail> findAll() {
        return XQuery.getBeanList(ImportDetail.class, findAllSql);
    }

    @Override
    public ImportDetail findById(Long id) {
        return XQuery.getSingleBean(ImportDetail.class, findByIdSql, id);
    }

    @Override
    public List<ImportDetail> findByImportReceiptId(Long importReceiptId) {
        return XQuery.getBeanList(ImportDetail.class, findByImportReceiptIdSql, importReceiptId);
    }

    @Override
    public List<ImportDetail> findByIngredientId(Integer ingredientId) {
        return XQuery.getBeanList(ImportDetail.class, findByIngredientIdSql, ingredientId);
    }
}
