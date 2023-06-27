package org.umbrella.tracker.student;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.mockito.Mock;

class StudentTest {
    @Mock
    Student student;


    @BeforeEach
    void setUp() {
        student = new Student("John", "Doe", "jonD@goopy.com");
    }

    /**
     * Method under test: {@link Student#displayStudentInfo()}
     * */
    @Test
    void testDisplayStudentInfo_correct() {
        // Arrange
        student.setId(61682);
        student.setJava(90);
        student.setDsa(85);
        student.setDatabases(75);
        student.setSpring(80);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalPrintStream = System.out;
        System.setOut(printStream);

        // Act
        student.displayStudentInfo();
        System.out.flush();
        System.setOut(originalPrintStream);

        // Assert
        String expectedOutput = "61682 points: Java=90; DSA=85; Databases=75; Spring=80\n";
        assertThat(outputStream.toString()).isEqualTo(expectedOutput);
    }

//61682 points: Java=90; DSA=85; Databases=75; Spring=80
    @Test
    void equals_isEqual() {
        Student student2 = new Student("John", "Doe", "jonD@goopy.com");
        assertThat(student).isEqualTo(student2);
    }

    @Test
    void equals_notEqual() {
        Student student2 = new Student("Jane", "Doe", "jonD@goopy.com");
        assertThat(student).isNotEqualTo(student2);
    }
}

