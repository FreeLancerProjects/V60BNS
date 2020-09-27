package com.v60BNS.activities_fragments.activity_sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_home.HomeActivity;
import com.v60BNS.databinding.ActivitySignUpBinding;
import com.v60BNS.databinding.DialogYearBinding;
import com.v60BNS.interfaces.Listeners;
import com.v60BNS.language.Language_Helper;
import com.v60BNS.models.SignUpModel;
import com.v60BNS.models.UserModel;
import com.v60BNS.preferences.Preferences;
import com.v60BNS.remote.Api;
import com.v60BNS.share.Common;
import com.squareup.picasso.Picasso;
import com.v60BNS.tags.Tags;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements Listeners.SignUpListener {
    private ActivitySignUpBinding binding;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private Uri uri = null, uribanner = null;
    private SignUpModel signUpModel;
    private Preferences preferences;
    private int type, typeedit;
    private UserModel userModel;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        initView();
        getDataFromIntent();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        signUpModel = new SignUpModel();
        binding.setModel(signUpModel);
        binding.setListener(this);
        userModel = preferences.getUserData(this);
        if (typeedit == 2) {
            signUpModel.setEmail(userModel.getEmail());
            signUpModel.setName(userModel.getName());
            Picasso.get().load(Tags.IMAGE_URL + userModel.getLogo()).placeholder(R.drawable.ic_avatar).into(binding.imgLogo);
            Picasso.get().load(Tags.IMAGE_URL + userModel.getBanner()).placeholder(R.drawable.ic_gallery).into(binding.imgBanner);
            binding.setModel(signUpModel);
        }

        binding.rbChoose1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    signUpModel.setUser_type("client");

                }
            }
        });
        binding.rbChoose2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    signUpModel.setUser_type("expert");

                }
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("phone_code") != null) {
                String phone_code = intent.getStringExtra("phone_code");
                String phone = intent.getStringExtra("phone");

                signUpModel.setPhone_code(phone_code);
                signUpModel.setPhone(phone);
                typeedit = 1;
            } else if (intent.getSerializableExtra("data") != null) {
                binding.btsignup.setText(getResources().getString(R.string.edit_profile));
                typeedit = 2;
            }
        }
    }

    @Override
    public void openSheet(int type) {
        this.type = type;
        binding.expandLayout.setExpanded(true, true);
    }

    @Override
    public void closeSheet() {
        binding.expandLayout.collapse(true);

    }


    @Override
    public void checkDataValid() {

        if (signUpModel.isDataValid(this) && typeedit == 1) {
            Common.CloseKeyBoard(this, binding.edtName);
            signUp();
        } else if (signUpModel.isDataValid(this) && typeedit == 2) {
            Common.CloseKeyBoard(this, binding.edtName);
            updateWithoutImage();
        }

    }

    @Override
    public void checkReadPermission() {
        closeSheet();
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    @Override
    public void checkCameraPermission() {

        closeSheet();

        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }


    private void SelectImage(int req) {

        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, req);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {
            if (type == 2) {
                uri = data.getData();
                File file = new File(Common.getImagePath(this, uri));
                Picasso.get().load(file).fit().into(binding.imgLogo);
                if (typeedit == 2) {
                    updateWithImage();
                }
            } else {
                uribanner = data.getData();
                File file = new File(Common.getImagePath(this, uribanner));
                Picasso.get().load(file).fit().into(binding.imgBanner);
                if (typeedit == 2) {
                    updateWithImageBanner();
                }
            }

        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (type == 2) {
                uri = getUriFromBitmap(bitmap);
                if (uri != null) {
                    String path = Common.getImagePath(this, uri);

                    if (path != null) {
                        Picasso.get().load(new File(path)).fit().into(binding.imgLogo);

                    } else {
                        Picasso.get().load(uri).fit().into(binding.imgLogo);

                    }
                    if (typeedit == 2) {
                        updateWithImage();
                    }
                }
            } else {
                uribanner = getUriFromBitmap(bitmap);
                if (uribanner != null) {
                    String path = Common.getImagePath(this, uribanner);

                    if (path != null) {
                        Picasso.get().load(new File(path)).fit().into(binding.imgBanner);

                    } else {
                        Picasso.get().load(uribanner).fit().into(binding.imgBanner);

                    }
                    if (typeedit == 2) {
                        updateWithImageBanner();
                    }
                }
            }

        }

    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }


    private void createDialogAlert() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogYearBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_year, null, false);
        List<String> years = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int start = year - 50;
        int end = year - 18;
        for (int y = start; y <= end; y++) {
            years.add(String.valueOf(y));
        }


        String[] values = new String[years.size()];

        binding.picker.setMinValue(0);
        binding.picker.setMaxValue(years.size() - 1);
        binding.picker.setWrapSelectorWheel(true);
        binding.picker.setDisplayedValues(years.toArray(values));
        binding.picker.setValue(1);
        binding.imageUp.setOnClickListener(v -> {
            binding.picker.setValue(binding.picker.getValue() - 1);
        });

        binding.imageDown.setOnClickListener(v -> {
            binding.picker.setValue(binding.picker.getValue() + 1);
        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void signUp() {
        if (uri == null) {
            if (uribanner != null) {
                signUpWithoutImage();
            } else {
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.banner_image), Toast.LENGTH_LONG).show();
            }
        } else {
            signUpWithImage();
        }
        //navigateToHomeActivity();
    }

    private void signUpWithoutImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(signUpModel.getPhone_code().replace("+", "00"));
        RequestBody phone_part = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody email_part = Common.getRequestBodyText(signUpModel.getEmail());

        RequestBody type_part = Common.getRequestBodyText(signUpModel.getUser_type());
        RequestBody soft_part = Common.getRequestBodyText("android");

        MultipartBody.Part image = Common.getMultiPart(this, uribanner, "banner");


        Api.getService(Tags.base_url)
                .signUpWithImagewithoutlogo(name_part, email_part, phone_code_part, phone_part, type_part, soft_part, image)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            preferences.create_update_userdata(SignUpActivity.this, response.body());
                            navigateToHomeActivity();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 422) {
                                Toast.makeText(SignUpActivity.this, R.string.user_found, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void signUpWithImage() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getName());
        RequestBody phone_code_part = Common.getRequestBodyText(signUpModel.getPhone_code().replace("+", "00"));
        RequestBody phone_part = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody email_part = Common.getRequestBodyText(signUpModel.getEmail());

        RequestBody type_part = Common.getRequestBodyText(signUpModel.getUser_type());
        RequestBody soft_part = Common.getRequestBodyText("android");

        MultipartBody.Part image = Common.getMultiPart(this, uri, "banner");


        MultipartBody.Part imagelogo = Common.getMultiPart(this, uri, "logo");


        Api.getService(Tags.base_url)
                .signUpWithImagewithout(name_part, email_part, phone_code_part, phone_part, type_part, soft_part, image, imagelogo)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            preferences.create_update_userdata(SignUpActivity.this, response.body());
                            navigateToHomeActivity();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 422) {
                                Toast.makeText(SignUpActivity.this, R.string.user_found, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void updateWithoutImage() {

        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody id_part = Common.getRequestBodyText(String.valueOf(userModel.getId()));
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getName());
        RequestBody email_part = Common.getRequestBodyText(signUpModel.getEmail());


        try {
            Api.getService(Tags.base_url)
                    .editClientProfileWithoutImage("Bearer " + userModel.getToken(), name_part, email_part)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                response.body().setToken(userModel.getToken());
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                Intent intent = getIntent();
                                if (intent != null) {
                                    setResult(Activity.RESULT_OK, intent);
                                }
                                finish();

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(SignUpActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();

        }
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateWithImage() {

        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();


        MultipartBody.Part image_part = Common.getMultiPart(this, uri, "logo");

        try {
            Api.getService(Tags.base_url)
                    .editClientProfileWithImage("Bearer " + userModel.getToken(), image_part)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                response.body().setToken(userModel.getToken());
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                Intent intent = getIntent();
                                if (intent != null) {
                                    setResult(Activity.RESULT_OK, intent);
                                }
                                finish();

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(SignUpActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();

        }

    }

    private void updateWithImageBanner() {

        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();


        MultipartBody.Part image_part = Common.getMultiPart(this, uri, "banner");

        try {
            Api.getService(Tags.base_url)
                    .editClientProfileWithImage("Bearer " + userModel.getToken(), image_part)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                response.body().setToken(userModel.getToken());
                                preferences.create_update_userdata(SignUpActivity.this, response.body());
                                Intent intent = getIntent();
                                if (intent != null) {
                                    setResult(Activity.RESULT_OK, intent);
                                }
                                finish();

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(SignUpActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();

        }

    }
}
