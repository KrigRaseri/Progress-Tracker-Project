package org.umbrella.tracker;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.umbrella.tracker.menu.MainMenu;

public class ProgressTrackerMain {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        MainMenu mainMenu = injector.getInstance(MainMenu.class);

        mainMenu.runMenu();
    }
}