package ru.mininn.rosberrytestapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ru.mininn.rosberrytestapp.Models.User;
import ru.mininn.rosberrytestapp.R;

/**
    User class have Login and pass. User to JSON, JSON to String , String to Preferences and all it in the AsyncTask
 */
public class LoginActivity extends AppCompatActivity  {

    private boolean isSignUpForm = false;
    private EditText mLoginView;
    private EditText mPasswordRepeatView;
    private EditText mPasswordView;
    private Button mSignInButton;
    private Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
        if(prefs.getString("CurrentUser",null)!=null){
            Intent myIntent = new Intent(LoginActivity.this,MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
            LoginActivity.this.finish();
        }
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mPasswordRepeatView = (EditText) findViewById(R.id.repeat_password);
        mLoginView = (EditText) findViewById(R.id.login);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                attemptLogin(false);
                return false;
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                attemptLogin(false);
                return false;
            }
        });
        mPasswordRepeatView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                attemptLogin(false);
                return false;
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSignUpForm){
                    isSignUpForm = true;
                    mPasswordRepeatView.setVisibility(View.VISIBLE);
                    mSignInButton.setVisibility(View.GONE);
                }else {
                    attemptLogin(true);
                }
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(true);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(isSignUpForm){
            mPasswordRepeatView.setVisibility(View.GONE);
            mSignInButton.setVisibility(View.VISIBLE);
            isSignUpForm = false;
        }else{
            super.onBackPressed();
        }
    }
    // Tests of Password and login
    private void attemptLogin(boolean isClick) {

        mLoginView.setError(null);
        mPasswordView.setError(null);
        mPasswordRepeatView.setError(null);

        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordRepeated = mPasswordRepeatView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isLoginValid(login)) {
            mLoginView.setError(getString(R.string.error_invalid_email));
            focusView = mLoginView;
            cancel = true;
        }

        if(isSignUpForm && !isRepeatedPasswordCorrect(password,passwordRepeated)){
            mPasswordRepeatView.setError(getString(R.string.passwords_not_match));
            focusView = mPasswordRepeatView;
            cancel =true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if(isClick) {
                UserLoginTask authTask = new UserLoginTask(login, password, isSignUpForm);
                authTask.execute((Void) null);
            }
        }
    }

    private boolean isLoginValid(String login) {
        return login.length() >=4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isRepeatedPasswordCorrect(String password,String passwordRepeat){
        return password.equals(passwordRepeat);
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private boolean isSignUpForm;
        private final String mLogin;
        private final String mPassword;
        UserLoginTask(String email, String password,boolean isSignUpForm) {
            this.isSignUpForm = isSignUpForm;
            mLogin = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
            SharedPreferences.Editor prefEdit = prefs.edit();
            Set<String>jsonUsers = prefs.getStringSet("Users",new HashSet<String>());
            User thisUser = new User(mLogin,mPassword);
            if(isSignUpForm){
                Log.d("isForm",String.valueOf(isSignUpForm));
                if(jsonUsers.isEmpty()){
                    jsonUsers.add(new User(mLogin,mPassword).toJson());
                    prefEdit.putStringSet("Users",jsonUsers).commit();
                    Log.d("JSON",new User(mLogin,mPassword).toJson());
                    return true;
                }else {
                    boolean isExist = false;
                    Iterator<String> iterator = jsonUsers.iterator();
                    while (iterator.hasNext()){
                        User user = User.fromJson(iterator.next());
                        if(user.equals(thisUser)){
                            isExist = true;
                            break;
                        }
                    }
                    if(isExist){
                        return false;
                    }else {
                        jsonUsers.add(thisUser.toJson());
                        prefEdit.putStringSet("Users", jsonUsers);
                        prefEdit.commit();
                        return true;
                    }
                }
            }else {
                boolean isExist = false;
                Iterator<String> iterator = jsonUsers.iterator();
                while (iterator.hasNext()){
                    User user = User.fromJson(iterator.next());
                    if(user.equals(thisUser)){
                        isExist = true;
                        break;
                    }
                }
                if(isExist){
                    return true;
                }else {
                    return false;
                }
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(!success){
                if(isSignUpForm){
                    EditText loginView = (EditText) findViewById(R.id.login);
                    loginView.setError(getString(R.string.login_exist));
                    return;
                }else {
                    EditText passwordView = (EditText) findViewById(R.id.password);
                    passwordView.setError(getString(R.string.wrong_password));
                    return;
                }
            }else {
                SharedPreferences prefs = getSharedPreferences("Prefs", MODE_PRIVATE);
                SharedPreferences.Editor prefEdit = prefs.edit();
                prefEdit.putString("CurrentUser",mLogin);
                prefEdit.commit();
                Intent myIntent = new Intent(LoginActivity.this,MainActivity.class);
                LoginActivity.this.startActivity(myIntent);
                LoginActivity.this.finish();
            }
        }
        }

    }

