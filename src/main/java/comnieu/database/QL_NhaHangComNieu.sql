-- TẠO CƠ SỞ DỮ LIỆU
CREATE DATABASE QL_NhaHangComNieu;
GO
USE QL_NhaHangComNieu;
GO
-- XÓA CÁC BẢNG NẾU ĐÃ TỒN TẠI (ĐẢM BẢO THỨ TỰ KHÓA NGOẠI)
DROP TABLE IF EXISTS NguyenLieu_MonAn;
DROP TABLE IF EXISTS ChiTietPN;
DROP TABLE IF EXISTS PhieuNhap;
DROP TABLE IF EXISTS NguyenLieu;
DROP TABLE IF EXISTS MonAn;
DROP TABLE IF EXISTS LoaiMonAn;
DROP TABLE IF EXISTS NhaCungCap;
GO

-- 1. Loại món ăn
CREATE TABLE LoaiMonAn (
    MaLoaiMA VARCHAR(10) PRIMARY KEY,
    TenLoai NVARCHAR(100)
);

-- 2. Món ăn
CREATE TABLE MonAn (
    MaMA VARCHAR(10) PRIMARY KEY,
    TenMon NVARCHAR(100),
    DonGia DECIMAL,
    DonVi NVARCHAR(50),
    MoTa TEXT,
    MaLoaiMA VARCHAR(10),
    TinhTrang NVARCHAR(20),
    Size INT,
    FOREIGN KEY (MaLoaiMA) REFERENCES LoaiMonAn(MaLoaiMA)
);

-- 3. Nhà cung cấp
CREATE TABLE NhaCungCap (
    MaNCC VARCHAR(10) PRIMARY KEY,
    TenNCC NVARCHAR(100),
    DiaChi NVARCHAR(255),
    DienThoai VARCHAR(15)
);

-- 4. Nguyên liệu
CREATE TABLE NguyenLieu (
    MaNL VARCHAR(10) PRIMARY KEY,
    TenNL NVARCHAR(100),
    DonVi NVARCHAR(20),
    TrangThai NVARCHAR(20),
    MaNCC VARCHAR(10),
    FOREIGN KEY (MaNCC) REFERENCES NhaCungCap(MaNCC)
);

CREATE TABLE NhanVien (
    MaNV VARCHAR(10) PRIMARY KEY,
    HoTen NVARCHAR(100),
    GioiTinh NVARCHAR(10),
    NgaySinh DATE,
    DienThoai VARCHAR(15),
    [User] NVARCHAR(50),
    [Pass] NVARCHAR(50),
    ChucVu NVARCHAR(50)
);

-- 6. Phiếu nhập
CREATE TABLE PhieuNhap (
    MaPN VARCHAR(10) PRIMARY KEY,
    NgayNhap DATE,
    MaNCC VARCHAR(10),
    MaNV VARCHAR(10),
    FOREIGN KEY (MaNCC) REFERENCES NhaCungCap(MaNCC),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

-- 7. Chi tiết phiếu nhập
CREATE TABLE ChiTietPN (
    MaCTPN VARCHAR(10) PRIMARY KEY,
    MaPN VARCHAR(10),
    MaNL VARCHAR(10),
    SL FLOAT,
    DonGia DECIMAL,
    FOREIGN KEY (MaPN) REFERENCES PhieuNhap(MaPN),
    FOREIGN KEY (MaNL) REFERENCES NguyenLieu(MaNL)
);

-- 8. Liên kết nguyên liệu - món ăn
CREATE TABLE NguyenLieu_MonAn (
    MaMA VARCHAR(10),
    MaNL VARCHAR(10),
    SoLuong FLOAT,
    DonVi NVARCHAR(20),
    PRIMARY KEY (MaMA, MaNL),
    FOREIGN KEY (MaMA) REFERENCES MonAn(MaMA),
    FOREIGN KEY (MaNL) REFERENCES NguyenLieu(MaNL)
);

-- 9. Bàn
CREATE TABLE Ban (
    MaBan VARCHAR(10) PRIMARY KEY,
    TenBan NVARCHAR(50),
    TrangThai NVARCHAR(20)
);

-- 10. Khuyến mãi
CREATE TABLE KhuyenMai (
    MaKM VARCHAR(10) PRIMARY KEY,
    TenKM NVARCHAR(100),
    NgayBatDau DATE,
    NgayKetThuc DATE,
    GiamGia FLOAT
);

-- 11. Hóa đơn
CREATE TABLE HoaDon (
    MaHD VARCHAR(10) PRIMARY KEY,
    NgayLap DATE,
    MaNV VARCHAR(10),
    MaBan VARCHAR(10),
    MaKM VARCHAR(10),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan),
    FOREIGN KEY (MaKM) REFERENCES KhuyenMai(MaKM)
);

