package com.internship.tool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.internship.tool.entity.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
}