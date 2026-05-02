package com.internship.tool.vendor_offboarding_manager.config;

import com.internship.tool.vendor_offboarding_manager.entity.Vendor;
import com.internship.tool.vendor_offboarding_manager.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final VendorRepository vendorRepository;

    public DataSeeder(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) {
        if (vendorRepository.count() > 0) {
            return;
        }

        for (int i = 1; i <= 15; i++) {
            Vendor vendor = new Vendor();
            vendor.setVendorName("Vendor " + i);
            vendor.setVendorEmail("vendor" + i + "@example.com");
            vendor.setVendorPhone("98765432" + String.format("%02d", i));
            vendor.setCompanyName("Company " + i);
            vendor.setContractStartDate(LocalDate.now().minusMonths(i));
            vendor.setContractEndDate(LocalDate.now().plusMonths(i));
            vendor.setStatus(i % 3 == 0 ? "PENDING" : i % 2 == 0 ? "INACTIVE" : "ACTIVE");

            vendorRepository.save(vendor);
        }

        System.out.println("15 demo vendor records inserted successfully.");
    }
}