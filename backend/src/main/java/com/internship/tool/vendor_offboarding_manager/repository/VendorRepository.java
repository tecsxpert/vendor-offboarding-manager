package com.internship.tool.vendor_offboarding_manager.repository;

import com.internship.tool.vendor_offboarding_manager.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    // Search
    List<Vendor> findByVendorNameContainingIgnoreCaseOrVendorEmailContainingIgnoreCase(
            String name, String email
    );

    // Status count (VERY IMPORTANT - fixes your error)
    long countByStatus(String status);

    // Filter + pagination
    @Query("""
        SELECT v FROM Vendor v
        WHERE 
        (:search IS NULL OR LOWER(v.vendorName) LIKE LOWER(CONCAT('%', :search, '%')) 
        OR LOWER(v.vendorEmail) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:status IS NULL OR v.status = :status)
    """)
    Page<Vendor> filterVendors(
            @Param("search") String search,
            @Param("status") String status,
            Pageable pageable
    );
}