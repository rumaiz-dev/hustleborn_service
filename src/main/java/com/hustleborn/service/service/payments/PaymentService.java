package com.hustleborn.service.service.payments;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustleborn.service.model.bills.Bills;
import com.hustleborn.service.model.billitems.BillItems;
import com.hustleborn.service.model.bills.BillStatus;
import com.hustleborn.service.repository.bills.BillRepository;
import com.hustleborn.service.service.inventory.InventoryService;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public Bills payBill(Long billId, Long userId) {
        Bills bill = billRepository.findById(billId).orElse(null);
        if (bill != null && bill.getStatus() == BillStatus.Pending) {
            bill.setStatus(BillStatus.Paid);
            bill.setUpdatedAt(LocalDateTime.now());

            for (BillItems item : bill.getBillItems()) {
                inventoryService.recordSale(item.getVariant().getId(), item.getQuantity(), "Bill " + billId, userId);
            }

            return billRepository.save(bill);
        }
        return null;
    }

    @Override
    public Bills refundBill(Long billId, Long userId) {
        Bills bill = billRepository.findById(billId).orElse(null);
        if (bill != null && bill.getStatus() == BillStatus.Paid) {
            bill.setStatus(BillStatus.Refunded);
            bill.setUpdatedAt(LocalDateTime.now());

            for (BillItems item : bill.getBillItems()) {
                int refundQuantity = item.getQuantity() - (item.getRefundedQuantity() != null ? item.getRefundedQuantity() : 0);
                if (refundQuantity > 0) {
                    inventoryService.recordRefund(item.getVariant().getId(), refundQuantity, "Bill refund", userId);
                    item.setRefundedQuantity(item.getQuantity());
                }
            }
            return billRepository.save(bill);
        }
        return null;
    }

    @Override
    public Bills partialRefund(Long billId, Map<Long, Integer> itemRefunds, Long userId) {
        Bills bill = billRepository.findById(billId).orElse(null);
        if (bill != null && bill.getStatus() == BillStatus.Paid) {
            for (Map.Entry<Long, Integer> entry : itemRefunds.entrySet()) {
                Long itemId = entry.getKey();
                Integer qty = entry.getValue();
                BillItems item = bill.getBillItems().stream().filter(it -> it.getId().equals(itemId)).findFirst().orElse(null);
                if (item != null && qty > 0) {
                    int alreadyRefunded = item.getRefundedQuantity() != null ? item.getRefundedQuantity() : 0;
                    int availableToRefund = item.getQuantity() - alreadyRefunded;
                    if (qty <= availableToRefund) {
                        inventoryService.recordRefund(item.getVariant().getId(), qty, "Partial bill refund", userId);
                        item.setRefundedQuantity(alreadyRefunded + qty);
                    }
                }
            }
            bill.setUpdatedAt(LocalDateTime.now());
            boolean allRefunded = bill.getBillItems().stream().allMatch(it -> (it.getRefundedQuantity() != null ? it.getRefundedQuantity() : 0) >= it.getQuantity());
            if (allRefunded) {
                bill.setStatus(BillStatus.Refunded);
            }
            return billRepository.save(bill);
        }
        return null;
    }
}