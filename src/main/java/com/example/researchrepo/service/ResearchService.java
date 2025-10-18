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
        if (filterOption.equalsIgnoreCase(textInput)) {
            return researchRepository.getAllFromTable(tableName);
        } else if (filterOption.equalsIgnoreCase("DEPARTMENT")) {
            Department dept = Department.fromName(textInput);
            return researchRepository.getByDepartment(tableName, dept.getId());
        } else {
            return researchRepository.getByFilter(tableName, filterOption, textInput);
        }
    }

    public void saveJournalData(String department, Map<String, String> formData) {
        Department dept = Department.fromName(department);
        researchRepository.insertJournal(
            dept.getId(),
            formData.get("Journal-Authors"),
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

    public String convertDepartmentIdToName(Object value) {
        if (value instanceof Integer) {
            return Department.fromId((Integer) value).name();
        } else if (value instanceof Long) {
            return Department.fromId(((Long) value).intValue()).name();
        }
        return value.toString();
    }
}