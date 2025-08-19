/* ====== RESET DATABASE ====== */
USE master;
IF DB_ID(N'RestaurantDB') IS NOT NULL
BEGIN
    ALTER DATABASE RestaurantDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE RestaurantDB;
END
GO

CREATE DATABASE RestaurantDB;
GO
USE RestaurantDB;
GO

/* ====== SCHEMA ====== */

-- Bảng loại món ăn
CREATE TABLE dbo.Category (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL
);

-- Bảng món ăn
CREATE TABLE dbo.Dish (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL,
    UnitPrice DECIMAL(18,2) NOT NULL,
    Unit NVARCHAR(50) NOT NULL,
    CategoryId INT NOT NULL,
    Status INT NOT NULL, -- 0: Còn, 1: Hết
    Size INT NOT NULL,   -- 1: nhỏ, 2: lớn
    ImageUrl NVARCHAR(500) NULL
    CONSTRAINT FK_Dish_Category FOREIGN KEY (CategoryId) REFERENCES dbo.Category(Id)
);


-- Bảng nhà cung cấp
CREATE TABLE dbo.Supplier (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL,
    Address NVARCHAR(255) NULL,
    Phone NVARCHAR(15) NULL
);

-- Bảng nguyên liệu
CREATE TABLE dbo.Ingredient (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL,
    Quantity DECIMAL(18,3) NOT NULL, -- tồn kho
    Unit NVARCHAR(20) NOT NULL,
    Status INT NOT NULL, -- 0: Còn, 1: Hết
    SupplierId INT NOT NULL,
    CONSTRAINT FK_Ingredient_Supplier FOREIGN KEY (SupplierId) REFERENCES dbo.Supplier(Id),
    CONSTRAINT CK_Ingredient_Qty_NonNeg CHECK (Quantity >= 0)
);

-- Bảng nhân viên
CREATE TABLE dbo.Employee (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    FullName NVARCHAR(100) NOT NULL,
    Gender INT NOT NULL,     -- 0: Nam, 1: Nữ
    BirthDate DATE NOT NULL,
    Phone NVARCHAR(15) NULL,
    Username NVARCHAR(50) NOT NULL,
    [Password] NVARCHAR(50) NOT NULL,
    Position NVARCHAR(50) NOT NULL,
    Role INT NOT NULL        -- 0: user, 1: admin...
);

-- Phiếu nhập
CREATE TABLE dbo.ImportReceipt (
    Id BIGINT IDENTITY(1000,1) PRIMARY KEY,
    ImportDate DATE NOT NULL,
    SupplierId INT NOT NULL,
    EmployeeId INT NOT NULL,
    CONSTRAINT FK_ImportReceipt_Supplier FOREIGN KEY (SupplierId) REFERENCES dbo.Supplier(Id),
    CONSTRAINT FK_ImportReceipt_Employee FOREIGN KEY (EmployeeId) REFERENCES dbo.Employee(Id)
);

-- Chi tiết phiếu nhập
CREATE TABLE dbo.ImportDetail (
    Id BIGINT IDENTITY(100000,1) PRIMARY KEY,
    ImportReceiptId BIGINT NOT NULL,
    IngredientId INT NOT NULL,
    Quantity DECIMAL(18,3) NOT NULL,
    Unit NVARCHAR(50) NOT NULL,
    UnitPrice DECIMAL(18,2) NOT NULL,
    Total AS (Quantity * UnitPrice) PERSISTED,
    CONSTRAINT FK_ImportDetail_Receipt FOREIGN KEY (ImportReceiptId) REFERENCES dbo.ImportReceipt(Id),
    CONSTRAINT FK_ImportDetail_Ingredient FOREIGN KEY (IngredientId) REFERENCES dbo.Ingredient(Id),
    CONSTRAINT CK_ImportDetail_Qty_Pos CHECK (Quantity > 0),
    CONSTRAINT CK_ImportDetail_Price_Pos CHECK (UnitPrice >= 0)
);

-- Món - Nguyên liệu
CREATE TABLE dbo.DishIngredient (
    DishId INT NOT NULL,
    IngredientId INT NOT NULL,
    Quantity DECIMAL(18,3) NOT NULL,
    Unit NVARCHAR(20) NOT NULL,
    CONSTRAINT PK_DishIngredient PRIMARY KEY (DishId, IngredientId),
    CONSTRAINT FK_DI_Dish FOREIGN KEY (DishId) REFERENCES dbo.Dish(Id),
    CONSTRAINT FK_DI_Ingredient FOREIGN KEY (IngredientId) REFERENCES dbo.Ingredient(Id),
    CONSTRAINT CK_DI_Qty_Pos CHECK (Quantity > 0)
);