-- 12. Hóa đơn chi tiết
CREATE TABLE HoaDonChiTiet (
    MaHDCT VARCHAR(10) PRIMARY KEY,
    MaHD VARCHAR(10),
    MaMA VARCHAR(10),
    SL INT,
    DonGia DECIMAL,
    FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD),
    FOREIGN KEY (MaMA) REFERENCES MonAn(MaMA)
);

-- Bảng: LoaiMonAn
INSERT INTO LoaiMonAn (MaLoaiMA, TenLoai) VALUES (N'A', N'Canh chua');
INSERT INTO LoaiMonAn (MaLoaiMA, TenLoai) VALUES (N'B', N'Canh rau');
INSERT INTO LoaiMonAn (MaLoaiMA, TenLoai) VALUES (N'C', N'Cá kho');
INSERT INTO LoaiMonAn (MaLoaiMA, TenLoai) VALUES (N'D', N'Sườn');
INSERT INTO LoaiMonAn (MaLoaiMA, TenLoai) VALUES (N'E', N'Tôm');
INSERT INTO LoaiMonAn (MaLoaiMA, TenLoai) VALUES (N'F', N'Mực');
INSERT INTO LoaiMonAn (MaLoaiMA, TenLoai) VALUES (N'G', N'Gà');
INSERT INTO LoaiMonAn (MaLoaiMA, TenLoai) VALUES (N'H', N'Rau');
INSERT INTO LoaiMonAn (MaLoaiMA, TenLoai) VALUES (N'I', N'Nước');
INSERT INTO LoaiMonAn (MaLoaiMA, TenLoai) VALUES (N'J', N'Lẩu');

-- Bảng: MonAn
INSERT INTO MonAn (MaMA, TenMon, DonGia, DonVi, MaLoaiMA, TinhTrang, Size) VALUES
-- Canh chua
('A1', N'Canh chua cá dứa', 120000, N'Tô',  'A', N'Còn', 1),
('A3', N'Canh chua cá dứa', 180000, N'Tô',  'A', N'Còn', 2),
('A4', N'Canh chua cá điêu hồng', 120000, N'Tô',  'A', N'Còn', 1),
('A5', N'Canh chua cá điêu hồng', 180000, N'Tô',  'A', N'Còn', 2),
('A6', N'Canh chua cá kèo', 120000, N'Tô',  'A', N'Hết', 1),
('A7', N'Canh chua cá kèo', 180000, N'Tô',  'A', N'Hết', 2),
('A8', N'Canh chua cá lóc', 120000, N'Tô',  'A', N'Còn', 1),
('A9', N'Canh chua cá lóc', 180000, N'Tô',  'A', N'Còn', 2),
('A10', N'Canh chua cá bớp', 120000, N'Tô',  'A', N'Còn', 1),
('A11', N'Canh chua cá bớp', 180000, N'Tô',  'A', N'Còn', 2),
('A12', N'Canh chua cá ngát', 180000, N'Tô',  'A', N'Còn', 2),
('A13', N'Canh chua cá ngát', 120000, N'Tô',  'A', N'Còn', 1),

-- Canh rau
('B1', N'Canh cua rau đay', 80000, N'Tô',  'B', N'Còn', 1),
('B2', N'Canh cải thịt xay', 80000, N'Tô',  'B', N'Còn', 1),
('B3', N'Canh bầu nấu tôm', 80000, N'Tô',  'B', N'Còn', 1),
('B4', N'Canh khổ qua', 80000, N'Tô',  'B', N'Còn', 1),

