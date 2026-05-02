package com.internship.tool.vendor_offboarding_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.vendor_offboarding_manager.entity.Vendor;
import com.internship.tool.vendor_offboarding_manager.repository.VendorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String uniqueEmail(String prefix) {
        return prefix + System.currentTimeMillis() + "@gmail.com";
    }

    private Vendor createSampleVendor(String name, String emailPrefix) {
        Vendor vendor = new Vendor();
        vendor.setVendorName(name);
        vendor.setVendorEmail(uniqueEmail(emailPrefix));
        vendor.setVendorPhone("9876543210");
        vendor.setCompanyName("Test Company");
        vendor.setStatus("ACTIVE");
        return vendor;
    }

    @Test
    void testGetAllVendors() throws Exception {
        mockMvc.perform(get("/vendors"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllVendorsPaginated() throws Exception {
        mockMvc.perform(get("/vendors/all")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateVendor() throws Exception {
        Vendor vendor = createSampleVendor("Test Vendor", "create");

        mockMvc.perform(post("/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendor)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetVendorById() throws Exception {
        Vendor saved = vendorRepository.save(createSampleVendor("ABC Vendor", "abc"));

        mockMvc.perform(get("/vendors/" + saved.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateVendor() throws Exception {
        Vendor saved = vendorRepository.save(createSampleVendor("Old Vendor", "old"));

        saved.setVendorName("Updated Vendor");

        mockMvc.perform(put("/vendors/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteVendor() throws Exception {
        Vendor saved = vendorRepository.save(createSampleVendor("Delete Vendor", "delete"));

        mockMvc.perform(delete("/vendors/" + saved.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchVendor() throws Exception {
        Vendor saved = vendorRepository.save(createSampleVendor("Search Vendor", "search"));

        mockMvc.perform(get("/vendors/search")
                        .param("q", "Search"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetStats() throws Exception {
        mockMvc.perform(get("/vendors/stats"))
                .andExpect(status().isOk());
    }
}