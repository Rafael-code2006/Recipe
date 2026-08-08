package com.example.recipe.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.R;
import com.example.recipe.adapters.AdapterEditRecipes;
import com.example.recipe.model.Ingredient;
import com.example.recipe.model.Recipes;
import com.example.recipe.setting.MyApp;
import com.example.recipe.setting.TextKey;
import com.example.recipe.viewmodel.EditRecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

public class EditRecipe extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RecyclerView recyclerView;

    private TextView title;
    private TextView nameRecipeTitle;
    private TextView ingredientsTitle;
    private TextView instructionTitle;

    private FloatingActionButton addIngredient;

    private ImageView imageView;

    private EditText editTextInsctruction;
    private EditText editTextRecipe;
    private Button saveButton;
    private EditRecipeViewModel viewModel;
    private AdapterEditRecipes adapter;

    private Recipes recipe;

    private static final int PICK_IMAGE_REQUEST = 101;
    private static final int REQUEST_PERMISSION = 102;

    private String image;

    private int sizeIngedient;
    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_recipe);
        initView();

        showRecipe();

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        CardView cardView = findViewById(R.id.CardView);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            getWindow().getDecorView().setWindowInsetsAnimationCallback(
                    new WindowInsetsAnimation.Callback(WindowInsetsAnimation.Callback.DISPATCH_MODE_STOP) {
                        @Override
                        public WindowInsets onProgress(WindowInsets insets, List<WindowInsetsAnimation> runningAnimations) {
                            int imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom;
                            int navBar = insets.getInsets(WindowInsets.Type.navigationBars()).bottom;
                            int offset = imeHeight - navBar;

                            // Не двигаем вниз — только вверх
                            int translation = offset > 0 ? -offset : 0;

                            floatingActionButton.setTranslationY(translation);
                            cardView.setTranslationY(translation);

                            return insets;
                        }
                    }
            );
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

    private void showRecipe() {
        Recipes recipes = (Recipes) getIntent().getSerializableExtra("Recipe");
        viewModel.loadRecipe(recipes);
        viewModel.getRecipe().observe(EditRecipe.this, recipeDB-> {
            recipe = recipeDB;

            setStarted(recipe);

            Swipe();

            showImage(recipe);

            clickSaveButton(recipe);

            changeLanguage();

            addNewIngredient(recipe);

            imageView.setOnClickListener(v -> openGallery());
        });
    }

    private static void setTextChanged(TextView name) {
        name.addTextChangedListener(new TextWatcher() {
            private boolean isEditing = false;

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                if (s.length() == 0) return;

                isEditing = true;

                // Первая буква заглавная
                String firstChar = s.subSequence(0,1).toString().toUpperCase();
                String rest = s.length() > 1 ? s.subSequence(1, s.length()).toString().toLowerCase() : "";

                s.replace(0, s.length(), firstChar + rest);

                isEditing = false;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            }
        });
    }


    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
                return;
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                return;
            }
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Выберите фото"), PICK_IMAGE_REQUEST);
    }

    // Получаем результат запроса разрешения
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
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
                imageView.setImageURI(Uri.fromFile(file));

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Не удалось сохранить изображение", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImage(Recipes recipe) {
        try {
            String imagePath = recipe.getImage();
            Log.d("ShowRecipe", "URI из базы: " + imagePath);

            if (imagePath == null || imagePath.isEmpty()) {
                imageView.setImageResource(R.drawable.steak);
                return;
            }

            File file = new File(imagePath);

            if (file.exists()) {
                imageView.setImageURI(Uri.fromFile(file)); // <-- вот здесь валидный Uri
            } else {
                Log.d("ShowRecipe", "Файл не найден: " + imagePath);
                imageView.setImageResource(R.drawable.steak);
            }

        } catch (Exception e) {
            Log.e("ShowRecipe", "Ошибка показа изображения", e);
            imageView.setImageResource(R.drawable.steak);
        }
    }

    private void addNewIngredient(Recipes recipes) {
        addIngredient.setOnClickListener(v -> {
            List<Ingredient> newDescriptions = adapter.getIngredients();
            Ingredient desc = new Ingredient();
            desc.setRecipeId(recipes.getId());
            desc.setName("");
            desc.setUnit("kg");
            recyclerView.post(() -> setRecyclerViewHeightBasedOnChildren(recyclerView));
            newDescriptions.add(desc);
            adapter.setIngredient(newDescriptions);
            adapter.notifyDataSetChanged();
            sizeIngedient = adapter.getItemCount();
            Log.d("Testovich", "size: " + sizeIngedient);
        });
    }

    public void Swipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d("Rafa", "Swipe сработал");
                int position = viewHolder.getBindingAdapterPosition(); // Получаем позицию рецепта


                // Если позиция существует
                if (position != RecyclerView.NO_POSITION) {
                    Ingredient ingredient = adapter.getIngredients().get(position); // Берем рецепт по позиции из адаптера
                    deletedIngredient(position, ingredient);
                    recyclerView.post(() -> setRecyclerViewHeightBasedOnChildren(recyclerView));
                }
            }


            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }


    private void deletedIngredient(int position, Ingredient ingredient) {
        adapter.notifyItemChanged(position); // Указываем перепроверить позицию

        // Диалог подтверждения
        new AlertDialog.Builder(EditRecipe.this)
                .setTitle("Удалить рецепт")
                .setMessage("Вы уверены, что хотите удалить этот ингредиент?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    viewModel.deleteIngredient(ingredient); // Удаляем из базы
                    adapter.removeIngredient(position); // Удаляем из адаптера
                    sizeIngedient = adapter.getItemCount();
                    Log.d("Testovich", "size: " + sizeIngedient);
                })
                .setNegativeButton("Отмена", (dialog, which) -> {
                    adapter.notifyItemChanged(position); // Указываем перепроверить позицию
                })
                .show();
    }



    private void clickSaveButton(Recipes recipes) {
        saveButton.setOnClickListener(v -> {
            boolean success = false;

                List<Ingredient> descriptions = adapter.getIngredients();

                recipes.setName(editTextRecipe.getText().toString());
                recipes.setInsctruction(editTextInsctruction.getText().toString());
            if(image != null){
                Log.d("Testtest", image);
                recipes.setImage(image);
            }

                for (Ingredient x : descriptions) {
                    if (x.getName() == null || x.getName().isEmpty()) {
                        success = false;
                        break;
                    } else {
                        success = true;
                    }
                }
                Log.d("Testovich", "size: " + sizeIngedient);
                if(success || sizeIngedient == 0) {

                    Disposable disposable = viewModel.saveAllIngredients(descriptions)
                            .andThen(viewModel.editRecipe(recipes))
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError(new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Throwable {
                                    Log.d("EditRecipe", throwable.getMessage());
                                }
                            })
                            .subscribe(() -> {

                                Intent intent = ShowRecipe.newIntent(EditRecipe.this, recipes);
                                startActivity(intent);
                                finish();

                            }, throwable -> {
                                Log.d("EditRecipe", throwable.getMessage());
                            });

                    compositeDisposable.add(disposable);
                } else {
                    Toast.makeText(myApp, "Введите имя игредиента", Toast.LENGTH_SHORT).show();
                }

        });


    }


    private void setStarted(Recipes recipes) {
        editTextRecipe.setText(recipes.getName());
        editTextInsctruction.setText(recipes.getInsctruction());

        viewModel.loadIngredients(recipes);
        viewModel.getIngredients().observe(this, descriptions -> {
            adapter.setIngredient(descriptions);
            sizeIngedient = descriptions.size();

            // Пересчитываем высоту RecyclerView после обновления данных
            recyclerView.post(() -> setRecyclerViewHeightBasedOnChildren(recyclerView));
        });
    }

    private void changeLanguage() {
        // Заголовки и подписи
        title.setText(MyApp.text(TextKey.EDIT));                 // "Изменить Рецепт", "Edit Recipe", "Рецептті Өңдеу"
        nameRecipeTitle.setText(MyApp.text(TextKey.NAME));       // "Имя", "Name", "Атауы"
        ingredientsTitle.setText(MyApp.text(TextKey.INGREDIENTS));// "Ингредиенты", "Ingredients", "Құрамы"
        instructionTitle.setText(MyApp.text(TextKey.INSTRUCTION));// "Инструкция", "Instruction", "Нұсқаулық"

        // Хинты для EditText
        editTextRecipe.setHint(MyApp.text(TextKey.NAME));        // "имя", "name", "атауы"
        editTextInsctruction.setHint(MyApp.text(TextKey.INSTRUCTION)); // "инструкция", "insctruction", "нұсқаулық"

        saveButton.setText(MyApp.text(TextKey.SAVE));
    }

    private void initView(){
        recyclerView = findViewById(R.id.RecyclerViewIngredients);
        editTextRecipe = findViewById(R.id.EditTextRecipe);
        setTextChanged(editTextRecipe);
        adapter = new AdapterEditRecipes();
        editTextInsctruction = findViewById(R.id.EditTextInsctructionRecipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(EditRecipeViewModel.class);
        saveButton = findViewById(R.id.RecipeEditButton);
        myApp = MyApp.getInstance();
        title = findViewById(R.id.YourRecipeTextView);
        nameRecipeTitle = findViewById(R.id.TextViewNameRecipe);
        ingredientsTitle = findViewById(R.id.TextViewIngredients);
        instructionTitle = findViewById(R.id.TextViewInstruction);
        addIngredient = findViewById(R.id.floatingActionButton);
        imageView = findViewById(R.id.RecipeImageView);
    }


    private void setRecyclerViewHeightBasedOnChildren(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null || adapter.getItemCount() == 0) return;

        int totalHeight = 0;
        int marginPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8, // отступ между элементами
                recyclerView.getResources().getDisplayMetrics()
        );

        for (int i = 0; i < adapter.getItemCount(); i++) {
            RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
            adapter.onBindViewHolder(holder, i);
            holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.UNSPECIFIED
            );
            totalHeight += holder.itemView.getMeasuredHeight() + marginPx;
        }

        // добавляем небольшой запас, чтобы последний элемент точно помещался
        totalHeight += (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                19,
                recyclerView.getResources().getDisplayMetrics()
        );

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = totalHeight;
        recyclerView.setLayoutParams(params);
        recyclerView.requestLayout();
    }





    public static Intent getIntent(Context context, Recipes recipes){
     Intent intent = new Intent(context, EditRecipe.class);
        intent.putExtra("Recipe", (Serializable) recipes);
     return intent;
    }
}