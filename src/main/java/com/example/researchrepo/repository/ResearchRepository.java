package com.example.researchrepo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ResearchRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String[] TABLES = {
        "JOURNAL", "CONFERENCE", "BOOKCHAPTER", "FUNDEDRESEARCHPROJECT",
        "RESEARCHPROPOSALSUBMITTED", "CONSULTANCY", "PRODUCTDEVELOPMENT", "PATENT",
        "FDPWORKSHOPSEMINAR", "MOUCS", "ACHIEVEMENTSANDAWARDS", "MOUS", "FUNDEDSTUDENTPROJECT"
    };

    public int[] getCountsByDepartment(int departmentId) {
        int[] counts = new int[TABLES.length];
        for (int i = 0; i < TABLES.length; i++) {
            String sql = String.format("SELECT COUNT(*) FROM %s WHERE DEPARTMENT_ID = ?", TABLES[i]);
            try {
                counts[i] = jdbcTemplate.queryForObject(sql, Integer.class, departmentId);
            } catch (Exception e) {
                counts[i] = 0;
            }
        }
        return counts;
    }

    public List<Map<String, Object>> getAllFromTable(String tableName) {
        String sql = String.format("SELECT * FROM %s", tableName);
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getByDepartment(String tableName, int departmentId) {
        String sql = String.format("SELECT * FROM %s WHERE DEPARTMENT_ID = ?", tableName);
        return jdbcTemplate.queryForList(sql, departmentId);
    }

    public List<Map<String, Object>> getByFilter(String tableName, String filterColumn, String filterValue) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", tableName, filterColumn);
        return jdbcTemplate.queryForList(sql, filterValue);
    }

    // Journal
    public void insertJournal(int deptId, String authors, String year, String title, 
                              String journalName, String volumePage, String issn, String impactFactor) {
        String sql = "INSERT INTO JOURNAL(DEPARTMENT_ID, AUTHORS, YEAR_OF_PUBLICATION, TITLE, " +
                     "JOURNAL_NAME, VOLUME_PAGE_NUMBER, ISSN, IMPACT_FACTOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, authors, year, title, journalName, volumePage, issn, impactFactor);
    }

    // Conference
    public void insertConference(int deptId, String year, String author, String title,
                                 String conferenceName, String volumePage, String organizedBy, String place) {
        String sql = "INSERT INTO CONFERENCE(DEPARTMENT_ID, YEAR_OF_PUBLICATION, AUTHOR, TITLE, " +
                     "CONFERENCE_NAME, VOLUME_PAGE_COUNT, ORGANIZED_BY, PLACE_OF_CONFERENCE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, year, author, title, conferenceName, volumePage, organizedBy, place);
    }

    // Book Chapter
    public void insertBookChapter(int deptId, String year, String author, String chapterTitle,
                                  String bookTitle, String publisher, String issn) {
        String sql = "INSERT INTO BOOKCHAPTER(DEPARTMENT_ID, YEAR_OF_PUBLICATION, AUTHOR, CHAPTER_TITLE, " +
                     "BOOK_TITLE, PUBLISHER, ISSN) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, year, author, chapterTitle, bookTitle, publisher, issn);
    }

    // Funded Research Project
    public void insertFundedResearchProject(int deptId, String principalInvestigator, String coInvestigator,
                                           String title, String fundingAgency, String amount, String duration, String year) {
        String sql = "INSERT INTO FUNDEDRESEARCHPROJECT(DEPARTMENT_ID, PRINCIPAL_INVESTIGATOR, CO_INVESTIGATOR, " +
                     "TITLE, FUNDING_AGENCY, AMOUNT, DURATION, YEAR) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, principalInvestigator, coInvestigator, title, fundingAgency, amount, duration, year);
    }

    // Research Proposal Submitted
    public void insertResearchProposalSubmitted(int deptId, String principalInvestigator, String coInvestigator,
                                               String title, String fundingAgency, String amount, String year) {
        String sql = "INSERT INTO RESEARCHPROPOSALSUBMITTED(DEPARTMENT_ID, PRINCIPAL_INVESTIGATOR, CO_INVESTIGATOR, " +
                     "TITLE, FUNDING_AGENCY, AMOUNT, YEAR) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, principalInvestigator, coInvestigator, title, fundingAgency, amount, year);
    }

    // Consultancy
    public void insertConsultancy(int deptId, String facultyName, String organization, String consultancyProvided,
                                 String amount, String year) {
        String sql = "INSERT INTO CONSULTANCY(DEPARTMENT_ID, FACULTY_NAME, ORGANIZATION, CONSULTANCY_PROVIDED, " +
                     "AMOUNT, YEAR) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, facultyName, organization, consultancyProvided, amount, year);
    }

    // Product Development
    public void insertProductDevelopment(int deptId, String facultyName, String productName, String description, String year) {
        String sql = "INSERT INTO PRODUCTDEVELOPMENT(DEPARTMENT_ID, FACULTY_NAME, PRODUCT_NAME, DESCRIPTION, YEAR) " +
                     "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, facultyName, productName, description, year);
    }

    // Patent
    public void insertPatent(int deptId, String inventorName, String title, String patentNumber, String status, String year) {
        String sql = "INSERT INTO PATENT(DEPARTMENT_ID, INVENTOR_NAME, TITLE, PATENT_NUMBER, STATUS, YEAR) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, inventorName, title, patentNumber, status, year);
    }

    // FDP Workshop Seminar
    public void insertFDPWorkshopSeminar(int deptId, String facultyName, String programName, String programType,
                                        String organizedBy, String duration, String year) {
        String sql = "INSERT INTO FDPWORKSHOPSEMINAR(DEPARTMENT_ID, FACULTY_NAME, PROGRAM_NAME, PROGRAM_TYPE, " +
                     "ORGANIZED_BY, DURATION, YEAR) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, facultyName, programName, programType, organizedBy, duration, year);
    }

    // MOUCS
    public void insertMOUCS(int deptId, String organization, String purpose, String dateOfSigning, String duration) {
        String sql = "INSERT INTO MOUCS(DEPARTMENT_ID, ORGANIZATION, PURPOSE, DATE_OF_SIGNING, DURATION) " +
                     "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, organization, purpose, dateOfSigning, duration);
    }

    // Achievements and Awards
    public void insertAchievementsAndAwards(int deptId, String facultyStudentName, String achievement, 
                                           String awardBy, String year) {
        String sql = "INSERT INTO ACHIEVEMENTSANDAWARDS(DEPARTMENT_ID, FACULTY_STUDENT_NAME, ACHIEVEMENT, " +
                     "AWARD_BY, YEAR) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, facultyStudentName, achievement, awardBy, year);
    }

    // MOUS
    public void insertMOUS(int deptId, String organization, String purpose, String dateOfSigning, String validity) {
        String sql = "INSERT INTO MOUS(DEPARTMENT_ID, ORGANIZATION, PURPOSE, DATE_OF_SIGNING, VALIDITY) " +
                     "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, organization, purpose, dateOfSigning, validity);
    }

    // Funded Student Project
    public void insertFundedStudentProject(int deptId, String studentName, String projectTitle, String fundingAgency,
                                          String amount, String year) {
        String sql = "INSERT INTO FUNDEDSTUDENTPROJECT(DEPARTMENT_ID, STUDENT_NAME, PROJECT_TITLE, " +
                     "FUNDING_AGENCY, AMOUNT, YEAR) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deptId, studentName, projectTitle, fundingAgency, amount, year);
    }
}