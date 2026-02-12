package com.example.recipe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public void setIngredient(List<Descriptions> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new AdapterEditRecipes.IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.editTextNameIngredient.setText(ingredients.get(position).getName());
        holder.editTextWeight.setText(String.valueOf(ingredients.get(position).getWeight()));
        String unit = holder.unit.getSelectedItem().toString();
        holder.TextViewUnit.setText(unit);
    }


    @Override
    public int getItemCount() {
        return new ArrayList<>(ingredients).size();
    }


    class IngredientViewHolder extends RecyclerView.ViewHolder{

        private EditText editTextNameIngredient;
        private EditText editTextWeight;
        private TextView TextViewUnit;
        private Spinner unit;
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextNameIngredient = itemView.findViewById(R.id.EditTextNameIngredient);
            editTextWeight = itemView.findViewById(R.id.EditTextWeight);
            TextViewUnit = itemView.findViewById(R.id.TextViewUnit);
            unit = itemView.findViewById(R.id.SpinnerUnit);
        }
    }
}