-- Bàn ăn
CREATE TABLE dbo.DiningTable (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(50) NOT NULL,
    Status INT NOT NULL -- 0: Trống, 1: Có khách, 2: Đặt trước
);

-- Khuyến mãi
CREATE TABLE dbo.Promotion (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    DiscountRate DECIMAL(5,4) NOT NULL, -- 0.1000 = 10%
    CONSTRAINT CK_Promo_Rate CHECK (DiscountRate BETWEEN 0 AND 1),
    CONSTRAINT CK_Promo_Dates CHECK (StartDate <= EndDate)
);

-- Hóa đơn (CheckOut & PromotionId CHO PHÉP NULL)
CREATE TABLE dbo.Bill (
    Id BIGINT IDENTITY(10000,1) PRIMARY KEY,
    CreatedDate DATE NOT NULL
        CONSTRAINT DF_Bill_CreatedDate DEFAULT (CONVERT(date, GETDATE())),
    CheckIn DATETIME NOT NULL
        CONSTRAINT DF_Bill_CheckIn DEFAULT (GETDATE()),
    CheckOut DATETIME NULL,
    Status INT NOT NULL
        CONSTRAINT DF_Bill_Status DEFAULT (0), -- 0: đang dùng, 1: đã thanh toán, 2: huỷ
    EmployeeId INT NOT NULL,
    TableId INT NOT NULL,
    PromotionId INT NULL,
    CONSTRAINT FK_Bill_Employee FOREIGN KEY (EmployeeId) REFERENCES dbo.Employee(Id),
    CONSTRAINT FK_Bill_Table FOREIGN KEY (TableId) REFERENCES dbo.DiningTable(Id),
    CONSTRAINT FK_Bill_Promotion FOREIGN KEY (PromotionId) REFERENCES dbo.Promotion(Id)
);

-- Chi tiết hóa đơn
CREATE TABLE dbo.BillDetail (
    Id BIGINT IDENTITY(100000,1) PRIMARY KEY,
    BillId BIGINT NOT NULL,
    DishId INT NOT NULL,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(18,2) NOT NULL,
    CONSTRAINT FK_BillDetail_Bill FOREIGN KEY (BillId) REFERENCES dbo.Bill(Id),
    CONSTRAINT FK_BillDetail_Dish FOREIGN KEY (DishId) REFERENCES dbo.Dish(Id),
    CONSTRAINT CK_BillDetail_Qty_Pos CHECK (Quantity > 0),
    CONSTRAINT CK_BillDetail_Price_Pos CHECK (UnitPrice >= 0)
);
GO

/* ====== SEED DATA ====== */

INSERT INTO dbo.Category (Name) VALUES 
(N'Canh chua'),(N'Canh rau'),(N'Cá kho'),(N'Sườn'),(N'Tôm'),
(N'Mực'),(N'Gà'),(N'Rau'),(N'Nước'),(N'Lẩu');

