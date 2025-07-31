
USE master;
ALTER DATABASE QuanLyNhatro_SGS SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
DROP DATABASE QuanLyNhatro_SGS;

CREATE DATABASE QuanLyNhatro_SGS;

USE QuanLyNhatro_SGS;

CREATE TABLE NguoiDung (
    ID_NguoiDung INT IDENTITY(1,1) PRIMARY KEY,
    tenNguoiDung NVARCHAR(255) NOT NULL,
    soDienThoai VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    matKhau VARCHAR(255) NOT NULL,
    diaChi NVARCHAR(255), 
    cccdCmnn VARCHAR(20) UNIQUE NOT NULL,
    anhTruocCccd VARCHAR(255) NOT NULL,
    anhSauCccd VARCHAR(255) NOT NULL,
    vaiTro NVARCHAR(50) NOT NULL,
    trangThai NVARCHAR(50) NOT NULL,
);

CREATE TABLE ChiNhanh (
    ID_ChiNhanh INT IDENTITY(1,1) PRIMARY KEY,
    tenChiNhanh NVARCHAR(255) NOT NULL,
    diaChi NVARCHAR(255) NOT NULL,
    giaDien INT NOT NULL,
    giaNuoc INT NOT NULL
);

CREATE TABLE Phong (
    ID_Phong INT IDENTITY(1,1) PRIMARY KEY,
    soPhong VARCHAR(10) UNIQUE NOT NULL,
    giaPhong INT NOT NULL,
    loaiPhong VARCHAR(50) NOT NULL,
    moTa NVARCHAR(200),
    anhPhong VARCHAR(255) NOT NULL,
    trangThai NVARCHAR(50) NOT NULL,
    ID_ChiNhanh INT NOT NULL,
    FOREIGN KEY (ID_ChiNhanh) REFERENCES ChiNhanh(ID_ChiNhanh)
);

CREATE TABLE HopDong (
    ID_HopDong INT IDENTITY(1,1) PRIMARY KEY,
    ngayTao DATETIME NOT NULL,
    thoiHan INT NOT NULL,
    tienCoc INT NOT NULL,
    nuocBanDau INT NOT NULL,
    dienBanDau INT NOT NULL,
    ID_NguoiDung INT NOT NULL,
    ID_Phong INT NOT NULL,
    FOREIGN KEY (ID_NguoiDung) REFERENCES NguoiDung(ID_NguoiDung),
    FOREIGN KEY (ID_Phong) REFERENCES Phong(ID_Phong)
);

CREATE TABLE NguoiThue_HopDong(
    ID_NT_HD INT IDENTITY(1,1) PRIMARY KEY,
    ID_HopDong INT NOT NULL,
    ID_NguoiDung INT NOT NULL,
    FOREIGN KEY (ID_HopDong) REFERENCES HopDong(ID_HopDong),
    FOREIGN KEY (ID_Nguoidung) REFERENCES NguoiDung(ID_Nguoidung)
)

CREATE TABLE HoaDon (
    ID_HoaDon INT IDENTITY(1,1) PRIMARY KEY,
    trangThai NVARCHAR(50) NOT NULL,
    ngayTao DATETIME NOT NULl,
    ID_NguoiDung INT NOT NULL,
    ID_Phong INT NOT NULL,
    ID_HopDong INT NOT NULL,
    ID_ChiNhanh INT NOT NULL,

    FOREIGN KEY (ID_NguoiDung) REFERENCES NguoiDung(ID_NguoiDung),
    FOREIGN KEY (ID_Phong) REFERENCES Phong(ID_Phong),
    FOREIGN KEY (ID_HopDong) REFERENCES HopDong(ID_HopDong),
    FOREIGN KEY (ID_ChiNhanh) REFERENCES ChiNhanh(ID_ChiNhanh)
);

CREATE TABLE GopY (
    ID_GopY INT IDENTITY(1,1) PRIMARY KEY,
    noiDung NVARCHAR(200) NOT NULL,
    ID_NguoiDung INT NOT NULL,
    ID_ChiNhanh INT NOT NULL,
    FOREIGN KEY (ID_NguoiDung) REFERENCES NguoiDung(ID_NguoiDung),
    FOREIGN KEY (ID_ChiNhanh) REFERENCES ChiNhanh(ID_ChiNhanh)
);

