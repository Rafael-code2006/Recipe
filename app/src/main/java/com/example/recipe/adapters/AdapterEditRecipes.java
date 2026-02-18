package com.example.recipe.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.R;
import com.example.recipe.model.Descriptions;
import com.example.recipe.setting.MyApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdapterEditRecipes extends RecyclerView.Adapter<AdapterEditRecipes.IngredientViewHolder> {

    private List<Descriptions> ingredients = new ArrayList<>();

    private MyApp myApp = MyApp.getInstance();

    public interface CheckIngredient {
        void getIngredient(Descriptions ingredient);
    }

    private CheckIngredient checkIngredient;

    public void setCheckRecipes(CheckIngredient checkIngredient) {
        this.checkIngredient = checkIngredient;
    }

    public void setIngredient(List<Descriptions> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public List<Descriptions> getIngredients() {
        return ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Descriptions ingredient = ingredients.get(position);

        if(myApp.getBaseLanguage().equals("Рус")){
            holder.title.setText("Ингредиент");
            holder.editTextNameIngredient.setHint("имя");
            holder.editTextWeight.setHint("вес");
            List<String> newEntries = Arrays.asList("кг", "гр", "л", "мл", "мсл", "мчл");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    holder.itemView.getContext(), // важно: контекст берём из itemView
                    android.R.layout.simple_spinner_item,
                    newEntries
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            holder.unit.setAdapter(adapter);
        }
        if(myApp.getBaseLanguage().equals("Eng")){
            holder.title.setText("Ingredient");
            holder.editTextNameIngredient.setHint("name");
            holder.editTextWeight.setHint("weight");
            List<String> newEntries = Arrays.asList("kg", "gr", "l", "ml", "tbsp", "tsp");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    holder.itemView.getContext(), // важно: контекст берём из itemView
                    android.R.layout.simple_spinner_item,
                    newEntries
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            holder.unit.setAdapter(adapter);
        }
        if(myApp.getBaseLanguage().equals("Каз")){
            holder.title.setText("Құрамы");
            holder.editTextNameIngredient.setHint("атауы");
            holder.editTextWeight.setHint("салмағы");
            List<String> newEntries = Arrays.asList("кг", "гр", "л", "мл", "өақ", "өшқ");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    holder.itemView.getContext(), // важно: контекст берём из itemView
                    android.R.layout.simple_spinner_item,
                    newEntries
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            holder.unit.setAdapter(adapter);
        }



        // Заполняем поля текущими данными
        holder.editTextNameIngredient.setText(ingredient.getName());
        holder.editTextWeight.setText(String.valueOf(ingredient.getWeight()));

        // Устанавливаем Spinner
        String[] units = holder.unit.getResources().getStringArray(R.array.ingredients_unit);
        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(ingredient.getUnit())) {
                holder.unit.setSelection(i);

                break;
            }
        }


        // --- TextWatcher для EditText Name ---
        holder.editTextNameIngredient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                ingredient.setName(s.toString());
                if (checkIngredient != null) {
                    checkIngredient.getIngredient(ingredient);
                }
            }
        });

        // --- TextWatcher для EditText Weight ---
        holder.editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                float weight = 0;
                try {
                    weight = Float.parseFloat(s.toString());
                } catch (NumberFormatException e) { }
                ingredient.setWeight(weight);
                if (checkIngredient != null) {
                    checkIngredient.getIngredient(ingredient);
                }
            }
        });

        // --- Listener для Spinner Unit ---
        holder.unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ingredient.setUnit(parent.getItemAtPosition(position).toString());
                if (checkIngredient != null) {
                    checkIngredient.getIngredient(ingredient);
                }
                String selected = parent.getItemAtPosition(position).toString();
                holder.TextViewUnit.setText(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    // --- ViewHolder ---
    class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        EditText editTextNameIngredient;
        EditText editTextWeight;
        Spinner unit;
        TextView TextViewUnit;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.TextViewIngredient);
            editTextNameIngredient = itemView.findViewById(R.id.EditTextNameIngredient);
            editTextWeight = itemView.findViewById(R.id.EditTextWeight);
            unit = itemView.findViewById(R.id.SpinnerUnit);
            TextViewUnit = itemView.findViewById(R.id.TextViewUnit);
        }
    }
}
