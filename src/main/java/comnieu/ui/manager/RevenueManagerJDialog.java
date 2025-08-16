package comnieu.ui.manager;

import comnieu.dao.RevenueDAO;
import comnieu.dao.impl.RevenueDAOImpl;
import comnieu.entity.Revenue;
import comnieu.ui.manager.RevenueController;
import comnieu.util.XDate;
import comnieu.util.XDialog;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import javax.swing.table.DefaultTableModel;

import comnieu.dao.PromotionStatsDAO;
import comnieu.dao.impl.PromotionStatsDAOImpl;
import comnieu.entity.PromotionStats;

public class RevenueManagerJDialog extends javax.swing.JFrame implements RevenueController {
    
    // formatter VND cho UI: 85.000đ
    private static final java.text.DecimalFormat vndFmt =
        new java.text.DecimalFormat(
            "#,###'đ'",
            new java.text.DecimalFormatSymbols(new java.util.Locale("vi","VN"))
        );

    public RevenueManagerJDialog() {
        initComponents();
        open();
    }
    
    @Override
    public void open() {
        selectTimeRange();
    }

    @Override
    public void selectTimeRange() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");

        java.util.Date now = new java.util.Date();
        Date begin = now, end = now;

        switch (cboTimeRanges.getSelectedIndex()) {
            case 0: // Hôm nay
                begin = end = now;
                break;
            case 1: // Tháng này
                cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
                begin = cal.getTime();
                cal.set(java.util.Calendar.DAY_OF_MONTH, cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
                end = cal.getTime();
                break;
            case 2: // Năm nay
                cal.set(java.util.Calendar.DAY_OF_YEAR, 1);
                begin = cal.getTime();
                cal.set(java.util.Calendar.MONTH, 11);
                cal.set(java.util.Calendar.DAY_OF_MONTH, 31);
                end = cal.getTime();
                break;
        }

        txtBegin.setText(sdf.format(begin));
        txtEnd.setText(sdf.format(end));
    }

    @Override
    public void fillAll() {
        try {
            Date from = XDate.parse(txtBegin.getText());
            Date to = XDate.parse(txtEnd.getText());
            fillRevenueByEmployee(from, to);
            fillPopularDishes(from, to);
            fillPromotionUsage(from, to);   // <-- THÊM DÒNG NÀY
        } catch (Exception e) {
            XDialog.alert(this, "Ngày không hợp lệ!");
        }
    }

    @Override
    public void fillRevenueByEmployee(Date from, Date to) {
        DefaultTableModel model = (DefaultTableModel) tblEmployee.getModel();
        model.setRowCount(0);

        List<Revenue> list = dao.getRevenueByEmployee(from, to);
        BigDecimal total = BigDecimal.ZERO;

        for (Revenue r : list) {
            model.addRow(new Object[]{
                r.getEmployeeName(),
                vndFmt.format(r.getTotalRevenue())   // ví dụ: 85.000đ
            });
            total = total.add(r.getTotalRevenue());
        }

        txtTotalRevenue.setText(vndFmt.format(total));
    }

    @Override
    public void fillPopularDishes(Date from, Date to) {
        DefaultTableModel modelMost  = (DefaultTableModel) tblMostPopularDishes.getModel();
        DefaultTableModel modelLeast = (DefaultTableModel) tblLeastPopularDishes.getModel();
        modelMost.setRowCount(0);
        modelLeast.setRowCount(0);

        List<Revenue.PopularDish> list = dao.getPopularDishes(from, to);

        for (int i = 0; i < list.size(); i++) {
            Revenue.PopularDish d = list.get(i);
            Object[] row = new Object[]{
                d.getName(),
                d.getQuantity() + " lần",
                vndFmt.format(d.getTotal())  // <-- 85.000đ thay vì 85000đ
            };
            if (i < 3) {
                modelMost.addRow(row);
            } else {
                modelLeast.addRow(row);
            }
        }
    }

