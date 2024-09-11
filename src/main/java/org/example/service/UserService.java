package org.example.service;

import jakarta.inject.Inject;
import org.example.model.RoleType;
import org.example.model.User;
import org.example.utils.ApplicationSettingsSingleton;
import org.example.utils.ConstantsRomanian;
import org.example.utils.Language;
import org.example.utils.Utils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Scanner;

public class UserService {
    @Inject
    private Scanner scanner;

    @Inject
    private Session session;

    public void register() {
        User u = new User();
        System.out.print(ConstantsRomanian.LABEL_NAME);
        u.setName(scanner.next());
        System.out.print(ConstantsRomanian.LABEL_EMAIL);
        u.setEmail(scanner.next());
        System.out.print(ConstantsRomanian.LABEL_PASSWORD);
        u.setPassword(scanner.next());
        u.setRoleType(RoleType.CUSTOMER);

        Transaction t = session.beginTransaction();
        try {
            session.persist(u);
            Utils.printSuccess(ConstantsRomanian.USER_CREATED);
            t.commit();
        }
        catch (ConstraintViolationException exception) {
            Utils.printError(Language.getErrorUserExist());
        }

    }

    public User login() {
        System.out.print(ConstantsRomanian.LABEL_EMAIL);
        String email = scanner.next();
        System.out.print(ConstantsRomanian.LABEL_PASSWORD);
        String password = scanner.next();

        Query<User> query = session.createQuery("SELECT u FROM User u where u.email = :email AND u.password = :password", User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);

        List<User> users = query.getResultList();
        return (users.isEmpty() ? null : users.get(0));
    }

    public void deleteAccount() {

        Query<User> query = session.createQuery("SELECT u FROM User u where u.email = :email ", User.class);
        query.setParameter("email", ApplicationSettingsSingleton.getInstance().getCurrentUser().getEmail());

        List<User> users = query.getResultList();

        Transaction t = session.beginTransaction();
        if (!users.isEmpty()) {
            session.remove(users.get(0));
            Utils.printSuccess(ConstantsRomanian.USER_CREATED);
        }
        else {
            Utils.printError(ConstantsRomanian.ERROR_PASSWORD);
        }
        t.commit();
        ApplicationSettingsSingleton.getInstance().setCurrentUser(null);
    }


    public void changePassword() {
        Query<User> query = session.createQuery("SELECT u FROM User u where u.email = :email ", User.class);
        query.setParameter("email", ApplicationSettingsSingleton.getInstance().getCurrentUser().getEmail());

        List<User> users = query.getResultList();
        if(users.size()>0){
            User user = users.get(0);
            System.out.print(ConstantsRomanian.NEW_PASSWORD);
            user.setPassword(scanner.next());

            Transaction t = session.beginTransaction();
            session.merge(user);
            Utils.printSuccess(ConstantsRomanian.CHANGE_PASSWORD);
            t.commit();
        }

    }
}
