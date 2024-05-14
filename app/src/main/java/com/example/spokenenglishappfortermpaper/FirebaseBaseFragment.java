package com.example.spokenenglishappfortermpaper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spokenenglishappfortermpaper.databinding.FragmentFirebaseBaseBinding;
import com.example.spokenenglishappfortermpaper.databinding.FragmentFirstBinding;


public class FirebaseBaseFragment extends Fragment {

    private FragmentFirebaseBaseBinding binding;

    public FirebaseBaseFragment() {
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
        binding = FragmentFirebaseBaseBinding.inflate(inflater, container, false);




        return binding.getRoot();
    }

}