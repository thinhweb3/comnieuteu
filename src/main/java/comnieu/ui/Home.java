/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package comnieu.ui;

import comnieu.dao.BillDAO;
import comnieu.dao.DiningTableDAO;
import comnieu.dao.impl.BillDAOImpl;
import comnieu.dao.impl.DiningTableDAOImpl;
import comnieu.entity.Bill;
import comnieu.entity.DiningTable;
import comnieu.ui.components.RoundedButton;
import comnieu.ui.manager.BillManagerJDialog;
import comnieu.ui.manager.DishManagerJDialog;
import comnieu.ui.manager.EmployeeManagerJDialog;
import comnieu.ui.manager.ImportManagerJDialog;
import comnieu.ui.manager.IngredientManagerJDialog;
import comnieu.ui.manager.PromotionManager;
import comnieu.ui.manager.RevenueManagerJDialog;
import comnieu.ui.manager.TableManagerJDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import comnieu.net.SocketServer;
import comnieu.net.Message;
import comnieu.net.NetConfig;
import javax.swing.SwingUtilities;

 
/**
 *
 * @author phucp
 */
public class Home extends javax.swing.JFrame implements HomeController{
private SocketServer socketServer;      
private BillJDialog openedBillDialog;  
    /**
     * Creates new form Home
     */
    public Home() {
        initCustomUI();
        setupRolePermissions(); // ✅ Gọi hàm kiểm tra quyền
        if (ui.XAuth.isManager()) {
    try {
        socketServer = new SocketServer(NetConfig.PORT);
        socketServer.start();

        // Đăng ký listener để nhận message và cập nhật UI
        socketServer.onMessage((Message msg) -> {
            // Cập nhật UI phải chạy trên EDT
            SwingUtilities.invokeLater(() -> handleIncomingMessage(msg));
        });

    } catch (Exception ex) {
        ex.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(
            this, "Không thể khởi động SocketServer: " + ex.getMessage(),
            "Socket", javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}
            this.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override public void windowClosing(java.awt.event.WindowEvent e) {
            try { if (socketServer != null) socketServer.stop(); } catch (Exception ignored) {}
        }
    });
        open();
    }

    
    private Timer tableRefreshTimer;
    
    @Override
    public void open() {
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.loadTables();
        
        tableRefreshTimer = new Timer(2000, e -> loadTables());
        tableRefreshTimer.start();
    }

    @Override
    public void showBillJDialog(int tableId) {
        BillDAO billDao = new BillDAOImpl();
        DiningTableDAO tableDao = new DiningTableDAOImpl();

        try {
            DiningTable table = tableDao.findById(tableId);
            Bill bill = billDao.findServicingByTableId(tableId);
            this.currentBill = bill;
            this.currentTable = table;
            updateBillArea();
            btnPay.setEnabled(true);
            if (bill == null) {
                if (table == null || !(table.getStatus() == 0 || table.getStatus() == 2)) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Bàn không khả dụng.");
                    return;
                }
                int opt = javax.swing.JOptionPane.showConfirmDialog(
                        this,
                        "Bàn #" + tableId + " chưa có hoá đơn.\nTạo hoá đơn mới?",
                        "Tạo hoá đơn",
                        javax.swing.JOptionPane.YES_NO_OPTION);
                if (opt != javax.swing.JOptionPane.YES_OPTION) return;

                // Lấy nhân viên hiện tại; fallback 1 nếu chạy demo chưa login
                Integer empId = ui.XAuth.getCurrentEmployeeId();
                if (empId == null) empId = 1;   // <-- tránh null gây lỗi INSERT

                bill = new Bill();
                bill.setTableId(tableId);
                bill.setEmployeeId(empId);
                bill.setStatus(Bill.BillStatus.Servicing.getValue());
                bill.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));   // cột DATE
                bill.setCheckIn(new java.util.Date());                                // cột DATETIME

