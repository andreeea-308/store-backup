package org.example.binding;

import com.google.inject.AbstractModule;
import org.example.provider.SessionFactoryProvider;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Scanner;

public class UiModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Scanner.class).toInstance(new Scanner(System.in));
        SessionFactory sessionFactory = SessionFactoryProvider.provideSessionFactory();
        bind(Session.class).toInstance(sessionFactory.openSession());
        //sessionFactory.close();
    }
}
