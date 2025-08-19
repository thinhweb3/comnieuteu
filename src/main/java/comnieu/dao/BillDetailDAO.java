package comnieu.dao;

import comnieu.entity.BillDetail;
import java.util.List;

public interface BillDetailDAO extends CrudDAO<BillDetail, Long> {
    List<BillDetail> findByBillId(Long billId); 
    List<BillDetail> findByDishId(Integer dishId);
}
