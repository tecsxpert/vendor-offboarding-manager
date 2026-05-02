package com.internship.tool.service;

import com.internship.tool.model.Vendor;
import com.internship.tool.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    // CREATE
    public Vendor saveVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    // READ ALL
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // READ BY ID
    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id).orElse(null);
    }

    // UPDATE
    public Vendor updateVendor(Long id, Vendor vendorDetails) {
        Vendor vendor = vendorRepository.findById(id).orElse(null);

        if (vendor != null) {
            vendor.setName(vendorDetails.getName());
            vendor.setEmail(vendorDetails.getEmail());
            vendor.setCompany(vendorDetails.getCompany());
            return vendorRepository.save(vendor);
        }

        return null;
    }

    // DELETE
    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }
}