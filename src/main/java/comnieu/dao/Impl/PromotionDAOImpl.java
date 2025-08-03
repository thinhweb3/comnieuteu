package comnieu.dao.impl;

import comnieu.dao.PromotionDAO;
import comnieu.entity.Promotion;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;

import java.util.Date;
import java.util.List;

/**
 * DAO implementation cho Promotion
 * 
 * @author Admin
 */
public class PromotionDAOImpl implements PromotionDAO {

    final String insertSql = """
        INSERT INTO Promotion (Name, StartDate, EndDate, DiscountRate)
        VALUES (?, ?, ?, ?)
    """;

    final String updateSql = """
        UPDATE Promotion
        SET Name = ?, StartDate = ?, EndDate = ?, DiscountRate = ?
        WHERE Id = ?
    """;

    final String deleteSql = "DELETE FROM Promotion WHERE Id = ?";
    final String findAllSql = "SELECT * FROM Promotion";
    final String findByIdSql = "SELECT * FROM Promotion WHERE Id = ?";
    final String findByDateRangeSql = "SELECT * FROM Promotion WHERE StartDate >= ? AND EndDate <= ?";
    final String findByNameSql = "SELECT * FROM Promotion WHERE Name = ?";

    @Override
    public Promotion create(Promotion entity) {
        Object[] values = {
            entity.getName(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getDiscountRate()
        };
        int id = (int) XJdbc.executeInsert(insertSql, values);
        entity.setId(id);
        return entity;
    }

    @Override
    public void update(Promotion entity) {
        Object[] values = {
            entity.getName(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getDiscountRate(),
            entity.getId()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Promotion> findAll() {
        return XQuery.getBeanList(Promotion.class, findAllSql);
    }

    @Override
    public Promotion findById(Integer id) {
        return XQuery.getSingleBean(Promotion.class, findByIdSql, id);
    }

    @Override
    public List<Promotion> findByDateRange(Date start, Date end) {
        return XQuery.getBeanList(Promotion.class, findByDateRangeSql, start, end);
    }

    @Override
    public Promotion findByName(String name) {
        return XQuery.getSingleBean(Promotion.class, findByNameSql, name);
    }


}
