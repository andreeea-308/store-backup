package org.example.utils;

public class Language {
    public static String getErrorUserExist() {
        if (ApplicationSettingsSingleton.getInstance().getCurrentUser().getLanguage() == org.example.model.Language.ENGLISH)
            return ConstantsEnglish.ERROR_USER_EXIST;
        else return ConstantsRomanian.ERROR_USER_EXIST;
    }

    public static String getMessageWelcome() {
        if (ApplicationSettingsSingleton.getInstance().getCurrentUser().getLanguage() == org.example.model.Language.ENGLISH)
            return ConstantsEnglish.MESSAGE_WELCOME;
        else return ConstantsRomanian.MESSAGE_WELCOME;
    }
}