-- Cá kho
('C1', N'Cá dứa kho', 120000, N'Dĩa',  'C', N'Còn', 1),
('C2', N'Cá dứa kho', 180000, N'Dĩa',  'C', N'Còn', 2),
('C3', N'Cá kèo kho', 120000, N'Dĩa',  'C', N'Hết', 1),
('C4', N'Cá kèo kho', 180000, N'Dĩa',  'C', N'Hết', 2),

-- Sườn
('D1', N'Sườn ram mặn', 160000, N'Dĩa',  'D', N'Còn', 1),
('D2', N'Sườn nướng mật ong', 170000, N'Dĩa',  'D', N'Còn', 1),
('D3', N'Sườn chua ngọt', 160000, N'Dĩa',  'D', N'Còn', 1),

-- Tôm
('E1', N'Tôm rim mặn', 180000, N'Dĩa',  'E', N'Còn', 1),
('E2', N'Tôm hấp bia', 200000, N'Dĩa',  'E', N'Còn', 1),
('E3', N'Tôm chiên xù', 190000, N'Dĩa',  'E', N'Còn', 1),

-- Mực
('F1', N'Mực xào chua ngọt', 180000, N'Dĩa',  'F', N'Còn', 1),
('F2', N'Mực nướng sa tế', 190000, N'Dĩa',  'F', N'Còn', 1),
('F3', N'Mực hấp gừng', 180000, N'Dĩa',  'F', N'Còn', 1),

-- Gà
('G1', N'Gà luộc', 150000, N'Dĩa',  'G', N'Còn', 1),
('G2', N'Gà rô ti', 160000, N'Dĩa',  'G', N'Còn', 1),
('G3', N'Gà xào sả ớt', 150000, N'Dĩa',  'G', N'Còn', 1),

-- Rau
('H1', N'Rau muống xào tỏi', 50000, N'Dĩa',  'H', N'Còn', 1),
('H2', N'Rau lang xào tỏi', 50000, N'Dĩa',  'H', N'Còn', 1),

-- Nước
('I1', N'Nước ngọt', 15000, N'Chai',  'I', N'Còn', 1),
('I2', N'Bia', 30000, N'Chai',  'I', N'Còn', 1),
('I3', N'Nước suối', 10000, N'Chai',  'I', N'Còn', 1),

-- Lẩu (bổ sung thêm)
('J1', N'Lẩu hải sản', 300000, N'Nồi',  'J', N'Còn', 1),
('J2', N'Lẩu thái chua cay', 280000, N'Nồi',  'J', N'Còn', 1),
('J3', N'Lẩu cá đuối', 260000, N'Nồi',  'J', N'Còn', 1);

-- Bảng: NhaCungCap
INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, DienThoai) VALUES (N'NCC1', N'Nhà cung cấp 1', N'1 Đường Vũng Tàu', N'0900000000');
INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, DienThoai) VALUES (N'NCC2', N'Nhà cung cấp 2', N'2 Đường Vũng Tàu', N'0900000001');
INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, DienThoai) VALUES (N'NCC3', N'Nhà cung cấp 3', N'3 Đường Vũng Tàu', N'0900000002');
INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, DienThoai) VALUES (N'NCC4', N'Nhà cung cấp 4', N'4 Đường Vũng Tàu', N'0900000003');
INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, DienThoai) VALUES (N'NCC5', N'Nhà cung cấp 5', N'5 Đường Vũng Tàu', N'0900000004');
INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, DienThoai) VALUES (N'NCC6', N'Nhà cung cấp 6', N'6 Đường Vũng Tàu', N'0900000005');
INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, DienThoai) VALUES (N'NCC7', N'Nhà cung cấp 7', N'7 Đường Vũng Tàu', N'0900000006');
INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, DienThoai) VALUES (N'NCC8', N'Nhà cung cấp 8', N'8 Đường Vũng Tàu', N'0900000007');
INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, DienThoai) VALUES (N'NCC9', N'Nhà cung cấp 9', N'9 Đường Vũng Tàu', N'0900000008');
INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, DienThoai) VALUES (N'NCC10', N'Nhà cung cấp 10', N'10 Đường Vũng Tàu', N'0900000009');

