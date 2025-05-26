package com.example.companymanagement.controller;

import com.example.companymanagement.entity.Company;
import com.example.companymanagement.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "companies";
    }

    @GetMapping("/{id}")
    public String getCompanyDetails(@PathVariable Long id, Model model) {
        Company company = companyService.getCompanyById(id);
        if (company == null) {
            return "redirect:/companies";
        }
        model.addAttribute("company", company);
        return "company_details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("company", new Company());
        return "create_company";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Company company = companyService.getCompanyById(id);
        if (company == null) {
            return "redirect:/companies";
        }
        model.addAttribute("company", company);
        return "edit_company";
    }
} 