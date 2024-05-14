package com.example.spokenenglishappfortermpaper;

import androidx.appcompat.widget.AppCompatButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spokenenglishappfortermpaper.databinding.FragmentSignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;

    private SharedViewModel sharedViewModel;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputName;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputConfirmPassword;
    private AppCompatButton loginSignUpButton;
    private TextView toLoginTextView;

    private boolean selectedOption = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSignupBinding.inflate(inflater, container, false);

        textInputEmail = binding.textInputEmail;
        textInputName = binding.textInputName;
        textInputPassword = binding.textInputPassword;
        textInputConfirmPassword = binding.textInputConfirmPassword;
        loginSignUpButton = binding.loginSignUpButton;
        toLoginTextView = binding.toLoginTextView;

        mAuth = FirebaseAuth.getInstance();

        toLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_nav_signup_to_nav_login);
            }
        });

        loginSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmail();
                validateName();
                validatePassword();
                validateConfirmPassword();
                signUpUser();
            }
        });

        RadioGroup radioGroup = binding.radioGroup;

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = binding.getRoot().findViewById(checkedId);
            if (radioButton.getId() == R.id.radioButton1) {
                Toast.makeText(getContext(), "Option 1 selected", Toast.LENGTH_SHORT).show();
                selectedOption = false;
            } else if (radioButton.getId() == R.id.radioButton2) {
                Toast.makeText(getContext(), "Option 2 selected", Toast.LENGTH_SHORT).show();
                selectedOption = true;
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_nav_login_to_nav_account);;
        }
    }

    private boolean validateEmail(){
        String emailInput = textInputEmail.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            textInputEmail.setError("Empty string");
            return false;
        } else {
            textInputEmail.setError("");
            return true;
        }
    }
    private boolean validateName(){
        String input = textInputName.getEditText().getText().toString().trim();
        if(input.isEmpty()){
            textInputName.setError("Empty string");
            return false;
        } else {
            textInputName.setError("");
            return true;
        }
    }
    private boolean validatePassword(){
        String input = textInputPassword.getEditText().getText().toString().trim();
        if(input.isEmpty()){
            textInputPassword.setError("Empty string");
            return false;
        } else {
            textInputPassword.setError("");
            return true;
        }
    }
    private boolean validateConfirmPassword(){
        String input = textInputConfirmPassword.getEditText().getText().toString().trim();
        String input0 = textInputPassword.getEditText().getText().toString().trim();
        if(input.isEmpty()){
            textInputConfirmPassword.setError("Empty string");
            return false;
        } else if(!input.equals(input0)){
            textInputConfirmPassword.setError("Password is not confirmed");
            return false;
        } else {
            textInputConfirmPassword.setError("");
            return true;
        }
    }

    private void signUpUser(){
        if(validateConfirmPassword() && validatePassword() && validateName() && validateEmail()){

            String email = textInputEmail.getEditText().getText().toString().trim();
            String password = textInputPassword.getEditText().getText().toString().trim();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String inputName = textInputName.getEditText().getText().toString().trim();

                                // Sign in success, update UI with the signed-in user's information
                                Log.d("signup", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                Tools.NAME = textInputName.getEditText().getText().toString().trim();

                                // Write a message to the database
                                database = FirebaseDatabase.getInstance();
                                myRef = database.getReference("message");
                                myRef.child("users").child(user.getUid()).child("username").setValue(inputName);
                                if (selectedOption){
                                    myRef.child("users").child(user.getUid()).child("status").setValue("teacher");
                                } else {
                                    myRef.child("users").child(user.getUid()).child("status").setValue("student");
                                }
//                                myRef.child("exercises").child("New text").setValue("i am the abobus and you are amogus");
//                                myRef.child("users").child(user.getUid()).child("points").setValue(77);
//                                myRef.child("users").child(user.getUid()).child("date_time").setValue(LocalDateTime.now());

                                Navigation.findNavController(binding.getRoot())
                                        .navigate(R.id.action_nav_signup_to_nav_account);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("signup", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });

            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}