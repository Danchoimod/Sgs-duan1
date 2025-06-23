package poly.cafe.entity;

import lombok.*;

/**
 * Thông tin loại thức uống / danh mục
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private String id;
    private String name;
}