    // === Đổ dữ liệu tab "Thống kê chương trình khuyến mãi"
    private void fillPromotionUsage(Date from, Date to) {
        List<PromotionStats> list = promoDao.getPromotionsUsed(from, to);
        DefaultTableModel model = (DefaultTableModel) tblPromotionUsage.getModel();
        model.setRowCount(0);

        for (PromotionStats p : list) {
            model.addRow(new Object[]{
                p.getPromotionId(),
                p.getPromotionName(),
                (int) (p.getDiscountRate() * 100) + "%",  // hiển thị 10%
                p.getUsedCount()
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jLabel1 = new javax.swing.JLabel();
        txtBegin = new javax.swing.JTextField();
        txtEnd = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnFilter = new javax.swing.JButton();
        cboTimeRanges = new javax.swing.JComboBox<>();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtTotalRevenue = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblEmployee = new javax.swing.JTable();
        btnExcel1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMostPopularDishes = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLeastPopularDishes = new javax.swing.JTable();
        btnExcel2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPromotionUsage = new javax.swing.JTable();
        btnExcel3 = new javax.swing.JButton();

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Thống kê");

        jLabel1.setText("Từ ngày:");

        jLabel2.setText("Đến ngày:");

        btnFilter.setText("Lọc");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        cboTimeRanges.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hôm nay", "Tháng này", "Năm nay" }));
        cboTimeRanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTimeRangesActionPerformed(evt);
            }
        });

        jLabel3.setText("Tổng doanh thu:");

        tblEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Họ Tên Nhân Viên", "Doanh Thu"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblEmployee);

        btnExcel1.setBackground(new java.awt.Color(0, 255, 0));
        btnExcel1.setText("Xuất file Excel");
        btnExcel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcel1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnExcel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExcel1)
                    .addComponent(jLabel3)
                    .addComponent(txtTotalRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        tabs.addTab("Doanh thu từng nhân viên", jPanel1);

        tblMostPopularDishes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tên Món", "Số Lần Gọi ", "Tổng Tiền "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblMostPopularDishes);

        jLabel4.setText("Top 3 món ăn bán chạy nhất:");

        jLabel5.setText("Top 3 món ăn ít bán chạy nhất:");

        tblLeastPopularDishes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tên Món", "Số Lần Gọi ", "Tổng Tiền "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblLeastPopularDishes);

        btnExcel2.setBackground(new java.awt.Color(0, 255, 0));
        btnExcel2.setText("Xuất file Excel");
        btnExcel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcel2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(btnExcel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExcel2)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        tabs.addTab("Thống kê món ăn", jPanel2);

        tblPromotionUsage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Khuyến Mãi", "Tên Chương Trình", "Phần Trăm Giảm Giá", "Số Lần Dùng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblPromotionUsage);

