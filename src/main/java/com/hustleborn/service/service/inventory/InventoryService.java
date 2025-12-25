package com.hustleborn.service.service.inventory;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustleborn.service.config.InventoryConfig;
import com.hustleborn.service.model.inventorytransactions.InventoryTransactions;
import com.hustleborn.service.model.inventorytransactions.TransactionType;
import com.hustleborn.service.model.productvariants.ProductVariants;
import com.hustleborn.service.model.productvariants.StockStatus;
import com.hustleborn.service.repository.inventorytransactions.InventoryTransactionsRepository;
import com.hustleborn.service.repository.productvariants.ProductVariantsRepository;
import com.hustleborn.service.service.inventorytransaction.InventoryTransactionService;

@Service
public class InventoryService implements IInventoryService {

    @Autowired
    private ProductVariantsRepository productVariantsRepository;

    @Autowired
    private InventoryTransactionsRepository inventoryTransactionsRepository;

    @Autowired
    private InventoryConfig inventoryConfig;

    @Autowired
    private InventoryTransactionService inventoryTransactionService;

    public List<ProductVariants> getAllInventory() {
        return productVariantsRepository.findAll();
    }

    public List<ProductVariants> getLowStockItems() {
        return productVariantsRepository.findByStockStatus(StockStatus.Lowstock);
    }

    public List<ProductVariants> getOutOfStockItems() {
        return productVariantsRepository.findByStockStatus(StockStatus.Outofstock);
    }

    public ProductVariants adjustStock(Long variantId, int quantity, String reason, Long userId) {
        ProductVariants variant = productVariantsRepository.findById(variantId).orElse(null);
        if (variant != null) {
            int newQuantity = variant.getStockQuantity() + quantity;
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Adjustment would result in negative stock quantity");
            }
            variant.setStockQuantity(newQuantity);
            variant.setStockStatus(determineStockStatus(newQuantity));
            variant.setUpdatedAt(LocalDateTime.now());

            inventoryTransactionService.logTransaction(variant, TransactionType.Adjustment, quantity, reason, null, userId);

            return productVariantsRepository.save(variant);
        }
        return null;
    }

    public ProductVariants recordSale(Long variantId, int quantity, String posReference, Long userId) {
        ProductVariants variant = productVariantsRepository.findById(variantId).orElse(null);
        if (variant != null && variant.getStockQuantity() >= quantity) {
            int newQuantity = variant.getStockQuantity() - quantity;
            variant.setStockQuantity(newQuantity);
            variant.setStockStatus(determineStockStatus(newQuantity));
            variant.setUpdatedAt(LocalDateTime.now());

            inventoryTransactionService.logTransaction(variant, TransactionType.Sale, -quantity, null, posReference, userId);

            return productVariantsRepository.save(variant);
        }
        return null;
    }

    public ProductVariants recordRefund(Long variantId, int quantity, String reason, Long userId) {
        ProductVariants variant = productVariantsRepository.findById(variantId).orElse(null);
        if (variant != null) {
            int newQuantity = variant.getStockQuantity() + quantity;
            variant.setStockQuantity(newQuantity);
            variant.setStockStatus(determineStockStatus(newQuantity));
            variant.setUpdatedAt(LocalDateTime.now());

            inventoryTransactionService.logTransaction(variant, TransactionType.Refund, quantity, reason, null, userId);

            return productVariantsRepository.save(variant);
        }
        return null;
    }

    public ProductVariants recordRestock(Long variantId, int quantity, String reason, Long userId) {
        ProductVariants variant = productVariantsRepository.findById(variantId).orElse(null);
        if (variant != null) {
            int newQuantity = variant.getStockQuantity() + quantity;
            variant.setStockQuantity(newQuantity);
            variant.setStockStatus(determineStockStatus(newQuantity));
            variant.setUpdatedAt(LocalDateTime.now());

            inventoryTransactionService.logTransaction(variant, TransactionType.Restock, quantity, reason, null, userId);

            return productVariantsRepository.save(variant);
        }
        return null;
    }

    private StockStatus determineStockStatus(int quantity) {
        if (quantity <= inventoryConfig.getOutOfStockThreshold()) {
            return StockStatus.Outofstock;
        } else if (quantity <= inventoryConfig.getLowStockThreshold()) {
            return StockStatus.Lowstock;
        } else {
            return StockStatus.Instock;
        }
    }

    public List<InventoryTransactions> getTransactionHistory(Long variantId) {
        return inventoryTransactionsRepository.findByVariantId(variantId);
    }
}