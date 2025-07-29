-- Add missing columns to HoaDon table for DoanhThu functionality
USE QuanLyNhatro_SGS;

-- Add columns for electricity and water readings
ALTER TABLE HoaDon ADD soDienMoi INT DEFAULT 0;
ALTER TABLE HoaDon ADD soNuocMoi INT DEFAULT 0;
ALTER TABLE HoaDon ADD soDienCu INT DEFAULT 0;
ALTER TABLE HoaDon ADD soNuocCu INT DEFAULT 0;

-- Add columns for money amounts
ALTER TABLE HoaDon ADD tienDien DECIMAL(10,2) DEFAULT 0;
ALTER TABLE HoaDon ADD tienNuoc DECIMAL(10,2) DEFAULT 0;
ALTER TABLE HoaDon ADD tienPhong DECIMAL(10,2) DEFAULT 0;
ALTER TABLE HoaDon ADD tongTien DECIMAL(10,2) DEFAULT 0;

-- Add payment status column (boolean equivalent)
ALTER TABLE HoaDon ADD trangThaiThanhToan BIT DEFAULT 0;

-- Update existing records to have default values
UPDATE HoaDon SET 
    soDienMoi = 0,
    soNuocMoi = 0,
    soDienCu = 0,
    soNuocCu = 0,
    tienDien = 0,
    tienNuoc = 0,
    tienPhong = 0,
    tongTien = 0,
    trangThaiThanhToan = CASE WHEN trangThai = N'Đã thanh toán' THEN 1 ELSE 0 END; 