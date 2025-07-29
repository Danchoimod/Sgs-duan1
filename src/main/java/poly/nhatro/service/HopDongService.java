package poly.nhatro.service;

import poly.nhatro.entity.HopDong;
import java.util.List;
import java.util.Date; // For date calculations in business logic

/**
 * Giao diện định nghĩa các dịch vụ (business logic) cho đối tượng HopDong.
 *
 * @author Gia Bao
 */
public interface HopDongService {

    /**
     * Creates a new contract. Includes business logic like validation.
     * @param hopDong The contract object to create.
     * @return The created HopDong object.
     * @throws IllegalArgumentException if validation fails.
     */
    HopDong createHopDong(HopDong hopDong);

    /**
     * Updates an existing contract. Includes business logic like validation.
     * @param hopDong The contract object with updated information.
     * @return The updated HopDong object.
     * @throws IllegalArgumentException if validation fails or contract not found.
     */
    HopDong updateHopDong(HopDong hopDong);

    /**
     * Deletes a contract by its ID. Might include checks for dependencies (e.g., existing invoices).
     * @param ID_HopDong The ID of the contract to delete.
     * @throws RuntimeException if deletion is not allowed due to business rules.
     */
    void deleteHopDong(int ID_HopDong);

    /**
     * Retrieves a contract by its ID.
     * @param ID_HopDong The ID of the contract.
     * @return The HopDong object if found, null otherwise.
     */
    HopDong getHopDongById(int ID_HopDong);

    /**
     * Retrieves all contracts.
     * @return A list of all HopDong objects.
     */
    List<HopDong> getAllHopDongs();

    /**
     * Retrieves contracts that are currently active.
     * @return A list of active HopDong objects.
     */
    List<HopDong> getActiveHopDongs();

    /**
     * Retrieves contracts that have expired.
     * @return A list of expired HopDong objects.
     */
    List<HopDong> getExpiredHopDongs();

    /**
     * Searches for contracts based on a keyword across relevant fields.
     * @param keyword The search term.
     * @return A list of HopDong objects matching the search criteria.
     */
    List<HopDong> searchHopDongs(String keyword);

    /**
     * Retrieves contracts belonging to a specific branch.
     * @param ID_ChiNhanh The ID of the branch.
     * @return A list of HopDong objects for the specified branch.
     */
    List<HopDong> getHopDongsByChiNhanh(int ID_ChiNhanh);

    /**
     * Retrieves contracts signed by a specific user.
     * @param ID_NguoiDung The ID of the user who signed the contract.
     * @return A list of HopDong objects signed by the specified user.
     */
    List<HopDong> getHopDongsByNguoiKiHopDong(int ID_NguoiDung);

    /**
     * Retrieves contracts associated with a specific room.
     * @param ID_Phong The ID of the room.
     * @return A list of HopDong objects for the specified room.
     */
    List<HopDong> getHopDongsByRoom(int ID_Phong);

    /**
     * Calculates the estimated end date of a contract.
     * @param ngayTao The creation date of the contract.
     * @param thoiHan The contract duration in months.
     * @return The calculated end date.
     */
    Date calculateEndDate(Date ngayTao, int thoiHan);

    // Potentially other business logic methods, e.g.,
    // boolean isContractValidForRoom(int ID_Phong, Date startDate, Date endDate);
    // double calculateRemainingDeposit(int ID_HopDong);
}