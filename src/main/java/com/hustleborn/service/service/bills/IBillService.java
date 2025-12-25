package com.hustleborn.service.service.bills;

import java.util.List;
import java.util.Map;

import com.hustleborn.service.model.bills.Bills;

public interface IBillService {

    List<Bills> getAllBills();

    Bills getBillById(Long id);

    Bills createBill(Bills bill);

    Bills updateBill(Bills bill);

    Bills payBill(Long billId, Long userId);

    void deleteBill(Long id);

    Bills refundBill(Long billId, Long userId);

    Bills partialRefund(Long billId, Map<Long, Integer> itemRefunds, Long userId);

}