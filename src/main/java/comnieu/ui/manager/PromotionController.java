package comnieu.controller;

import comnieu.dao.PromotionDAO;
import comnieu.entity.Promotion;
import comnieu.util.XDate;
import comnieu.util.XDialog;

import java.util.Date;
import java.util.List;

public class PromotionController {

    private final PromotionDAO dao;

    public PromotionController(PromotionDAO dao) {
        this.dao = dao;
    }

    // Thêm mới khuyến mãi
    public boolean create(Promotion promotion) {
        try {
            dao.create(promotion);
            return true;
        } catch (Exception e) {
            XDialog.alert( "Lỗi tạo khuyến mãi: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật khuyến mãi
    public boolean update(Promotion promotion) {
        try {
            dao.update(promotion);
            return true;
        } catch (Exception e) {
            XDialog.alert( "Lỗi cập nhật: " + e.getMessage());
            return false;
        }
    }

    // Xóa khuyến mãi theo ID
    public boolean delete(Integer id) {
        try {
            dao.deleteById(id);
            return true;
        } catch (Exception e) {
            XDialog.alert( "Lỗi xóa: " + e.getMessage());
            return false;
        }
    }

    // Tìm tất cả khuyến mãi
    public List<Promotion> findAll() {
        return dao.findAll();
    }

    // Tìm khuyến mãi theo khoảng thời gian
    public List<Promotion> findByDateRange(Date from, Date to) {
        return dao.findByDateRange(from, to);
    }

    // Tìm khuyến mãi theo tên
    public Promotion findByName(String name) {
        return dao.findByName(name);
    }

    // Kiểm tra ràng buộc dữ liệu (ví dụ như dùng khi lấy dữ liệu từ form)
    public boolean validatePromotion(Promotion p) {
        if (p.getName() == null || p.getName().trim().isEmpty()) {
            XDialog.alert( "Tên khuyến mãi không được để trống!");
            return false;
        }
        if (p.getStartDate().after(p.getEndDate())) {
            XDialog.alert( "Ngày bắt đầu phải trước ngày kết thúc!");
            return false;
        }
        if (p.getDiscountRate() == null || p.getDiscountRate() <= 0) {
            XDialog.alert( "Giảm giá phải lớn hơn 0!");
            return false;
        }
        return true;
    }
}
