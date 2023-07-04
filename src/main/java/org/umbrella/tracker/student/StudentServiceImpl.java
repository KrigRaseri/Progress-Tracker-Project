package org.umbrella.tracker.student;

import com.google.inject.Inject;
import lombok.*;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.umbrella.tracker.menu.MenuUtil;

import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class StudentServiceImpl implements StudentService {

    @NonNull private final Logger LOGGER;
    private Map<Integer, Student> studentMap = new HashMap<>();

    /***
     * Returns a list of students from the studentMap.
     * @return List<Student>.
     */
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

            Student student = getStudentMapCopy().get(id);
            student.setDatabases(student.getDatabases() + databasesPoints);
            student.setDsa(student.getDsa() + dsaPoints);
            student.setJava(student.getJava() + javaPoints);
            student.setSpring(student.getSpring() + springPoints);
            studentMap.replace(id, student);
            return "Points updated.";

        } catch (NumberFormatException e) {
            return "Incorrect points format.";
        }
    }

    /***
     * Adds a student to the studentMap.
     * @param studentInput String[].
     */
    @Override
    public void addStudents(String[] studentInput) {
        Optional<Student> student = MenuUtil.inputValidator(studentInput, new ArrayList<>(getStudentMapCopy().values()));
        student.ifPresent(stu -> {
            stu.setId(Math.abs(stu.hashCode() % 100000));
            studentMap.put(Math.abs(stu.hashCode() % 100000), stu);
            System.out.println("The student has been added.");
        });
    }

    /***
     * Checks if id is present in student list.
     * @param id String student ID.
     * @return boolean.
     */
    @Override
    public boolean isIdPresent(String id) {
        List<Student> studentList = new ArrayList<>(getStudentMapCopy().values());
        try {
            int stuID = Integer.parseInt(id);
            Optional<Student> optStudent = studentList.stream()
                    .filter(student -> student.getId() == stuID)
                    .findFirst();

            return optStudent.isPresent();
        } catch (NumberFormatException e) {
            System.out.println("Error: invalid id!");
            return false;
        }
    }

    /***
     * Finds a student by id then displays the student info.
     * @param id int.
     */
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

    /***
     * Lists students out from the map based on ID.
     */
    public void listStudents() {
        if (studentMap.isEmpty()) {
            System.out.println("No students found");
        } else {
            System.out.println("Students:");
            getStudentMapCopy().values().stream().map(Student::getId).forEach(System.out::println);
        }
    }

    @Override
    public Map<Integer, Student> getStudentMapCopy() {
        return Collections.unmodifiableMap(studentMap);
    }
}
