-- Xóa và tạo mới database
USE master
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'QuanLyNhatro_SGS')
BEGIN
    ALTER DATABASE QuanLyNhatro_SGS SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QuanLyNhatro_SGS;
END
GO

CREATE DATABASE QuanLyNhatro_SGS;
GO

USE QuanLyNhatro_SGS;
GO

-- Bảng chi nhánh
CREATE TABLE CHI_NHANH (
    ID_ChiNhanh INT PRIMARY KEY IDENTITY(1,1),
    diaChi NVARCHAR(255) NOT NULL,
    giaDien DECIMAL(10,2) NOT NULL,
    giaNuoc DECIMAL(10,2) NOT NULL,
    tenChiNhanh NVARCHAR(50) NOT NULL
);

-- Bảng phòng
CREATE TABLE PHONG (
    ID_Phong INT PRIMARY KEY IDENTITY(1,1),
    giaPhong DECIMAL(10,2) NOT NULL,
    trangThai BIT NOT NULL,
    soPhong NVARCHAR(20) NOT NULL,
    moTa NVARCHAR(255),
    hinhAnh NVARCHAR(255),
    ID_ChiNhanh INT FOREIGN KEY REFERENCES CHI_NHANH(ID_ChiNhanh)
);

-- Bảng người dùng
CREATE TABLE NGUOI_DUNG (
    ID_NguoiDung INT PRIMARY KEY IDENTITY(1,1),
    hoVaTen NVARCHAR(100) NOT NULL,
    matKhau NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL,
    sdt VARCHAR(12) NOT NULL,
    soCCCD VARCHAR(12) NOT NULL,
    trangThai BIT NOT NULL,
    gioiTinh BIT NOT NULL,
    queQuan NVARCHAR(100),
    ngaySinh DATE,
    ID_Phong INT FOREIGN KEY REFERENCES PHONG(ID_Phong)
);

-- Bảng hợp đồng
CREATE TABLE HOP_DONG (
    ID_HopDong INT PRIMARY KEY IDENTITY(1,1), 
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,
    soTienCoc DECIMAL(10,2) NOT NULL,
    ID_NguoiDung INT FOREIGN KEY REFERENCES NGUOI_DUNG(ID_NguoiDung),
    ID_Phong INT FOREIGN KEY REFERENCES PHONG(ID_Phong),
    ID_ChiNhanh INT FOREIGN KEY REFERENCES CHI_NHANH(ID_ChiNhanh)
);

-- Bảng hóa đơn
CREATE TABLE HOA_DON (
    ID_HoaDon INT PRIMARY KEY IDENTITY(1,1),
    soDienMoi INT NOT NULL,
    soNuocMoi INT NOT NULL,
    soDienCu INT NOT NULL,
    soNuocCu INT NOT NULL,
    tienDien DECIMAL(10,2) NOT NULL,
    tienNuoc DECIMAL(10,2) NOT NULL,
    tienPhong DECIMAL(10,2) NOT NULL,
    tongTien DECIMAL(10,2) NOT NULL,
    trangThaiThanhToan BIT NOT NULL,
    ngayThanhToan DATE,
    ngayTao DATETIME DEFAULT GETDATE(),
    ID_HopDong INT FOREIGN KEY REFERENCES HOP_DONG(ID_HopDong) ON DELETE CASCADE
);

-- Bảng số điện nước
CREATE TABLE SODIENNUOC (
    ID_DienNuoc INT PRIMARY KEY IDENTITY(1,1),       
    thang INT NOT NULL,                      
    nam INT NOT NULL,                         
    soDien INT NOT NULL,                      
    soNuoc INT NOT NULL,                      
    ID_Phong INT NOT NULL,                    
    ID_ChiNhanh INT NOT NULL,
    FOREIGN KEY (ID_Phong) REFERENCES PHONG(ID_Phong),
    FOREIGN KEY (ID_ChiNhanh) REFERENCES CHI_NHANH(ID_ChiNhanh)
);

-- Bảng góp ý
CREATE TABLE Gop_Y (
    ID_Gop_Y INT PRIMARY KEY IDENTITY(1,1),
    noiDungGop_Y NVARCHAR(255),
    ngayGop_Y DATETIME DEFAULT GETDATE(),
    ID_NguoiDung INT FOREIGN KEY REFERENCES NGUOI_DUNG(ID_NguoiDung),
    ID_ChiNhanh INT FOREIGN KEY REFERENCES CHI_NHANH(ID_ChiNhanh)
);

-- Dữ liệu mẫu chi nhánh
INSERT INTO CHI_NHANH (diaChi, giaDien, giaNuoc, tenChiNhanh) VALUES
(N'123 Đường 3/2, Quận Ninh Kiều, Cần Thơ', 3000.00, 15000.00, N'Chi Nhánh Cần Thơ 1'),
(N'456 Đường CMT8, Quận Bình Thủy, Cần Thơ', 3200.00, 16000.00, N'Chi Nhánh Cần Thơ 2');

