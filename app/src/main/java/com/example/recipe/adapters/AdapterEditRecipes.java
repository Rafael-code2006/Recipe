package com.example.recipe.adapters;

import static android.view.View.INVISIBLE;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.R;
import com.example.recipe.model.Ingredient;
import com.example.recipe.setting.MyApp;import com.example.recipe.setting.TextKey;

import java.util.ArrayList;
import java.util.List;

public class AdapterEditRecipes extends RecyclerView.Adapter<AdapterEditRecipes.IngredientViewHolder> {

    private List<Ingredient> ingredients = new ArrayList<>();



    private MyApp myApp = MyApp.getInstance();


    private List<String> unit;

    public void removeIngredient(int position) {
        this.ingredients.remove(position);
        notifyDataSetChanged();
    }

    public interface CheckIngredient {
        void getIngredient(Ingredient ingredient);
    }

    private CheckIngredient checkIngredient;

    public void setCheckRecipes(CheckIngredient checkIngredient) {
        this.checkIngredient = checkIngredient;
    }

    public void setIngredient(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public List<Ingredient> getIngredients() {
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
        holder.buttonDelete.setVisibility(INVISIBLE);
        Ingredient ingredient = ingredients.get(position);

        setTextChanged(holder.editTextNameIngredient);

        holder.isBinding = true; // начинаем биндинг

        // --- Язык ---
        holder.editTextNameIngredient.setHint(MyApp.text(TextKey.NAME));
        holder.editTextWeight.setHint(MyApp.text(TextKey.WEIGHT));

        // --- Spinner ---
        List<String> tempUnits = myApp.unit();
        if (tempUnits == null) tempUnits = new ArrayList<>();
        final List<String> units = tempUnits;

        String unitText = ingredient.getUnit();
        if (unitText == null || unitText.isEmpty()) unitText = units.isEmpty() ? "" : units.get(0);
        unitText = checkLanguage(unitText, units);
        holder.TextViewUnit.setText(unitText);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                holder.itemView.getContext(),
                android.R.layout.simple_spinner_item,
                units
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.unit.setAdapter(spinnerAdapter);

        int index = units.indexOf(unitText);
        holder.unit.setSelection(index != -1 ? index : 0, false);

        // --- Spinner Listener ---
        holder.unit.setOnItemSelectedListener(null);
        holder.unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (holder.isBinding) return; // игнорируем событие при биндинге
                String selected = units.get(pos);
                ingredient.setUnit(selected);
                holder.TextViewUnit.setText(selected);
                if (checkIngredient != null) checkIngredient.getIngredient(ingredient);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // --- Заполняем EditText ---
        holder.editTextNameIngredient.setText(ingredient.getName());
        holder.editTextWeight.setText(
                ingredient.getQuantity() == 0 ? "" : String.valueOf(ingredient.getQuantity())
        );

        // --- TextWatcher NAME ---
        if (holder.editTextNameIngredient.getTag() instanceof TextWatcher) {
            holder.editTextNameIngredient.removeTextChangedListener(
                    (TextWatcher) holder.editTextNameIngredient.getTag()
            );
        }
        TextWatcher nameWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (holder.isBinding) return; // игнорируем при биндинге
                ingredient.setName(s.toString());
                if (checkIngredient != null) checkIngredient.getIngredient(ingredient);
            }
        };
        holder.editTextNameIngredient.addTextChangedListener(nameWatcher);
        holder.editTextNameIngredient.setTag(nameWatcher);

        // --- TextWatcher WEIGHT ---
        if (holder.editTextWeight.getTag() instanceof TextWatcher) {
            holder.editTextWeight.removeTextChangedListener(
                    (TextWatcher) holder.editTextWeight.getTag()
            );
        }
        TextWatcher weightWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (holder.isBinding) return; // игнорируем при биндинге
                float weight = 0;
                try { weight = Float.parseFloat(s.toString()); } catch (NumberFormatException ignored) {}
                ingredient.setQuantity(weight);
                if (checkIngredient != null) checkIngredient.getIngredient(ingredient);
            }
        };
        holder.editTextWeight.addTextChangedListener(weightWatcher);
        holder.editTextWeight.setTag(weightWatcher);

        holder.isBinding = false; // биндинг завершён
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


    private String checkLanguage(String unit, List<String> units) {
        for(int i=0; i<3; i++){
            List<String> current;
            if(i == 0){
                current = myApp.getRusUnit();
            } else if(i == 1){
                current = myApp.getEngUnit();
            } else{
                current = myApp.getKazUnit();
            }
            for(int j=0; j < current.size(); j++){
                if(unit.equals(current.get(j))){
                    unit = units.get(j);
                }
            }
        }
        return unit;
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    // --- ViewHolder ---
    class IngredientViewHolder extends RecyclerView.ViewHolder {
        EditText editTextNameIngredient;
        EditText editTextWeight;

        Button buttonDelete;
        Spinner unit;
        TextView TextViewUnit;
        boolean isBinding = false; // <-- новый флаг

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextNameIngredient = itemView.findViewById(R.id.EditTextNameIngredient);
            editTextWeight = itemView.findViewById(R.id.EditTextWeight);
            unit = itemView.findViewById(R.id.SpinnerUnit);
            TextViewUnit = itemView.findViewById(R.id.TextViewUnit);
            buttonDelete = itemView.findViewById(R.id.ButtonDelete);
        }
    }

}