-- Bảng: NguyenLieu
INSERT INTO NguyenLieu (MaNL, TenNL, DonVi, TrangThai, MaNCC) VALUES (N'NL1', N'Nguyên liệu 1', N'kg', N'Còn', N'NCC4');
INSERT INTO NguyenLieu (MaNL, TenNL, DonVi, TrangThai, MaNCC) VALUES (N'NL2', N'Nguyên liệu 2', N'kg', N'Hết', N'NCC6');
INSERT INTO NguyenLieu (MaNL, TenNL, DonVi, TrangThai, MaNCC) VALUES (N'NL3', N'Nguyên liệu 3', N'kg', N'Còn', N'NCC2');
INSERT INTO NguyenLieu (MaNL, TenNL, DonVi, TrangThai, MaNCC) VALUES (N'NL4', N'Nguyên liệu 4', N'kg', N'Hết', N'NCC7');
INSERT INTO NguyenLieu (MaNL, TenNL, DonVi, TrangThai, MaNCC) VALUES (N'NL5', N'Nguyên liệu 5', N'kg', N'Còn', N'NCC2');
INSERT INTO NguyenLieu (MaNL, TenNL, DonVi, TrangThai, MaNCC) VALUES (N'NL6', N'Nguyên liệu 6', N'kg', N'Hết', N'NCC6');
INSERT INTO NguyenLieu (MaNL, TenNL, DonVi, TrangThai, MaNCC) VALUES (N'NL7', N'Nguyên liệu 7', N'kg', N'Còn', N'NCC4');
INSERT INTO NguyenLieu (MaNL, TenNL, DonVi, TrangThai, MaNCC) VALUES (N'NL8', N'Nguyên liệu 8', N'kg', N'Hết', N'NCC9');
INSERT INTO NguyenLieu (MaNL, TenNL, DonVi, TrangThai, MaNCC) VALUES (N'NL9', N'Nguyên liệu 9', N'kg', N'Còn', N'NCC3');
INSERT INTO NguyenLieu (MaNL, TenNL, DonVi, TrangThai, MaNCC) VALUES (N'NL10', N'Nguyên liệu 10', N'kg', N'Hết', N'NCC3');

-- Bảng: PhieuNhap
INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNCC, MaNV) VALUES (N'PN1', '2025-07-08', N'NCC6', N'NV1');
INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNCC, MaNV) VALUES (N'PN2', '2025-07-07', N'NCC1', N'NV2');
INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNCC, MaNV) VALUES (N'PN3', '2025-07-06', N'NCC10', N'NV3');
INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNCC, MaNV) VALUES (N'PN4', '2025-07-05', N'NCC1', N'NV4');
INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNCC, MaNV) VALUES (N'PN5', '2025-07-04', N'NCC3', N'NV5');
INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNCC, MaNV) VALUES (N'PN6', '2025-07-03', N'NCC10', N'NV6');
INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNCC, MaNV) VALUES (N'PN7', '2025-07-02', N'NCC5', N'NV7');
INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNCC, MaNV) VALUES (N'PN8', '2025-07-01', N'NCC2', N'NV8');
INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNCC, MaNV) VALUES (N'PN9', '2025-06-30', N'NCC6', N'NV9');
INSERT INTO PhieuNhap (MaPN, NgayNhap, MaNCC, MaNV) VALUES (N'PN10', '2025-06-29', N'NCC7', N'NV10');