-- Dữ liệu mẫu phòng
INSERT INTO PHONG (giaPhong, trangThai, soPhong, moTa, hinhAnh, ID_ChiNhanh) VALUES
(2500000.00, 0, N'P101', N'Phòng có ban công, view đẹp', N'images/p101.jpg', 1),
(2000000.00, 0, N'P102', N'Phòng tiêu chuẩn, yên tĩnh', N'images/p102.jpg', 1),
(3000000.00, 0, N'P201', N'Phòng lớn, có bếp nhỏ', N'images/p201.jpg', 2),
(2200000.00, 0, N'P202', N'Phòng đôi, gần thang máy', N'images/p202.jpg', 2),
(1800000.00, 0, N'P103', N'Phòng nhỏ, giá phải chăng', N'images/p103.jpg', 1);

-- Dữ liệu mẫu người dùng
INSERT INTO NGUOI_DUNG (hoVaTen, matKhau, email, sdt, soCCCD, trangThai, gioiTinh, queQuan, ngaySinh, ID_Phong) VALUES
(N'Nguyễn Văn A', N'matkhau123', N'nguyenvana@example.com', '0901234567', '123456789012', 1, 1, N'Cần Thơ', '2000-01-15', 1),
(N'Trần Thị B', N'matkhau456', N'tranthib@example.com', '0902345678', '234567890123', 1, 0, N'Vĩnh Long', '1999-05-20', 2),
(N'Lê Văn C', N'matkhau789', N'levanc@example.com', '0903456789', '345678901234', 1, 1, N'An Giang', '2001-11-10', 3),
(N'Phạm Thị D', N'matkhauabc', N'phamthid@example.com', '0904567890', '456789012345', 1, 0, N'Đồng Tháp', '1998-03-25', 4),
(N'Hoàng Minh E', N'matkhauxyz', N'hoangminhe@example.com', '0905678901', '567890123456', 1, 1, N'Kiên Giang', '2002-07-01', 5);

-- Dữ liệu hợp đồng
INSERT INTO HOP_DONG (ngayBatDau, ngayKetThuc, soTienCoc, ID_NguoiDung, ID_Phong, ID_ChiNhanh) VALUES
('2024-01-01', '2025-01-01', 5000000.00, 1, 1, 1),
('2024-02-01', '2025-02-01', 4000000.00, 2, 2, 1),
('2024-03-01', '2025-03-01', 6000000.00, 3, 3, 2),
('2024-04-01', '2025-04-01', 4400000.00, 4, 4, 2),
('2024-05-01', '2025-05-01', 3600000.00, 5, 5, 1);

-- Dữ liệu hóa đơn
INSERT INTO HOA_DON (ID_HopDong) VALUES
(150, 15, 100, 10, 150000.00, 75000.00, 2500000.00, 2725000.00, 1, '2024-06-05', '2024-06-01', 1),
(120, 12, 80, 8, 120000.00, 60000.00, 2000000.00, 2180000.00, 1, '2024-06-06', '2024-06-01', 2),
(180, 18, 120, 12, 192000.00, 96000.00, 3000000.00, 3288000.00, 0, NULL, '2024-06-01', 3),
(130, 13, 90, 9, 128000.00, 64000.00, 2200000.00, 2392000.00, 1, '2024-06-07', '2024-06-01', 4),
(110, 11, 70, 7, 120000.00, 60000.00, 1800000.00, 1980000.00, 0, NULL, '2024-06-01', 5);

-- Dữ liệu số điện nước
INSERT INTO SODIENNUOC (thang, nam, soDien, soNuoc, ID_Phong, ID_ChiNhanh) VALUES 
(7, 2025, 120, 15, 1, 1),
(7, 2025, 95, 12, 5, 1),
(7, 2025, 105, 10, 3, 2),
(6, 2025, 110, 14, 4, 1),
(6, 2025, 130, 20, 2, 1),
(6, 2025, 100, 11, 3, 2);

-- Dữ liệu góp ý
INSERT INTO Gop_Y (noiDungGop_Y, ID_NguoiDung, ID_ChiNhanh) VALUES
(N'Phòng rất sạch sẽ và tiện nghi.', 1, 1),
(N'Nhân viên hỗ trợ nhiệt tình.', 2, 1),
(N'Cần cải thiện tốc độ internet.', 3, 2),
(N'Vị trí thuận tiện, gần chợ.', 4, 2),
(N'Giá điện nước hơi cao so với khu vực.', 5, 1);


SELECT *from NGUOI_DUNG

SELECT *from Gop_Y
