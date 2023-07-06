package org.umbrella.tracker.student;

import com.google.inject.Inject;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE, force = true)
public class StudentNotificationServiceImpl implements StudentNotificationService {

    private final StudentService studentService;

    int JAVA_SCORE = 600;
    int DSA_SCORE = 400;
    int DATABASES_SCORE = 480;
    int SPRING_SCORE = 550;

    List<Integer> javaCompletionList = new ArrayList<>();
    List<Integer> dsaCompletionList = new ArrayList<>();
    List<Integer> databasesCompletionList = new ArrayList<>();
    List<Integer> springCompletionList = new ArrayList<>();

    @Override
    public int sendNotification() {
        boolean hasCompletedCourse = false;
        assert studentService != null;
        Map<Integer, Student> studentMapCopy = studentService.getStudentMapCopy();

        for (Student student : studentMapCopy.values()) {
            if (checkCompletion(student, "Java", javaCompletionList, JAVA_SCORE)) {
                hasCompletedCourse = true;
            }
            if (checkCompletion(student, "DSA", dsaCompletionList, DSA_SCORE)) {
                hasCompletedCourse = true;
            }
            if (checkCompletion(student, "Databases", databasesCompletionList, DATABASES_SCORE)) {
                hasCompletedCourse = true;
            }
            if (checkCompletion(student, "Spring", springCompletionList, SPRING_SCORE)) {
                hasCompletedCourse = true;
            }
        }
        return hasCompletedCourse ? 1 : 0;
    }

    private boolean checkCompletion(Student student, String course, List<Integer> completionList, int scoreThreshold) {
        if (completionList.contains(student.getId())) {
            return false;
        }

        if (getScoreByCourse(student, course) >= scoreThreshold) {
            completionList.add(student.getId());
            printNotification(student, course);
            return true;
        }
        return false;
    }

    private void printNotification(Student student, String course) {
        System.out.printf("""
                To: %s
                Re: Your Learning Progress
                Hello, %s %s You have accomplished our %s course!
                """,
                student.getEmail(),
                student.getFirstName(), student.getLastName(),
                course);
    }

    private int getScoreByCourse(Student student, String course) {
        return switch (course) {
            case "Java" -> student.getJava();
            case "DSA" -> student.getDsa();
            case "Databases" -> student.getDatabases();
            case "Spring" -> student.getSpring();
            default -> 0;
        };
    }
}
