package com.probitorg.stroogle.loader;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.probitorg.stroogle.R;
import com.probitorg.stroogle.app.AppState;
import com.probitorg.stroogle.app.HomeActivityInterface;
import com.probitorg.stroogle.app.MyApplication;
import com.probitorg.stroogle.network.DummyObject;
import com.probitorg.stroogle.network.GsonPutRequest;
import com.probitorg.stroogle.utils.NameGenerator;


import butterknife.ButterKnife;
import butterknife.InjectView;




public class LoginUserFragment extends Fragment {

    //app state
    private AppState appState;
    HomeActivityInterface mCallback;

    @InjectView(R.id.input_username) EditText _usernameText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.button_login) Button _loginButton;
    @InjectView(R.id.button_link_to_register_screen) Button _linkToRegisterScreenButton;
    @InjectView(R.id.button_register_as_guest) Button _registerAsGuestButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		/*code*/

        View view = inflater.inflate(R.layout.login_user, container, false);

        ButterKnife.inject(this, view);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _linkToRegisterScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.updateUI("RegisterUserFragment");
            }
        });

        _registerAsGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerGuestUser();
            }
        });

        appState = ((MyApplication) getActivity().getApplication()).getAppStateRef();

		/*code*/

        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



    public void login() {
        //Log.d("xxx", "Login");

        //if (!validate()) {
        //	onSignupFailed();
        //	return;
        //}

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Waiting to Login ...");
        progressDialog.show();

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        //Log.d("xxx", "name= " + username + " password= " + password );
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);

        // TODO: Implement your own login logic here.
        final GsonPutRequest<DummyObject> gsonPutRequest =
                com.probitorg.stroogle.network.ApiRequests.login
                        (
                                new Response.Listener<DummyObject>() {
                                    @Override
                                    public void onResponse(DummyObject dummyObject) {

                                        //setData(dummyObject);
                                        // Deal with the JSONObject here
                                        //Log.d("xxx", "login succes");
                                        //Log.d("xxx", "dummyObject1" + dummyObject.getUsername());
                                        onLoginSuccess(dummyObject);
                                        progressDialog.dismiss();

                                    }
                                }
                                ,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Deal with the error here

                                        //Log.d("xxx", ".fail .reason[" + error.toString() + "]");
                                        onLoginFailed();
                                        progressDialog.dismiss();

                                    }
                                }
                                ,
                                jsonObject.toString()

                        );
        MyApplication.addRequest(gsonPutRequest, "xxx");
    }

    public void onLoginSuccess(DummyObject dummyObject) {

        mCallback.setState(dummyObject);

        appState.setActiveUIFragment(appState.getUserStatus());
        mCallback.updateUI(appState.getActiveUIFragment());

        _loginButton.setEnabled(true);

    }

    public void onLoginFailed() {

        Toast.makeText((Activity)mCallback, "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
        _linkToRegisterScreenButton.setEnabled(true);
        _registerAsGuestButton.setEnabled(true);

    }













    public boolean validate() {
        boolean valid = true;

        String name = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _usernameText.setError("at least 3 characters");
            valid = false;
        } else {
            _usernameText.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void registerGuestUser() {

        String username = NameGenerator.generateName();

        //Log.d("xxx", "name= " + username + " status= " + "guest");
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("status", AppState.USER_STATUS_GUEST);
        registerUser(jsonObject);

    }
    public void registerUser(JsonObject jsonObject) {

        //Log.d("xxx", "Register");

        _loginButton.setEnabled(false);
        _linkToRegisterScreenButton.setEnabled(false);
        _registerAsGuestButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        // TODO: Implement your own signup logic here.

        final GsonPutRequest<DummyObject> gsonPutRequest =
                com.probitorg.stroogle.network.ApiRequests.putDummyObject
                        (
                                new Response.Listener<DummyObject>() {
                                    @Override
                                    public void onResponse(DummyObject dummyObject) {
                                        onRegisterSuccess(dummyObject);
                                        progressDialog.dismiss();

                                    }
                                }
                                ,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //Log.d("xxx", ".fail .reason[" + error.toString() + "]");
                                        onRegisterFailed();
                                        progressDialog.dismiss();

                                    }
                                }
                                ,
                                jsonObject.toString()
                        );
        MyApplication.addRequest(gsonPutRequest, "xxx");
    }
    public void onRegisterSuccess(DummyObject dummyObject) {
        //finish();
        //Log.d("xxx", "succes");
        mCallback.setState(dummyObject);
        mCallback.updateUI(appState.getUserStatus());
    }
    public void onRegisterFailed() {
        Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
        _linkToRegisterScreenButton.setEnabled(true);
        _registerAsGuestButton.setEnabled(true);
    }










    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//<23
            onAttachToContext(activity);
        }
    }

    protected void onAttachToContext(Context context) {
        //Log.d("xxx", "1:");
        try {
            mCallback = (HomeActivityInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AppState interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }








}
