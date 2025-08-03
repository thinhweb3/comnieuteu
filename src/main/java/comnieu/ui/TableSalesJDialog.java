package comnieu.ui;

import comnieu.dao.DiningTableDAO;
import comnieu.dao.impl.DiningTableDAOImpl;
import comnieu.entity.DiningTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Giao diện hiển thị bàn ăn phục vụ bán hàng
 * 
 * @author Admin
 */
public class TableSalesJDialog extends JDialog {

    private final DiningTableDAO dao = new DiningTableDAOImpl();
    private JPanel pnlTable;

    public TableSalesJDialog() {
        initComponents();
        setTitle("Cơm Niêu - Quản lý Bàn Ăn");
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        pnlTable = new JPanel(new GridLayout(0, 5, 10, 10));
        add(pnlTable);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent evt) {
                loadTables();
            }
        });
    }

    private void loadTables() {
        pnlTable.removeAll();
        List<DiningTable> list = dao.findAll();
        for (DiningTable table : list) {
            pnlTable.add(createButton(table));
        }
        pnlTable.revalidate();
        pnlTable.repaint();
    }

    private JButton createButton(DiningTable table) {
        JButton btn = new JButton(table.getName());
        btn.setPreferredSize(new Dimension(100, 80));

        // Đặt màu theo status
        Color bg = switch (table.getStatus()) {
            case 1 -> Color.ORANGE;  // Có khách
            case 2 -> Color.YELLOW;  // Đặt trước
            default -> Color.GREEN;  // Trống
        };

        btn.setBackground(bg);
        btn.setEnabled(true);
        btn.setActionCommand(String.valueOf(table.getId()));
        btn.addActionListener((ActionEvent e) -> {
            int tableId = Integer.parseInt(e.getActionCommand());
            showOrderDialog(tableId);
        });
        return btn;
    }

    private void showOrderDialog(int tableId) {
        JOptionPane.showMessageDialog(this, "Bàn #" + tableId + " được chọn");
        // TODO: mở hóa đơn tương ứng
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TableSalesJDialog().setVisible(true));
    }
}