INSERT INTO dbo.Dish (Name, UnitPrice, Unit, CategoryId, Status, Size, ImageUrl) VALUES
(N'Canh chua cá dứa', 120000, N'Tô', 1, 0, 1, N'canh-chua-ca-dua.png'),
(N'Canh chua cá dứa', 180000, N'Tô', 1, 0, 2, N'canh-chua-ca-dua.png'),
(N'Canh chua cá điêu hồng', 120000, N'Tô', 1, 0, 1, N'canh-chua-ca-dieu-hong.png'),
(N'Canh chua cá điêu hồng', 180000, N'Tô', 1, 0, 2, N'canh-chua-ca-dieu-hong.png'),
(N'Canh chua cá kèo', 120000, N'Tô', 1, 1, 1, N'canh-chua-ca-keo.png'),
(N'Canh chua cá kèo', 180000, N'Tô', 1, 1, 2, N'canh-chua-ca-keo.png'),
(N'Canh chua cá lóc', 120000, N'Tô', 1, 0, 1, N'canh-chua-ca-loc.png'),
(N'Canh chua cá lóc', 180000, N'Tô', 1, 0, 2, N'canh-chua-ca-loc.png'),
(N'Canh chua cá bớp', 120000, N'Tô', 1, 0, 1, N'canh-chua-ca-bop.png'),
(N'Canh chua cá bớp', 180000, N'Tô', 1, 0, 2, N'canh-chua-ca-bop.png'),
(N'Canh chua cá ngát', 180000, N'Tô', 1, 0, 2, N'canh-chua-ca-ngat.png'),
(N'Canh chua cá ngát', 120000, N'Tô', 1, 0, 1, N'canh-chua-ca-ngat.png'),
(N'Canh cua rau đay', 80000, N'Tô', 2, 0, 1, N'canh-cua-rau-day.png'),
(N'Canh cải thịt xay', 80000, N'Tô', 2, 0, 1, N'canh-cai-thit-xay.png'),
(N'Canh bầu nấu tôm', 80000, N'Tô', 2, 0, 1, N'canh-bau-nau-tom.png'),
(N'Canh khổ qua', 80000, N'Tô', 2, 0, 1, N'canh-kho-qua.png'),
(N'Cá dứa kho', 120000, N'Dĩa', 3, 0, 1, N'ca-dua-kho.png'),
(N'Cá dứa kho', 180000, N'Dĩa', 3, 0, 2, N'ca-dua-kho.png'),
(N'Cá kèo kho', 120000, N'Dĩa', 3, 1, 1, N'ca-keo-kho.png'),
(N'Cá kèo kho', 180000, N'Dĩa', 3, 1, 2, N'ca-keo-kho.png'),
(N'Sườn ram mặn', 160000, N'Dĩa', 4, 0, 1, N'suon-ram-man.png'),
(N'Sườn nướng mật ong', 170000, N'Dĩa', 4, 0, 1, N'suon-nuong-mat-ong.png'),
(N'Sườn chua ngọt', 160000, N'Dĩa', 4, 0, 1, N'suon-chua-ngot.png'),
(N'Tôm rim mặn', 180000, N'Dĩa', 5, 0, 1, N'tom-rim-man.png'),
(N'Tôm hấp bia', 200000, N'Dĩa', 5, 0, 1, N'tom-hap-bia.png'),
(N'Tôm chiên xù', 190000, N'Dĩa', 5, 0, 1, N'tom-chien-xu.png'),
(N'Mực xào chua ngọt', 180000, N'Dĩa', 6, 0, 1, N'muc-xao-chua-ngot.png'),
(N'Mực nướng sa tế', 190000, N'Dĩa', 6, 0, 1, N'muc-nuong-sa-te.png'),
(N'Mực hấp gừng', 180000, N'Dĩa', 6, 0, 1, N'muc-hap-gung.png'),
(N'Gà luộc', 150000, N'Dĩa', 7, 0, 1, N'ga-luoc.png'),
(N'Gà rô ti', 160000, N'Dĩa', 7, 0, 1, N'ga-ro-ti.png'),
(N'Gà xào sả ớt', 150000, N'Dĩa', 7, 0, 1, N'ga-xao-sa-ot.png'),
(N'Rau muống xào tỏi', 50000, N'Dĩa', 8, 0, 1, N'rau-muong-xao-toi.png'),
(N'Rau lang xào tỏi', 50000, N'Dĩa', 8, 0, 1, N'rau-lang-xao-toi.png'),
(N'Nước ngọt', 15000, N'Chai', 9, 0, 1, N'nuoc-ngot.png'),
(N'Bia', 30000, N'Chai', 9, 0, 1, N'bia.png'),
(N'Nước suối', 10000, N'Chai', 9, 0, 1, N'nuoc-suoi.png'),
(N'Lẩu hải sản', 300000, N'Nồi', 10, 0, 1, N'lau-hai-san.png'),
(N'Lẩu thái chua cay', 280000, N'Nồi', 10, 0, 1, N'lau-thai-chua-cay.png'),
(N'Lẩu cá đuối', 260000, N'Nồi', 10, 0, 1, N'lau-ca-duoi.png');


