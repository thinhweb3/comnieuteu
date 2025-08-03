package comnieu.ui.manager;

import comnieu.entity.ImportReceipt;

public interface ImportReceiptController {
    void open(); // Hiển thị dialog, chọn khoảng thời gian, clear form
    void fillToTable(); // Đổ danh sách phiếu nhập theo khoảng thời gian
    void edit(); // Chỉnh sửa phiếu được chọn trong bảng

    void checkAll(); // Chọn tất cả checkbox trong bảng
    void uncheckAll(); // Bỏ chọn tất cả checkbox
    void deleteCheckedItems(); // Xoá các mục đã được chọn

    void fillImportDetails(); // Đổ chi tiết phiếu nhập vào bảng dưới
    void setForm(ImportReceipt entity); // Set dữ liệu lên form từ entity
    ImportReceipt getForm(); // Lấy dữ liệu từ form thành entity

    void create(); // Thêm mới phiếu nhập
    void update(); // Cập nhật phiếu nhập
    void delete(); // Xoá phiếu nhập

    void clear(); // Làm sạch form, reset trạng thái
    void setEditable(boolean editable); // Cho phép chỉnh sửa hay không

    void moveFirst();
    void movePrevious();
    void moveNext();
    void moveLast();
    void moveTo(int index);

    void selectTimeRange(); // Chọn nhanh thời gian: hôm nay, tuần này, v.v.
}