-- Bảng: ChiTietPN
INSERT INTO ChiTietPN (MaCTPN, MaPN, MaNL, SL, DonGia) VALUES (N'CTPN1', N'PN3', N'NL7', '1.79', '50000');
INSERT INTO ChiTietPN (MaCTPN, MaPN, MaNL, SL, DonGia) VALUES (N'CTPN2', N'PN5', N'NL1', '1.96', '100000');
INSERT INTO ChiTietPN (MaCTPN, MaPN, MaNL, SL, DonGia) VALUES (N'CTPN3', N'PN6', N'NL2', '2.52', '70000');
INSERT INTO ChiTietPN (MaCTPN, MaPN, MaNL, SL, DonGia) VALUES (N'CTPN4', N'PN10', N'NL8', '2.71', '70000');
INSERT INTO ChiTietPN (MaCTPN, MaPN, MaNL, SL, DonGia) VALUES (N'CTPN5', N'PN5', N'NL10', '1.96', '100000');
INSERT INTO ChiTietPN (MaCTPN, MaPN, MaNL, SL, DonGia) VALUES (N'CTPN6', N'PN8', N'NL4', '1.04', '100000');
INSERT INTO ChiTietPN (MaCTPN, MaPN, MaNL, SL, DonGia) VALUES (N'CTPN7', N'PN6', N'NL2', '1.57', '100000');
INSERT INTO ChiTietPN (MaCTPN, MaPN, MaNL, SL, DonGia) VALUES (N'CTPN8', N'PN7', N'NL7', '1.66', '70000');
INSERT INTO ChiTietPN (MaCTPN, MaPN, MaNL, SL, DonGia) VALUES (N'CTPN9', N'PN6', N'NL2', '2.39', '50000');
INSERT INTO ChiTietPN (MaCTPN, MaPN, MaNL, SL, DonGia) VALUES (N'CTPN10', N'PN2', N'NL1', '4.35', '50000');

-- Bảng: NguyenLieu_MonAn
INSERT INTO NguyenLieu_MonAn (MaMA, MaNL, SoLuong, DonVi) VALUES (N'A9', N'NL10', '0.23', N'kg');
INSERT INTO NguyenLieu_MonAn (MaMA, MaNL, SoLuong, DonVi) VALUES (N'A5', N'NL4', '0.7', N'kg');
INSERT INTO NguyenLieu_MonAn (MaMA, MaNL, SoLuong, DonVi) VALUES (N'A3', N'NL10', '0.59', N'kg');
INSERT INTO NguyenLieu_MonAn (MaMA, MaNL, SoLuong, DonVi) VALUES (N'A10', N'NL2', '0.76', N'kg');
INSERT INTO NguyenLieu_MonAn (MaMA, MaNL, SoLuong, DonVi) VALUES (N'A4', N'NL6', '0.82', N'kg');
INSERT INTO NguyenLieu_MonAn (MaMA, MaNL, SoLuong, DonVi) VALUES (N'A6', N'NL4', '0.23', N'kg');
INSERT INTO NguyenLieu_MonAn (MaMA, MaNL, SoLuong, DonVi) VALUES (N'A8', N'NL1', '0.2', N'kg');
INSERT INTO NguyenLieu_MonAn (MaMA, MaNL, SoLuong, DonVi) VALUES (N'A4', N'NL8', '0.66', N'kg');
INSERT INTO NguyenLieu_MonAn (MaMA, MaNL, SoLuong, DonVi) VALUES (N'A1', N'NL2', '0.63', N'kg');


-- Dữ liệu mẫu cho bảng NhanVien
INSERT INTO NhanVien (MaNV, HoTen, GioiTinh, NgaySinh, DienThoai, [User], [Pass], ChucVu) VALUES
('NV1', N'Nguyễn Văn A', N'Nam', '1990-01-01', '0901111111', 'user1', 'pass1', N'Quản lý'),
('NV2', N'Trần Thị B', N'Nữ', '1992-02-02', '0902222222', 'user2', 'pass2', N'Nhân viên'),
('NV3', N'Lê Văn C', N'Nam', '1993-03-03', '0903333333', 'user3', 'pass3', N'Nhân viên'),
('NV4', N'Phạm Thị D', N'Nữ', '1994-04-04', '0904444444', 'user4', 'pass4', N'Phục vụ'),
('NV5', N'Huỳnh Văn E', N'Nam', '1995-05-05', '0905555555', 'user5', 'pass5', N'Phục vụ'),
('NV6', N'Võ Thị F', N'Nữ', '1996-06-06', '0906666666', 'user6', 'pass6', N'Thu ngân'),
('NV7', N'Đỗ Văn G', N'Nam', '1997-07-07', '0907777777', 'user7', 'pass7', N'Thu ngân'),
('NV8', N'Ngô Thị H', N'Nữ', '1998-08-08', '0908888888', 'user8', 'pass8', N'Bếp trưởng'),
('NV9', N'Bùi Văn I', N'Nam', '1999-09-09', '0909999999', 'user9', 'pass9', N'Bếp phụ'),
('NV10', N'Lý Thị J', N'Nữ', '1991-10-10', '0901010101', 'user10', 'pass10', N'Bếp phụ');

