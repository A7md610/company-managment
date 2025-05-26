package com.example.companymanagement.controller;

import com.example.companymanagement.entity.Company;
import com.example.companymanagement.entity.CompanyFile;
import com.example.companymanagement.service.CompanyService;
import com.example.companymanagement.service.CompanyFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyFileService companyFileService;

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

    @PostMapping("/new-multipart")
    public String createCompanyMultipart(
            @RequestParam("name") String name,
            @RequestParam("city") String city,
            @RequestParam("employee") String employee,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            RedirectAttributes redirectAttributes
    ) {
        Company company = new Company();
        company.setName(name);
        company.setCity(city);
        company.setEmployee(employee);
        company.setDescription(description);
        companyService.saveCompany(company);

        if (files != null && !files.isEmpty() && files.get(0).getSize() > 0) {
            List<CompanyFile> savedFiles = new ArrayList<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    CompanyFile saved = companyFileService.storeFile(file, company.getId());
                    savedFiles.add(saved);
                }
            }
            company.setFiles(savedFiles);
            companyService.saveCompany(company);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Company added successfully!");
        return "redirect:/companies";
    }
} 
