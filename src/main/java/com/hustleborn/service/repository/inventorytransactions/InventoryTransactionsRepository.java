package com.hustleborn.service.repository.inventorytransactions;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hustleborn.service.model.inventorytransactions.InventoryTransactions;
import com.hustleborn.service.model.inventorytransactions.TransactionType;

@Repository
public interface InventoryTransactionsRepository extends JpaRepository<InventoryTransactions, Long> {

    List<InventoryTransactions> findByVariantId(Long variantId);

    List<InventoryTransactions> findByTransactionType(TransactionType transactionType);

    List<InventoryTransactions> findByPosReference(String posReference);
}