INSERT INTO dbo.Supplier (Name, Address, Phone) VALUES
(N'Công ty thực phẩm An Khang', N'123 Lý Thường Kiệt, Hà Nội', '0909123456'),
(N'Công ty Hải sản Biển Đông', N'456 Nguyễn Văn Cừ, Hải Phòng', '0912345678'),
(N'Công ty Rau sạch Việt', N'789 Trần Hưng Đạo, Đà Lạt', '0987654321'),
(N'NCC Gia vị Phương Đông', N'12 Hoàng Hoa Thám, Huế', '0938123456'),
(N'Công ty TNHH Tươi Ngon', N'34 Nguyễn Trãi, TP.HCM', '0909898989'),
(N'Cửa hàng Gạo sạch Minh Phú', N'88 Hai Bà Trưng, Hà Nội', '0911222333'),
(N'NCC Thực phẩm Quang Vinh', N'101 Lê Lợi, Đà Nẵng', '0923456789'),
(N'Nhà cung cấp Gia vị Ngon', N'78 Lê Văn Sỹ, TP.HCM', '0966334455'),
(N'Công ty Hải sản Xanh', N'222 Trường Sa, Nha Trang', '0919988776'),
(N'Cửa hàng Thực phẩm Lành Mạnh', N'9 Nguyễn Huệ, Cần Thơ', '0944332211');

INSERT INTO dbo.Ingredient (Name, Quantity, Unit, Status, SupplierId) VALUES
(N'Cá dứa', 10, N'kg', 0, 9),
(N'Cá điêu hồng', 10, N'kg', 0, 9),
(N'Cá kèo', 10, N'kg', 0, 9),
(N'Cá lóc', 10, N'kg', 0, 9),
(N'Cá bớp', 10, N'kg', 0, 9),
(N'Cá ngát', 10, N'kg', 0, 9),
(N'Cua đồng', 10, N'kg', 0, 2),
(N'Rau đay', 10, N'kg', 0, 3),
(N'Thịt xay', 10, N'kg', 0, 1),
(N'Bầu', 10, N'kg', 0, 3),
(N'Tôm tươi', 10, N'kg', 0, 2),
(N'Khổ qua', 10, N'kg', 0, 3),
(N'Sườn heo', 10, N'kg', 0, 1),
(N'Tôm sú', 10, N'kg', 0, 2),
(N'Mực tươi', 10, N'kg', 0, 2),
(N'Thịt gà ta', 10, N'kg', 0, 1),
(N'Rau muống', 10, N'kg', 0, 3),
(N'Rau lang', 10, N'kg', 0, 3),
(N'Nước ngọt đóng chai', 10, N'lít', 0, 8),
(N'Bia chai', 10, N'lít', 0, 8),
(N'Nước suối đóng chai', 10, N'lít', 0, 8),
(N'Hải sản tổng hợp', 10, N'kg', 0, 2),
(N'Gia vị lẩu thái', 10, N'kg', 0, 4),
(N'Cá đuối', 10, N'kg', 0, 9);

INSERT INTO dbo.Employee (FullName, Gender, BirthDate, Phone, Username, [Password], Position, Role) VALUES
(N'Nguyễn Văn A', 0, '1990-01-01', '0901234567', 'nguyenvana', '123456', N'Quản lý', 1),
(N'Trần Thị B', 1, '1992-02-02', '0912345678', 'tranthib', 'abcdef', N'Phục vụ', 0),
(N'Lê Văn C', 0, '1985-03-03', '0923456789', 'levanc', 'pass123', N'Thu ngân', 0),
(N'Phạm Thị D', 1, '1995-04-04', '0934567890', 'phamthid', 'hello123', N'Phục vụ', 0),
(N'Hoàng Văn E', 0, '1988-05-05', '0945678901', 'hoangvane', 'e12345', N'Đầu bếp', 0),
(N'Vũ Thị F', 1, '1991-06-06', '0956789012', 'vuthif', 'fpass', N'Phục vụ', 0),
(N'Đặng Văn G', 0, '1993-07-07', '0967890123', 'dangvang', 'gpass', N'Thu ngân', 0),
(N'Bùi Thị H', 1, '1994-08-08', '0978901234', 'buithih', 'h12345', N'Đầu bếp', 0),
(N'Tô Văn I', 0, '1987-09-09', '0989012345', 'tovani', 'ipass', N'Phục vụ', 0),
(N'Ngô Thị J', 1, '1996-10-10', '0990123456', 'ngothij', 'jpass', N'Quản lý', 1);

