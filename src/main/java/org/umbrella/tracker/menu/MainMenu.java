package org.umbrella.tracker.menu;

import org.umbrella.tracker.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainMenu {

    public void runMenu() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Learning Progress Tracker");

            while (true) {
                String input = reader.readLine().toLowerCase().trim();
                switch (input) {
                    case "add students" -> addStudents(reader);
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

    private void addStudents(BufferedReader reader) throws IOException {
        List<Student> studentList = new ArrayList<>();
        System.out.println("Enter student credentials or 'back' to return");
        String input;

        while(true) {
            input = reader.readLine();

            if (input.equals("back")) {
                break;
            }

            String[] studentInput = input.split(" ");
            Optional<Student> student = MenuUtil.inputValidator(studentInput);
            student.ifPresent(stu -> {
                studentList.add(stu);
                System.out.printf("Added student %s %s to list.", stu.getFirstName(), stu.getLastName());
            });
        }
        System.out.printf("Total %d students have been added.\n", studentList.size());
    }
}
