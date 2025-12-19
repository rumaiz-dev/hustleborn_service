package com.hustleborn.service.repository.bills;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hustleborn.service.model.bills.BillStatus;
import com.hustleborn.service.model.bills.Bills;

@Repository
public interface BillRepository extends JpaRepository<Bills, Long> {

    List<Bills> findByStatus(BillStatus status);
}