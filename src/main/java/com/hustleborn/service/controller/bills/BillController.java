package com.hustleborn.service.controller.bills;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hustleborn.service.model.bills.Bills;
import com.hustleborn.service.service.bills.BillService;
import com.hustleborn.service.utils.exceptions.ApiException;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping
    public ResponseEntity<List<Bills>> getAllBills() {
        try {
            return ResponseEntity.ok(billService.getAllBills());
        } catch (Exception e) {
            throw new ApiException("Unable to fetch bills", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bills> getBill(@PathVariable Long id) {
        try {
            Bills bill = billService.getBillById(id);
            if (bill != null) {
                return ResponseEntity.ok(bill);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ApiException("Unable to fetch bill", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Bills> createBill(@RequestBody Bills bill) {
        try {
            return ResponseEntity.ok(billService.createBill(bill));
        } catch (Exception e) {
            throw new ApiException("Unable to create bill", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bills> updateBill(@PathVariable Long id, @RequestBody Bills bill) {
        try {
            bill.setId(id);
            return ResponseEntity.ok(billService.updateBill(bill));
        } catch (Exception e) {
            throw new ApiException("Unable to update bill", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<Bills> refundBill(@PathVariable Long id, @RequestParam Long userId) {
        try {
            Bills refundedBill = billService.refundBill(id, userId);
            if (refundedBill != null) {
                return ResponseEntity.ok(refundedBill);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            throw new ApiException("Unable to refund bill", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/partial-refund")
    public ResponseEntity<Bills> partialRefund(@PathVariable Long id, @RequestBody Map<Long, Integer> itemRefunds, @RequestParam Long userId) {
        try {
            Bills refundedBill = billService.partialRefund(id, itemRefunds, userId);
            if (refundedBill != null) {
                return ResponseEntity.ok(refundedBill);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            throw new ApiException("Unable to process partial refund", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}