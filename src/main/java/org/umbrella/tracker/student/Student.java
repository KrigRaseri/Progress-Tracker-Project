package org.umbrella.tracker.student;

import lombok.*;

import java.util.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class Student {
    @NonNull private final String firstName;
    @NonNull private final String lastName;
    @NonNull private final String email;

    private int id;
    private int java;
    private int dsa;
    private int databases;
    private int spring;

    public void displayStudentInfo() {
        System.out.printf("%d points: Java=%d; DSA=%d; Databases=%d; Spring=%d\n", getId(), getJava(), getDsa(),
                getDatabases(), getSpring());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName) && Objects.equals(email, student.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }
}