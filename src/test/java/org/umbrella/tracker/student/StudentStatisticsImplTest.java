package org.umbrella.tracker.student;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StudentStatisticsImplTest {
    @Mock
    private StudentStatisticsImpl studentStatisticsImpl;

    @Mock
    private StudentServiceImpl studentService;

    @Mock
    private Logger LOGGER;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        studentStatisticsImpl = new StudentStatisticsImpl(LOGGER, studentService);
    }

    @Test
    void printStatistics_CorrectOutput() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.com",
                        1, 5, 0, 0, 1),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 5, 4, 0, 0),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 5, 4, 2, 1));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalPrintStream = System.out;
        System.setOut(printStream);

        // Act
        studentStatisticsImpl.printStatistics();
        System.out.flush();
        System.setOut(originalPrintStream);

        String expectedOutput = """
                        Most popular: Java
                        Least popular: Databases
                        Highest activity: Java
                        Lowest activity: Databases, Spring
                        Easiest course: Java
                        Hardest course: Databases, Spring
                        """;

        // Assert
        assertThat(outputStream.toString().trim()).isEqualTo(expectedOutput.trim());
    }

    @Test
    void calculateStatistics_ShouldBeCorrect() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.com",
                        1, 5, 0, 0, 1),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 5, 4, 0, 0),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 5, 4, 2, 1));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        // Act
        studentStatisticsImpl.calculateStatistics();

        // Assert
        assertEquals("Java", studentStatisticsImpl.getMostPopular());
        assertEquals("Databases", studentStatisticsImpl.getLeastPopular());
        assertEquals("Java", studentStatisticsImpl.getHighestActivity());
        assertEquals("Databases, Spring", studentStatisticsImpl.getLowestActivity());
        assertEquals("Java", studentStatisticsImpl.getEasiestCourse());
        assertEquals("Databases, Spring", studentStatisticsImpl.getHardestCourse());
    }

    //========================================== Calculate Popular Courses ============================================

    @Test
    void calculatePopularCourses_MostPopular_ShouldBeJava() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.com",
                        1, 5, 0, 0, 1),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 5, 4, 0, 0),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 5, 4, 2, 1));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        // Act
        String result = studentStatisticsImpl.calculatePopularCourses(mostPopular, true);

        // Assert
        assertThat(result).isEqualTo("Java");
    }

    @Test
    void calculatePopularCourses_MostPopular_ShouldBeNA() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.yare",
                        1, 0, 0, 0, 0),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 0, 0, 0, 0),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 0, 0, 0, 0));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        // Act
        String result = studentStatisticsImpl.calculatePopularCourses(mostPopular, true);

        // Assert
        assertThat(result).isEqualTo("n/a");
    }

    @Test
    void calculatePopularCourses_MostPopular_ShouldBeAll() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.yare",
                        1, 3, 45, 56, 32),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 8, 88, 2, 26),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 90, 12, 1, 78));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        // Act
        String result = studentStatisticsImpl.calculatePopularCourses(mostPopular, true);

        // Assert
        assertThat(result).isEqualTo("Java, DSA, Databases, Spring");
    }

    @Test
    void calculatePopularCourses_LeastPopular_ShouldBeJava() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.yare",
                        1, 0, 6, 0, 0),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 0, 9, 0, 8),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 0, 8, 3, 1));
        System.out.println(studentStatisticsImpl.getMostPopular());
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        // Act
        String result = studentStatisticsImpl.calculatePopularCourses(mostPopular, false);

        // Assert
        assertThat(result).isEqualTo("Java");
    }

    @Test
    void calculatePopularCourses_leastPopular_ShouldBeNA() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.yare",
                        1, 1, 6, 1, 1),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 1, 9, 1, 8),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 1, 8, 3, 1));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        Field mostPopularField = StudentStatisticsImpl.class.getDeclaredField("mostPopular");
        mostPopularField.setAccessible(true);
        mostPopularField.set(studentStatisticsImpl, "Java, Databases, Spring, DSA");

        // Act
        String result = studentStatisticsImpl.calculatePopularCourses(mostPopular, false);

        // Assert
        assertThat(result).isEqualTo("n/a");
    }

    @Test
    void calculatePopularCourses_SplitResults() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.yare",
                        1, 10, 9, 0, 0),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 10, 9, 0, 0),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 10, 8, 0, 0));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        Field mostPopularField = StudentStatisticsImpl.class.getDeclaredField("mostPopular");
        mostPopularField.setAccessible(true);
        mostPopularField.set(studentStatisticsImpl, "Java, DSA");

        // Act
        String result = studentStatisticsImpl.calculatePopularCourses(mostPopular, false);

        // Assert
        assertThat(result).isEqualTo("Databases, Spring");
    }

    //=======================================Calculate Course Activity==============================================

    @Test
    void calculateCourseActivity_HighestActivity_ShouldBeJava() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.com",
                        1, 50, 14, 5, 1),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 50, 49, 9, 90),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 50, 4, 2, 1));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        // Act
        String result = studentStatisticsImpl.calculateCourseActivity(mostPopular, true);

        // Assert
        assertThat(result).isEqualTo("Java");
    }

    @Test
    void calculateCourseActivity_HighestActivity_ShouldBeAll() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.com",
                        1, 5, 5, 5, 5),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 5, 5, 5, 5),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 5, 5, 5, 5));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        // Act
        String result = studentStatisticsImpl.calculateCourseActivity(mostPopular, true);

        // Assert
        assertThat(result).isEqualTo("Java, DSA, Databases, Spring");
    }

    @Test
    void calculateCourseActivity_LowestActivity_ShouldBeJava() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.com",
                        1, 5, 50, 75, 5),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 5, 500, 35, 5),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 5, 50, 57, 50));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        // Act
        String result = studentStatisticsImpl.calculateCourseActivity(mostPopular, false);

        // Assert
        assertThat(result).isEqualTo("Java");
    }

    @Test
    void calculateCourseActivity_LowestActivity_ShouldBeNA() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.yare",
                        1, 10, 10, 10, 10),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 10, 10, 10, 10),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 10, 10, 10, 10));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        Field highestActivity = StudentStatisticsImpl.class.getDeclaredField("highestActivity");
        highestActivity.setAccessible(true);
        highestActivity.set(studentStatisticsImpl, "Java, DSA, Databases, Spring");

        // Act
        String result = studentStatisticsImpl.calculateCourseActivity(mostPopular, false);

        // Assert
        assertThat(result).isEqualTo("n/a");
    }

    @Test
    void calculateCourseActivity_SplitResults() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.yare",
                        1, 10, 10, 1, 1),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 10, 10, 1, 1),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 10, 10, 1, 1));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        Field highestActivity = StudentStatisticsImpl.class.getDeclaredField("highestActivity");
        highestActivity.setAccessible(true);
        highestActivity.set(studentStatisticsImpl, "Java, DSA");

        // Act
        String result = studentStatisticsImpl.calculateCourseActivity(mostPopular, false);

        // Assert
        assertThat(result).isEqualTo("Databases, Spring");
    }

    //=======================================Calculate Course Difficulty==============================================

    @Test
    void calculateCourseDifficulty_Easiest_ShouldBeJava() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.com",
                        1, 90, 14, 50, 47),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 80, 49, 94, 90),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 67, 40, 21, 15));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        // Act
        String result = studentStatisticsImpl.calculateCourseDifficulty(mostPopular, true);

        // Assert
        assertThat(result).isEqualTo("Java");
    }

    @Test
    void calculateCourseDifficulty_Easiest_ShouldBeAll() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.com",
                        1, 5, 5, 5, 5),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 5, 5, 5, 5),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 5, 5, 5, 5));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        // Act
        String result = studentStatisticsImpl.calculateCourseDifficulty(mostPopular, true);

        // Assert
        assertThat(result).isEqualTo("Java, DSA, Databases, Spring");
    }

    @Test
    void calculateCourseDifficulty_Hardest_ShouldBeJava() {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.com",
                        1, 5, 50, 75, 59),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 5, 500, 35, 40),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 5, 50, 57, 50));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        // Act
        String result = studentStatisticsImpl.calculateCourseDifficulty(mostPopular, false);

        // Assert
        assertThat(result).isEqualTo("Java");
    }

    //fix
    @Test
    void calculateCourseDifficulty_Hardest_ShouldBeNA() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.yare",
                        1, 10, 10, 10, 10),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 10, 10, 10, 10),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 10, 10, 10, 10));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        Field hardestCourse = StudentStatisticsImpl.class.getDeclaredField("easiestCourse");
        hardestCourse.setAccessible(true);
        hardestCourse.set(studentStatisticsImpl, "Java, DSA, Databases, Spring");

        // Act
        String result = studentStatisticsImpl.calculateCourseDifficulty(mostPopular, false);

        // Assert
        assertThat(result).isEqualTo("n/a");
    }

    @Test
    void calculateCourseDifficulty_SplitResults() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Map<Integer, Student> mostPopular = Map.of(
                1, new Student("jOE", "jo", "joejo@star.yare",
                        1, 10, 10, 1, 1),
                2, new Student("Sol", "aire", "praise@sun.com",
                        2, 10, 10, 1, 1),
                3, new Student("Art", "Orias", "ofthe@abyss.com",
                        2, 10, 10, 1, 1));
        when(studentService.getStudentMapCopy()).thenReturn(mostPopular);

        Field highestActivity = StudentStatisticsImpl.class.getDeclaredField("highestActivity");
        highestActivity.setAccessible(true);
        highestActivity.set(studentStatisticsImpl, "Java, DSA");

        // Act
        String result = studentStatisticsImpl.calculateCourseDifficulty(mostPopular, false);

        // Assert
        assertThat(result).isEqualTo("Databases, Spring");
    }
}
