package com.hustleborn.service.service.bills;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustleborn.service.model.bills.BillItems;
import com.hustleborn.service.model.bills.Bills;
import com.hustleborn.service.model.discounts.DiscountType;
import com.hustleborn.service.model.bills.BillStatus;
import com.hustleborn.service.model.inventorytransactions.InventoryTransactions;
import com.hustleborn.service.model.inventorytransactions.TransactionType;
import com.hustleborn.service.model.productvariants.ProductVariants;
import com.hustleborn.service.model.productvariants.StockStatus;
import com.hustleborn.service.repository.bills.BillRepository;
import com.hustleborn.service.repository.inventorytransactions.InventoryTransactionsRepository;
import com.hustleborn.service.repository.products.ProductVariantsRepository;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductVariantsRepository productVariantsRepository;

    @Autowired
    private InventoryTransactionsRepository inventoryTransactionsRepository;

    public List<Bills> getAllBills() {
        return billRepository.findAll();
    }

    public Bills getBillById(Long id) {
        return billRepository.findById(id).orElse(null);
    }

    public Bills createBill(Bills bill) {
        bill.setCreatedAt(LocalDateTime.now());
        bill.setUpdatedAt(LocalDateTime.now());
        bill.setStatus(BillStatus.Pending);

        double total = 0.0;
        if (bill.getBillItems() != null) {
            for (BillItems item : bill.getBillItems()) {
                double itemTotal = item.getUnitPrice() * item.getQuantity();
                item.setTotalPrice(itemTotal);
                total += itemTotal;
            }
        }
        bill.setTotalAmount(total);

        double discountAmount = 0.0;
        if (bill.getDiscountType() != null && bill.getDiscountValue() != null) {
            if (bill.getDiscountType() == DiscountType.Percentage) {
                discountAmount = total * (bill.getDiscountValue() / 100.0);
            } else if (bill.getDiscountType() == DiscountType.Fixed) {
                discountAmount = bill.getDiscountValue();
            }
        }
        bill.setDiscountAmount(discountAmount);
        bill.setFinalAmount(total - discountAmount);

        return billRepository.save(bill);
    }

    public Bills updateBill(Bills bill) {
        bill.setUpdatedAt(LocalDateTime.now());
        return billRepository.save(bill);
    }

    public Bills payBill(Long billId, Long userId) {
        Bills bill = billRepository.findById(billId).orElse(null);
        if (bill != null && bill.getStatus() == BillStatus.Pending) {
            bill.setStatus(BillStatus.Paid);
            bill.setUpdatedAt(LocalDateTime.now());

            // Reduce stock and record inventory transactions
            for (BillItems item : bill.getBillItems()) {
                ProductVariants variant = item.getVariant();
                if (variant.getStockQuantity() >= item.getQuantity()) {
                    variant.setStockQuantity(variant.getStockQuantity() - item.getQuantity());
                    variant.setStockStatus(determineStockStatus(variant.getStockQuantity()));
                    variant.setUpdatedAt(LocalDateTime.now());
                    productVariantsRepository.save(variant);

                    InventoryTransactions transaction = new InventoryTransactions();
                    transaction.setVariant(variant);
                    transaction.setTransactionType(TransactionType.Sale);
                    transaction.setQuantity(-item.getQuantity()); // negative for outgoing
                    transaction.setReason("Bill payment");
                    transaction.setTimestamp(LocalDateTime.now());
                    transaction.setUserId(userId);
                    transaction.setBillId(billId);
                    inventoryTransactionsRepository.save(transaction);
                } else {
                    // Insufficient stock, but since we assume it's checked, perhaps throw error
                    throw new RuntimeException("Insufficient stock for variant " + variant.getId());
                }
            }

            return billRepository.save(bill);
        }
        return null;
    }

    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }

    public Bills refundBill(Long billId, Long userId) {
        Bills bill = billRepository.findById(billId).orElse(null);
        if (bill != null && bill.getStatus() == BillStatus.Paid) {
            bill.setStatus(BillStatus.Refunded);
            bill.setUpdatedAt(LocalDateTime.now());

            for (BillItems item : bill.getBillItems()) {
                ProductVariants variant = item.getVariant();
                int refundQuantity = item.getQuantity() - (item.getRefundedQuantity() != null ? item.getRefundedQuantity() : 0);
                if (refundQuantity > 0) {
                    variant.setStockQuantity(variant.getStockQuantity() + refundQuantity);
                    variant.setStockStatus(determineStockStatus(variant.getStockQuantity()));
                    variant.setUpdatedAt(LocalDateTime.now());
                    productVariantsRepository.save(variant);

                    InventoryTransactions transaction = new InventoryTransactions();
                    transaction.setVariant(variant);
                    transaction.setTransactionType(TransactionType.Refund);
                    transaction.setQuantity(refundQuantity);
                    transaction.setReason("Bill refund");
                    transaction.setTimestamp(LocalDateTime.now());
                    transaction.setUserId(userId);
                    transaction.setBillId(billId);
                    inventoryTransactionsRepository.save(transaction);

                    item.setRefundedQuantity(item.getQuantity());
                }
            }
            return billRepository.save(bill);
        }
        return null;
    }

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
                        ProductVariants variant = item.getVariant();
                        variant.setStockQuantity(variant.getStockQuantity() + qty);
                        variant.setStockStatus(determineStockStatus(variant.getStockQuantity()));
                        variant.setUpdatedAt(LocalDateTime.now());
                        productVariantsRepository.save(variant);

                        InventoryTransactions transaction = new InventoryTransactions();
                        transaction.setVariant(variant);
                        transaction.setTransactionType(TransactionType.Refund);
                        transaction.setQuantity(qty);
                        transaction.setReason("Partial bill refund");
                        transaction.setTimestamp(LocalDateTime.now());
                        transaction.setUserId(userId);
                        transaction.setBillId(billId);
                        inventoryTransactionsRepository.save(transaction);

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

    private StockStatus determineStockStatus(int quantity) {
        if (quantity <= 0) {
            return StockStatus.Outofstock;
        } else if (quantity <= 5) {
            return StockStatus.Lowstock;
        } else {
            return StockStatus.Instock;
        }
    }
}