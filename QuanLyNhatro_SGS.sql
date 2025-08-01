USE master;
ALTER DATABASE QuanLyNhatro_SGS SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
DROP DATABASE QuanLyNhatro_SGS;

CREATE DATABASE QuanLyNhatro_SGS;

USE QuanLyNhatro_SGS;
GO

CREATE TABLE NguoiDung
(
    ID_NguoiDung INT IDENTITY(1,1) PRIMARY KEY,
    tenNguoiDung NVARCHAR(255) NOT NULL,
    soDienThoai VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    matKhau VARCHAR(255) NOT NULL,
    namSinh DATE NOT NULL,
    diaChi NVARCHAR(255),
    cccdCmnn VARCHAR(20) UNIQUE NOT NULL,
    anhTruocCccd VARCHAR(255) NOT NULL,
    anhSauCccd VARCHAR(255) NOT NULL,
    vaiTro NVARCHAR(50) NOT NULL,
    trangThai NVARCHAR(50) NOT NULL
);

CREATE TABLE ChiNhanh
(
    ID_ChiNhanh INT IDENTITY(1,1) PRIMARY KEY,
    tenChiNhanh NVARCHAR(255) NOT NULL,
    diaChi NVARCHAR(255) NOT NULL,
    giaDien INT NOT NULL,
    giaNuoc INT NOT NULL
);

