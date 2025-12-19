package com.hustleborn.service.service.inventory;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustleborn.service.model.inventorytransactions.InventoryTransactions;
import com.hustleborn.service.model.inventorytransactions.TransactionType;
import com.hustleborn.service.model.products.StockStatus;
import com.hustleborn.service.model.productvariants.ProductVariants;
import com.hustleborn.service.repository.inventorytransactions.InventoryTransactionsRepository;
import com.hustleborn.service.repository.products.ProductVariantsRepository;

@Service
public class InventoryService {

    @Autowired
    private ProductVariantsRepository productVariantsRepository;

    @Autowired
    private InventoryTransactionsRepository inventoryTransactionsRepository;

    public List<ProductVariants> getAllInventory() {
        return productVariantsRepository.findAll();
    }

    public List<ProductVariants> getLowStockItems() {
        return productVariantsRepository.findByStockStatus(StockStatus.Lowstock);
    }

    public ProductVariants adjustStock(Long variantId, int quantity, String reason, Long userId) {
        ProductVariants variant = productVariantsRepository.findById(variantId).orElse(null);
        if (variant != null) {
            int newQuantity = variant.getStockQuantity() + quantity;
            variant.setStockQuantity(newQuantity);
            variant.setStockStatus(determineStockStatus(newQuantity));
            variant.setUpdatedAt(LocalDateTime.now());

            InventoryTransactions transaction = new InventoryTransactions();
            transaction.setVariant(variant);
            transaction.setTransactionType(TransactionType.Adjustment);
            transaction.setQuantity(quantity);
            transaction.setReason(reason);
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setUserId(userId);
            inventoryTransactionsRepository.save(transaction);

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

            InventoryTransactions transaction = new InventoryTransactions();
            transaction.setVariant(variant);
            transaction.setTransactionType(TransactionType.Sale);
            transaction.setQuantity(-quantity); 
            transaction.setPosReference(posReference);
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setUserId(userId);
            inventoryTransactionsRepository.save(transaction);

            return productVariantsRepository.save(variant);
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

    public List<InventoryTransactions> getTransactionHistory(Long variantId) {
        return inventoryTransactionsRepository.findByVariantId(variantId);
    }
}