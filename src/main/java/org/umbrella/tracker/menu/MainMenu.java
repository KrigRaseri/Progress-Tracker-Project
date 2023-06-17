package org.umbrella.tracker.menu;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MainMenu {

    public static void runMenu() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Learning Progress Tracker");

            while (true) {
                String input = reader.readLine().toLowerCase().trim();
                switch (input) {
                    case "1" -> System.out.println();
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
}
