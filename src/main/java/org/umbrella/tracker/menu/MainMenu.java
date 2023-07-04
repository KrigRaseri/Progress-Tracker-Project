package org.umbrella.tracker.menu;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.umbrella.tracker.student.StudentService;
import org.umbrella.tracker.student.StudentStatistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * The MainMenu class represents the main menu of the Learning Progress Tracker.
 * It provides various options for interacting with the student data and statistics.
 */
@AllArgsConstructor(onConstructor = @__({@Inject}))
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MainMenu {
    private StudentService studentService;
    private StudentStatistics studentStatisticsImpl;
    private Logger LOGGER;

    /**
     * Starts the main menu and handles user interactions.
     */
    public void runMenu() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            LOGGER.info("Learning Progress Tracker");

            while (true) {
                String input = reader.readLine().toLowerCase().trim();
                LOGGER.info("Received input: {}", input);
                switch (input) {
                    case "add students" -> addStudents(reader);
                    case "list" -> studentService.listStudents();
                    case "find" -> findStudent(reader);
                    case "add points" -> addPoints(reader);
                    case "statistics" -> statistics(reader);
                    case "back" -> LOGGER.info("Enter 'exit' to exit the program.");
                    case "exit" -> Runtime.getRuntime().halt(0);
                    case "" -> LOGGER.info("No input.");
                    default -> LOGGER.error("Error: unknown command!");
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error reading input.", e);
            throw new RuntimeException("Error reading input.", e);
        } finally {
            LOGGER.info("bye");
        }
    }

    /**
     * Displays the statistics menu and handles user interactions for course selection.
     *
     * @param reader the BufferedReader for reading user input.
     */
    private void statistics(BufferedReader reader) {
        try {
            LOGGER.info("Type the name of a course to see details or 'back' to quit:");
            studentStatisticsImpl.printStatistics();
            String input;

            while (true) {
                input = reader.readLine().toLowerCase().trim();
                if (input.equals("back")) {
                    break;
                }

                switch (input) {
                    case "java" -> studentStatisticsImpl.displayCourseInfo("Java");
                    case "dsa" -> studentStatisticsImpl.displayCourseInfo("DSA");
                    case "databases" -> studentStatisticsImpl.displayCourseInfo("Databases");
                    case "spring" -> studentStatisticsImpl.displayCourseInfo("Spring");
                    default -> LOGGER.error("Error: unknown command!");
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error reading input.", e);
            throw new RuntimeException("Error reading input.", e);
        }
    }

    /**
     * Handles adding points to student records based on user input.
     *
     * @param reader the BufferedReader for reading user input.
     * @throws IOException if an error occurs while reading input.
     */
    private void addPoints(BufferedReader reader) throws IOException {
        List<String> inputSections;
        try {
            LOGGER.info("Enter an id and points or 'back' to return:");
            String input;

            while(true) {
                input = reader.readLine();

                if (input.equals("back")) {
                    break;
                }

                inputSections = Arrays.asList(input.split(" "));
                if (inputSections.size() != 5) {
                    LOGGER.error("Incorrect points format.");
                    continue;

                } else if (!studentService.isIdPresent(inputSections.get(0))) {
                    LOGGER.error("No student is found for id={}", inputSections.get(0));
                    continue;
                }
                LOGGER.info(studentService.addPoints(inputSections));
            }
        } catch (IOException e) {
            LOGGER.error("Error reading input.", e);
            throw new RuntimeException("Error reading input.", e);
        }
    }

    /**
     * Handles finding a student by their ID based on user input.
     *
     * @param reader the BufferedReader for reading user input.
     * @throws IOException if an error occurs while reading input.
     * @throws NumberFormatException if the input is not a number.
     */
    private void findStudent(BufferedReader reader) throws IOException, NumberFormatException {
        try {
            LOGGER.info("Enter an id or 'back' to return:");
            String input;

            while (true) {
                input = reader.readLine();
                if (input.equals("back")) {
                    break;
                }
                studentService.findStudent(Integer.parseInt(input));
            }
        } catch (IOException e) {
            LOGGER.error("Error reading input.", e);
            throw new RuntimeException("Error reading input.", e);
        } catch (NumberFormatException e) {
            LOGGER.error("Error: invalid id!");
        }
    }

    /**
     * Handles adding multiple students based on user input.
     *
     * @param reader the BufferedReader for reading user input.
     * @throws IOException if an error occurs while reading input.
     */
    private void addStudents(BufferedReader reader) throws IOException {
        LOGGER.info("Enter student credentials or 'back' to return");
        String input;

        while(true) {
            input = reader.readLine();
            if (input.equalsIgnoreCase("back")) {
                break;
            }
            String[] studentInput = input.split(" ");
            studentService.addStudents(studentInput);
        }
        LOGGER.info("Total {} students have been added.", studentService.getStudentMapCopy().size());
    }
}

