package org.umbrella.tracker;

import com.google.inject.AbstractModule;
import org.umbrella.tracker.student.StudentService;
import org.umbrella.tracker.student.StudentServiceImpl;

public class GuiceConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(StudentService.class).to(StudentServiceImpl.class);
    }
}
