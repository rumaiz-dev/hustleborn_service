package com.hustleborn.service.service.payments;

import java.util.Map;

import com.hustleborn.service.model.bills.Bills;

public interface IPaymentService {
    Bills payBill(Long billId, Long userId);
    Bills refundBill(Long billId, Long userId);
    Bills partialRefund(Long billId, Map<Long, Integer> itemRefunds, Long userId);
}