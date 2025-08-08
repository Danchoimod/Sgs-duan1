package poly.nhatro.dao;

import poly.nhatro.entity.HopDong;
import java.util.List;

public interface HopDongDAO {

    /**
     * Inserts a new contract into the database.
     * @param hopDong The HopDong object to insert.
     * @return The inserted HopDong object, potentially with an updated ID.
     */
    HopDong insert(HopDong hopDong);

    /**
     * Updates an existing contract in the database.
     * @param hopDong The HopDong object with updated information.
     * @return The updated HopDong object.
     */
    HopDong update(HopDong hopDong);

    /**
     * Deletes a contract from the database by its ID.
     * @param ID_HopDong The ID of the contract to delete.
     */
    void delete(int ID_HopDong);
    // Soft delete: chuyển trạng thái sang 1
    void softDelete(int ID_HopDong);
    // Đánh dấu hết hạn theo ID
    void markExpired(int ID_HopDong);
    // Đánh dấu tất cả hợp đồng đã hết hạn (ngày kết thúc <= hiện tại) sang trạng thái 1
    void markAllExpired();

    /**
     * Retrieves a contract by its ID.
     * @param ID_HopDong The ID of the contract to retrieve.
     * @return The HopDong object if found, null otherwise.
     */
    HopDong selectById(int ID_HopDong);

    /**
     * Retrieves all contracts from the database.
     * @return A list of all HopDong objects.
     */
    List<HopDong> selectAll();

    /**
     * Retrieves contracts that are currently active (not expired).
     * This will require calculating the end date based on ngayTao and thoiHan.
     * @return A list of active HopDong objects.
     */
    List<HopDong> selectActiveContracts();

    /**
     * Retrieves contracts that have expired.
     * This will require calculating the end date based on ngayTao and thoiHan.
     * @return A list of expired HopDong objects.
     */
    List<HopDong> selectExpiredContracts();

    /**
     * Searches for contracts based on a keyword.
     * The search could apply to various fields like contract ID, room number, etc.
     * @param keyword The search term.
     * @return A list of HopDong objects matching the search criteria.
     */
    List<HopDong> searchContracts(String keyword);

    /**
     * Retrieves contracts by branch ID.
     * @param ID_ChiNhanh The ID of the branch.
     * @return A list of HopDong objects for the specified branch.
     */
    List<HopDong> selectByChiNhanh(int ID_ChiNhanh);

    /**
     * Retrieves contracts by the signing user's ID.
     * @param ID_NguoiDung The ID of the user who signed the contract.
     * @return A list of HopDong objects signed by the specified user.
     */
    List<HopDong> selectByNguoiKiHopDong(int ID_NguoiDung);

    /**
     * Retrieves contracts by room ID.
     * @param ID_Phong The ID of the room.
     * @return A list of HopDong objects for the specified room.
     */
    List<HopDong> selectByRoom(int ID_Phong);
    
    int timIdHopDongTheoSoPhongVaChiNhanh(String soPhong, int idChiNhanh);

}