CREATE TABLE Phong
(
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

CREATE TABLE DienNuoc
(
    ID_DienNuoc INT IDENTITY(1,1) PRIMARY KEY,
    soDienCu INT NOT NULL,
    soDienMoi INT NOT NULL,
    soNuocCu INT NOT NULL,
    soNuocMoi INT NOT NULL,
    thangNam DATE NOT NULL,
    ID_Phong INT NOT NULL,
    FOREIGN KEY (ID_Phong) REFERENCES Phong(ID_Phong)
);

CREATE TABLE HopDong
(
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

CREATE TABLE NguoiThue_HopDong
(
    ID_NT_HD INT IDENTITY(1,1) PRIMARY KEY,
    ID_HopDong INT NOT NULL,
    ID_NguoiDung INT NOT NULL,
    FOREIGN KEY (ID_HopDong) REFERENCES HopDong(ID_HopDong),
    FOREIGN KEY (ID_NguoiDung) REFERENCES NguoiDung(ID_NguoiDung)
);

CREATE TABLE HoaDon
(
    ID_HoaDon INT IDENTITY(1,1) PRIMARY KEY,
    trangThai NVARCHAR(50) NOT NULL,
    ngayTao DATETIME NOT NULL,
    ID_NguoiDung INT NOT NULL,
    ID_Phong INT NOT NULL,
    ID_HopDong INT NOT NULL,
    ID_ChiNhanh INT NOT NULL,
    FOREIGN KEY (ID_NguoiDung) REFERENCES NguoiDung(ID_NguoiDung),
    FOREIGN KEY (ID_Phong) REFERENCES Phong(ID_Phong),
    FOREIGN KEY (ID_HopDong) REFERENCES HopDong(ID_HopDong),
    FOREIGN KEY (ID_ChiNhanh) REFERENCES ChiNhanh(ID_ChiNhanh)
);

CREATE TABLE GopY
(
    ID_GopY INT IDENTITY(1,1) PRIMARY KEY,
    noiDung NVARCHAR(200) NOT NULL,
    ID_NguoiDung INT NOT NULL,
    ID_ChiNhanh INT NOT NULL,
    FOREIGN KEY (ID_NguoiDung) REFERENCES NguoiDung(ID_NguoiDung),
    FOREIGN KEY (ID_ChiNhanh) REFERENCES ChiNhanh(ID_ChiNhanh)
);

CREATE TABLE OTP
(
    ID_OTP INT IDENTITY(1,1) PRIMARY KEY,
    maOtp VARCHAR(10) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    ngayTao DATETIME NOT NULL,
    FOREIGN KEY (email) REFERENCES NguoiDung(email)
);
INSERT INTO NguoiDung
    (
    tenNguoiDung, soDienThoai, email, matKhau, namSinh, diaChi, cccdCmnn,
    anhTruocCccd, anhSauCccd, vaiTro, trangThai
    )
VALUES
    (N'Nguyễn Văn A', '0901234567', 'nguyenvana@example.com', 'hashed_pass_A', '1990-01-01', N'Hà Nội', '123456789012', 'url_anh_truoc_A.jpg', 'url_anh_sau_A.jpg', N'Người thuê', N'Hoạt động'),
    (N'Trần Thị B', '0902345678', 'tranthib@example.com', 'hashed_pass_B', '1992-03-15', N'TP. Hồ Chí Minh', '234567890123', 'url_anh_truoc_B.jpg', 'url_anh_sau_B.jpg', N'Người thuê', N'Hoạt động'),
    (N'Lê Văn C', '0903456789', 'levanc@example.com', 'hashed_pass_C', '1988-07-20', N'Đà Nẵng', '345678901234', 'url_anh_truoc_C.jpg', 'url_anh_sau_C.jpg', N'Chủ trọ', N'Hoạt động'),
    (N'Phạm Thị D', '0904567890', 'phamthid@example.com', 'hashed_pass_D', '1995-05-10', N'Cần Thơ', '456789012345', 'url_anh_truoc_D.jpg', 'url_anh_sau_D.jpg', N'Người thuê', N'Hoạt động'),
    (N'Hoàng Văn E', '0905678901', 'hoangvane@example.com', 'hashed_pass_E', '1987-12-25', N'Hải Phòng', '567890123456', 'url_anh_truoc_E.jpg', 'url_anh_sau_E.jpg', N'Người thuê', N'Hoạt động'),
    (N'Nguyễn Thị F', '0906789012', 'nguyenthif@example.com', 'hashed_pass_F', '1993-09-09', N'Khánh Hòa', '678901234567', 'url_anh_truoc_F.jpg', 'url_anh_sau_F.jpg', N'Người thuê', N'Hoạt động'),
    (N'Trần Văn G', '0907890123', 'tranvang@example.com', 'hashed_pass_G', '1991-11-30', N'Đồng Nai', '789012345678', 'url_anh_truoc_G.jpg', 'url_anh_sau_G.jpg', N'Người thuê', N'Hoạt động'),
    (N'Lê Thị H', '0908901234', 'lethih@example.com', 'hashed_pass_H', '1994-06-06', N'Gia Lai', '890123456789', 'url_anh_truoc_H.jpg', 'url_anh_sau_H.jpg', N'Người thuê', N'Hoạt động'),
    (N'Phạm Văn I', '0909012345', 'phamvani@example.com', 'hashed_pass_I', '1989-10-01', N'An Giang', '901234567890', 'url_anh_truoc_I.jpg', 'url_anh_sau_I.jpg', N'Người thuê', N'Hoạt động'),
    (N'Hoàng Thị K', '0900123456', 'hoangthik@example.com', 'hashed_pass_K', '1990-04-04', N'Thừa Thiên Huế', '012345678901', 'url_anh_truoc_K.jpg', 'url_anh_sau_K.jpg', N'Chủ trọ', N'Hoạt động');

INSERT INTO ChiNhanh
    (tenChiNhanh, diaChi, giaDien, giaNuoc)
VALUES
    (N'Chi nhánh Miền Bắc', N'Hà Nội', 3500, 15000),
    (N'Chi nhánh Miền Nam', N'TP.HCM', 4000, 18000);
INSERT INTO Phong
    (soPhong, giaPhong, loaiPhong, moTa, anhPhong, trangThai, ID_ChiNhanh)
VALUES
   ('1015', 2500000, 'Đơn', N'Phòng đơn, đầy đủ tiện nghi', 'phong_101A.jpg', N'Con Trống', 1),
    ('101A', 2500000, 'Đơn', N'Phòng đơn, đầy đủ tiện nghi', 'phong_101A.jpg', N'Đang thuê', 1),
    ('102B', 3000000, 'Đôi', N'Phòng đôi, ban công đẹp', 'phong_102B.jpg', N'Đang thuê', 1),
    ('201A', 3500000, 'VIP', N'Phòng VIP sang trọng', 'phong_201A.jpg', N'Đang thuê', 2),
    ('202B', 2200000, 'Đơn', N'Phòng đơn, gần lối thoát hiểm', 'phong_202B.jpg', N'Đang thuê', 2);
INSERT INTO DienNuoc
    (soDienCu, soDienMoi, soNuocCu, soNuocMoi, thangNam, ID_Phong)
VALUES
    (10, 15, 5, 8, '2025-07-01', 1),
    (20, 27, 7, 11, '2025-07-01', 2),
    (15, 22, 6, 9, '2025-07-01', 3),
    (8, 12, 4, 7, '2025-07-01', 4);
INSERT INTO HopDong
    (ngayTao, thoiHan, tienCoc, nuocBanDau, dienBanDau, ID_NguoiDung, ID_Phong)
VALUES
    (GETDATE(), 12, 1000000, 5, 10, 1, 1),
    -- Nguyễn Văn A
    (GETDATE(), 6, 800000, 6, 12, 2, 2),
    -- Trần Thị B
    (GETDATE(), 12, 1000000, 7, 15, 4, 3),
    -- Phạm Thị D
    (GETDATE(), 9, 500000, 4, 8, 5, 4);
-- Hoàng Văn E
INSERT INTO NguoiThue_HopDong
    (ID_HopDong, ID_NguoiDung)
VALUES
    (1, 1),
    (2, 2),
    (3, 4),
    (4, 5);
INSERT INTO HoaDon
    (trangThai, ngayTao, ID_NguoiDung, ID_Phong, ID_HopDong, ID_ChiNhanh)
VALUES
    (N'Chưa thanh toán', GETDATE(), 1, 1, 1, 1),
    (N'Đã thanh toán', GETDATE(), 2, 2, 2, 1),
    (N'Chưa thanh toán', GETDATE(), 4, 3, 3, 2),
    (N'Đã thanh toán', GETDATE(), 5, 4, 4, 2);
INSERT INTO GopY
    (noiDung, ID_NguoiDung, ID_ChiNhanh)
VALUES
    (N'Phòng sạch sẽ, phục vụ tốt.', 1, 1),
    (N'Máy giặt cần được bảo trì.', 2, 1),
    (N'Mong muốn có wifi mạnh hơn.', 4, 2),
    (N'Cần thêm chỗ để xe.', 5, 2);
INSERT INTO OTP
    (maOtp, email, ngayTao)
VALUES
    ('OTP001', 'nguyenvana@example.com', GETDATE()),
    ('OTP002', 'tranthib@example.com', GETDATE()),
    ('OTP003', 'phamthid@example.com', GETDATE()),
    ('OTP004', 'hoangvane@example.com', GETDATE());

-- =====================================================
-- QUERY ĐƠN GIẢN CHỈ LẤY CÁC TRƯỜNG CẦN THIẾT CHO DOANHTHU
-- (Giống như bảng hóa đơn nhưng tập trung vào doanh thu)
-- =====================================================

SELECT
    hdon.ID_HoaDon,
    p.soPhong,
    (dn.soDienMoi - dn.soDienCu) * cn.giaDien AS tienDien,
    (dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc AS tienNuoc,
    p.giaPhong AS tienPhong,
    ((dn.soDienMoi - dn.soDienCu) * cn.giaDien + 
     (dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc + 
     p.giaPhong) AS tongTien,
    hdon.ngayTao AS ngayThanhToan,
    hdon.trangThai AS trangThaiThanhToan
FROM HoaDon hdon
    JOIN HopDong hd ON hdon.ID_HopDong = hd.ID_HopDong
    JOIN Phong p ON hd.ID_Phong = p.ID_Phong
    JOIN DienNuoc dn ON p.ID_Phong = dn.ID_Phong
    JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh
ORDER BY hdon.ngayTao DESC;

select *from Phong