CREATE TABLE OTP (
    ID_OTP INT IDENTITY(1,1) PRIMARY KEY,
    maOtp VARCHAR(10) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    ngayTao DATETIME NOT NULL,
    FOREIGN KEY (email) REFERENCES NguoiDung(email)
);


-- 1. Chèn dữ liệu vào bảng NguoiDung (10 bản ghi)
INSERT INTO NguoiDung (tenNguoiDung, soDienThoai, email, matKhau, diaChi, cccdCmnn, anhTruocCccd, anhSauCccd, vaiTro, trangThai) VALUES
(N'Nguyễn Văn A', '0901234567', 'nguyenvana@example.com', 'hashed_pass_A', N'123 Đường ABC, Quận 1', '123456789012', 'url_anh_truoc_A.jpg', 'url_anh_sau_A.jpg', N'Người thuê', N'Hoạt động'),
(N'Trần Thị B', '0902345678', 'tranthib@example.com', 'hashed_pass_B', N'456 Đường XYZ, Quận 2', '234567890123', 'url_anh_truoc_B.jpg', 'url_anh_sau_B.jpg', N'Người thuê', N'Hoạt động'),
(N'Lê Văn C', '0903456789', 'levanc@example.com', 'hashed_pass_C', N'789 Đường DEF, Quận 3', '345678901234', 'url_anh_truoc_C.jpg', 'url_anh_sau_C.jpg', N'Chủ trọ', N'Hoạt động'),
(N'Phạm Thị D', '0904567890', 'phamthid@example.com', 'hashed_pass_D', N'101 Đường GHI, Quận 4', '456789012345', 'url_anh_truoc_D.jpg', 'url_anh_sau_D.jpg', N'Người thuê', N'Hoạt động'),
(N'Hoàng Văn E', '0905678901', 'hoangvane@example.com', 'hashed_pass_E', N'202 Đường JKL, Quận 5', '567890123456', 'url_anh_truoc_E.jpg', 'url_anh_sau_E.jpg', N'Người thuê', N'Hoạt động'),
(N'Nguyễn Thị F', '0906789012', 'nguyenthif@example.com', 'hashed_pass_F', N'303 Đường MNO, Quận 6', '678901234567', 'url_anh_truoc_F.jpg', 'url_anh_sau_F.jpg', N'Người thuê', N'Hoạt động'),
(N'Trần Văn G', '0907890123', 'tranvang@example.com', 'hashed_pass_G', N'404 Đường PQR, Quận 7', '789012345678', 'url_anh_truoc_G.jpg', 'url_anh_sau_G.jpg', N'Người thuê', N'Hoạt động'),
(N'Lê Thị H', '0908901234', 'lethih@example.com', 'hashed_pass_H', N'505 Đường STU, Quận 8', '890123456789', 'url_anh_truoc_H.jpg', 'url_anh_sau_H.jpg', N'Người thuê', N'Hoạt động'),
(N'Phạm Văn I', '0909012345', 'phamvani@example.com', 'hashed_pass_I', N'606 Đường VWX, Quận 9', '901234567890', 'url_anh_truoc_I.jpg', 'url_anh_sau_I.jpg', N'Người thuê', N'Hoạt động'),
(N'Hoàng Thị K', '0900123456', 'hoangthik@example.com', 'hashed_pass_K', N'707 Đường YZA, Quận 10', '012345678901', 'url_anh_truoc_K.jpg', 'url_anh_sau_K.jpg', N'Chủ trọ', N'Hoạt động');

-- 2. Chèn dữ liệu vào bảng ChiNhanh (10 bản ghi)
INSERT INTO ChiNhanh (tenChiNhanh, diaChi, giaDien, giaNuoc) VALUES
(N'Chi nhánh Quận 1', N'100 Đường Nguyễn Huệ, Quận 1', 3000, 15000),
(N'Chi nhánh Quận 3', N'200 Đường Nam Kỳ Khởi Nghĩa, Quận 3', 3200, 16000),
(N'Chi nhánh Quận 5', N'300 Đường Trần Hưng Đạo, Quận 5', 3100, 15500),
(N'Chi nhánh Quận Bình Thạnh', N'400 Đường Điện Biên Phủ, Quận Bình Thạnh', 2900, 14500),
(N'Chi nhánh Quận Gò Vấp', N'500 Đường Quang Trung, Quận Gò Vấp', 2800, 14000),
(N'Chi nhánh Quận Tân Bình', N'600 Đường Cộng Hòa, Quận Tân Bình', 3050, 15200),
(N'Chi nhánh Quận 7', N'700 Đường Nguyễn Thị Thập, Quận 7', 3300, 16500),
(N'Chi nhánh Quận Thủ Đức', N'800 Đường Võ Văn Ngân, Quận Thủ Đức', 2950, 14800),
(N'Chi nhánh Quận 10', N'900 Đường 3 Tháng 2, Quận 10', 3150, 15800),
(N'Chi nhánh Quận Phú Nhuận', N'1000 Đường Phan Đăng Lưu, Quận Phú Nhuận', 3000, 15000);

