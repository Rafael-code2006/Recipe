package com.example.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeShowAdapter extends RecyclerView.Adapter<RecipeShowAdapter.RecipeShowViewHolder> {


    // ArrayList
    private List<Descriptions> descriptions = new ArrayList<>();


    // Setters
    public void setDescriptions(List<Descriptions> descriptions) {
        this.descriptions = descriptions;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public RecipeShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.show_ingredient_item,
                parent,
                false);
        return new RecipeShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeShowViewHolder holder, int position) {

        Descriptions description = descriptions.get(position);
        holder.TxtViewNameDescription.setText(description.getName());
        String unit = "g";
        float weight = description.getWeight();
        unit = (weight >= 1000) ? "kg" : unit;
        if(weight >= 1000){
            weight = weight/1000;
        }
        holder.TxtViewWeight.setText(String.valueOf(weight));
        holder.ingredientWeightType.setText(unit);
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    class RecipeShowViewHolder extends RecyclerView.ViewHolder{
        private TextView TxtViewNameDescription;
        private TextView TxtViewWeight;

        private TextView ingredientWeightType;

        public RecipeShowViewHolder(@NonNull View itemView) {
            super(itemView);
            TxtViewNameDescription = itemView.findViewById(R.id.ingredientText);
            TxtViewWeight = itemView.findViewById(R.id.ingredientWeight);
            ingredientWeightType = itemView.findViewById(R.id.ingredientWeightType);
        }
    }
}
