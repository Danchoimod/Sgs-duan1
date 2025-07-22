# 🏠 Ứng Dụng Quản Lý Nhà Trọ

## 📌 Mô tả
Phần mềm desktop viết bằng **Java Swing**, giúp quản lý các nghiệp vụ nhà trọ:

- Người thuê
- Phòng trọ
- Hợp đồng thuê
- Ghi chỉ số điện nước
- Tạo hóa đơn thanh toán
- Quản lý chi nhánh

Ứng dụng sử dụng cơ sở dữ liệu **PostgreSQL** kết nối qua **Supabase** với giao diện hiện đại nhờ **FlatLaf UI**.

---

## 🚀 Công nghệ sử dụng
| Công nghệ       | Vai trò                         |
|----------------|----------------------------------|
| Java Swing     | Giao diện ứng dụng               |
| PostgreSQL     | Lưu trữ dữ liệu                  |
| Supabase       | Backend DB hosting + API         |
| FlatLaf        | Giao diện người dùng (Flat UI)   |
| JDBC           | Kết nối Java ↔ PostgreSQL        |

---

## 📂 Cấu trúc chức năng

- `Người thuê`: Thêm/sửa/xóa người thuê, quản lý thông tin CMND, số điện thoại, email.
- `Phòng`: Gán phòng cho chi nhánh, trạng thái còn trống/đã thuê.
- `Hợp đồng`: Quản lý thời hạn thuê, tiền cọc, trạng thái.
- `Điện nước`: Nhập chỉ số mỗi tháng, tính tiền theo số cũ → số mới.
- `Hóa đơn`: Tạo hóa đơn điện, nước, tiền phòng và quản lý thanh toán.
- `Chi nhánh`: Thêm chi nhánh, quản lý đơn giá điện/nước riêng.

---

## ⚙️ Cách chạy dự án

1. **Clone repo**:
   ```bash
   git clone https://github.com/your-username/ten-repo.git
