package com.example.researchrepo.service;

import com.example.researchrepo.model.Department;
import com.example.researchrepo.repository.ResearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResearchService {

    @Autowired
    private ResearchRepository researchRepository;

    public int[] getDepartmentCounts(String departmentName) {
        Department dept = Department.fromName(departmentName);
        return researchRepository.getCountsByDepartment(dept.getId());
    }

    public List<Map<String, Object>> getTableData(String tableName, String filterOption, String textInput) {
        // If filter is "All" or same as text input, get all records
        if (filterOption.equalsIgnoreCase(textInput) || filterOption.equalsIgnoreCase("ALL")) {
            return researchRepository.getAllFromTable(tableName);
        } 
        // If filtering by department
        else if (filterOption.equalsIgnoreCase("DEPARTMENT")) {
            Department dept = Department.fromName(textInput);
            return researchRepository.getByDepartment(tableName, dept.getId());
        } 
        // Filter by specific column
        else {
            // Map frontend filter names to actual database column names
            String dbColumnName = mapFilterToColumn(tableName, filterOption);
            return researchRepository.getByFilter(tableName, dbColumnName, textInput);
        }
    }
    
    private String mapFilterToColumn(String tableName, String filterOption) {
        // Map common filter names to database column names
        switch (filterOption.toUpperCase().replace(" ", "_")) {
            case "AUTHORS": return "AUTHORS";
            case "AUTHOR": return "AUTHOR";
            case "YEAR_OF_PUBLICATION": return "YEAR_OF_PUBLICATION";
            case "TITLE": return "TITLE";
            case "JOURNAL_NAME": return "JOURNAL_NAME";
            case "VOLUME_PAGE_NUMBER": return "VOLUME_PAGE_NUMBER";
            case "VOLUME_&_PAGE_COUNT": return "VOLUME_PAGE_COUNT";
            case "ISSN": return "ISSN";
            case "IMPACT_FACTOR": return "IMPACT_FACTOR";
            case "CONFERENCE_NAME": return "CONFERENCE_NAME";
            case "ORGANIZED_BY": return "ORGANIZED_BY";
            case "PLACE_OF_CONFERENCE": return "PLACE_OF_CONFERENCE";
            case "CHAPTER_TITLE": return "CHAPTER_TITLE";
            case "BOOK_TITLE": return "BOOK_TITLE";
            case "PUBLISHER": return "PUBLISHER";
            case "PRINCIPAL_INVESTIGATION": return "PRINCIPAL_INVESTIGATOR";
            case "PRINCIPAL_INVESTIGATOR": return "PRINCIPAL_INVESTIGATOR";
            case "CO-INVESTIGATOR": return "CO_INVESTIGATOR";
            case "COPI": return "CO_INVESTIGATOR";
            default: return filterOption.toUpperCase().replace(" ", "_");
        }
    }

    public void saveJournalData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        String authors = formData.get("Journal-Authors");
        if (authors == null || authors.trim().isEmpty()) {
            throw new IllegalArgumentException("Authors field is required for Journal");
        }
        researchRepository.insertJournal(
            dept.getId(),
            authors,
            formData.get("Journal-Year of publication"),
            formData.get("Journal-Title"),
            formData.get("Journal-Journal name"),
            formData.get("Journal-Volume and page number"),
            formData.get("Journal-ISSN"),
            formData.get("Journal-Impact factor")
        );
    }

    public void saveConferenceData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertConference(
            dept.getId(),
            formData.get("Conference-Year of publication"),
            formData.get("Conference-Authors"),
            formData.get("Conference-Title"),
            formData.get("Conference-Conference Name"),
            formData.get("Conference-Volume and page count"),
            formData.get("Conference-Organized by"),
            formData.get("Conference-Place of conference")
        );
    }

    public void saveBookChapterData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertBookChapter(
            dept.getId(),
            formData.get("BookChapter-Year of publication"),
            formData.get("BookChapter-Authors"),
            formData.get("BookChapter-Chapter title"),
            formData.get("BookChapter-Book title"),
            formData.get("BookChapter-Publisher"),
            formData.get("BookChapter-ISSN")
        );
    }

    public void saveFundedResearchProjectData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertFundedResearchProject(
            dept.getId(),
            formData.get("FundedResearchProject-Principal investigator"),
            formData.get("FundedResearchProject-Co-investigator"),
            formData.get("FundedResearchProject-Title"),
            formData.get("FundedResearchProject-Funding agency"),
            formData.get("FundedResearchProject-Amount"),
            formData.get("FundedResearchProject-Duration"),
            formData.get("FundedResearchProject-Year")
        );
    }

    public void saveResearchProposalSubmittedData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertResearchProposalSubmitted(
            dept.getId(),
            formData.get("ResearchProposalSubmitted-Principal investigator"),
            formData.get("ResearchProposalSubmitted-Co-investigator"),
            formData.get("ResearchProposalSubmitted-Title"),
            formData.get("ResearchProposalSubmitted-Funding agency"),
            formData.get("ResearchProposalSubmitted-Amount"),
            formData.get("ResearchProposalSubmitted-Year")
        );
    }

    public void saveConsultancyData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertConsultancy(
            dept.getId(),
            formData.get("Consultancy-Faculty name"),
            formData.get("Consultancy-Organization"),
            formData.get("Consultancy-Consultancy provided"),
            formData.get("Consultancy-Amount"),
            formData.get("Consultancy-Year")
        );
    }

    public void saveProductDevelopmentData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertProductDevelopment(
            dept.getId(),
            formData.get("ProductDevelopment-Faculty name"),
            formData.get("ProductDevelopment-Product name"),
            formData.get("ProductDevelopment-Description"),
            formData.get("ProductDevelopment-Year")
        );
    }

    public void savePatentData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertPatent(
            dept.getId(),
            formData.get("Patent-Inventor name"),
            formData.get("Patent-Title"),
            formData.get("Patent-Patent number"),
            formData.get("Patent-Status"),
            formData.get("Patent-Year")
        );
    }

    public void saveFDPWorkshopSeminarData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertFDPWorkshopSeminar(
            dept.getId(),
            formData.get("FDPWorkshopSeminar-Faculty name"),
            formData.get("FDPWorkshopSeminar-Program name"),
            formData.get("FDPWorkshopSeminar-Program type"),
            formData.get("FDPWorkshopSeminar-Organized by"),
            formData.get("FDPWorkshopSeminar-Duration"),
            formData.get("FDPWorkshopSeminar-Year")
        );
    }

    public void saveMOUCSData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertMOUCS(
            dept.getId(),
            formData.get("MOUCS-Organization"),
            formData.get("MOUCS-Purpose"),
            formData.get("MOUCS-Date of signing"),
            formData.get("MOUCS-Duration")
        );
    }

    public void saveAchievementsAndAwardsData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertAchievementsAndAwards(
            dept.getId(),
            formData.get("AchievementsAndAwards-Faculty/Student name"),
            formData.get("AchievementsAndAwards-Achievement"),
            formData.get("AchievementsAndAwards-Award by"),
            formData.get("AchievementsAndAwards-Year")
        );
    }

    public void saveMOUSData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertMOUS(
            dept.getId(),
            formData.get("MOUS-Organization"),
            formData.get("MOUS-Purpose"),
            formData.get("MOUS-Date of signing"),
            formData.get("MOUS-Validity")
        );
    }

    public void saveFundedStudentProjectData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertFundedStudentProject(
            dept.getId(),
            formData.get("FundedStudentProject-Student name"),
            formData.get("FundedStudentProject-Project title"),
            formData.get("FundedStudentProject-Funding agency"),
            formData.get("FundedStudentProject-Amount"),
            formData.get("FundedStudentProject-Year")
        );
    }

    public String convertDepartmentIdToName(Object value) {
        if (value instanceof Integer) {
            return Department.fromId((Integer) value).name();
        } else if (value instanceof Long) {
            return Department.fromId(((Long) value).intValue()).name();
        }
        return value.toString();
    }
}