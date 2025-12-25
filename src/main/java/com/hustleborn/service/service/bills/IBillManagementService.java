package com.hustleborn.service.service.bills;

import java.util.List;
import com.hustleborn.service.model.bills.Bills;

public interface IBillManagementService {
    List<Bills> getAllBills();
    Bills getBillById(Long id);
    Bills createBill(Bills bill);
    Bills updateBill(Bills bill);
    void deleteBill(Long id);
}