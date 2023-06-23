package org.umbrella.tracker.menu;

import org.jetbrains.annotations.NotNull;
import org.umbrella.tracker.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuUtil {

    public static boolean firstNameCheck(@NotNull String name) {
        String nameRegex = "^[A-Za-z]+[-']?[A-Za-z]+";
        if (!name.matches(nameRegex)) {
            System.out.println("Incorrect first name.");
            return false;
        }
        return true;
    }

    public static boolean lastNameCheck(@NotNull String name) {
        String nameRegex = "^[A-Za-z]{2,}(([\\s-'][A-Za-z]+)?)+|[A-Za-z]+[-'][A-Za-z]+";
        if (!name.matches(nameRegex)) {
            System.out.println("Incorrect last name.");
            return false;
        }
        return true;
    }

    // ^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$
    public static boolean emailCheck(@NotNull String email) {
        if (!isEmailUnique(email)) {
            System.out.println("This email is already taken.");
        }

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
    private static boolean isEmailUnique(@NotNull String email) {
        List<Student> studentList = new ArrayList<>(MainMenu.studentList);
        return studentList.stream().noneMatch(student -> student.getEmail().equals(email));
    }

    /***
     * Validates user input for student credentials.
     * @param input String array of student credentials.
     * @return Optional of Student object.
     */
    public static Optional<Student> inputValidator(String @NotNull [] input) {
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

        if (!lastNameCheck(lastName) || !emailCheck(email) || !firstNameCheck(firstName)) {
            return Optional.empty();
        }
        return Optional.of(new Student(firstName, lastName, email));
    }
}
