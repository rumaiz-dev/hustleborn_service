package com.hustleborn.service.service.inventorytransaction;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustleborn.service.model.inventorytransactions.InventoryTransactions;
import com.hustleborn.service.model.inventorytransactions.TransactionType;
import com.hustleborn.service.model.productvariants.ProductVariants;
import com.hustleborn.service.repository.inventorytransactions.InventoryTransactionsRepository;

@Service
public class InventoryTransactionService {

    @Autowired
    private InventoryTransactionsRepository inventoryTransactionsRepository;

    public void logTransaction(ProductVariants variant, TransactionType transactionType, int quantity, String reason, String posReference, Long userId) {
        InventoryTransactions transaction = new InventoryTransactions();
        transaction.setVariant(variant);
        transaction.setTransactionType(transactionType);
        transaction.setQuantity(quantity);
        transaction.setReason(reason);
        transaction.setPosReference(posReference);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setUserId(userId);
        inventoryTransactionsRepository.save(transaction);
    }
}