package com.example.spokenenglishappfortermpaper;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.spokenenglishappfortermpaper.databinding.FragmentCustomLessonBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class CustomLessonFragment extends Fragment {

    private SpeechRecognizer speechRecognizer;

    private TextToSpeech textToSpeech;
    FragmentCustomLessonBinding binding;

    public CustomLessonFragment() {
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
        binding = FragmentCustomLessonBinding.inflate(inflater, container, false);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                Toast.makeText(getContext(), "Recognition started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                binding.micOff2.setVisibility(View.VISIBLE);
                binding.micOn2.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Recognition ended", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i) {
                binding.micOff2.setVisibility(View.VISIBLE);
                binding.micOn2.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Recognition failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String text = binding.customText.getText().toString();
                int score = LevenshteinDistanceDP(data.get(0), text);
                binding.customScore.setText("Score " + score + "%");
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        binding.buttonUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.setLanguage(Locale.US);
                textToSpeech.speak(binding.customText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        binding.buttonUk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.setLanguage(Locale.UK);
                textToSpeech.speak(binding.customText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        binding.micOff2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechRecognizer.startListening(speechRecognizerIntent);
                binding.micOn2.setVisibility(View.VISIBLE);
                binding.micOff2.setVisibility(View.INVISIBLE);
            }
        });

        binding.micOn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechRecognizer.stopListening();
                binding.micOff2.setVisibility(View.VISIBLE);
                binding.micOn2.setVisibility(View.INVISIBLE);
            }
        });

        binding.buttonPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = clipboardManager.getPrimaryClip();

                if (clipData != null && clipData.getItemCount() > 0) {
                    CharSequence text = clipData.getItemAt(0).getText();

                    binding.customText.setText(text);
                }

            }
        });

        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.customText.setText("");
            }
        });

        return binding.getRoot();
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

    static int LevenshteinDistanceDP(String str1,
                                     String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                } else {

                    dp[i][j] = minm_edits(dp[i - 1][j - 1]
                                    + NumOfReplacement(str1.charAt(i - 1), str2.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }
        Integer distance = dp[str1.length()][str2.length()];
        Integer maxLen = Math.max(str1.length(), str2.length());
        int accuracy = (int) (((double) (maxLen - distance)) / maxLen * 100);
        return accuracy;
    }

    static int NumOfReplacement(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

    static int minm_edits(int... nums) {

        return Arrays.stream(nums).min().orElse(
                Integer.MAX_VALUE);
    }

}