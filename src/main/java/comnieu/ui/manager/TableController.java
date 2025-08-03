package comnieu.ui.manager;

import comnieu.entity.DiningTable;

public interface TableController {
    void open();
    void fillToTable();
    void edit();
    void checkAll();
    void uncheckAll();
    void deleteCheckedItems();
    void setForm(DiningTable entity);
    DiningTable getForm();
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
