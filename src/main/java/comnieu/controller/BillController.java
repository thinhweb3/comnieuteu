package comnieu.controller;

import comnieu.entity.Bill;

public interface BillController {
    void open();
    void selectTimeRange();
    void fillToTable();
    void fillBillDetails();
    void checkAll();
    void uncheckAll();
    void deleteCheckedItems();
    void edit();
    void setForm(Bill entity);
    Bill getForm();
    void create();
    void update();
    void delete();
    void clear();
    void setEditable(boolean editable);
    void moveFirst();
    void movePrevious();
    void moveNext();
    void moveLast();
    void moveTo(int index);
}