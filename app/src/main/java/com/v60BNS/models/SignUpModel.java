package com.v60BNS.models;

import android.content.Context;
import android.net.Uri;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.v60BNS.BR;
import com.v60BNS.R;


public class SignUpModel extends BaseObservable {
    private Uri logo;
    private Uri banner;
    private String name;
    private String email;
    private String user_type;

    private String phone_code;
    private String phone;
    
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();


    public boolean isDataValid(Context context)
    {
        if (!name.trim().isEmpty()&&
                !email.trim().isEmpty()&&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        ){
            error_name.set(null);
            error_email.set(null);

            return true;
        }else
            {
                if (name.trim().isEmpty())
                {
                    error_name.set(context.getString(R.string.field_required));

                }else
                    {
                        error_name.set(null);

                    }

                if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches())
                {
                    error_email.set(context.getString(R.string.inv_email));

                }else {
                    error_email.set(null);

                }

                return false;
            }
    }
    public SignUpModel() {
        setName("");
        setEmail("");
        setLogo(null);
        setBanner(null);

    }



    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }


    @Bindable
    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
        notifyPropertyChanged(BR.user_type);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    public Uri getLogo() {
        return logo;
    }

    public void setLogo(Uri logo) {
        this.logo = logo;
    }

    public Uri getBanner() {
        return banner;
    }

    public void setBanner(Uri banner) {
        this.banner = banner;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

