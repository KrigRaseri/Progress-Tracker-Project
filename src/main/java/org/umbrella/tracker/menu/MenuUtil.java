package org.umbrella.tracker.menu;

import org.jetbrains.annotations.NotNull;
import org.umbrella.tracker.student.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MenuUtil {

    static boolean firstNameCheck(@NotNull String name) {
        String nameRegex = "^[A-Za-z]+[-']?[A-Za-z]+";
        if (!name.matches(nameRegex)) {
            System.out.println("Incorrect first name.");
            return false;
        }
        return true;
    }

    /***
     * Checks if last name is valid.
     * @param name String last name.
     * @return boolean.
     */
    static boolean lastNameCheck(@NotNull String name) {
        String nameRegex = "^[A-Za-z]{2,}(([\\s-'][A-Za-z]+)?)+|[A-Za-z]+[-'][A-Za-z]+";
        if (!name.matches(nameRegex)) {
            System.out.println("Incorrect last name.");
            return false;
        }
        return true;
    }

    // ^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$
    /***
     * Checks if email is valid and unique.
     * @param email String email.
     * @return boolean.
     */
    static boolean emailCheck(@NotNull String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z0-9]+$";
        if (!email.matches(emailRegex)) {
            System.out.println("Incorrect email.");
            return false;
        }
        return true;
    }

    /***
     * Checks if email is unique.
     * @param email String email.
     * @return boolean.
     */
    private static boolean isEmailUnique(@NotNull String email, @NotNull List<Student> studentList) {
        return studentList.stream().noneMatch(student -> student.getEmail().equals(email));
    }

    /***
     * Validates user input for student credentials.
     * @param input String array of student credentials.
     * @return Optional of Student object.
     */
    public static Optional<Student> inputValidator(String @NotNull [] input, List<Student> studentList) {
        String firstName;
        String lastName;
        String email;

        List<String> nameParts = new ArrayList<>(List.of(input));

        if (input.length < 3 || input[0].equals("")) {
            System.out.println("Incorrect credentials.");
            return Optional.empty();
        }

        firstName = nameParts.get(0);
        nameParts.remove(0);
        email = nameParts.get(nameParts.size()-1);
        nameParts.remove(nameParts.size()-1);
        lastName = String.join(" ", nameParts);

        if (!isEmailUnique(email, studentList)) {
            System.out.println("This email is already taken.");
            return Optional.empty();
        }

        if (!lastNameCheck(lastName) || !emailCheck(email) || !firstNameCheck(firstName)) {
            return Optional.empty();
        }
        return Optional.of(new Student(firstName, lastName, email));
    }

    /***
     * Checks if id is present in student list.
     * @param id String id.
     * @param studentList List of Student objects.
     * @return boolean.
     */
    public static boolean isIdPresent(String id, List<Student> studentList) {
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
}