-- Dữ liệu mẫu cho bảng Ban
INSERT INTO Ban (MaBan, TenBan, TrangThai) VALUES
('B01', N'Bàn 1', N'Trống'),
('B02', N'Bàn 2', N'Có khách'),
('B03', N'Bàn 3', N'Trống'),
('B04', N'Bàn 4', N'Đặt trước'),
('B05', N'Bàn 5', N'Có khách'),
('B06', N'Bàn 6', N'Trống'),
('B07', N'Bàn 7', N'Trống'),
('B08', N'Bàn 8', N'Có khách'),
('B09', N'Bàn 9', N'Đặt trước'),
('B10', N'Bàn 10', N'Trống');

-- Dữ liệu mẫu cho bảng KhuyenMai
INSERT INTO KhuyenMai (MaKM, TenKM, NgayBatDau, NgayKetThuc, GiamGia) VALUES
('KM01', N'Giảm 10% món lẩu', '2025-07-01', '2025-07-31', 0.1),
('KM02', N'Ưu đãi hè', '2025-06-01', '2025-08-31', 0.15),
('KM03', N'Thứ 2 vui vẻ', '2025-07-01', '2025-07-31', 0.2),
('KM04', N'Mua 1 tặng 1', '2025-07-10', '2025-07-20', 0.5),
('KM05', N'Khuyến mãi tôm', '2025-07-05', '2025-07-25', 0.1),
('KM06', N'Combo gia đình', '2025-07-01', '2025-07-31', 0.25),
('KM07', N'Happy Hour', '2025-07-01', '2025-07-31', 0.3),
('KM08', N'Khuyến mãi cuối tuần', '2025-07-01', '2025-07-31', 0.2),
('KM09', N'Giảm cho hóa đơn trên 500k', '2025-07-01', '2025-07-31', 0.1),
('KM10', N'Sinh nhật khách hàng', '2025-07-01', '2025-07-31', 0.2);

-- Dữ liệu mẫu cho bảng HoaDon
INSERT INTO HoaDon (MaHD, NgayLap, MaNV, MaBan, MaKM) VALUES
('HD01', '2025-07-01', 'NV1', 'B01', 'KM01'),
('HD02', '2025-07-02', 'NV2', 'B02', 'KM02'),
('HD03', '2025-07-03', 'NV3', 'B03', 'KM03'),
('HD04', '2025-07-04', 'NV4', 'B04', 'KM04'),
('HD05', '2025-07-05', 'NV5', 'B05', 'KM05'),
('HD06', '2025-07-06', 'NV6', 'B06', 'KM06'),
('HD07', '2025-07-07', 'NV7', 'B07', 'KM07'),
('HD08', '2025-07-08', 'NV8', 'B08', 'KM08'),
('HD09', '2025-07-09', 'NV9', 'B09', 'KM09'),
('HD10', '2025-07-10', 'NV10', 'B10', 'KM10');

-- Dữ liệu mẫu cho bảng HoaDonChiTiet
INSERT INTO HoaDonChiTiet (MaHDCT, MaHD, MaMA, SL, DonGia) VALUES
('HDCT01', 'HD01', 'A1', 2, 120000),
('HDCT02', 'HD02', 'B1', 1, 80000),
('HDCT03', 'HD03', 'C1', 3, 120000),
('HDCT04', 'HD04', 'D1', 1, 160000),
('HDCT05', 'HD05', 'E1', 2, 180000),
('HDCT06', 'HD06', 'F1', 1, 180000),
('HDCT07', 'HD07', 'G1', 2, 150000),
('HDCT08', 'HD08', 'H1', 1, 50000),
('HDCT09', 'HD09', 'I1', 1, 15000),
('HDCT10', 'HD10', 'J1', 2, 300000);


