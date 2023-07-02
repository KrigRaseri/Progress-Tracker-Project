package org.umbrella.tracker;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.umbrella.tracker.menu.MainMenu;
import org.umbrella.tracker.student.StudentService;
import org.umbrella.tracker.student.StudentServiceImpl;

public class GuiceConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(StudentService.class).to(StudentServiceImpl.class).in(Singleton.class);
        bind((StudentServiceImpl.class)).in(Singleton.class);
        bind(MainMenu.class).in(Singleton.class);
    }
}
