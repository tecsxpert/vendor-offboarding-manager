package com.internship.tool.vendor_offboarding_manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Validation added
    @NotBlank(message = "Vendor name is required")
    private String vendorName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Vendor email is required")
    @Column(unique = true)
    private String vendorEmail;

    @NotBlank(message = "Vendor phone is required")
    private String vendorPhone;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private LocalDate contractStartDate;
    private LocalDate contractEndDate;

    // Status should always be present
    @NotBlank(message = "Status is required")
    private String status;

    private LocalDate offboardingDate;
    private String offboardingReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ Auto set timestamps
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 🔹 Getters and Setters

    public Long getId() {
        return id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getVendorPhone() {
        return vendorPhone;
    }

    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone = vendorPhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public LocalDate getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getOffboardingDate() {
        return offboardingDate;
    }

    public void setOffboardingDate(LocalDate offboardingDate) {
        this.offboardingDate = offboardingDate;
    }

    public String getOffboardingReason() {
        return offboardingReason;
    }

    public void setOffboardingReason(String offboardingReason) {
        this.offboardingReason = offboardingReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}