package com.example.spokenenglishappfortermpaper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.spokenenglishappfortermpaper.databinding.FragmentAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.function.ObjIntConsumer;


public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private boolean userStatus = false;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();

        binding.btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(binding.getRoot()).navigateUp();
            }
        });

        binding.btnAddNewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewTextInDB(binding.textInput.getText().toString());
            }
        });

        makeUiForUser();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Navigation.findNavController(binding.getRoot())
//                .popBackStack();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void makeUiForUser(){
        FirebaseUser user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        myRef.child("users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    //Toast.makeText(getContext(), task.getResult().toString(), Toast.LENGTH_SHORT).show();
                    binding.tvUsername.setText("Welcome, " + task.getResult().child("username").getValue());
                    if (task.getResult().child("status").getValue().equals("teacher")){
                        binding.textInput.setVisibility(View.VISIBLE);
                        binding.tvTextInput.setVisibility(View.VISIBLE);
                        binding.btnAddNewText.setVisibility(View.VISIBLE);
                        binding.btnPast.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void writeNewTextInDB(String text){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        String uniqueKey = myRef.child("exercises").push().getKey(); // Generate a unique key
        myRef.child("exercises").child(uniqueKey).setValue(text);
        Toast.makeText(getContext(), "Ваш текст успешно добавлен", Toast.LENGTH_SHORT).show();
    }

}