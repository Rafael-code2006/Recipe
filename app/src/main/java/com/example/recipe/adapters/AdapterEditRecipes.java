package com.example.recipe.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.R;
import com.example.recipe.model.Descriptions;

import java.util.ArrayList;
import java.util.List;

public class AdapterEditRecipes extends RecyclerView.Adapter<AdapterEditRecipes.IngredientViewHolder> {

    private List<Descriptions> ingredients = new ArrayList<>();

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
        EditText editTextNameIngredient;
        EditText editTextWeight;
        Spinner unit;
        TextView TextViewUnit;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextNameIngredient = itemView.findViewById(R.id.EditTextNameIngredient);
            editTextWeight = itemView.findViewById(R.id.EditTextWeight);
            unit = itemView.findViewById(R.id.SpinnerUnit);
            TextViewUnit = itemView.findViewById(R.id.TextViewUnit);
        }
    }
}
