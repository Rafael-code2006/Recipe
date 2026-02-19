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


    private List<String> unit;

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

        // ---------- Язык ----------
        switch (myApp.getBaseLanguage()) {
            case "Рус":
                holder.title.setText("Ингредиент");
                holder.editTextNameIngredient.setHint("имя");
                holder.editTextWeight.setHint("вес");
                break;

            case "Eng":
                holder.title.setText("Ingredient");
                holder.editTextNameIngredient.setHint("name");
                holder.editTextWeight.setHint("weight");
                break;

            case "Каз":
                holder.title.setText("Құрамы");
                holder.editTextNameIngredient.setHint("атауы");
                holder.editTextWeight.setHint("салмағы");
                break;
        }

        // ---------- Spinner ----------
        List<String> units = myApp.unit();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                holder.itemView.getContext(),
                android.R.layout.simple_spinner_item,
                units
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.unit.setAdapter(adapter);

        // ---------- Используем checkLanguage ----------
        String unitText = checkLanguage(ingredient.getUnit(), units);

        // Ищем индекс в текущем списке
        int index = -1;
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).equals(unitText)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            holder.unit.setSelection(index, false); // false чтобы не вызвать listener
            holder.TextViewUnit.setText(unitText);
        } else {
            holder.TextViewUnit.setText("");
        }

        // ---------- Заполняем данные ----------
        holder.editTextNameIngredient.setText(ingredient.getName());
        holder.editTextWeight.setText(
                ingredient.getWeight() == 0 ? "" : String.valueOf(ingredient.getWeight())
        );

        // ---------- Listener Spinner ----------
        holder.unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selected = units.get(pos);
                ingredient.setUnit(selected);
                holder.TextViewUnit.setText(selected);

                if (checkIngredient != null) {
                    checkIngredient.getIngredient(ingredient);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // ---------- TextWatcher NAME ----------
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

        // ---------- TextWatcher WEIGHT ----------
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
                } catch (NumberFormatException ignored) { }

                ingredient.setWeight(weight);

                if (checkIngredient != null) {
                    checkIngredient.getIngredient(ingredient);
                }
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
