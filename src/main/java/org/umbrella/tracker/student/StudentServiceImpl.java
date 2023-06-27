package org.umbrella.tracker.student;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.umbrella.tracker.menu.MenuUtil;

import java.util.*;

public class StudentServiceImpl implements StudentService {
    private Map<Integer, Student> studentMap = new HashMap<>();

    public Map<Integer, Student> getStudentMap() {
        return Collections.unmodifiableMap(studentMap);
    }

    public void putStudentMap(int id, Student student) {
        this.studentMap.put(id, student);
    }

    public void replaceStudentMap(int id, Student student) {
        this.studentMap.replace(id, student);
    }

    @Override
    public String addPoints(@NotNull List<String> inputSections) {
        try {
            int id = Integer.parseInt(inputSections.get(0));
            int javaPoints = Integer.parseInt(inputSections.get(1));
            int dsaPoints = Integer.parseInt(inputSections.get(2));
            int databasesPoints = Integer.parseInt(inputSections.get(3));
            int springPoints = Integer.parseInt(inputSections.get(4));


            if (javaPoints < 0 || dsaPoints < 0 || databasesPoints < 0 || springPoints < 0) {
                return "Points cannot be negative.";
            }

            Student student = getStudentMap().get(id);
            student.setDatabases(student.getDatabases() + databasesPoints);
            student.setDsa(student.getDsa() + dsaPoints);
            student.setJava(student.getJava() + javaPoints);
            student.setSpring(student.getSpring() + springPoints);
            replaceStudentMap(id, student);
            return "Points updated.";

        } catch (NumberFormatException e) {
            return "Incorrect points format.";
        }
    }

    @Override
    public void addStudents(String[] studentInput) {
        Optional<Student> student = MenuUtil.inputValidator(studentInput, getStudentsFromMap());
        student.ifPresent(stu -> {
            stu.setId(Math.abs(stu.hashCode() % 100000));
            putStudentMap(Math.abs(stu.hashCode() % 100000), stu);
            System.out.println("The student has been added.");
        });
    }

    @Override
    public void findStudent(int id) {
        Optional<Map.Entry<Integer, Student>> s = studentMap.entrySet().stream()
                .filter(e -> e.getKey() == id).findFirst();

        if (s.isPresent()) {
            s.get().getValue().displayStudentInfo();
        } else {
            System.out.printf("No student is found for id=%d\n", id);
        }
    }

    @Override
    public void listStudents() {
        if (studentMap.isEmpty()) {
            System.out.println("No students found");
        } else {
            System.out.println("Students:");
            getStudentMap().values().stream().map(Student::getId).forEach(System.out::println);
        }
    }

    @Contract(" -> new")
    public @NotNull List<Student> getStudentsFromMap() {
        return new ArrayList<>(getStudentMap().values());
    }

    public int mapSize() {
        return getStudentMap().size();
    }
}
