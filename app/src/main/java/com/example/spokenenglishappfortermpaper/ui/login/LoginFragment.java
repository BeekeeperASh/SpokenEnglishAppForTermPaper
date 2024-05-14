package com.example.spokenenglishappfortermpaper.ui.login;

import androidx.appcompat.widget.AppCompatButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spokenenglishappfortermpaper.SharedViewModel;
import com.example.spokenenglishappfortermpaper.Tools;
import com.example.spokenenglishappfortermpaper.databinding.FragmentLoginBinding;

import com.example.spokenenglishappfortermpaper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private SharedViewModel sharedViewModel;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private AppCompatButton loginSignUpButton;

    private TextView toSignUpTextView;

    private FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        textInputEmail = binding.textInputEmail;
        textInputPassword = binding.textInputPassword;
        loginSignUpButton = binding.loginSignUpButton;

        toSignUpTextView = binding.toSignUpTextView;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        toSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_nav_login_to_nav_signup);
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Navigation.findNavController(binding.getRoot())
//                .popBackStack();
        //sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_nav_login_to_nav_account);

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

    private void loginUser(){
        if(validatePassword() & validateEmail()){

            String email = textInputEmail.getEditText().getText().toString().trim();
            String password = textInputPassword.getEditText().getText().toString().trim();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("signup", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);


                                database = FirebaseDatabase.getInstance();
                                myRef = database.getReference("message");

                                myRef.child("users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("firebase", "Error getting data", task.getException());
                                        }
                                        else {
                                            Tools.NAME = String.valueOf(task.getResult().child("username").getValue());

                                            Log.d("firebase", "Tools.NAME -" + Tools.NAME);
                                            Log.d("firebase", String.valueOf(task.getResult().getValue()));

//                                            SharedPreferences mSettings;
//                                            mSettings = getSharedPreferences("savedData", Context.MODE_PRIVATE);
//                                            SharedPreferences.Editor editor = mSettings.edit();
//                                            editor.putString("name", Tools.NAME);
//                                            editor.apply();

                                        }
                                    }
                                });

                                Navigation.findNavController(binding.getRoot())
                                        .navigate(R.id.action_nav_login_to_nav_account);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("signup", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });

            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}