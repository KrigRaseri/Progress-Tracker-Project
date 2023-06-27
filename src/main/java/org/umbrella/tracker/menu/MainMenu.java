package org.umbrella.tracker.menu;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.umbrella.tracker.student.StudentServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor(onConstructor = @__({@Inject}))
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MainMenu {
    private StudentServiceImpl studentService;

    public void runMenu() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Learning Progress Tracker");

            while (true) {
                String input = reader.readLine().toLowerCase().trim();
                switch (input) {
                    case "add students" -> addStudents(reader);
                    case "list" -> studentService.listStudents();
                    case "find" -> findStudent(reader);
                    case "add points" -> addPoints(reader);
                    case "back" -> System.out.println("Enter 'exit' to exit the program.");
                    case "exit" -> Runtime.getRuntime().halt(0);
                    case "" -> System.out.println("No input.");
                    default -> System.out.println("Error: unknown command!");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("bye");
        }
    }

    /**
     * Adds points to a student. Enter id and points separated by a space.
     */
    private void addPoints(BufferedReader reader) {
        List<String> inputSections;
        try {
            System.out.println("Enter an id and points or 'back' to return:");
            String input;

            while(true) {
                input = reader.readLine();

                if (input.equals("back")) {
                    break;
                }

                inputSections = Arrays.asList(input.split(" "));

                if (inputSections.size() != 5) {
                    System.out.println("Incorrect points format.");
                    continue;

                } else if (!MenuUtil.isIdPresent(inputSections.get(0), studentService.getStudentsFromMap())) {
                    System.out.printf("No student is found for id=%s\n", inputSections.get(0));
                    continue;
                }

                System.out.println(studentService.addPoints(inputSections));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void findStudent(BufferedReader reader) {
        try {
            System.out.println("Enter an id or 'back' to return:");
            String input;

            while (true) {
                input = reader.readLine();
                if (input.equals("back")) {
                    break;
                }
                studentService.findStudent(Integer.parseInt(input));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.out.println("Error: invalid id!");
        }
    }

    private void addStudents(BufferedReader reader) throws IOException {
        System.out.println("Enter student credentials or 'back' to return");
        String input;

        while(true) {
            input = reader.readLine();
            if (input.equalsIgnoreCase("back")) {
                break;
            }
            String[] studentInput = input.split(" ");
            studentService.addStudents(studentInput);
        }
        System.out.printf("Total %d students have been added.\n", studentService.mapSize());
    }
}

