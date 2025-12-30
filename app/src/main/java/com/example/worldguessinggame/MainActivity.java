package com.example.worldguessinggame;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordguessinggame.R;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private List<NameEntity> names;
    private int currentIndex = 0;

    private TextView txtGuess, txtCounter;
    private GridLayout letterGrid;

    private String currentName = null;
    private char[] guessed = null;

    private final char[] letters = {
            'ا','آ','ب','پ','ت','ث','ج','چ','ح','خ',
            'د','ر','ز','ژ','س','ش','ص','ض','ط','ظ',
            'ع','غ','ف','ق','ک','گ','ل','م','ن',
            'و','ه','ی'
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGuess = findViewById(R.id.txtGuess);
        txtCounter = findViewById(R.id.txtCounter);
        letterGrid = findViewById(R.id.letterGrid);

        db = AppDatabase.getInstance(this);

        if (db.nameDao().getAllNames().isEmpty()) {
            generateRandomSingleNames(db);
        }

        names = db.nameDao().getAllNames();
        Collections.shuffle(names);

        setupLetterButtons();
        loadName();
    }

    // --------------------
    // دکمه‌های حروف
    // --------------------
//
    private void setupLetterButtons() {
        letterGrid.removeAllViews();

        int columnCount = 4; // تعداد ستون‌ها
        letterGrid.setColumnCount(columnCount);
        letterGrid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        letterGrid.setUseDefaultMargins(true);

        for (char c : letters) {

            Button btn = new Button(this, null, 0, R.style.LetterButton);
            btn.setText(String.valueOf(c));
            btn.setOnClickListener(v -> onLetterClicked(c));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            // هر دکمه یک ستون کامل رو پر کنه
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;

            // وزن مساوی برای پخش شدن
            params.columnSpec = GridLayout.spec(
                    GridLayout.UNDEFINED,
                    1f
            );

            // فاصله بین دکمه‌ها
            params.setMargins(12, 12, 12, 12);

            btn.setLayoutParams(params);

            letterGrid.addView(btn);
        }
    }


    // --------------------
    // لود اسم
    // --------------------
    private void loadName() {
        currentName = null;
        guessed = null;

        while (currentIndex < names.size()) {
            String raw = names.get(currentIndex).name;
            String cleaned = cleanName(raw);

            if (!cleaned.isEmpty()) {
                currentName = cleaned;
                break;
            }
            currentIndex++;
        }

        if (currentName == null) {
            txtGuess.setText("پایان بازی");
            return;
        }

        guessed = new char[currentName.length()];
        for (int i = 0; i < guessed.length; i++) {
            guessed[i] = '_';
        }

        updateGuessText();
        txtCounter.setText((currentIndex + 1) + " / " + names.size());
    }

    // --------------------
    // کلیک روی حرف
    // --------------------
    private void onLetterClicked(char letter) {
        if (currentName == null || guessed == null) return;

        boolean found = false;

        for (int i = 0; i < currentName.length(); i++) {
            if (currentName.charAt(i) == letter) {
                guessed[i] = letter;
                found = true;
            }
        }

        if (found) {
            updateGuessText();
            if (isComplete()) {
                currentIndex++;
                loadName();
            }
        }
    }

    private void updateGuessText() {
        StringBuilder sb = new StringBuilder();
        for (char c : guessed) {
            sb.append(c).append(' ');
        }
        txtGuess.setText(sb.toString());
    }

    private boolean isComplete() {
        for (char c : guessed) {
            if (c == '_') return false;
        }
        return true;
    }

    private String cleanName(String name) {
        return name.replaceAll("[^آابپتثجچحخدذرزژسشصضطظعغفقکگلمنوهی]", "");
    }

    private void generateRandomSingleNames(AppDatabase db) {
        try {
            List<String> baseNames = JsonUtils.readNamesFromAssets(this);
            for (int i = 0; i < 1000; i++) {
                String name = baseNames.get((int) (Math.random() * baseNames.size()));
                db.nameDao().insert(new NameEntity(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}