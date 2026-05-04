package com.internship.tool.vendor_offboarding_manager.service;

import com.internship.tool.vendor_offboarding_manager.entity.Vendor;
import com.internship.tool.vendor_offboarding_manager.exception.ResourceNotFoundException;
import com.internship.tool.vendor_offboarding_manager.repository.VendorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    // ✅ Status constants (clean code)
    private static final String ACTIVE = "ACTIVE";
    private static final String INACTIVE = "INACTIVE";
    private static final String PENDING = "PENDING";

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    // ✅ Get all vendors
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // ✅ Pagination
    public Page<Vendor> getAllVendorsPaginated(Pageable pageable) {
        return vendorRepository.findAll(pageable);
    }

    // ✅ Create vendor
    public Vendor createVendor(Vendor vendor) {
        vendor.setStatus(ACTIVE);
        return vendorRepository.save(vendor);
    }

    // ✅ Get by ID
    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
    }

    // ✅ Update vendor
    public Vendor updateVendor(Long id, Vendor updatedVendor) {
        Vendor vendor = getVendorById(id);

        vendor.setVendorName(updatedVendor.getVendorName());
        vendor.setVendorEmail(updatedVendor.getVendorEmail());
        vendor.setVendorPhone(updatedVendor.getVendorPhone());
        vendor.setCompanyName(updatedVendor.getCompanyName());
        vendor.setContractStartDate(updatedVendor.getContractStartDate());
        vendor.setContractEndDate(updatedVendor.getContractEndDate());

        // ✅ Update status only if provided
        if (updatedVendor.getStatus() != null) {
            vendor.setStatus(updatedVendor.getStatus());
        }

        return vendorRepository.save(vendor);
    }

    // ✅ Soft delete
    public Vendor deleteVendor(Long id) {
        Vendor vendor = getVendorById(id);
        vendor.setStatus(INACTIVE);
        return vendorRepository.save(vendor);
    }

    // ✅ Search
    public List<Vendor> searchVendors(String q) {
        return vendorRepository
                .findByVendorNameContainingIgnoreCaseOrVendorEmailContainingIgnoreCase(q, q);
    }

    // ✅ Stats
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("total", vendorRepository.count());
        stats.put("active", vendorRepository.countByStatus(ACTIVE));
        stats.put("inactive", vendorRepository.countByStatus(INACTIVE));
        stats.put("pending", vendorRepository.countByStatus(PENDING));

        return stats;
    }
}