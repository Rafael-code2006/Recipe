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

    private List<Descriptions> descriptions = new ArrayList<>();

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
        holder.TxtViewWeight.setText(String.valueOf(description.getWeight()));

    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    class RecipeShowViewHolder extends RecyclerView.ViewHolder{
        private TextView TxtViewNameDescription;
        private TextView TxtViewWeight;
        private TextView TxtViewWeightType;

        public RecipeShowViewHolder(@NonNull View itemView) {
            super(itemView);
            TxtViewNameDescription = itemView.findViewById(R.id.TxtViewNameDescription);
            TxtViewWeight = itemView.findViewById(R.id.TxtViewWeight);
            TxtViewWeightType = itemView.findViewById(R.id.TxtViewWeightType);
        }
    }
}
