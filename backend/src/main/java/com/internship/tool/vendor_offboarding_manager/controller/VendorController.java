package com.internship.tool.vendor_offboarding_manager.controller;

import com.internship.tool.vendor_offboarding_manager.entity.Vendor;
import com.internship.tool.vendor_offboarding_manager.service.VendorService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vendors")
@CrossOrigin(origins = "*")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    // ✅ Get all vendors
    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }

    // ✅ Pagination
    @GetMapping("/all")
    public Page<Vendor> getAllVendorsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return vendorService.getAllVendorsPaginated(pageable);
    }

    // ✅ Create vendor (with validation)
    @PostMapping
    public Vendor createVendor(@Valid @RequestBody Vendor vendor) {
        return vendorService.createVendor(vendor);
    }

    // ✅ Update vendor (with validation)
    @PutMapping("/{id}")
    public Vendor updateVendor(
            @PathVariable Long id,
            @Valid @RequestBody Vendor vendor
    ) {
        return vendorService.updateVendor(id, vendor);
    }

    // ✅ Soft delete
    @DeleteMapping("/{id}")
    public Vendor deleteVendor(@PathVariable Long id) {
        return vendorService.deleteVendor(id);
    }

    // ✅ Search
    @GetMapping("/search")
    public List<Vendor> searchVendors(@RequestParam String q) {
        return vendorService.searchVendors(q);
    }

    // ✅ Stats
    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        return vendorService.getStats();
    }

    // ✅ Get by ID
    @GetMapping("/{id}")
    public Vendor getVendorById(@PathVariable Long id) {
        return vendorService.getVendorById(id);
    }
}