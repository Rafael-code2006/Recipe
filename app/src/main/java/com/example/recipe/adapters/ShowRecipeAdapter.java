package com.example.recipe.adapters;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.R;
import com.example.recipe.model.Descriptions;
import com.example.recipe.setting.MyApp;import com.example.recipe.setting.TextKey;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Descriptions desc = data.get(position);

        holder.name.setText(desc.getName());

        String oldWeight = String.valueOf(desc.getWeight());
        Log.d("AdapterShowRecipe", "Изначальный вес: " + oldWeight);
        holder.weight.setText(oldWeight);

        holder.weight.setOnClickListener((view) -> {

            Function3<Float, Float, Float, Float> recalculation = (W_old, W_new, X_old) -> {
                float X_new = X_old * (W_new / W_old);
                return X_new;
            };

            EditText tempWeight = new EditText(holder.itemView.getContext());
            tempWeight.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            tempWeight.setHint("Введите новый вес");

            new AlertDialog.Builder(view.getContext())
                    .setTitle("Перерасчет рецепта")
                    .setMessage("Введите новый вес ингредиента")
                    .setView(tempWeight)
                    .setPositiveButton("OК", (dialogInterface, i) -> {

                    })
                    .setNegativeButton("Отмена", (dialogInterface, i) -> {

                    });
        });

        String lang = myApp.getBaseLanguage();

        // переводим любую единицу в текущий язык
        String unitText = TextKey.unitTextByValue(desc.getUnit(), lang);

        holder.unit.setText(unitText);
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