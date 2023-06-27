package org.umbrella.tracker.student;

import java.util.List;

public interface StudentService {
    String addPoints(List<String> inputSections);
    void listStudents();
    void findStudent(int id);
    void addStudents(String[] studentInput);
}