INSERT INTO dbo.ImportReceipt (ImportDate, SupplierId, EmployeeId) VALUES
('2025-07-01', 1, 1),('2025-07-02', 2, 2),('2025-07-03', 3, 3),('2025-07-04', 4, 4),
('2025-07-05', 5, 5),('2025-07-06', 6, 6),('2025-07-07', 7, 7),('2025-07-08', 8, 8),
('2025-07-09', 9, 9),('2025-07-10', 10, 10);

INSERT INTO dbo.ImportDetail (ImportReceiptId, IngredientId, Quantity, Unit, UnitPrice) VALUES
(1000, 1, 5, N'kg', 200000),(1001, 2, 10, N'kg', 15000),(1002, 3, 2, N'kg', 25000),
(1003, 4, 3, N'lít', 40000),(1004, 5, 10, N'kg', 18000),(1005, 6, 20, N'kg', 12000),
(1006, 7, 1, N'kg', 30000),(1007, 8, 7, N'kg', 250000),(1008, 9, 5, N'kg', 150000),
(1009, 10, 3, N'kg', 300000);

INSERT INTO dbo.DiningTable (Name, Status) VALUES
(N'Bàn 1',0),(N'Bàn 2',0),(N'Bàn 3',0),(N'Bàn 4',0),(N'Bàn 5',0),
(N'Bàn 6',0),(N'Bàn 7',0),(N'Bàn 8',0),(N'Bàn 9',0),(N'Bàn 10',0),
(N'Bàn 11',0),(N'Bàn 12',0),(N'Vip 1',0), (N'Vip 2',0);

INSERT INTO dbo.Promotion (Name, StartDate, EndDate, DiscountRate) VALUES
(N'Giảm giá mùa hè','2025-06-01','2025-08-31',0.10),
(N'Khuyến mãi 1/6','2025-06-01','2025-06-01',0.15),
(N'Mừng Quốc Khánh','2025-09-02','2025-09-05',0.20),
(N'Ưu đãi thứ 6','2025-08-01','2025-08-31',0.05),
(N'Combo tiết kiệm','2025-07-15','2025-09-15',0.12),
(N'Giảm giá học sinh','2025-08-01','2025-12-31',0.08),
(N'Happy Hour','2025-08-01','2025-08-31',0.25),
(N'Tặng món tráng miệng','2025-07-01','2025-07-31',0.10),
(N'Ưu đãi đặt bàn online','2025-08-01','2025-09-01',0.07),
(N'Miễn phí vận chuyển','2025-07-20','2025-08-20',0.05);

INSERT INTO dbo.Bill (CreatedDate, CheckIn, CheckOut, Status, EmployeeId, TableId, PromotionId) VALUES
('2025-07-10','2025-07-10 18:00','2025-07-10 19:30',1,1,1,1),
('2025-07-11','2025-07-11 12:00','2025-07-11 13:00',1,2,2,2),
('2025-07-12','2025-07-12 19:00','2025-07-12 20:00',1,3,3,3),
('2025-07-13','2025-07-13 18:30','2025-07-13 19:15',1,4,4,4),
('2025-07-14','2025-07-14 17:45','2025-07-14 18:30',1,5,5,5),
('2025-07-15','2025-07-15 11:00','2025-07-15 12:00',1,6,6,6),
('2025-07-16','2025-07-16 14:00','2025-07-16 15:00',1,7,7,7),
('2025-07-17','2025-07-17 16:00','2025-07-17 17:00',1,8,8,8),
('2025-07-18','2025-07-18 13:30','2025-07-18 14:30',1,9,9,9),
('2025-07-19','2025-07-19 15:45','2025-07-19 16:30',1,10,10,10);

-- Ví dụ BillDetail
INSERT INTO dbo.BillDetail (BillId, DishId, Quantity, UnitPrice) VALUES
(10000,1,2,50000),(10001,2,1,80000),(10002,3,3,60000),(10003,4,2,70000),
(10004,5,1,90000),(10005,6,1,100000),(10006,7,2,75000),(10007,8,1,85000),
(10008,9,2,65000),(10009,10,1,95000);

