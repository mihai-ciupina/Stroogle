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


public class RegisterUserFragment extends Fragment {

    //app state
    private AppState appState;
    HomeActivityInterface mCallback;

    @InjectView(R.id.input_username) EditText _usernameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.button_signup) Button _signupButton;
    @InjectView(R.id.button_link_to_login_screen) Button _linkToLoginScreenButton;
    @InjectView(R.id.button_register_as_guest) Button _registerAsGuestButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //MyResources res = MyResources.getResource(QuizzFragment.class);

		/*code*/
        View view = inflater.inflate(R.layout.register_user, container, false);


        ButterKnife.inject(this, view);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNormalUser();
            }
        });

        _linkToLoginScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("xxx", "btnLinkToLoginScreen.setOnClickListener");

                appState.setActiveUIFragment("LoginUserFragment");
                mCallback.updateUI(appState.getActiveUIFragment());

                //mCallback.updateUI("LoginUserFragment");
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



    public void registerGuestUser() {

        String username = NameGenerator.generateName();

        //Log.d("xxx", "name= " + username + " status= " + "guest");
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("status", AppState.USER_STATUS_GUEST);
        registerUser(jsonObject);

    }

    public void registerNormalUser() {

        //release
        //if (!validate()) {
        //	onSignupFailed();
        //	return;
        //}

        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //Log.d("xxx", "name= " + username + " email= " + email + " password= " + password );
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("status", AppState.USER_STATUS_NORMAL_NO_CONFIRM);

        registerUser(jsonObject);

    }

    public void registerUser(JsonObject jsonObject) {

        //Log.d("xxx", "Signup");

        _signupButton.setEnabled(false);
        _linkToLoginScreenButton.setEnabled(false);
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
        _signupButton.setEnabled(true);
        _linkToLoginScreenButton.setEnabled(true);
        _registerAsGuestButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _usernameText.setError("at least 3 characters");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
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
