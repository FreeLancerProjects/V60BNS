package com.v60BNS.interfaces;


public interface Listeners {

    interface SignUpListener{

        void openSheet(int type);
        void closeSheet();
        void checkDataValid();
        void checkReadPermission();
        void checkCameraPermission();

    }

    interface BackListener
    {
        void back();
    }
    interface LoginListener{
        void validate();
        void showCountryDialog();
    }


    interface SettingAction{
        void onEditProfile();
        void onLanguageSetting();
        void onTerms();
        void onPrivacy();
        void onRate();
        void onTone();
        void about();
        void logout();

    }
}
