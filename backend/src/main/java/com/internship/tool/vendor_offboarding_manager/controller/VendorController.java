package com.internship.tool.vendor_offboarding_manager.controller;

import com.internship.tool.vendor_offboarding_manager.entity.Vendor;
import com.internship.tool.vendor_offboarding_manager.service.VendorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }

    @GetMapping("/all")
    public Page<Vendor> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return vendorService.getAllVendorsPaginated(PageRequest.of(page, size));
    }

    @PostMapping
    public Vendor createVendor(@RequestBody Vendor vendor) {
        return vendorService.createVendor(vendor);
    }

    @GetMapping("/{id}")
    public Vendor getVendor(@PathVariable Long id) {
        return vendorService.getVendorById(id);
    }

    @PutMapping("/{id}")
    public Vendor updateVendor(@PathVariable Long id, @RequestBody Vendor vendor) {
        return vendorService.updateVendor(id, vendor);
    }

    @DeleteMapping("/{id}")
    public Vendor deleteVendor(@PathVariable Long id) {
        return vendorService.softDeleteVendor(id);
    }

    @GetMapping("/search")
    public List<Vendor> search(@RequestParam String q) {
        return vendorService.searchVendors(q);
    }

    @GetMapping("/stats")
    public Map<String, Long> stats() {
        Map<String, Long> map = new HashMap<>();
        map.put("total", vendorService.getTotal());
        map.put("active", vendorService.getActive());
        map.put("inactive", vendorService.getInactive());
        map.put("pending", vendorService.getPending());
        return map;
    }
}