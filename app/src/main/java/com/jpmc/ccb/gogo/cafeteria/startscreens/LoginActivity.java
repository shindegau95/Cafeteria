package com.jpmc.ccb.gogo.cafeteria.startscreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jpmc.ccb.gogo.cafeteria.Applications.CafeteriaApplication;
import com.jpmc.ccb.gogo.cafeteria.ConnectionPackage.ConnectivityReceiver;
import com.jpmc.ccb.gogo.cafeteria.MainActivity;
import com.jpmc.ccb.gogo.cafeteria.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 05-02-2018.
 */

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    @BindView(R.id.email)
    EditText inputEmail;
    @BindView(R.id.password)
    EditText inputPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_signup)
    Button btnSignUp;
    @BindView(R.id.btn_reset_password)
    Button btnResetPassword;

    private ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CafeteriaApplication.setStatusBarColor(getWindow(), R.color.black);
        }

        //getting the firebase auth instance
        auth = FirebaseAuth.getInstance();

        //check if the user has already signed in
        if(auth.getCurrentUser() != null){

                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            finish();
        }

    }

    @OnClick(R.id.btn_signup)
    public void onBtnSignUpClick(){
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    @OnClick(R.id.btn_reset_password)
    public void onBtnResetClick(){
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }

    @OnClick(R.id.btn_login)
    public void onBtnLoginClick(){
        final String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();

        //Validation
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Email required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Password required", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //first check connection

                if(checkConnection()) {
                    progressBar.setVisibility(View.GONE);

                    if (!task.isSuccessful()) {
                        if (password.length() < 6) {
                            inputPassword.setError(getString(R.string.minimum_password));
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    //to check Internet Connection
    @Override
    protected void onResume() {
        super.onResume();
        CafeteriaApplication.getInstance().setConnectivityListener(this);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
    }

    private void showSnack(boolean isConnected) {
        if(!isConnected) {
            String msg = String.valueOf(R.string.check_connection);
            Snackbar snackbar = Snackbar.make(findViewById(R.id.linearLayout), msg, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

}
