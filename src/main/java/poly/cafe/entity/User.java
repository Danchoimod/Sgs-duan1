package poly.cafe.entity;

import lombok.*;

/**
 * Thông tin người dùng (User)
 * - Bao gồm tài khoản, mật khẩu, vai trò, hình ảnh đại diện
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private boolean enabled;
    private String fullname;

    @Builder.Default
    private String photo = "photo.png";

    private boolean manager;
}
