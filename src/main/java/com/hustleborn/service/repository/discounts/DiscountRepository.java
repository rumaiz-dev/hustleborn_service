package com.hustleborn.service.repository.discounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hustleborn.service.model.discounts.Discounts;

@Repository
public interface DiscountRepository extends JpaRepository<Discounts, Long> {

}