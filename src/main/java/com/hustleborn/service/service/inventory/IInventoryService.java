package com.hustleborn.service.service.inventory;

import java.util.List;
import com.hustleborn.service.model.inventorytransactions.InventoryTransactions;
import com.hustleborn.service.model.productvariants.ProductVariants;

public interface IInventoryService {
    List<ProductVariants> getAllInventory();
    List<ProductVariants> getLowStockItems();
    List<ProductVariants> getOutOfStockItems();
    ProductVariants adjustStock(Long variantId, int quantity, String reason, Long userId);
    ProductVariants recordSale(Long variantId, int quantity, String posReference, Long userId);
    ProductVariants recordRefund(Long variantId, int quantity, String reason, Long userId);
    ProductVariants recordRestock(Long variantId, int quantity, String reason, Long userId);
    List<InventoryTransactions> getTransactionHistory(Long variantId);
}