-- 3. Chèn dữ liệu vào bảng Phong (10 bản ghi) - Đã thêm cột giaPhong
INSERT INTO Phong (soPhong, giaPhong, loaiPhong, moTa, anhPhong, trangThai, ID_ChiNhanh) VALUES
('P101', 3000000, N'Phòng đơn', N'Phòng có ban công, thoáng mát', 'url_phong_101.jpg', N'Trống', 1),
('P102', 5000000, N'Phòng đôi', N'Phòng rộng rãi, có điều hòa', 'url_phong_102.jpg', N'Đã thuê', 1),
('P201', 3200000, N'Phòng đơn', N'Phòng yên tĩnh, cửa sổ hướng vườn', 'url_phong_201.jpg', N'Trống', 2),
('P202', 5500000, N'Phòng đôi', N'Phòng có nội thất cơ bản', 'url_phong_202.jpg', N'Đã thuê', 2),
('P301', 7000000, N'Phòng VIP', N'Phòng cao cấp, đầy đủ tiện nghi', 'url_phong_301.jpg', N'Trống', 3),
('P302', 3500000, N'Phòng đơn', N'Phòng nhỏ gọn, giá phải chăng', 'url_phong_302.jpg', N'Đã thuê', 3),
('P401', 4800000, N'Phòng đôi', N'Phòng sáng sủa, có tủ quần áo', 'url_phong_401.jpg', N'Trống', 4),
('P402', 3100000, N'Phòng đơn', N'Phòng có quạt trần', 'url_phong_402.jpg', N'Đã thuê', 4),
('P501', 8000000, N'Phòng VIP', N'Phòng có view đẹp', 'url_phong_501.jpg', N'Trống', 5),
('P502', 5200000, N'Phòng đôi', N'Phòng có máy giặt riêng', 'url_phong_502.jpg', N'Đã thuê', 5);

-- 4. Chèn dữ liệu vào bảng HopDong (10 bản ghi)
INSERT INTO HopDong (ngayTao, thoiHan, tienCoc, nuocBanDau, dienBanDau, ID_NguoiDung, ID_Phong) VALUES
('2024-01-15 10:00:00', 12, 5000000, 10, 100, 2, 2), -- Nguyen Thi B (ID 2) thuê P102 (ID 2)
('2024-02-01 11:30:00', 6, 3000000, 5, 50, 4, 4), -- Pham Thi D (ID 4) thuê P202 (ID 4)
('2024-03-10 09:00:00', 12, 4500000, 12, 120, 5, 6), -- Hoang Van E (ID 5) thuê P302 (ID 6)
('2024-04-05 14:00:00', 6, 2500000, 8, 80, 6, 8), -- Nguyen Thi F (ID 6) thuê P402 (ID 8)
('2024-05-20 16:00:00', 12, 6000000, 15, 150, 7, 10), -- Tran Van G (ID 7) thuê P502 (ID 10)
('2024-06-01 08:00:00', 6, 3500000, 7, 70, 8, 1), -- Le Thi H (ID 8) thuê P101 (ID 1)
('2024-07-10 10:30:00', 12, 4000000, 9, 90, 9, 3), -- Pham Van I (ID 9) thuê P201 (ID 3)
('2024-08-15 13:00:00', 6, 2000000, 6, 60, 1, 5), -- Nguyen Van A (ID 1) thuê P301 (ID 5)
('2024-09-01 15:00:00', 12, 5500000, 11, 110, 2, 7), -- Tran Thi B (ID 2) thuê P401 (ID 7)
('2024-10-20 17:00:00', 6, 3000000, 10, 100, 4, 9); -- Pham Thi D (ID 4) thuê P501 (ID 9)

