package com.hustleborn.service.service.bills;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustleborn.service.model.bills.Bills;
import com.hustleborn.service.model.bills.BillStatus;
import com.hustleborn.service.repository.bills.BillRepository;

@Service
public class BillManagementService implements IBillManagementService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillCalculator billCalculator;

    @Override
    public List<Bills> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public Bills getBillById(Long id) {
        return billRepository.findById(id).orElse(null);
    }

    @Override
    public Bills createBill(Bills bill) {
        bill.setCreatedAt(LocalDateTime.now());
        bill.setUpdatedAt(LocalDateTime.now());
        bill.setStatus(BillStatus.Pending);

        billCalculator.calculateBillAmounts(bill);

        return billRepository.save(bill);
    }

    @Override
    public Bills updateBill(Bills bill) {
        bill.setUpdatedAt(LocalDateTime.now());
        return billRepository.save(bill);
    }

    @Override
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }
}