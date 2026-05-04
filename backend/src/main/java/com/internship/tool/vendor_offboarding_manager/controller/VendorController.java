package com.internship.tool.vendor_offboarding_manager.controller;
import java.util.Map;
import com.internship.tool.vendor_offboarding_manager.entity.Vendor;
import com.internship.tool.vendor_offboarding_manager.repository.VendorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/vendors")
@CrossOrigin(origins = "*")
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @GetMapping("/all")
    public Page<Vendor> getAllVendorsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return vendorRepository.findAll(pageable);
    }

    @PostMapping
    public Vendor createVendor(@RequestBody Vendor vendor) {
        vendor.setStatus("ACTIVE");
        return vendorRepository.save(vendor);
    }

    @PutMapping("/{id}")
    public Vendor updateVendor(@PathVariable Long id, @RequestBody Vendor updatedVendor) {
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

    @DeleteMapping("/{id}")
    public Vendor softDeleteVendor(@PathVariable Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setStatus("INACTIVE");
        return vendorRepository.save(vendor);
    }

    @GetMapping("/search")
    public List<Vendor> searchVendors(@RequestParam String q) {
        return vendorRepository
                .findByVendorNameContainingIgnoreCaseOrVendorEmailContainingIgnoreCase(q, q);
    }

  @GetMapping("/stats")
public Map<String, Long> getStats() {

    long total = vendorRepository.count();
    long active = vendorRepository.countByStatus("ACTIVE");
    long inactive = vendorRepository.countByStatus("INACTIVE");
    long pending = vendorRepository.countByStatus("PENDING"); // ✅ here

    Map<String, Long> stats = new HashMap<>();
    stats.put("total", total);
    stats.put("active", active);
    stats.put("inactive", inactive);
    stats.put("pending", pending); // ✅ here

    return stats;

}
@GetMapping("/{id}")
public Vendor getVendorById(@PathVariable Long id) {
    return vendorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vendor not found"));
}
}