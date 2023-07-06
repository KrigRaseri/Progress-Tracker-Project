package org.umbrella.tracker;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.umbrella.tracker.menu.MainMenu;
import org.umbrella.tracker.student.*;

public class GuiceConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(StudentService.class).to(StudentServiceImpl.class).in(Singleton.class);
        bind(StudentStatistics.class).to(StudentStatisticsImpl.class).in(Singleton.class);
        bind(StudentNotificationService.class).to(StudentNotificationServiceImpl.class).in(Singleton.class);
        bind((StudentServiceImpl.class)).in(Singleton.class);
        bind(MainMenu.class).in(Singleton.class);

        Logger logger = LogManager.getLogger("MyLogger");
        bind(Logger.class).toInstance(logger);
    }
}
