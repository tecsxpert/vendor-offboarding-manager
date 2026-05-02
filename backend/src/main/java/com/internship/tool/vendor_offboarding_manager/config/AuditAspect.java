package com.internship.tool.vendor_offboarding_manager.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {

    @After("execution(* com.internship.tool.vendor_offboarding_manager.controller.VendorController.createVendor(..))")
    public void logCreate(JoinPoint joinPoint) {
        System.out.println("Vendor Created: " + joinPoint.getArgs()[0]);
    }

    @After("execution(* com.internship.tool.vendor_offboarding_manager.controller.VendorController.updateVendor(..))")
    public void logUpdate(JoinPoint joinPoint) {
        System.out.println("Vendor Updated: ID = " + joinPoint.getArgs()[0]);
    }

    @After("execution(* com.internship.tool.vendor_offboarding_manager.controller.VendorController.softDeleteVendor(..))")
    public void logDelete(JoinPoint joinPoint) {
        System.out.println("Vendor Deleted: ID = " + joinPoint.getArgs()[0]);
    }
}