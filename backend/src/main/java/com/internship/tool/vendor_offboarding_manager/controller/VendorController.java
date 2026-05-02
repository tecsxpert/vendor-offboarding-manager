package com.internship.tool.vendor_offboarding_manager.controller;

import com.internship.tool.vendor_offboarding_manager.entity.Vendor;
import com.internship.tool.vendor_offboarding_manager.repository.VendorRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        Pageable pageable = PageRequest.of(page, size);

        if (search != null && search.isBlank()) {
            search = null;
        }

        if (status != null && status.isBlank()) {
            status = null;
        }

        return vendorRepository.filterVendors(search, status, pageable);
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
        long pending = vendorRepository.countByStatus("PENDING");

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("active", active);
        stats.put("inactive", inactive);
        stats.put("pending", pending);

        return stats;
    }

    @GetMapping("/export")
    public void exportVendorsToCsv(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=vendors.csv");

        try (PrintWriter writer = response.getWriter()) {
            List<Vendor> vendors = vendorRepository.findAll();

            writer.println("ID,Vendor Name,Email,Phone,Company,Status");

            for (Vendor v : vendors) {
                writer.println(
                        v.getId() + "," +
                        v.getVendorName() + "," +
                        v.getVendorEmail() + "," +
                        v.getVendorPhone() + "," +
                        v.getCompanyName() + "," +
                        v.getStatus()
                );
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/upload")
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("message", "File is empty");
            return response;
        }

        String type = file.getContentType();

        if (!"text/csv".equals(type) &&
                !"application/vnd.ms-excel".equals(type)) {
            response.put("message", "Only CSV files are allowed");
            return response;
        }

        long maxSize = 2 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            response.put("message", "File size exceeds 2MB limit");
            return response;
        }

        response.put("message", "File uploaded successfully");
        response.put("fileName", file.getOriginalFilename());
        response.put("size", String.valueOf(file.getSize()));

        return response;
    }

    @GetMapping("/{id}")
    public Vendor getVendorById(@PathVariable Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
}