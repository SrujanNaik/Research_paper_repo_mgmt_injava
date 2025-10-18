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
}