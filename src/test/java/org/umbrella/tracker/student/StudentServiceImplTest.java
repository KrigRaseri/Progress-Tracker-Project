package org.umbrella.tracker.student;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for StudentServiceImpl. Mostly not needed, but I wanted to try out Mockito and practice.
 */
public class StudentServiceImplTest {
    @InjectMocks
    StudentServiceImpl studentService;
    @Mock
    private Map<Integer, Student> studentMap;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addStudents_ShouldAddStudent_WhenInputIsCorrect() {
        // Act
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalPrintStream = System.out;
        System.setOut(printStream);

        //Act
        studentService.addStudents(new String[]{"John", "Doe", "jjDOE@gmol.com"});
        System.out.flush();
        System.setOut(originalPrintStream);

        // Assert
        String expectedOutput = "The student has been added.\n";
        assertThat(outputStream.toString().trim()).isEqualTo(expectedOutput.trim());
    }

    @Test
    void addPoints_ShouldUpdatePoints_WhenInputIsCorrect() {
        // Arrange
        when(studentMap.get(1)).thenReturn(new Student("John", "Doe", "JDoe@boring.com"));
        String expectedOutput = "Points updated.";
        // Act
        String actualOutput = studentService.addPoints(List.of("1", "5", "10", "5", "8"));
        // Assert
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    @ParameterizedTest
    @MethodSource("inputListsIncorrect_Negative")
    void addPoints_ShouldReturnErrorMessage_WhenPointsAreNegative(List<String> inputList) {
        // Arrange
        String expectedOutput = "Points cannot be negative.";
        // Act
        String actualOutput = studentService.addPoints(inputList);
        // Assert
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    static List<List<String>> inputListsIncorrect_Negative() {
        return Arrays.asList(
                Arrays.asList("1", "-5", "10", "5", "8"),
                Arrays.asList("1", "5", "-10", "5", "8"),
                Arrays.asList("1", "5", "10", "-5", "8"),
                Arrays.asList("1", "5", "10", "-5", "-8")
        );
    }

    @ParameterizedTest
    @MethodSource("inputListsIncorrect_NotInteger")
    void addPoints_ShouldReturnErrorMessage_WhenPointsAreNotInteger(List<String> inputList) {
        // Arrange
        String expectedOutput = "Incorrect points format.";
        // Act
        String actualOutput = studentService.addPoints(inputList);
        // Assert
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    static List<List<String>> inputListsIncorrect_NotInteger() {
        return Arrays.asList(
                Arrays.asList("jim", "5", "10", "5", "8.5"),
                Arrays.asList("1", "five", "10", "5", "8.5"),
                Arrays.asList("1", "5", "10", "bb", "8.5"),
                Arrays.asList("1", "5", "10", "5", "8.5")
        );
    }



    @ParameterizedTest
    @MethodSource("findStudentCorrectList")
    void findStudent_ShouldDisplayStudentInfo_WhenStudentExists(int id, Student student) {
        // Arrange
        when(studentMap.entrySet()).thenReturn(Set.of(Map.entry(id, student)));
        String expectedOutput = String.format("%d points: Java=0; DSA=0; Databases=0; Spring=0", id);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalPrintStream = System.out;
        System.setOut(printStream);

        // Act
        studentService.findStudent(id);
        System.out.flush();
        System.setOut(originalPrintStream);

        // Assert
        assertThat(outputStream.toString().trim()).isEqualTo(expectedOutput.trim());
    }

    static Stream<Arguments> findStudentCorrectList() {
        Student student1 = new Student("John", "Doe", "JDoe@boring.com");
        int id1 = Math.abs(Objects.hash(student1.getFirstName(), student1.getLastName(), student1.getEmail()) % 100000);
        student1.setId(id1);

        Student student2 = new Student("Solaire", "Dagoat", "praiseSun@lothric.net");
        int id2 = Math.abs(Objects.hash(student2.getFirstName(), student2.getLastName(), student2.getEmail()) % 100000);
        student2.setId(id2);

        Student student3 = new Student("Goon spoon", "gpson", "gpson@shploink.com");
        int id3 = Math.abs(Objects.hash(student3.getFirstName(), student3.getLastName(), student3.getEmail()) % 100000);
        student3.setId(id3);

        return Stream.of(
                Arguments.of(id1, student1),
                Arguments.of(id2, student2),
                Arguments.of(id3, student3)
        );
    }


    @Test
    void findStudent_ShouldDisplayErrorMessage_WhenStudentDoesNotExist() {
        // Arrange
        String expectedOutput = String.format("No student is found for id=%d", 5);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalPrintStream = System.out;
        System.setOut(printStream);

        // Act
        studentService.findStudent(5);
        System.out.flush();
        System.setOut(originalPrintStream);

        // Assert
        assertThat(outputStream.toString().trim()).isEqualTo(expectedOutput.trim());
    }

    @Test
    void listStudents_ShouldDisplayNoStudentsFound_WhenStudentMapIsEmpty() {
        // Arrange
        when(studentMap.isEmpty()).thenReturn(true);
        String expectedOutput = "No students found";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalPrintStream = System.out;
        System.setOut(printStream);

        // Act
        studentService.listStudents();
        System.out.flush();
        System.setOut(originalPrintStream);

        // Assert
        assertThat(outputStream.toString().trim()).isEqualTo(expectedOutput);
    }
}
