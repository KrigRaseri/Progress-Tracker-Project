package org.umbrella.tracker.student;

import com.google.inject.Inject;
import lombok.*;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;

/**
 * The StudentStatistics class represents a set of statistics calculations and operations related to students and courses.
 * It provides methods to calculate various statistics, display course information, and print overall statistics.
 */
@Setter
@Getter
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class StudentStatisticsImpl implements StudentStatistics{

    @NonNull private  final Logger LOGGER;
    @NonNull private final StudentService studentService;

    private String leastPopular = "n/a";
    private String mostPopular = "n/a";
    private String highestActivity = "n/a";
    private String lowestActivity = "n/a";
    private String easiestCourse = "n/a";
    private String hardestCourse = "n/a";

    private Map<Integer, Student> studentMap;

    /**
     * Prints the calculated statistics for the students and courses.
     * The statistics include most popular course, the least popular course, highest activity course,
     * lowest activity course, easiest course, and hardest course.
     */
    @Override
    public void printStatistics() {
        calculateStatistics();
        System.out.printf("""
                        Most popular: %s
                        Least popular: %s
                        Highest activity: %s
                        Lowest activity: %s
                        Easiest course: %s
                        Hardest course: %s
                        """, mostPopular, leastPopular, highestActivity,
                lowestActivity, easiestCourse, hardestCourse);
    }

    /**
     * Calculates and assigns the statistics by invoking the necessary helper methods.
     * The studentMap is populated with a copy of the student map from the studentService.
     */
    protected void calculateStatistics() {
        studentMap = new HashMap<>(studentService.getStudentMapCopy());

        /*studentMap = Map.of(
                1, new Student("jOE", "jo", "joejo@star.yare",
                        1, 50, 50, 0, 0),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 60, 1, 0, 0),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        3, 70, 1, 0, 0));*/

        mostPopular = calculatePopularCourses(studentMap, true);
        leastPopular = calculatePopularCourses(studentMap, false);
        highestActivity = calculateCourseActivity(studentMap, true);
        lowestActivity = calculateCourseActivity(studentMap, false);
        easiestCourse = calculateCourseDifficulty(studentMap, true);
        hardestCourse =  calculateCourseDifficulty(studentMap, false);
    }

    /**
     * Displays information about the specified course.
     * It prints the student ID, points, and completion percentage for the given course.
     * The courseName parameter specifies the course for which the information is displayed.
     *
     * @param courseName the name of the course to display information for
     */
    @Override
    public void displayCourseInfo(String courseName) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0%");
        StringBuilder sb = new StringBuilder();

        final int javaMaxPoints = 600;
        final int dsaMaxPoints = 400;
        final int databasesMaxPoints = 480;
        final int springMaxPoints = 550;

        int id = 0;
        int points = 0;
        String completed = "";

        System.out.println(courseName);
        System.out.println("id     points completed");

        List<Student> sortedEntries = new ArrayList<>(studentMap.values());
        switch (courseName) {
            case "Java" ->
                    sortedEntries.sort(Comparator.comparingInt(Student::getJava).reversed().thenComparing(Student::getId, Comparator.reverseOrder()));
            case "DSA" ->
                    sortedEntries.sort(Comparator.comparingInt(Student::getDsa).reversed().thenComparing(Student::getId, Comparator.reverseOrder()));
            case "Databases" ->
                    sortedEntries.sort(Comparator.comparingInt(Student::getDatabases).reversed().thenComparing(Student::getId, Comparator.reverseOrder()));
            case "Spring" ->
                    sortedEntries.sort(Comparator.comparingInt(Student::getSpring).reversed().thenComparing(Student::getId, Comparator.reverseOrder()));
            default -> {
                System.out.println("Invalid course name");
                return;
            }
        }

        for (Student student : sortedEntries) {
            switch (courseName) {
                case "Java" -> {
                    id = student.getId();
                    points = student.getJava();
                    if (points == 0) {
                        continue;
                    }
                    completed = decimalFormat.format(points / (double) javaMaxPoints);
                }

                case "DSA" -> {
                    id = student.getId();
                    points = student.getDsa();
                    if (points == 0) {
                        continue;
                    }
                    completed = decimalFormat.format(points / (double) dsaMaxPoints);
                }

                case "Databases" -> {
                    id = student.getId();
                    points = student.getDatabases();
                    if (points == 0) {
                        continue;
                    }
                    completed = decimalFormat.format(points / (double) databasesMaxPoints);
                }

                case "Spring" -> {
                    id = student.getId();
                    points = student.getSpring();
                    if (points == 0) {
                        continue;
                    }
                    completed = decimalFormat.format(points / (double) springMaxPoints);
                }
            }
            sb.append(String.format("%d %d    %s\n", id, points, completed));
        }
        System.out.println(sb);
    }

    /**
     * Calculates the difficulty level of each course based on average activity across students.
     * The courseMap parameter is used to determine whether the easiest or hardest course is calculated.
     *
     * @param studentMap the map of student IDs to Student objects
     * @param isEasiestCourse a flag indicating whether to calculate the easiest course (true) or hardest course (false)
     * @return the name of the easiest or hardest course
     */
    protected String calculateCourseDifficulty(Map<Integer, Student> studentMap, boolean isEasiestCourse) {
        long java = avgCourseActivity(studentMap, Student::getJava);
        long dsa = avgCourseActivity(studentMap, Student::getDsa);
        long databases = avgCourseActivity(studentMap, Student::getDatabases);
        long spring = avgCourseActivity(studentMap, Student::getSpring);

        return createStringFromResults(isEasiestCourse, java, dsa, databases, spring, easiestCourse);
    }

    /**
     * Calculates the average activity level for a specific course across all students.
     *
     * @param studentMap the map of student IDs to Student objects
     * @param courseExtractor the function to extract the activity value for a specific course from a Student object
     * @return the average activity level for the course
     */
    private long avgCourseActivity(Map<Integer, Student> studentMap, Function<Student, Integer> courseExtractor) {
        double average = studentMap.values()
                .stream()
                .map(courseExtractor)
                .mapToDouble(Integer::intValue)
                .average()
                .orElse(0.0);

        return Math.round(average);
    }

    /**
     * Calculates the total activity level for a specific course across all students.
     *
     * @param studentMap the map of student IDs to Student objects
     * @param isHighestActivity a flag indicating whether to calculate the highest activity (true) or lowest activity (false)
     * @return the total activity level for the course
     */
    protected String calculateCourseActivity(Map<Integer, Student> studentMap, boolean isHighestActivity) {
        long java = countCourseActivity(studentMap, Student::getJava);
        long dsa = countCourseActivity(studentMap, Student::getDsa);
        long databases = countCourseActivity(studentMap, Student::getDatabases);
        long spring = countCourseActivity(studentMap, Student::getSpring);

        return createStringFromResults(isHighestActivity, java, dsa, databases, spring, highestActivity);
    }

    /**
     * Calculates the total activity level for a specific course across all students. Activity is calculated by total
     * points earned by students in the course.
     *
     * @param studentMap the map of student IDs to Student objects
     * @param courseExtractor the function to extract the activity value for a specific course from a Student object
     * @return the total activity level for the course
     */
    private long countCourseActivity(Map<Integer, Student> studentMap, Function<Student, Integer> courseExtractor) {
        return studentMap.values()
                .stream()
                .map(courseExtractor)
                .mapToLong(Integer::intValue)
                .sum();
    }

    /**
     * Calculates the most popular or least popular course based on the number of students enrolled.
     * The courseMap parameter is used to determine whether the most popular or least popular course is calculated.
     *
     * @param studentMap the map of student IDs to Student objects
     * @param isMostPopular a flag indicating whether to calculate the most popular course (true) or least popular course (false)
     * @return the name of the most popular or least popular course
     */
    protected String calculatePopularCourses(Map<Integer, Student> studentMap, boolean isMostPopular) {
        long java = countCoursePopularity(studentMap, Student::getJava);
        long dsa = countCoursePopularity(studentMap, Student::getDsa);
        long databases = countCoursePopularity(studentMap, Student::getDatabases);
        long spring = countCoursePopularity(studentMap, Student::getSpring);

        return createStringFromResults(isMostPopular, java, dsa, databases, spring, mostPopular);
    }

    /**
     * Counts the number of students enrolled in a specific course. A student is considered enrolled if a courses points
     * is higher than 0.
     *
     * @param studentMap the map of student IDs to Student objects
     * @param courseExtractor the function to extract the course point values from the student object.
     * @return the name of the most popular or least popular course.
     */
    private long countCoursePopularity(Map<Integer, Student> studentMap, Function<Student, Integer> courseExtractor) {
        return studentMap.values()
                .stream()
                .map(courseExtractor)
                .filter(i -> i > 0)
                .count();
    }

    /**
     * Creates a string result from calculations to be assigned to the class fields mostPopular, leastPopular,
     * highestActivity, lowestActivity, easiestCourse, and hardestCourse.
     *
     * @param courseTypeFlag a flag indicating which course type true/false to calculate. ex. easiestCourse or hardestCourse
     * @param (java, dsa, databases, spring) the results from calculations to be used when making strings.
     * @param courseTypeField the name of the course type being calculated
     */
    @NotNull
    private String createStringFromResults(boolean courseTypeFlag, long java, long dsa, long databases, long spring, String courseTypeField) {
        if (java == 0 && dsa == 0 && databases == 0 && spring == 0) {
            return "n/a";
        }

        Map<String, Long> courseMap = createCourseMap(java, dsa, databases, spring);
        Optional<Map.Entry<String, Long>> opt = courseTypeFlag ? courseMap.entrySet().stream().max(Map.Entry.comparingByValue()) :
                courseMap.entrySet().stream().min(Map.Entry.comparingByValue());

        StringBuilder result = new StringBuilder();
        if (opt.isPresent()) {
            result.append(opt.get().getKey());
            for(Map.Entry<String, Long> entry : courseMap.entrySet()) {
                if (entry.getValue().equals(opt.get().getValue())
                        && !courseTypeField.contains(entry.getKey())
                        && !entry.getKey().equals(opt.get().getKey())) {
                    result.append(", ").append(entry.getKey());
                }
            }
            if (courseTypeField.contains(opt.get().getKey()))
                result = new StringBuilder("n/a");
        }
        return result.toString();
    }

    /**
     * Creates a map of course names to course point values.
     */
    private Map<String, Long> createCourseMap(long java, long dsa, long databases, long spring) {
        Map<String, Long> courseMap = new LinkedHashMap<>();
        courseMap.put("Java", java);
        courseMap.put("DSA", dsa);
        courseMap.put("Databases", databases);
        courseMap.put("Spring", spring);
        return courseMap;
    }
}
