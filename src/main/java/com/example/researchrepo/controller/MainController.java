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
        
        if (department != null && !department.isEmpty()) {
            try {
                int[] counts = researchService.getDepartmentCounts(department);
                StringBuilder countsStr = new StringBuilder();
                for (int i = 0; i < counts.length; i++) {
                    if (i > 0) countsStr.append(",");
                    countsStr.append(counts[i]);
                }
                model.addAttribute("counts", countsStr.toString());
                return "admin";
            } catch (Exception e) {
                model.addAttribute("message", "Invalid department");
                return "admin";
            }
        }

        if (info != null && !info.isEmpty()) {
            try {
                String infoType = info.toUpperCase();
                String filterOption = (filter != null) ? filter.toUpperCase() : "";
                String textInputUpper = (textInput != null) ? textInput.toUpperCase() : "";

                List<Map<String, Object>> data = researchService.getTableData(infoType, filterOption, textInputUpper);
                
                // Convert department IDs to names
                for (Map<String, Object> row : data) {
                    if (row.containsKey("DEPARTMENT_ID")) {
                        Object deptId = row.get("DEPARTMENT_ID");
                        row.put("DEPARTMENT_ID", researchService.convertDepartmentIdToName(deptId));
                    }
                }

                model.addAttribute("data", data);
                model.addAttribute("selected_table", infoType);
                return "admin";
            } catch (Exception e) {
                model.addAttribute("message", "Error retrieving data: " + e.getMessage());
                return "admin";
            }
        }

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
        try {
            // Check which form was submitted and save accordingly
            if (allParams.containsKey("Journal-Authors") && !allParams.get("Journal-Authors").isEmpty()) {
                researchService.saveJournalData(department, allParams);
            }
            
            if (allParams.containsKey("Conference-Authors") && !allParams.get("Conference-Authors").isEmpty()) {
                researchService.saveConferenceData(department, allParams);
            }
            
            if (allParams.containsKey("BookChapter-Authors") && !allParams.get("BookChapter-Authors").isEmpty()) {
                researchService.saveBookChapterData(department, allParams);
            }

            model.addAttribute("message", "Data submitted successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        
        return "user";
    }
}