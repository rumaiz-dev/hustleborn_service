package com.hustleborn.service.service.reports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hustleborn.service.model.bills.Bills;
import com.hustleborn.service.model.billitems.BillItems;
import com.hustleborn.service.model.bills.BillStatus;
import com.hustleborn.service.model.productvariants.ProductVariants;
import com.hustleborn.service.model.products.Products;
import com.hustleborn.service.model.reports.ProfitReport;
import com.hustleborn.service.repository.bills.BillRepository;

public class ReportingServiceTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private ReportingService reportingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProfitReport() {
        // Create mock product
        Products product = new Products();
        product.setPurchasingPrice(10.0);

        // Create mock variant
        ProductVariants variant = new ProductVariants();
        variant.setProduct(product);

        // Create mock bill item
        BillItems item = new BillItems();
        item.setVariant(variant);
        item.setQuantity(2);

        // Create mock bill
        Bills bill = new Bills();
        bill.setStatus(BillStatus.Paid);
        bill.setFinalAmount(25.0);
        bill.setDiscountAmount(5.0);
        bill.setBillItems(Arrays.asList(item));

        List<Bills> bills = Arrays.asList(bill);

        when(billRepository.findByStatus(BillStatus.Paid)).thenReturn(bills);

        ProfitReport report = reportingService.getProfitReport();

        assertNotNull(report);
        assertEquals(25.0, report.getTotalSalesVolume());
        assertEquals(25.0 - 20.0, report.getGrossProfit()); // 25 - (2*10)
        assertEquals(25.0 - 20.0 - 5.0, report.getNetProfit()); // gross - discounts - damagedLoss (0) - expenses (0)
        assertEquals(0.0, report.getDamagedLoss());
        assertEquals(0.0, report.getTotalExpenses());
    }
}