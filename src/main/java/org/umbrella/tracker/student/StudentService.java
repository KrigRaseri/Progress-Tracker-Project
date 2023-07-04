package org.umbrella.tracker.student;

import java.util.List;
import java.util.Map;

public interface StudentService {
    String addPoints(List<String> inputSections);
    void findStudent(int id);
    void addStudents(String[] studentInput);
    boolean isIdPresent(String id);
    void listStudents();
    Map<Integer, Student> getStudentMapCopy();
}