        btnExcel3.setBackground(new java.awt.Color(0, 255, 0));
        btnExcel3.setText("Xuất file Excel");
        btnExcel3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcel3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnExcel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnExcel3)
                .addGap(17, 17, 17))
        );

        tabs.addTab("Thống kê chương trình khuyến mãi", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBegin, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboTimeRanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFilter)
                    .addComponent(cboTimeRanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        fillAll();
    }//GEN-LAST:event_btnFilterActionPerformed

    private void cboTimeRangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTimeRangesActionPerformed
        selectTimeRange();
        fillAll();
    }//GEN-LAST:event_cboTimeRangesActionPerformed

    private void btnExcel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcel1ActionPerformed
        exportEmployeeToExcel();
    }//GEN-LAST:event_btnExcel1ActionPerformed

    private void btnExcel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcel2ActionPerformed
        exportDishesToExcel();
    }//GEN-LAST:event_btnExcel2ActionPerformed

    private void btnExcel3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcel3ActionPerformed
        exportPromotionsToExcel();
    }//GEN-LAST:event_btnExcel3ActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RevenueManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RevenueManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RevenueManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RevenueManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RevenueManagerJDialog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcel1;
    private javax.swing.JButton btnExcel2;
    private javax.swing.JButton btnExcel3;
    private javax.swing.JButton btnFilter;
    private javax.swing.JComboBox<String> cboTimeRanges;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblEmployee;
    private javax.swing.JTable tblLeastPopularDishes;
    private javax.swing.JTable tblMostPopularDishes;
    private javax.swing.JTable tblPromotionUsage;
    private javax.swing.JTextField txtBegin;
    private javax.swing.JTextField txtEnd;
    private javax.swing.JTextField txtTotalRevenue;
    // End of variables declaration//GEN-END:variables
    private final RevenueDAO dao = new RevenueDAOImpl();
    private final PromotionStatsDAO promoDao = new PromotionStatsDAOImpl();
    // ==== Biến format tiền (tuỳ chọn nếu muốn format VND trong Excel) ====


    // ==== Lấy chuỗi khoảng thời gian lọc ====
    private String rangeLabel() {
        return "Từ ngày " + txtBegin.getText() + " đến ngày " + txtEnd.getText();
    }

    // ==== Hộp thoại chọn file lưu ====
    private java.io.File chooseFile(String title, String defaultName, String... extensions) {
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setDialogTitle(title);
        fc.setSelectedFile(new java.io.File(defaultName));
        int opt = fc.showSaveDialog(this);
        if (opt == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File f = fc.getSelectedFile();
            if (extensions != null && extensions.length > 0) {
                String name = f.getName().toLowerCase();
                boolean hasExt = false;
                for (String ext : extensions) if (name.endsWith(ext.toLowerCase())) { hasExt = true; break; }
                if (!hasExt) f = new java.io.File(f.getParentFile(), f.getName() + extensions[0]);
            }
            return f;
        }
        return null;
    }

    // ====================== Excel: Doanh thu từng nhân viên ======================
    private void exportEmployeeToExcel() {
        fillAll();
        java.io.File file = chooseFile("Lưu Excel - Doanh thu từng nhân viên",
                                       "DoanhThuNhanVien.xlsx", ".xlsx");
        if (file == null) return;

        try (org.apache.poi.xssf.usermodel.XSSFWorkbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sh = wb.createSheet("Doanh thu NV");
            int rowIdx = 0;

            // ===== Styles =====
            org.apache.poi.ss.usermodel.Font fontHeader = wb.createFont();
            fontHeader.setFontName("Times New Roman");
            fontHeader.setFontHeightInPoints((short)13);
            fontHeader.setBold(true);

            org.apache.poi.ss.usermodel.CellStyle header = wb.createCellStyle();
            header.setFont(fontHeader);
            header.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.YELLOW.getIndex());
            header.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            header.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            header.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            header.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            header.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            org.apache.poi.ss.usermodel.Font fontBody = wb.createFont();
            fontBody.setFontName("Times New Roman");
            fontBody.setFontHeightInPoints((short)13);

            org.apache.poi.ss.usermodel.DataFormat df = wb.createDataFormat();

            org.apache.poi.ss.usermodel.CellStyle bodyText = wb.createCellStyle();
            bodyText.setFont(fontBody);
            bodyText.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyText.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyText.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyText.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            org.apache.poi.ss.usermodel.CellStyle bodyInt = wb.createCellStyle();
            bodyInt.setFont(fontBody);
            bodyInt.setDataFormat(df.getFormat("0"));
            bodyInt.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyInt.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyInt.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyInt.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            org.apache.poi.ss.usermodel.CellStyle money = wb.createCellStyle();
            money.setFont(fontBody);
            money.setDataFormat(df.getFormat("#,##0\" đ\""));
            money.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            money.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            money.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            money.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            org.apache.poi.ss.usermodel.Font fontTitle = wb.createFont();
            fontTitle.setFontName("Times New Roman");
            fontTitle.setFontHeightInPoints((short)13);
            fontTitle.setBold(true);
            org.apache.poi.ss.usermodel.CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFont(fontTitle);

            // ===== Title =====
            org.apache.poi.ss.usermodel.Row rowTitle = sh.createRow(rowIdx++);
            org.apache.poi.ss.usermodel.Cell cTitle = rowTitle.createCell(0);
            cTitle.setCellValue("BÁO CÁO DOANH THU TỪNG NHÂN VIÊN");
            cTitle.setCellStyle(titleStyle);

            org.apache.poi.ss.usermodel.Row rowRange = sh.createRow(rowIdx++);
            org.apache.poi.ss.usermodel.Cell cRange = rowRange.createCell(0);
            cRange.setCellValue(rangeLabel());
            cRange.setCellStyle(titleStyle);
            rowIdx++;

            // ===== Header =====
            org.apache.poi.ss.usermodel.Row h = sh.createRow(rowIdx++);
            String[] headers = {"STT", "Họ Tên Nhân Viên", "Doanh Thu"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = h.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(header);
            }

            // ===== Data =====
            javax.swing.table.TableModel m = tblEmployee.getModel();
            java.math.BigDecimal sum = java.math.BigDecimal.ZERO;
            for (int i = 0; i < m.getRowCount(); i++) {
                org.apache.poi.ss.usermodel.Row r = sh.createRow(rowIdx++);
                r.createCell(0).setCellValue(i + 1);
                r.getCell(0).setCellStyle(bodyInt);

                r.createCell(1).setCellValue(String.valueOf(m.getValueAt(i, 0)));
                r.getCell(1).setCellStyle(bodyText);

                String raw = String.valueOf(m.getValueAt(i, 1)).replaceAll("[^\\d]", "");
                java.math.BigDecimal val = raw.isEmpty() ? java.math.BigDecimal.ZERO : new java.math.BigDecimal(raw);
                sum = sum.add(val);

                r.createCell(2).setCellValue(val.doubleValue());
                r.getCell(2).setCellStyle(money);
            }

            // ===== Tổng =====
            org.apache.poi.ss.usermodel.Row rSum = sh.createRow(rowIdx++);
            org.apache.poi.ss.usermodel.Cell cLabel = rSum.createCell(0);
            cLabel.setCellValue("Tổng doanh thu");
            cLabel.setCellStyle(header); // nền vàng

            org.apache.poi.ss.usermodel.Cell cEmpty = rSum.createCell(1);
            cEmpty.setCellStyle(bodyText);

            org.apache.poi.ss.usermodel.Cell cSum = rSum.createCell(2);
            cSum.setCellValue(sum.doubleValue());
            cSum.setCellStyle(money); // chỉ border, không fill

            for (int c = 0; c <= 2; c++) sh.autoSizeColumn(c);

            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
                wb.write(fos);
            }
            XDialog.alert(this, "Xuất Excel thành công:\n" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            XDialog.alert(this, "Xuất Excel thất bại: " + e.getMessage());
        }
    }

    // ====================== Excel: Thống kê món ăn ======================
    private void exportDishesToExcel() {
        fillAll();
        java.io.File file = chooseFile("Lưu Excel - Thống kê món ăn",
                                       "ThongKeMonAn.xlsx", ".xlsx");
        if (file == null) return;

        try (org.apache.poi.xssf.usermodel.XSSFWorkbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.DataFormat df = wb.createDataFormat();

            org.apache.poi.ss.usermodel.Font fontHeader = wb.createFont();
            fontHeader.setFontName("Times New Roman");
            fontHeader.setFontHeightInPoints((short)13);
            fontHeader.setBold(true);

            org.apache.poi.ss.usermodel.CellStyle header = wb.createCellStyle();
            header.setFont(fontHeader);
            header.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.YELLOW.getIndex());
            header.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            header.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            header.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            header.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            header.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            org.apache.poi.ss.usermodel.Font fontTitle = wb.createFont();
            fontTitle.setFontName("Times New Roman");
            fontTitle.setFontHeightInPoints((short)13);
            fontTitle.setBold(true);
            org.apache.poi.ss.usermodel.CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFont(fontTitle);

            org.apache.poi.ss.usermodel.Font fontBody = wb.createFont();
            fontBody.setFontName("Times New Roman");
            fontBody.setFontHeightInPoints((short)13);

            org.apache.poi.ss.usermodel.CellStyle bodyText = wb.createCellStyle();
            bodyText.setFont(fontBody);
            bodyText.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyText.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyText.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyText.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            org.apache.poi.ss.usermodel.CellStyle bodyInt = wb.createCellStyle();
            bodyInt.setFont(fontBody);
            bodyInt.setDataFormat(df.getFormat("0"));
            bodyInt.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyInt.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyInt.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyInt.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            org.apache.poi.ss.usermodel.CellStyle money = wb.createCellStyle();
            money.setFont(fontBody);
            money.setDataFormat(df.getFormat("#,##0\" đ\""));
            money.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            money.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            money.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            money.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            fillDishSheet(wb, "Top bán chạy", tblMostPopularDishes, header, titleStyle, bodyText, bodyInt, money);
            fillDishSheet(wb, "Top ít bán",   tblLeastPopularDishes, header, titleStyle, bodyText, bodyInt, money);

            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
                wb.write(fos);
            }
            XDialog.alert(this, "Xuất Excel thành công:\n" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            XDialog.alert(this, "Xuất Excel thất bại: " + e.getMessage());
        }
    }

    private void fillDishSheet(org.apache.poi.xssf.usermodel.XSSFWorkbook wb, String sheetName,
                               javax.swing.JTable table,
                               org.apache.poi.ss.usermodel.CellStyle header,
                               org.apache.poi.ss.usermodel.CellStyle titleStyle,
                               org.apache.poi.ss.usermodel.CellStyle bodyText,
                               org.apache.poi.ss.usermodel.CellStyle bodyInt,
                               org.apache.poi.ss.usermodel.CellStyle money) {

        org.apache.poi.ss.usermodel.Sheet sh = wb.createSheet(sheetName);
        int rowIdx = 0;

        org.apache.poi.ss.usermodel.Row rowTitle = sh.createRow(rowIdx++);
        org.apache.poi.ss.usermodel.Cell cTitle = rowTitle.createCell(0);
        cTitle.setCellValue("THỐNG KÊ MÓN ĂN - " + sheetName.toUpperCase());
        cTitle.setCellStyle(titleStyle);

        org.apache.poi.ss.usermodel.Row rowRange = sh.createRow(rowIdx++);
        org.apache.poi.ss.usermodel.Cell cRange = rowRange.createCell(0);
        cRange.setCellValue(rangeLabel());
        cRange.setCellStyle(titleStyle);
        rowIdx++;

        org.apache.poi.ss.usermodel.Row h = sh.createRow(rowIdx++);
        String[] headers = {"STT", "Tên món", "Số lần gọi", "Tổng tiền"};
        for (int i = 0; i < headers.length; i++) {
            org.apache.poi.ss.usermodel.Cell cell = h.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(header);
        }

        javax.swing.table.TableModel m = table.getModel();
        for (int i = 0; i < m.getRowCount(); i++) {
            Object name = m.getValueAt(i, 0);
            Object qty  = m.getValueAt(i, 1);
            Object total= m.getValueAt(i, 2);
            if (name == null || qty == null || total == null) continue;

            org.apache.poi.ss.usermodel.Row r = sh.createRow(rowIdx++);
            r.createCell(0).setCellValue(i + 1);
            r.getCell(0).setCellStyle(bodyInt);

            r.createCell(1).setCellValue(name.toString());
            r.getCell(1).setCellStyle(bodyText);

            String qtyNum = qty.toString().replaceAll("\\D+", "");
            r.createCell(2).setCellValue(qtyNum.isEmpty() ? 0 : Integer.parseInt(qtyNum));
            r.getCell(2).setCellStyle(bodyInt);

            String raw = total.toString().replaceAll("[^\\d]", "");
            double v = raw.isEmpty() ? 0d : new java.math.BigDecimal(raw).doubleValue();
            r.createCell(3).setCellValue(v);
            r.getCell(3).setCellStyle(money);
        }

        for (int c = 0; c <= 3; c++) sh.autoSizeColumn(c);
    }

    // ====================== Excel: Thống kê chương trình khuyến mãi ======================
    private void exportPromotionsToExcel() {
        fillAll();
        java.io.File file = chooseFile("Lưu Excel - Thống kê khuyến mãi",
                                       "ThongKeKhuyenMai.xlsx", ".xlsx");
        if (file == null) return;

        try (org.apache.poi.xssf.usermodel.XSSFWorkbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sh = wb.createSheet("KM đã dùng");
            int rowIdx = 0;

            org.apache.poi.ss.usermodel.Font fontHeader = wb.createFont();
            fontHeader.setFontName("Times New Roman");
            fontHeader.setFontHeightInPoints((short)13);
            fontHeader.setBold(true);

            org.apache.poi.ss.usermodel.CellStyle header = wb.createCellStyle();
            header.setFont(fontHeader);
            header.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.YELLOW.getIndex());
            header.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            header.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            header.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            header.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            header.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            org.apache.poi.ss.usermodel.Font fontBody = wb.createFont();
            fontBody.setFontName("Times New Roman");
            fontBody.setFontHeightInPoints((short)13);

            org.apache.poi.ss.usermodel.DataFormat df = wb.createDataFormat();

            org.apache.poi.ss.usermodel.CellStyle bodyText = wb.createCellStyle();
            bodyText.setFont(fontBody);
            bodyText.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyText.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyText.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            bodyText.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            org.apache.poi.ss.usermodel.CellStyle intStyle = wb.createCellStyle();
            intStyle.setFont(fontBody);
            intStyle.setDataFormat(df.getFormat("0"));
            intStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            intStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            intStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            intStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            org.apache.poi.ss.usermodel.CellStyle percent = wb.createCellStyle();
            percent.setFont(fontBody);
            percent.setDataFormat(df.getFormat("0%"));
            percent.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            percent.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            percent.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            percent.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            org.apache.poi.ss.usermodel.Font fontTitle = wb.createFont();
            fontTitle.setFontName("Times New Roman");
            fontTitle.setFontHeightInPoints((short)13);
            fontTitle.setBold(true);
            org.apache.poi.ss.usermodel.CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFont(fontTitle);

            // ===== Title =====
            org.apache.poi.ss.usermodel.Row rowTitle = sh.createRow(rowIdx++);
            org.apache.poi.ss.usermodel.Cell cTitle = rowTitle.createCell(0);
            cTitle.setCellValue("THỐNG KÊ CHƯƠNG TRÌNH KHUYẾN MÃI ĐÃ SỬ DỤNG");
            cTitle.setCellStyle(titleStyle);

            org.apache.poi.ss.usermodel.Row rowRange = sh.createRow(rowIdx++);
            org.apache.poi.ss.usermodel.Cell cRange = rowRange.createCell(0);
            cRange.setCellValue(rangeLabel());
            cRange.setCellStyle(titleStyle);
            rowIdx++;

            org.apache.poi.ss.usermodel.Row h = sh.createRow(rowIdx++);
            String[] headers = {"Mã Khuyến Mãi", "Tên Chương Trình", "Phần Trăm Giảm Giá", "Số Lần Dùng"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = h.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(header);
            }

            javax.swing.table.TableModel m = tblPromotionUsage.getModel();
            int totalUsed = 0;
            for (int i = 0; i < m.getRowCount(); i++) {
                Object idObj   = m.getValueAt(i, 0);
                Object nameObj = m.getValueAt(i, 1);
                Object rateObj = m.getValueAt(i, 2);
                Object usedObj = m.getValueAt(i, 3);
                if (idObj == null || nameObj == null || rateObj == null || usedObj == null) continue;

                org.apache.poi.ss.usermodel.Row r = sh.createRow(rowIdx++);
                r.createCell(0).setCellValue(idObj.toString());
                r.getCell(0).setCellStyle(bodyText);

                r.createCell(1).setCellValue(nameObj.toString());
                r.getCell(1).setCellStyle(bodyText);

                String rateStr = rateObj.toString().replace("%", "").trim();
                double rateVal = rateStr.isEmpty() ? 0 : Double.parseDouble(rateStr) / 100.0;
                r.createCell(2).setCellValue(rateVal);
                r.getCell(2).setCellStyle(percent);

                int usedVal = Integer.parseInt(usedObj.toString().replaceAll("\\D+", ""));
                totalUsed += usedVal;
                r.createCell(3).setCellValue(usedVal);
                r.getCell(3).setCellStyle(intStyle);
            }

            // ===== Tổng =====
            org.apache.poi.ss.usermodel.Row rSum = sh.createRow(rowIdx++);
            org.apache.poi.ss.usermodel.Cell cLabel = rSum.createCell(0);
            cLabel.setCellValue("TỔNG SỐ LẦN DÙNG");
            cLabel.setCellStyle(header); // nền vàng

            rSum.createCell(1).setCellStyle(bodyText);
            rSum.createCell(2).setCellStyle(bodyText);

            org.apache.poi.ss.usermodel.Cell cTotal = rSum.createCell(3);
            cTotal.setCellValue(totalUsed);
            cTotal.setCellStyle(intStyle); // chỉ border, không fill

            for (int c = 0; c <= 3; c++) sh.autoSizeColumn(c);

            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
                wb.write(fos);
            }
            XDialog.alert(this, "Xuất Excel thành công:\n" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            XDialog.alert(this, "Xuất Excel thất bại: " + e.getMessage());
        }
    }
}