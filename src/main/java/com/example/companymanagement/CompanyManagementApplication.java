package com.example.companymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@SpringBootApplication
public class CompanyManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyManagementApplication.class, args);
	}

	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("company", new Company());
		return "create_company";
	}

	@PostMapping("/new")
	public String createCompany(
		@ModelAttribute Company company,
		@RequestParam(value = "files", required = false) List<MultipartFile> files,
		RedirectAttributes redirectAttributes
	) {
		// ... your save logic ...
		redirectAttributes.addFlashAttribute("successMessage", "Company added successfully!");
		return "redirect:/companies";
	}

}
