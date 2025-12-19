package com.hustleborn.service.repository.bills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hustleborn.service.model.bills.Bills;

@Repository
public interface BillRepository extends JpaRepository<Bills, Long> {

}