package comnieu.ui.manager;


import comnieu.dao.EmployeeDAO;
import comnieu.dao.impl.EmployeeDAOImpl;
import comnieu.entity.Employee;
import comnieu.util.XDialog;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;

public class EmployeeManagerJDialog extends javax.swing.JFrame implements EmployeeController {

     private final EmployeeDAO dao = new EmployeeDAOImpl();
    private List<Employee> items = new ArrayList<>();

    public EmployeeManagerJDialog() {
        initComponents();
        init();
    }

    public void init() {
        this.setLocationRelativeTo(null);
        this.fillToTable();
        this.clear();
        setupCheckboxColumn();
    }

    private void setupCheckboxColumn() {
        TableColumn checkBoxColumn = tblEmployee.getColumnModel().getColumn(8);
        checkBoxColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        checkBoxColumn.setPreferredWidth(40);
        checkBoxColumn.setMaxWidth(50);
        checkBoxColumn.setMinWidth(20);
    }

    @Override
    public void open() {
        this.setLocationRelativeTo(null);
        this.fillToTable();
        this.clear();
    }

    @Override
    public void checkAll() {
        setCheckedAll(true);
    }

    @Override
    public void uncheckAll() {
        setCheckedAll(false);
    }

    private void setCheckedAll(boolean checked) {
        for (int i = 0; i < tblEmployee.getRowCount(); i++) {
            tblEmployee.setValueAt(checked, i, 9);
        }
    }

