package com.example.spokenenglishappfortermpaper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spokenenglishappfortermpaper.databinding.FragmentTextExerciseBinding;

public class TextExerciseFragment extends Fragment {

    FragmentTextExerciseBinding binding;

    private static final String ARG_PARAM = "title";
    private String mParam = "Nothing";

    public TextExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = (String) getArguments().getSerializable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTextExerciseBinding.inflate(inflater, container, false);

        binding.title.setText(mParam);

        return binding.getRoot();
    }
}