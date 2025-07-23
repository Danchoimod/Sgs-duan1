package poly.nhatro.service;

import poly.nhatro.entity.HopDong;
import java.util.List;

/**
 * Giao diện định nghĩa các dịch vụ (business logic) cho đối tượng HopDong.
 * @author Gia Bao
 */
public interface HopDongService {
    void addHopDong(HopDong hopDong);
    void updateHopDong(HopDong hopDong);
    void deleteHopDong(String id);
    HopDong getHopDongById(String id);
    List<HopDong> getAllHopDongs();
}