-- 5. Chèn dữ liệu vào bảng NguoiThue_HopDong (10 bản ghi)
-- Đây là bảng liên kết giữa người dùng và hợp đồng.
-- Mỗi bản ghi ở đây thể hiện một người thuê cụ thể liên quan đến một hợp đồng.
-- Có thể một hợp đồng có nhiều người thuê, hoặc một người thuê có nhiều hợp đồng.
INSERT INTO NguoiThue_HopDong (ID_HopDong, ID_NguoiDung) VALUES
(1, 2), -- HD001 - Nguyen Thi B
(2, 4), -- HD002 - Pham Thi D
(3, 5), -- HD003 - Hoang Van E
(4, 6), -- HD004 - Nguyen Thi F
(5, 7), -- HD005 - Tran Van G
(6, 8), -- HD006 - Le Thi H
(7, 9), -- HD007 - Pham Van I
(8, 1), -- HD008 - Nguyen Van A
(9, 2), -- HD009 - Tran Thi B
(10, 4); -- HD010 - Pham Thi D

-- 6. Chèn dữ liệu vào bảng HoaDon (10 bản ghi)
-- Các ID người dùng, phòng, hợp đồng, chi nhánh phải tồn tại.
INSERT INTO HoaDon (trangThai, ngayTao, ID_NguoiDung, ID_Phong, ID_HopDong, ID_ChiNhanh) VALUES
(N'Chưa thanh toán', '2024-02-01 00:00:00', 2, 2, 1, 1), -- HD001
(N'Đã thanh toán', '2024-03-01 00:00:00', 4, 4, 2, 2), -- HD002
(N'Chưa thanh toán', '2024-04-01 00:00:00', 5, 6, 3, 3), -- HD003
(N'Đã thanh toán', '2024-05-01 00:00:00', 6, 8, 4, 4), -- HD004
(N'Chưa thanh toán', '2024-06-01 00:00:00', 7, 10, 5, 5), -- HD005
(N'Đã thanh toán', '2024-07-01 00:00:00', 8, 1, 6, 1), -- HD006
(N'Chưa thanh toán', '2024-08-01 00:00:00', 9, 3, 7, 2), -- HD007
(N'Đã thanh toán', '2024-09-01 00:00:00', 1, 5, 8, 3), -- HD008
(N'Chưa thanh toán', '2024-10-01 00:00:00', 2, 7, 9, 4), -- HD009
(N'Đã thanh toán', '2024-11-01 00:00:00', 4, 9, 10, 5); -- HD010

-- 7. Chèn dữ liệu vào bảng GopY (10 bản ghi)
INSERT INTO GopY (noiDung, ID_NguoiDung, ID_ChiNhanh) VALUES
(N'Phòng rất sạch sẽ và tiện nghi.', 2, 1),
(N'Dịch vụ hỗ trợ nhanh chóng.', 4, 2),
(N'Giá điện nước hơi cao.', 5, 3),
(N'Không gian chung cần được cải thiện.', 6, 4),
(N'Chủ trọ rất thân thiện.', 7, 5),
(N'Vị trí thuận tiện đi lại.', 8, 1),
(N'Cần thêm chỗ để xe.', 9, 2),
(N'An ninh tốt.', 1, 3),
(N'Đồ đạc trong phòng hơi cũ.', 2, 4),
(N'Hài lòng với trải nghiệm ở đây.', 4, 5);

-- 8. Chèn dữ liệu vào bảng OTP (10 bản ghi) - Đã thêm cột ngayTao
INSERT INTO OTP (maOtp, email, ngayTao) VALUES
('123456', 'nguyenvana@example.com', GETDATE()),
('654321', 'tranthib@example.com', GETDATE()),
('987654', 'levanc@example.com', GETDATE()),
('456789', 'phamthid@example.com', GETDATE()),
('789012', 'hoangvane@example.com', GETDATE()),
('321098', 'nguyenthif@example.com', GETDATE()),
('012345', 'tranvang@example.com', GETDATE()),
('543210', 'lethih@example.com', GETDATE()),
('876543', 'phamvani@example.com', GETDATE()),
('210987', 'hoangthik@example.com', GETDATE());


SELECT * FROM NguoiDung;
SELECT * FROM ChiNhanh;
SELECT * FROM Phong;
SELECT * FROM HopDong;
SELECT * FROM HoaDon;
SELECT * FROM GopY;
SELECT * FROM OTP;
SELECT * FROM NguoiThue_HopDong;
