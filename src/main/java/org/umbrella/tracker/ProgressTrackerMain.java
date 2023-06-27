package org.umbrella.tracker;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.umbrella.tracker.menu.MainMenu;

public class ProgressTrackerMain {
    public static void main(String[] args) {
        GuiceConfig guiceConfig = new GuiceConfig();
        Injector injector = Guice.createInjector(guiceConfig);
        MainMenu mainMenu = injector.getInstance(MainMenu.class);

        mainMenu.runMenu();
    }
}