-- Món - Nguyên liệu
INSERT INTO dbo.DishIngredient (DishId, IngredientId, Quantity, Unit) VALUES
(1,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá dứa'),0.200,N'kg'),
(2,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá dứa'),0.300,N'kg'),
(3,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá điêu hồng'),0.200,N'kg'),
(4,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá điêu hồng'),0.300,N'kg'),
(5,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá kèo'),0.200,N'kg'),
(6,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá kèo'),0.300,N'kg'),
(7,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá lóc'),0.200,N'kg'),
(8,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá lóc'),0.300,N'kg'),
(9,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá bớp'),0.200,N'kg'),
(10,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá bớp'),0.300,N'kg'),
(11,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá ngát'),0.300,N'kg'),
(12,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá ngát'),0.200,N'kg'),
(13,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cua đồng'),0.200,N'kg'),
(14,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Thịt xay'),0.200,N'kg'),
(15,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Tôm tươi'),0.200,N'kg'),
(16,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Khổ qua'),0.200,N'kg'),
(17,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá dứa'),0.200,N'kg'),
(18,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá dứa'),0.300,N'kg'),
(19,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá kèo'),0.200,N'kg'),
(20,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá kèo'),0.300,N'kg'),
(21,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Sườn heo'),0.200,N'kg'),
(22,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Sườn heo'),0.200,N'kg'),
(23,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Sườn heo'),0.200,N'kg'),
(24,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Tôm sú'),0.200,N'kg'),
(25,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Tôm sú'),0.200,N'kg'),
(26,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Tôm sú'),0.200,N'kg'),
(27,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Mực tươi'),0.200,N'kg'),
(28,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Mực tươi'),0.200,N'kg'),
(29,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Mực tươi'),0.200,N'kg'),
(30,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Thịt gà ta'),0.200,N'kg'),
(31,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Thịt gà ta'),0.200,N'kg'),
(32,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Thịt gà ta'),0.200,N'kg'),
(33,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Rau muống'),0.200,N'kg'),
(34,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Rau lang'),0.200,N'kg'),
(35,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Nước ngọt đóng chai'),0.200,N'lít'),
(36,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Bia chai'),0.200,N'lít'),
(37,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Nước suối đóng chai'),0.200,N'lít'),
(38,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Hải sản tổng hợp'),0.300,N'kg'),
(39,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Gia vị lẩu thái'),0.300,N'kg'),
(40,(SELECT Id FROM dbo.Ingredient WHERE Name=N'Cá đuối'),0.300,N'kg');
GO

/* ====== PROGRAMMABILITY ====== */

-- Proc thêm món vào hóa đơn + trừ kho
IF OBJECT_ID('dbo.AddDishToBill', 'P') IS NOT NULL
    DROP PROCEDURE dbo.AddDishToBill;
GO
CREATE PROCEDURE dbo.AddDishToBill
    @BillId BIGINT,
    @DishId INT,
    @Quantity INT,
    @UnitPrice DECIMAL(18,2)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        -- 1) Thêm chi tiết hóa đơn
        INSERT INTO dbo.BillDetail (BillId, DishId, Quantity, UnitPrice)
        VALUES (@BillId, @DishId, @Quantity, @UnitPrice);

        -- 2) Trừ tồn kho theo định mức
        UPDATE i
        SET i.Quantity = i.Quantity - x.TotalUsed
        FROM dbo.Ingredient AS i
        JOIN (
            SELECT di.IngredientId, SUM(di.Quantity * @Quantity) AS TotalUsed
            FROM dbo.DishIngredient di
            WHERE di.DishId = @DishId
            GROUP BY di.IngredientId
        ) AS x ON i.Id = x.IngredientId;

        -- 3) Cập nhật trạng thái những nguyên liệu liên quan
        UPDATE i
        SET i.Status = CASE WHEN i.Quantity <= 0 THEN 1 ELSE 0 END
        FROM dbo.Ingredient i
        WHERE i.Id IN (SELECT IngredientId FROM dbo.DishIngredient WHERE DishId = @DishId);

        COMMIT;
    END TRY
    BEGIN CATCH
        IF XACT_STATE() <> 0 ROLLBACK;
        THROW;
    END CATCH
END;
GO

-- Trigger đồng bộ trạng thái khi bất kỳ cập nhật tồn kho nào xảy ra
IF OBJECT_ID('dbo.trg_UpdateIngredientStatus', 'TR') IS NOT NULL
    DROP TRIGGER dbo.trg_UpdateIngredientStatus;
GO
CREATE TRIGGER dbo.trg_UpdateIngredientStatus
ON dbo.Ingredient
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE i
    SET i.Status = CASE WHEN i.Quantity <= 0 THEN 1 ELSE 0 END
    FROM dbo.Ingredient i
    JOIN inserted ins ON i.Id = ins.Id;
END;
GO
