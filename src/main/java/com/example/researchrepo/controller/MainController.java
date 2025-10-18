package com.example.researchrepo.controller;

import com.example.researchrepo.service.UserService;
import com.example.researchrepo.service.ResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResearchService researchService;

    @GetMapping("/")
    public String loginRedirect() {
        return "redirect:/Templates";
    }

    @GetMapping("/Templates")
    public String landingPage() {
        return "landing_page";
    }

    @PostMapping("/Templates")
    public String landingPagePost() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginGet() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String username, 
                           @RequestParam String password, 
                           Model model) {
        if (username.isEmpty()) {
            model.addAttribute("message", "haha, i fixed that bug hahahaha");
            return "login";
        }

        if (!userService.findByUsername(username).isPresent()) {
            model.addAttribute("message", "Invalid username");
            return "login";
        }

        if (userService.validateUser(username, password)) {
            return "home";
        } else {
            model.addAttribute("message", "Invalid password");
            return "login";
        }
    }

    @GetMapping("/create_user")
    public String createUserGet() {
        return "create_user";
    }

    @PostMapping("/create_user")
    public String createUserPost(@RequestParam("new_username") String newUsername,
                                @RequestParam("new_password") String newPassword,
                                Model model) {
        if (newUsername.isEmpty() || newUsername.equals(newPassword)) {
            model.addAttribute("message", "invalid entry!!!");
            return "create_user";
        }

        if (userService.userExists(newUsername)) {
            model.addAttribute("message", "Username already exists");
            return "create_user";
        }

        userService.createUser(newUsername, newPassword);
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminGet() {
        return "admin";
    }

    @PostMapping("/admin")
    public String adminPost(@RequestParam(required = false) String department,
                           @RequestParam(required = false) String info,
                           @RequestParam(required = false) String filter,
                           @RequestParam(name = "text-input", required = false) String textInput,
                           Model model) {
        
        System.out.println("=== ADMIN POST DEBUG ===");
        System.out.println("Department: " + department);
        System.out.println("Info: " + info);
        System.out.println("Filter: " + filter);
        System.out.println("Text Input: " + textInput);
        
        if (department != null && !department.isEmpty()) {
            try {
                System.out.println("Getting counts for department: " + department);
                int[] counts = researchService.getDepartmentCounts(department);
                StringBuilder countsStr = new StringBuilder();
                for (int i = 0; i < counts.length; i++) {
                    if (i > 0) countsStr.append(",");
                    countsStr.append(counts[i]);
                }
                System.out.println("Counts: " + countsStr.toString());
                model.addAttribute("counts", countsStr.toString());
                return "admin";
            } catch (Exception e) {
                System.err.println("Error getting counts: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("message", "Invalid department");
                return "admin";
            }
        }

        if (info != null && !info.isEmpty()) {
            try {
                String infoType = info.toUpperCase();
                String filterOption = (filter != null) ? filter.toUpperCase() : "";
                String textInputUpper = (textInput != null) ? textInput.toUpperCase() : "";

                System.out.println("Fetching data for table: " + infoType);
                System.out.println("Filter option: " + filterOption);
                System.out.println("Text input: " + textInputUpper);

                List<Map<String, Object>> data = researchService.getTableData(infoType, filterOption, textInputUpper);
                
                System.out.println("Rows retrieved: " + data.size());
                
                // Convert department IDs to names
                for (Map<String, Object> row : data) {
                    if (row.containsKey("DEPARTMENT_ID")) {
                        Object deptId = row.get("DEPARTMENT_ID");
                        System.out.println("Converting dept ID: " + deptId);
                        row.put("DEPARTMENT_ID", researchService.convertDepartmentIdToName(deptId));
                    }
                }

                model.addAttribute("data", data);
                model.addAttribute("selected_table", infoType);
                System.out.println("Data added to model");
                return "admin";
            } catch (Exception e) {
                System.err.println("Error retrieving data: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("message", "Error retrieving data: " + e.getMessage());
                return "admin";
            }
        }

        System.out.println("No action taken - returning admin page");
        return "admin";
    }

    @GetMapping("/user")
    public String userGet(Model model) {
        model.addAttribute("message", "Input not found");
        return "user";
    }

    @PostMapping("/user")
    public String userPost(@RequestParam String department,
                          @RequestParam Map<String, String> allParams,
                          Model model) {
        
        // Debug logging
        System.out.println("=== USER POST DEBUG ===");
        System.out.println("Department: " + department);
        System.out.println("All Parameters:");
        allParams.forEach((key, value) -> {
            if (!key.equals("department")) {
                System.out.println("  " + key + " = " + value);
            }
        });
        System.out.println("======================");
        
        try {
            // Check which form was submitted and save accordingly
            if (allParams.containsKey("Journal-Authors") && !allParams.get("Journal-Authors").isEmpty()) {
                System.out.println("Saving Journal data...");
                researchService.saveJournalData(department, allParams);
                model.addAttribute("message", "Journal data submitted successfully!");
            }
            else if (allParams.containsKey("Conference-Authors") && !allParams.get("Conference-Authors").isEmpty()) {
                researchService.saveConferenceData(department, allParams);
                model.addAttribute("message", "Conference data submitted successfully!");
            }
            else if (allParams.containsKey("BookChapter-Authors") && !allParams.get("BookChapter-Authors").isEmpty()) {
                researchService.saveBookChapterData(department, allParams);
                model.addAttribute("message", "Book Chapter data submitted successfully!");
            }
            else if (allParams.containsKey("FundedResearchProject-Principal investigator") && 
                     !allParams.get("FundedResearchProject-Principal investigator").isEmpty()) {
                researchService.saveFundedResearchProjectData(department, allParams);
                model.addAttribute("message", "Funded Research Project data submitted successfully!");
            }
            else if (allParams.containsKey("ResearchProposalSubmitted-Principal investigator") && 
                     !allParams.get("ResearchProposalSubmitted-Principal investigator").isEmpty()) {
                researchService.saveResearchProposalSubmittedData(department, allParams);
                model.addAttribute("message", "Research Proposal data submitted successfully!");
            }
            else if (allParams.containsKey("Consultancy-Faculty name") && 
                     !allParams.get("Consultancy-Faculty name").isEmpty()) {
                researchService.saveConsultancyData(department, allParams);
                model.addAttribute("message", "Consultancy data submitted successfully!");
            }
            else if (allParams.containsKey("ProductDevelopment-Faculty name") && 
                     !allParams.get("ProductDevelopment-Faculty name").isEmpty()) {
                researchService.saveProductDevelopmentData(department, allParams);
                model.addAttribute("message", "Product Development data submitted successfully!");
            }
            else if (allParams.containsKey("Patent-Inventor name") && 
                     !allParams.get("Patent-Inventor name").isEmpty()) {
                researchService.savePatentData(department, allParams);
                model.addAttribute("message", "Patent data submitted successfully!");
            }
            else if (allParams.containsKey("FDPWorkshopSeminar-Faculty name") && 
                     !allParams.get("FDPWorkshopSeminar-Faculty name").isEmpty()) {
                researchService.saveFDPWorkshopSeminarData(department, allParams);
                model.addAttribute("message", "FDP/Workshop/Seminar data submitted successfully!");
            }
            else if (allParams.containsKey("MOUCS-Organization") && 
                     !allParams.get("MOUCS-Organization").isEmpty()) {
                researchService.saveMOUCSData(department, allParams);
                model.addAttribute("message", "MOUCS data submitted successfully!");
            }
            else if (allParams.containsKey("AchievementsAndAwards-Faculty/Student name") && 
                     !allParams.get("AchievementsAndAwards-Faculty/Student name").isEmpty()) {
                researchService.saveAchievementsAndAwardsData(department, allParams);
                model.addAttribute("message", "Achievements and Awards data submitted successfully!");
            }
            else if (allParams.containsKey("MOUS-Organization") && 
                     !allParams.get("MOUS-Organization").isEmpty()) {
                researchService.saveMOUSData(department, allParams);
                model.addAttribute("message", "MOUS data submitted successfully!");
            }
            else if (allParams.containsKey("FundedStudentProject-Student name") && 
                     !allParams.get("FundedStudentProject-Student name").isEmpty()) {
                researchService.saveFundedStudentProjectData(department, allParams);
                model.addAttribute("message", "Funded Student Project data submitted successfully!");
            }
            else {
                model.addAttribute("message", "No valid form data found!");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "user";
    }
}