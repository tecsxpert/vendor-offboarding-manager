package com.internship.tool.vendor_offboarding_manager.repository;

import com.internship.tool.vendor_offboarding_manager.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
}