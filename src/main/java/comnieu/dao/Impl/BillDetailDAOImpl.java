package comnieu.dao.impl;

import comnieu.dao.BillDetailDAO;
import comnieu.entity.BillDetail;
import comnieu.util.XJdbc;
import comnieu.util.XQuery;

import java.util.List;

public class BillDetailDAOImpl implements BillDetailDAO {

    private static final String CREATE_SQL = """
        INSERT INTO BillDetail (BillId, DishId, Quantity, UnitPrice)
        VALUES (?, ?, ?, ?)
    """;

    private static final String UPDATE_SQL = """
        UPDATE BillDetail
        SET BillId = ?, DishId = ?, Quantity = ?, UnitPrice = ?
        WHERE Id = ?
    """;

    private static final String DELETE_SQL = "DELETE FROM BillDetail WHERE Id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM BillDetail";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM BillDetail WHERE Id = ?";
    private static final String FIND_BY_BILLID_SQL = """
    SELECT bd.Id, bd.BillId, bd.DishId, d.Name AS DishName, bd.Quantity, bd.UnitPrice
    FROM BillDetail bd
    JOIN Dish d ON bd.DishId = d.Id
    WHERE bd.BillId = ?
    """;
    private static final String FIND_BY_DISHID_SQL = "SELECT * FROM BillDetail WHERE DishId = ?";

    @Override
    public BillDetail create(BillDetail entity) {
        Object[] values = {
            entity.getBillId(),
            entity.getDishId(),
            entity.getQuantity(),
            entity.getUnitPrice()
        };
        long id = XJdbc.executeInsert(CREATE_SQL, values);
        entity.setId(id);
        return entity;
    }

    @Override
    public void update(BillDetail entity) {
        Object[] values = {
            entity.getBillId(),
            entity.getDishId(),
            entity.getQuantity(),
            entity.getUnitPrice(),
            entity.getId()
        };
        XJdbc.executeUpdate(UPDATE_SQL, values);
    }

    @Override
    public void deleteById(Long id) {
        XJdbc.executeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<BillDetail> findAll() {
        return XQuery.getBeanList(BillDetail.class, FIND_ALL_SQL);
    }

    @Override
    public BillDetail findById(Long id) {
        return XQuery.getSingleBean(BillDetail.class, FIND_BY_ID_SQL, id);
    }

    @Override
    public List<BillDetail> findByBillId(Long billId) {
        return XQuery.getBeanList(BillDetail.class, FIND_BY_BILLID_SQL, billId);
    }

    @Override
    public List<BillDetail> findByDishId(Integer dishId) {
        return XQuery.getBeanList(BillDetail.class, FIND_BY_DISHID_SQL, dishId);
    }
}
