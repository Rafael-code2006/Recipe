package com.example.recipe.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.R;
import com.example.recipe.model.Descriptions;
import com.example.recipe.setting.MyApp;

import java.util.ArrayList;
import java.util.List;

public class ShowRecipeAdapter extends RecyclerView.Adapter<ShowRecipeAdapter.ViewHolder> {

    private List<Descriptions> data = new ArrayList<>();
    private MyApp myApp;

    public ShowRecipeAdapter(MyApp myApp) {
        this.myApp = myApp;
    }

    public void setData(List<Descriptions> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShowRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowRecipeAdapter.ViewHolder holder, int position) {
        Descriptions desc = data.get(position);
        holder.name.setText(desc.getName());
        holder.weight.setText(String.valueOf(desc.getWeight()));

        List<String> units = myApp.unit();
        String unit = desc.getUnit();
        for (int i = 0; i < 3; i++) {
            List<String> current;
            if (i == 0) current = myApp.getRusUnit();
            else if (i == 1) current = myApp.getEngUnit();
            else current = myApp.getKazUnit();
            for (int j = 0; j < current.size(); j++) {
                if (unit.equals(current.get(j))) unit = units.get(j);
            }
        }
        holder.unit.setText(unit);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, weight, unit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ingredientText);
            weight = itemView.findViewById(R.id.ingredientWeight);
            unit = itemView.findViewById(R.id.ingredientWeightType);
        }
    }
}