    @Override
    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) tblEmployee.getModel();
        model.setRowCount(0); // xóa dữ liệu cũ

        try {
            items = dao.findAll();
            for (Employee u : items) {
                Object[] rowData = {
                    u.getId(),                                  // Id
                    u.getFullName(),                            // Họ tên
                    u.getGender() != null && u.getGender() == 1 ? "Nam" : "Nữ", // Giới tính
                    (u.getBirthDate() != null
                            ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(u.getBirthDate())
                            : ""),                               // Ngày sinh
                    u.getPhone(),                               // Phone
                    u.getUsername(),                            // UserName
                    u.getPassword(),                            // Password
                    (u.getPosition() != null ? u.getPosition() : ""), // Chức vụ
                    false                                       // Checkbox
                };
                model.addRow(rowData);
            }
        } catch (Exception e) {
            XDialog.alert("Lỗi tải dữ liệu nhân viên: " + e.getMessage());
        }
    }


    @Override
    public void edit() {
        int selectedRow = tblEmployee.getSelectedRow();
        if (selectedRow != -1) {
            Integer employeeId = (Integer) tblEmployee.getValueAt(selectedRow, 0);
            Employee entity = dao.findById(employeeId);
            if (entity != null) {
                setForm(entity);
                setEditable(true);
                tabs.setSelectedIndex(1);
            }
        }
    }

   @Override
    public void setForm(Employee u) {
        txtId.setText(u.getId() != null ? u.getId().toString() : "");
        txtUsername.setText(u.getUsername() != null ? u.getUsername() : "");
        txtName.setText(u.getFullName() != null ? u.getFullName() : "");
        txtPhone.setText(u.getPhone() != null ? u.getPhone() : "");
        txtPassword.setText(u.getPassword() != null ? u.getPassword() : "");
        pwAgreeUser.setText(u.getPassword() != null ? u.getPassword() : "");

        // Ngày sinh
        if (u.getBirthDate() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            txtBirthday.setText(sdf.format(u.getBirthDate()));
        } else {
            txtBirthday.setText("");
        }

        // Giới tính
        if (u.getGender() != null && u.getGender() == 1) {
            rdoMale.setSelected(true);
        } else {
            rdoFemale.setSelected(true);
        }

        // Vai trò / Quyền
        if (u.getRole() != null && u.getRole() == 1) {
            rdoManager.setSelected(true);
        } else {
            rdoStaff.setSelected(true);
        }
    }


    @Override
    public Employee getForm() {
        try {
            Integer id = txtId.getText().trim().isEmpty() ? null : Integer.valueOf(txtId.getText().trim());

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            Date birthDate = null;
            if (!txtBirthday.getText().trim().isEmpty()) {
                birthDate = sdf.parse(txtBirthday.getText().trim());
            }

            String password = new String(txtPassword.getPassword()).trim();
            String confirmPassword = new String(pwAgreeUser.getPassword()).trim();
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Mật khẩu xác nhận không khớp!");
            }

            Employee u = new Employee();
            u.setId(id);
            u.setFullName(txtName.getText().trim());
            u.setGender(rdoMale.isSelected() ? 1 : 0);
            u.setBirthDate(birthDate);
            u.setPhone(txtPhone.getText().trim());
            u.setUsername(txtUsername.getText().trim());
            u.setPassword(password);
            u.setPosition(rdoManager.isSelected() ? "Quản lý" : "Nhân viên");
            u.setRole(rdoManager.isSelected() ? 1 : 0);

            return u;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID phải là số!");
        } catch (java.text.ParseException e) {
            throw new IllegalArgumentException("Ngày sinh không đúng định dạng (dd/MM/yyyy)!");
        }
    }




    @Override
    public void create() {
        try {
            Employee u = getForm();
            if (dao.findByUsername(u.getUsername()) != null) {
                XDialog.alert("Tên đăng nhập đã tồn tại!");
                return;
            }
            dao.create(u);
            fillToTable();
            clear();
            XDialog.alert("Thêm nhân viên thành công!");
        } catch (Exception ex) {
            XDialog.alert("Lỗi khi thêm nhân viên: " + ex.getMessage());
        }
    }

    @Override
    public void update() {
        try {
            Employee u = getForm();
            if (u.getId() == null) {
                XDialog.alert("Vui lòng chọn nhân viên để cập nhật!");
                return;
            }
            dao.update(u);
            fillToTable();
            XDialog.alert("Cập nhật nhân viên thành công!");
        } catch (Exception ex) {
            XDialog.alert("Lỗi khi cập nhật nhân viên: " + ex.getMessage());
        }
    }

    @Override
    public void delete() {
        try {
            String idText = txtId.getText().trim();
            if (idText.isEmpty()) {
                XDialog.alert("Vui lòng chọn nhân viên cần xóa!");
                return;
            }
            Integer id = Integer.valueOf(idText);
            if (dao.findById(id) == null) {
                XDialog.alert("Nhân viên không tồn tại!");
                return;
            }
            if (XDialog.confirm("Bạn có chắc muốn xóa nhân viên này?")) {
                dao.deleteById(id);
                fillToTable();
                clear();
                XDialog.alert("Đã xóa nhân viên.");
            }
        } catch (Exception e) {
            XDialog.alert("Lỗi khi xóa nhân viên: " + e.getMessage());
        }
    }

    @Override
    public void deleteCheckedItems() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < tblEmployee.getRowCount(); i++) {
            Object value = tblEmployee.getValueAt(i, 9);
            if (value instanceof Boolean && (Boolean) value) {
                ids.add((Integer) tblEmployee.getValueAt(i, 0));
            }
        }
        if (ids.isEmpty()) {
            XDialog.alert("Chưa chọn nhân viên nào để xóa.");
            return;
        }
        if (XDialog.confirm("Xóa " + ids.size() + " nhân viên?")) {
            for (Integer id : ids) {
                dao.deleteById(id);
            }
            fillToTable();
            clear();
            XDialog.alert("Đã xóa " + ids.size() + " nhân viên.");
        }
    }

    @Override
    public void clear() {
        setForm(new Employee());
        setEditable(false);
        tabs.setSelectedIndex(1);
    }

    @Override
    public void setEditable(boolean editable) {
        txtId.setEnabled(false); // ID không cho nhập tay
        btnCreate.setEnabled(!editable);
        btnUpdate.setEnabled(editable);
        btnDelete.setEnabled(editable);
    }

    @Override
    public void moveFirst() { moveTo(0); }

    @Override
    public void movePrevious() { moveTo(tblEmployee.getSelectedRow() - 1); }

    @Override
    public void moveNext() { moveTo(tblEmployee.getSelectedRow() + 1); }

    @Override
    public void moveLast() { moveTo(tblEmployee.getRowCount() - 1); }

    @Override
    public void moveTo(int index) {
        if (index < 0) {
            moveLast();
        } else if (index >= tblEmployee.getRowCount()) {
            moveFirst();
        } else {
            tblEmployee.clearSelection();
            tblEmployee.setRowSelectionInterval(index, index);
            edit();
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

        brgRole = new javax.swing.ButtonGroup();
        brgGender = new javax.swing.ButtonGroup();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmployee = new javax.swing.JTable();
        btnCheckAll = new javax.swing.JButton();
        UnCheckAll = new javax.swing.JButton();
        btnDeleteItemCheck = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblImg = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        pwAgreeUser = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        rdoManager = new javax.swing.JRadioButton();
        rdoStaff = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnMoveFirst = new javax.swing.JButton();
        btnMovePrevious = new javax.swing.JButton();
        btnMoveNext = new javax.swing.JButton();
        btnMoveLast = new javax.swing.JButton();
        txtPhone = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtBirthday = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        rdoFemale = new javax.swing.JRadioButton();
        rdoMale = new javax.swing.JRadioButton();
        txtUsername = new javax.swing.JTextField();
        ID = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Họ tên", "Gender", "Ngày sinh", "Phone", "UserName", "Password", "Chức vụ", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblEmployee);

        btnCheckAll.setText("Chọn tất cả");
        btnCheckAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckAllActionPerformed(evt);
            }
        });

        UnCheckAll.setText("Bỏ chọn tất cả");
        UnCheckAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnCheckAllActionPerformed(evt);
            }
        });

        btnDeleteItemCheck.setText("Xóa các mục đã chọn");
        btnDeleteItemCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteItemCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCheckAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UnCheckAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDeleteItemCheck)
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteItemCheck)
                    .addComponent(UnCheckAll)
                    .addComponent(btnCheckAll))
                .addGap(15, 15, 15))
        );

        tabs.addTab("Danh sách", jPanel1);

        lblImg.setIcon(new javax.swing.ImageIcon("D:\\DA1\\ComNieuTeu\\src\\main\\java\\comnieu\\img\\Curryavt.jpg")); // NOI18N

        jLabel2.setText("Tên đăng nhập");

        jLabel3.setText("Họ và tên");

        jLabel4.setText("Mật khẩu");

        jLabel5.setText("Xác nhận mật khẩu");
        jLabel5.setToolTipText("");

        jLabel6.setText("Vai trò");

        brgRole.add(rdoManager);
        rdoManager.setText("Quản lý");

        brgRole.add(rdoStaff);
        rdoStaff.setText("Nhân viên");

        jLabel8.setText("__________________________________________________________________________________________________________________________________________");

        btnCreate.setText("Tạo mới");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnUpdate.setText("Cập nhật");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClear.setText("Nhập mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnMoveFirst.setText("|<");
        btnMoveFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveFirstActionPerformed(evt);
            }
        });

        btnMovePrevious.setText("<<");
        btnMovePrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovePreviousActionPerformed(evt);
            }
        });

        btnMoveNext.setText(">>");
        btnMoveNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveNextActionPerformed(evt);
            }
        });

        btnMoveLast.setText(">|");
        btnMoveLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveLastActionPerformed(evt);
            }
        });

        jLabel9.setText("Số điện thoại");

        jLabel10.setText("Số điện thoại");

        jLabel7.setText("Giới tính");

        brgGender.add(rdoFemale);
        rdoFemale.setText("Nữ");

        brgGender.add(rdoMale);
        rdoMale.setText("Nam");

        ID.setText("Id");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnCreate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMoveFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMovePrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMoveNext, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMoveLast, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lblImg)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                            .addComponent(txtBirthday)
                            .addComponent(txtUsername)
                            .addComponent(txtId)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel10))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel9)
                                .addComponent(jLabel5)
                                .addComponent(jLabel3)
                                .addComponent(txtName)
                                .addComponent(pwAgreeUser)
                                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(rdoMale)
                                .addGap(18, 18, 18)
                                .addComponent(rdoFemale)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(rdoManager)
                                .addGap(18, 18, 18)
                                .addComponent(rdoStaff))
                            .addComponent(ID))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblImg))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pwAgreeUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(ID)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoFemale)
                                    .addComponent(rdoMale))))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoManager))
                            .addComponent(rdoStaff, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnMoveFirst)
                        .addComponent(btnMovePrevious)
                        .addComponent(btnMoveNext)
                        .addComponent(btnMoveLast))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCreate)
                        .addComponent(btnUpdate)
                        .addComponent(btnDelete)
                        .addComponent(btnClear)))
                .addGap(24, 24, 24))
        );

        tabs.addTab("Biểu Mẫu", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(tabs))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMoveFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveFirstActionPerformed
        this.moveFirst();
    }//GEN-LAST:event_btnMoveFirstActionPerformed

    private void btnMovePreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovePreviousActionPerformed
        this.movePrevious();
    }//GEN-LAST:event_btnMovePreviousActionPerformed

    private void btnMoveNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveNextActionPerformed
        this.moveNext();
    }//GEN-LAST:event_btnMoveNextActionPerformed

    private void btnMoveLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveLastActionPerformed
        this.moveLast();
    }//GEN-LAST:event_btnMoveLastActionPerformed

    private void btnCheckAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckAllActionPerformed
        this.checkAll();
    }//GEN-LAST:event_btnCheckAllActionPerformed

    private void UnCheckAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnCheckAllActionPerformed
        this.uncheckAll();
    }//GEN-LAST:event_UnCheckAllActionPerformed

    private void btnDeleteItemCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteItemCheckActionPerformed
        this.deleteCheckedItems();
    }//GEN-LAST:event_btnDeleteItemCheckActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        this.create();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        this.update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        this.delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        this.clear();
    }//GEN-LAST:event_btnClearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EmployeeManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeManagerJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeManagerJDialog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ID;
    private javax.swing.JButton UnCheckAll;
    private javax.swing.ButtonGroup brgGender;
    private javax.swing.ButtonGroup brgRole;
    private javax.swing.JButton btnCheckAll;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteItemCheck;
    private javax.swing.JButton btnMoveFirst;
    private javax.swing.JButton btnMoveLast;
    private javax.swing.JButton btnMoveNext;
    private javax.swing.JButton btnMovePrevious;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblImg;
    private javax.swing.JPasswordField pwAgreeUser;
    private javax.swing.JRadioButton rdoFemale;
    private javax.swing.JRadioButton rdoMale;
    private javax.swing.JRadioButton rdoManager;
    private javax.swing.JRadioButton rdoStaff;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblEmployee;
    private javax.swing.JTextField txtBirthday;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

    void setVisivle(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
