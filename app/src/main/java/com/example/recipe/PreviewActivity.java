package com.example.recipe;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe.activities.MainActivity;

public class PreviewActivity extends AppCompatActivity {

    private VideoView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        startActivity(MainActivity.getIntent(this));
        finish();

     /*   // Инициализация VideoView
        preview = findViewById(R.id.preview);

        // Путь к видео в res/raw
        String path = "android.resource://" + getPackageName() + "/" + R.raw.test;
        Uri uri = Uri.parse(path);
        preview.setVideoURI(uri);
        preview.requestFocus();

        // Автозапуск при подготовке
        preview.setOnPreparedListener(mp -> preview.start());

        // Действие после окончания видео
        preview.setOnCompletionListener(mp -> {
            Toast.makeText(this, "Thank you", Toast.LENGTH_SHORT).show();
            preview.postDelayed(() -> {
                startActivity(MainActivity.getIntent(this));
                finish();
            }, 1000); // 1 секунда задержки
        });

        // Обработка ошибок воспроизведения
        preview.setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(this, "Error loading video", Toast.LENGTH_SHORT).show();
            return true;
        });

      */
    }
}
