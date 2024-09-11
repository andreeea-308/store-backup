package org.example.utils;

import lombok.Getter;
import lombok.Setter;
import org.example.model.User;

@Getter
@Setter
public class ApplicationSettingsSingleton {
    private static ApplicationSettingsSingleton applicationSettingsSingleton;

    private ApplicationSettingsSingleton() {
    }

    public static ApplicationSettingsSingleton getInstance() {
        if (applicationSettingsSingleton == null) {
            applicationSettingsSingleton = new ApplicationSettingsSingleton();
        }
        return applicationSettingsSingleton;
    }

    private User currentUser;

}
