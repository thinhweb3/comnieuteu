package comnieu.dao;

import comnieu.entity.Bill;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BillDAO extends CrudDAO<Bill, Long> {
    // Có thể mở rộng: tìm theo thời gian, thẻ, nhân viên v.v.
        List<Bill> findByUsername(String username); 
    List<Bill> findByTableId(Integer tableId);
    List<Bill> findByTimeRange(Date begin, Date end); 
    List<Bill> findByDateRange(LocalDate from, LocalDate to);
    public Bill findServicingByTableId(Integer tableId); 
List<Bill> findByUserAndTimeRange(String username, Date begin, Date end); 
}