                bill = billDao.create(bill);       // bill đã có id
                // cập nhật trạng thái bàn -> đang phục vụ
                table.setStatus(1);
                tableDao.update(table);
            }

                BillJDialog dialog = new BillJDialog(this, true);
                dialog.setBill(bill);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(
                this, "Lỗi khi mở hoá đơn bàn #" + tableId + ":\n" + ex.getMessage(),
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void payCurrentBill() throws HeadlessException {
    if (currentBill == null || currentTable == null) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Chưa chọn hóa đơn/bàn. Hãy double-click vào bàn để mở hóa đơn.",
                "Thông báo", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    if (currentBill.getStatus() != Bill.BillStatus.Servicing.getValue()) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Hóa đơn hiện không ở trạng thái 'Đang phục vụ'.",
                "Không thể thanh toán", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Xác nhận
    int opt = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Xác nhận thanh toán hóa đơn #" + currentBill.getId() +
            " cho bàn " + currentTable.getName() + " (#" + currentTable.getId() + ")?",
            "Xác nhận thanh toán",
            javax.swing.JOptionPane.YES_NO_OPTION
    );
    if (opt != javax.swing.JOptionPane.YES_OPTION) return;

    BillDAO billDao = new BillDAOImpl();
    DiningTableDAO tableDao = new DiningTableDAOImpl();

    try {
        // Nếu có tính tổng từ DAO, bạn có thể gọi ở đây:
        // double total = billDao.calculateTotal(currentBill.getId());
        // currentBill.setTotalAmount(total); // TODO: nếu entity có field này

        // Cập nhật trạng thái HĐ -> Paid
        currentBill.setStatus(Bill.BillStatus.Completed.getValue());
        currentBill.setCheckOut(new java.util.Date());

        // Nếu có field người thanh toán, ghi nhận:
        Integer empId = ui.XAuth.getCurrentEmployeeId();
        if (empId != null) {
            currentBill.setEmployeeId(empId);
        }

        billDao.update(currentBill);

        // Cập nhật trạng thái bàn -> Trống (0)
        currentTable.setStatus(0);
        tableDao.update(currentTable);

        // Thông báo
        javax.swing.JOptionPane.showMessageDialog(this,
                "Thanh toán thành công hóa đơn #" + currentBill.getId() + ".", "Thành công",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);

        // Reset UI + load lại bàn
        currentBill = null;
        currentTable = null;
        btnPay.setEnabled(false);
        billArea.setText("Thông tin hóa đơn sẽ hiển thị ở đây...");
        loadTables();

    } catch (Exception ex) {
        ex.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(
            this, "Lỗi khi thanh toán: " + ex.getMessage(),
            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
    private void showBillOnRightPanel(int tableId) {
        BillDAO billDao = new BillDAOImpl();
        DiningTableDAO tableDao = new DiningTableDAOImpl();

        try {
            DiningTable table = tableDao.findById(tableId);
            Bill bill = billDao.findServicingByTableId(tableId);

            // Lưu vào state để nút Thanh toán dùng được
            this.currentTable = table;
            this.currentBill  = bill;

            if (table == null) {
                billArea.setText("Không tìm thấy thông tin bàn #" + tableId);
                btnPay.setEnabled(false);
                return;
            }

            if (bill != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Bàn: ").append(table.getName())
                  .append(" (#").append(table.getId()).append(")\n");
                sb.append("Mã HĐ: ").append(bill.getId()).append("\n");
                sb.append("Trạng thái: ")
                  .append(bill.getStatus() == Bill.BillStatus.Servicing.getValue()
                          ? "Đang phục vụ" : (bill.getStatus() == Bill.BillStatus.Completed.getValue()
                          ? "Đã thanh toán" : "Đã hủy"))
                  .append("\n");
                sb.append("Giờ vào: ").append(bill.getCheckIn()).append("\n");
                if (bill.getCheckOut() != null) {
                    sb.append("Giờ ra: ").append(bill.getCheckOut()).append("\n");
                }
                billArea.setText(sb.toString());

                btnPay.setEnabled(bill.getStatus() == Bill.BillStatus.Servicing.getValue());
            } else {
                // Chưa có hóa đơn
                StringBuilder sb = new StringBuilder();
                sb.append("Bàn: ").append(table.getName())
                  .append(" (#").append(table.getId()).append(")\n");
                sb.append("Chưa có hóa đơn.\n");
                sb.append("• Nhấp đúp để tạo/mở hóa đơn.\n");
                billArea.setText(sb.toString());
                btnPay.setEnabled(false);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Lỗi khi tải hóa đơn bàn #" + tableId + ":\n" + ex.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void updateBillArea() {
    if (currentBill == null || currentTable == null) {
        billArea.setText("Chưa chọn bàn/hóa đơn.");
        btnPay.setEnabled(false);
        return;
    }

    StringBuilder sb = new StringBuilder();
    sb.append("Bàn: ").append(currentTable.getName())
      .append(" (#").append(currentTable.getId()).append(")\n");
    sb.append("Mã HĐ: ").append(currentBill.getId()).append("\n");
    sb.append("Trạng thái: ").append(currentBill.getStatus() == Bill.BillStatus.Servicing.getValue() ? "Đang phục vụ" : "Khác").append("\n");
    sb.append("Giờ vào: ").append(currentBill.getCheckIn()).append("\n");

    // TODO: Nếu Bill có tổng tiền, hiển thị:
    // sb.append("Tạm tính: ").append(currentBill.getTotalAmount()).append(" đ\n");

    billArea.setText(sb.toString());
    // Chỉ bật thanh toán nếu đang phục vụ
    btnPay.setEnabled(currentBill.getStatus() == Bill.BillStatus.Servicing.getValue());
    }

    
    private void loadTables() {
        DiningTableDAO dao = new DiningTableDAOImpl();
        List<DiningTable> tables = dao.findAll();

        pnlTable.removeAll();
        int cols = 4;
        int rows = (int) Math.ceil((double) tables.size() / cols);
        pnlTable.setLayout(new GridLayout(rows, cols, 25, 25)); // spacing lớn hơn

        int size = 160; // Kích thước nút to và vuông

        for (DiningTable table : tables) {
            String text = String.format("%s (#%d)", table.getName(), table.getId());
            RoundedButton btnTable = new RoundedButton(text, 40); // nút bo tròn đẹp

            btnTable.setPreferredSize(new Dimension(size, size));

            // Màu sắc theo trạng thái bàn
            switch (table.getStatus()) {
                case 0:
                    btnTable.setBackground(new Color(0, 200, 0));  // Trống
                    break;
                case 1:
                    btnTable.setBackground(new Color(255, 102, 102));  // Đang phục vụ
                    break;
                case 2:
                    btnTable.setBackground(Color.YELLOW);  // Đặt trước
                    break;
                default:
                    btnTable.setBackground(Color.LIGHT_GRAY);  // Không xác định
                    break;
            }

        btnTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                    if (e.getClickCount() == 1) {
                        showBillOnRightPanel(table.getId());  // ← 1 click: chỉ cập nhật panel bên phải
                    } else if (e.getClickCount() == 2) {
                        showBillJDialog(table.getId());       // ← 2 click: mở dialog như cũ
                    }
                }
            }
        });

            pnlTable.add(btnTable);
        }

        pnlTable.revalidate();
        pnlTable.repaint();
    }

        // === custom fields ===
        private JTextArea billArea;
        private JButton btnPay;
        private Bill currentBill;
        private DiningTable currentTable;
        
       private void initCustomUI() {
       // ====== KHU VỰC BÀN (TRÁI) ======
       pnlTable = new JPanel();
       pnlTable.setLayout(new GridLayout(0, 4, 25, 25)); // 4 cột, khoảng cách 25px

       JPanel leftPanel = new JPanel(new BorderLayout());
       leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
       JScrollPane scrollPane = new JScrollPane(pnlTable);
       scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
       scrollPane.getVerticalScrollBar().setUnitIncrement(16);
       leftPanel.add(scrollPane, BorderLayout.CENTER);

       // ====== KHU VỰC HÓA ĐƠN + THANH TOÁN (PHẢI) ======
       JPanel rightPanel = new JPanel();
       rightPanel.setPreferredSize(new Dimension(300, 0));
       rightPanel.setLayout(new BorderLayout(10, 10));
       rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

       // dùng field để còn cập nhật về sau
       billArea = new JTextArea("Thông tin hóa đơn sẽ hiển thị ở đây...");
       billArea.setEditable(false);
       billArea.setLineWrap(true);
       billArea.setWrapStyleWord(true);
       rightPanel.add(new JScrollPane(billArea), BorderLayout.CENTER);

       btnPay = new JButton("Thanh toán");
       btnPay.setPreferredSize(new Dimension(120, 40));
       btnPay.setEnabled(false); // chưa chọn bàn/hóa đơn thì khóa
       btnPay.addActionListener(evt -> payCurrentBill()); // handler thanh toán

       JPanel payPanel = new JPanel();
       payPanel.add(btnPay);
       rightPanel.add(payPanel, BorderLayout.SOUTH);

       // ====== GHÉP TRÁI + PHẢI ======
       JPanel mainPanel = new JPanel(new BorderLayout());
       mainPanel.add(leftPanel, BorderLayout.CENTER);
       mainPanel.add(rightPanel, BorderLayout.EAST);

       // ====== CÁC NÚT CHỨC NĂNG DƯỚI CÙNG ======
       btnEmployees = new JButton("Quản lý nhân viên");
       btnDishes    = new JButton("Quản lý món ăn");
       btnBills     = new JButton("Quản lý hóa đơn");
       btnImport    = new JButton("Quản lý nhập hàng");
       btnTable     = new JButton("Quản lý bàn");
       btnIngre     = new JButton("Quản lý nguyên liệu");
       btnPromotion = new JButton("Quản lý khuyến mãi");
       btnRevenue   = new JButton("Thống kê doanh thu");

       JButton[] bottomButtons = {
           btnEmployees, btnDishes, btnBills, btnImport,
           btnTable, btnIngre, btnPromotion, btnRevenue
       };
       for (JButton btn : bottomButtons) {
           btn.setPreferredSize(new Dimension(160, 40));
       }

       // gán action như cũ
       btnEmployees.addActionListener(evt -> new EmployeeManagerJDialog().setVisible(true));
       btnBills.addActionListener(evt -> new BillManagerJDialog().setVisible(true));
       btnImport.addActionListener(evt -> new ImportManagerJDialog().setVisible(true));
       btnTable.addActionListener(evt -> new TableManagerJDialog().setVisible(true));
       btnPromotion.addActionListener(evt -> new PromotionManager().setVisible(true));
       btnRevenue.addActionListener(evt -> new RevenueManagerJDialog().setVisible(true));
       btnIngre.addActionListener(evt -> new IngredientManagerJDialog().setVisible(true));
       btnDishes.addActionListener(evt -> new DishManagerJDialog().setVisible(true));

       JPanel bottomPanel = new JPanel(new GridLayout(2, 4, 15, 10));
       bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
       for (JButton btn : bottomButtons) bottomPanel.add(btn);

       // ====== LAYOUT TỔNG ======
       this.setLayout(new BorderLayout());
       this.add(mainPanel, BorderLayout.CENTER);
       this.add(bottomPanel, BorderLayout.SOUTH);

       // ====== CẤU HÌNH CỬA SỔ ======
       this.setTitle("Quản lý Nhà hàng");
       this.setDefaultCloseOperation(EXIT_ON_CLOSE);
       this.setExtendedState(JFrame.MAXIMIZED_BOTH);
       this.setLocationRelativeTo(null);
   }






private JButton createButton(DiningTable table) {
    JButton btnTable = new JButton(String.format("%s (#%d)", table.getName(), table.getId()));
    btnTable.setEnabled(true);
    btnTable.setFocusPainted(false);

    switch (table.getStatus()) {
        case 0:
            btnTable.setBackground(new Color(0, 200, 0));
            break;
        case 1:
            btnTable.setBackground(new Color(255, 102, 102));
            break;
        case 2:
            btnTable.setBackground(Color.YELLOW);
            break;
        default:
            btnTable.setBackground(Color.LIGHT_GRAY);
            break;
    }

    btnTable.setActionCommand(String.valueOf(table.getId()));
    return btnTable;
    }

    private void setupRolePermissions() {
    if (!ui.XAuth.isManager()) {
        btnEmployees.setEnabled(false);
        btnPromotion.setEnabled(false);
        btnIngre.setEnabled(false);
        btnImport.setEnabled(false);
        btnRevenue.setEnabled(false);
        btnBills.setEnabled(false);
        btnTable.setEnabled(false);
        btnDishes.setEnabled(false);
    }
    }
// Nhận message từ nhân viên và đồng bộ UI
private void handleIncomingMessage(Message msg) {
    try {
        // 1) Tải lại màu/trạng thái bàn cho “bảng bàn” (nếu cần)
        loadTables();

        // 2) Nếu panel bên phải đang xem đúng bàn, thì cập nhật thông tin
        if (currentTable != null && msg.tableId == currentTable.getId()) {
            // Bạn đã có showBillOnRightPanel và updateBillArea — gọi lại để reload từ DB
            showBillOnRightPanel(msg.tableId);
        }

        // 3) (Tuỳ chọn) Tự động mở BillJDialog khi có bill mới/updated
        if ("BILL_CREATED".equals(msg.type) || "BILL_UPDATED".equals(msg.type)) {
            // Nếu bạn muốn tự bật cửa sổ hoá đơn cho bàn đó:
            showBillJDialog(msg.tableId);

            // Hoặc nếu đã mở sẵn, chỉ refresh lại dialog:
            // if (openedBillDialog != null && openedBillDialog.isShowing()) {
            //     // giả sử BillJDialog có hàm refresh(); nếu chưa có thì setBill + open()
            //     openedBillDialog.refresh();
            // }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}




    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTable = new javax.swing.JPanel();
        btnEmployees = new javax.swing.JButton();
        btnDishes = new javax.swing.JButton();
        btnBills = new javax.swing.JButton();
        btnImport = new javax.swing.JButton();
        btnTable = new javax.swing.JButton();
        btnIngre = new javax.swing.JButton();
        btnPromotion = new javax.swing.JButton();
        btnRevenue = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout pnlTableLayout = new javax.swing.GroupLayout(pnlTable);
        pnlTable.setLayout(pnlTableLayout);
        pnlTableLayout.setHorizontalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1259, Short.MAX_VALUE)
        );
        pnlTableLayout.setVerticalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 502, Short.MAX_VALUE)
        );

        btnEmployees.setText("Quản lý nhân viên");
        btnEmployees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeesActionPerformed(evt);
            }
        });

        btnDishes.setText("Quản lý món ăn");
        btnDishes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDishesActionPerformed(evt);
            }
        });

        btnBills.setText("Quản lý hóa đơn");
        btnBills.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillsActionPerformed(evt);
            }
        });

        btnImport.setText("Quản lý nhập hàng");
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        btnTable.setText("Quản lý bàn");
        btnTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTableActionPerformed(evt);
            }
        });

        btnIngre.setText("Quản lý nguyên liệu");
        btnIngre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngreActionPerformed(evt);
            }
        });

        btnPromotion.setText("Quản lý khuyến mãi");
        btnPromotion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPromotionActionPerformed(evt);
            }
        });

        btnRevenue.setText("Thống kê doanh thu");
        btnRevenue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevenueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(pnlTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDishes, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEmployees, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPromotion, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnIngre, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBills, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(135, 135, 135))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(pnlTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBills, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnIngre, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTable, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnPromotion, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEmployees, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDishes, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDishesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDishesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDishesActionPerformed

    private void btnTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTableActionPerformed
        new TableManagerJDialog().setVisible(true);
    }//GEN-LAST:event_btnTableActionPerformed

    private void btnBillsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillsActionPerformed
        new BillManagerJDialog().setVisible(true);
    }//GEN-LAST:event_btnBillsActionPerformed

    private void btnRevenueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevenueActionPerformed
        new RevenueManagerJDialog().setVisible(true);
    }//GEN-LAST:event_btnRevenueActionPerformed

    private void btnPromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPromotionActionPerformed
        new PromotionManager().setVisible(true);
    }//GEN-LAST:event_btnPromotionActionPerformed

    private void btnIngreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIngreActionPerformed

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        new ImportManagerJDialog().setVisible(true);
    }//GEN-LAST:event_btnImportActionPerformed

    private void btnEmployeesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeesActionPerformed
        new EmployeeManagerJDialog().setVisible(true);
    }//GEN-LAST:event_btnEmployeesActionPerformed

    /**
     * @param args the command line arguments
     */
public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(() -> new Home().setVisible(true));
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBills;
    private javax.swing.JButton btnDishes;
    private javax.swing.JButton btnEmployees;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnIngre;
    private javax.swing.JButton btnPromotion;
    private javax.swing.JButton btnRevenue;
    private javax.swing.JButton btnTable;
    private javax.swing.JPanel pnlTable;
    // End of variables declaration//GEN-END:variables
}
