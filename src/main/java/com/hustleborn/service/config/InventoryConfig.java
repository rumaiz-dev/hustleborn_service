package com.hustleborn.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryConfig {

    @Value("${inventory.low.stock.threshold:5}")
    private int lowStockThreshold;

    @Value("${inventory.out.of.stock.threshold:0}")
    private int outOfStockThreshold;

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public int getOutOfStockThreshold() {
        return outOfStockThreshold;
    }
}