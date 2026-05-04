package com.internship.tool.vendor_offboarding_manager.service;

import com.internship.tool.vendor_offboarding_manager.entity.Vendor;
import com.internship.tool.vendor_offboarding_manager.repository.VendorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    // Get all vendors
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // Paginated vendors
    public Page<Vendor> getAllVendorsPaginated(Pageable pageable) {
        return vendorRepository.findAll(pageable);
    }

    // Create vendor
    public Vendor createVendor(Vendor vendor) {
        vendor.setStatus("ACTIVE");
        return vendorRepository.save(vendor);
    }

    // Update vendor
    public Vendor updateVendor(Long id, Vendor updatedVendor) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setVendorName(updatedVendor.getVendorName());
        vendor.setVendorEmail(updatedVendor.getVendorEmail());
        vendor.setVendorPhone(updatedVendor.getVendorPhone());
        vendor.setCompanyName(updatedVendor.getCompanyName());
        vendor.setContractStartDate(updatedVendor.getContractStartDate());
        vendor.setContractEndDate(updatedVendor.getContractEndDate());
        vendor.setStatus(updatedVendor.getStatus());

        return vendorRepository.save(vendor);
    }

    // Soft delete
    public Vendor softDeleteVendor(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setStatus("INACTIVE");
        return vendorRepository.save(vendor);
    }

    // Search
    public List<Vendor> searchVendors(String q) {
        return vendorRepository
                .findByVendorNameContainingIgnoreCaseOrVendorEmailContainingIgnoreCase(q, q);
    }

    // Stats
    public long getTotal() {
        return vendorRepository.count();
    }

    public long getActive() {
        return vendorRepository.countByStatus("ACTIVE");
    }

    public long getInactive() {
        return vendorRepository.countByStatus("INACTIVE");
    }

    public long getPending() {
        return vendorRepository.countByStatus("PENDING");
    }

    // Get by ID
    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
}