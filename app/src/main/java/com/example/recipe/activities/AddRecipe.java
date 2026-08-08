package com.example.recipe.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipe.activityHelpers.AddRecipeHelper;
import com.example.recipe.model.BottomMenu;
import com.example.recipe.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AddRecipe extends AppCompatActivity {
    private AddRecipeHelper helper;

    static final int PICK_IMAGE_REQUEST = 101;
    private static final int REQUEST_PERMISSION = 102;

    private ImageView imageRecipe;

    private String image;
    private static int counter_ingredients = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe);
        helper = new AddRecipeHelper(this);

        View menu = findViewById(R.id.bottomMenuAdd);
        BottomMenu bottom = new BottomMenu(menu);

        helper.changeLanguage();

        helper.FloatingClickButton(); // Добавление поле для ингридиента

        helper.SaveButtonClick(); // Нажатие кнопки сохранения рецепта

        helper.setImage();

        helper.AdjustToTheKeyboard();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    0
            );

            return insets;
        });
    }



    // Получаем результат запроса разрешения
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                helper.openGallery();
            } else {
                Toast.makeText(this, "Нужно разрешение на доступ к галерее", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Получаем результат выбора изображения
    // В onActivityResult после выбора изображения
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                // Создаём файл в internal storage
                File file = new File(getFilesDir(), String.format("temp_%s_image.jpg", System.currentTimeMillis()));

                try (InputStream input = getContentResolver().openInputStream(imageUri);
                     OutputStream output = new FileOutputStream(file)) {

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                }

                // Сохраняем путь к файлу в базе или переменной
                image = file.getAbsolutePath();

                // Ставим картинку в ImageView
                imageRecipe.setImageURI(Uri.fromFile(file));

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Не удалось сохранить изображение", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public static Intent newIntent(Context context) {
        return new Intent(context, AddRecipe.class);
    }


}