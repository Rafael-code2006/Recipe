package com.example.recipe.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.R;
import com.example.recipe.model.Descriptions;
import com.example.recipe.setting.MyApp;import com.example.recipe.setting.TextKey;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;

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
        Context context = holder.itemView.getContext();
        Descriptions desc = data.get(position);

        holder.name.setText(desc.getName());

        float oldWeight = desc.getWeight();
        Log.d("AdapterShowRecipe", "Изначальный вес: " + oldWeight);

        if(oldWeight % 1 == 0) {
            holder.weight.setText(String.format("%.0f", oldWeight));
        } else{
            holder.weight.setText(String.format("%.1f", oldWeight));
        }

        holder.itemView.setOnLongClickListener(view -> {
            
            BottomSheetDialog mainDialog = new BottomSheetDialog(context);


            LinearLayout mainLayout = new LinearLayout(context);
            mainLayout.setOrientation(LinearLayout.VERTICAL);
            mainLayout.setPadding(0, 16, 0, 32);

            TextView title = new TextView(context);
            title.setText(desc.getName());
            title.setTextSize(16);
            title.setTypeface(null, Typeface.BOLD);
            title.setPadding(48, 10, 48, 8);
            mainLayout.addView(title);

            addSheetItem(mainLayout, context, "Информация", () -> {
                BottomSheetDialog infoDialog = new BottomSheetDialog(context);

                LinearLayout infoLayout = new LinearLayout(context);
                infoLayout.setOrientation(LinearLayout.VERTICAL); // было mainLayout
                infoLayout.setPadding(0, 16, 0, 32);              // было mainLayout

                TextView infoTitle = new TextView(context);
                infoTitle.setText("Информация об ингредиенте");   // было title
                infoTitle.setTextSize(16);                         // было title
                infoTitle.setTypeface(null, Typeface.BOLD);        // было title
                infoTitle.setPadding(48, 10, 48, 8);               // было title
                infoLayout.addView(infoTitle);

                addSheetItem(infoLayout, context, "Название: " + desc.getName(), null);   // было mainLayout
                addSheetItem(infoLayout, context, "Вес: " + desc.getWeight() + " " + desc.getUnit(), null); // было mainLayout

                infoDialog.setContentView(infoLayout);
                infoDialog.show();
            });

            addSheetItem(mainLayout, context, "Перерасчитать", () -> {
                BottomSheetDialog recalculateDialog = new BottomSheetDialog(context);

                LinearLayout linear = new LinearLayout(context);
                linear.setOrientation(LinearLayout.VERTICAL);
                linear.setPadding(48, 32, 48, 32);

                TextView titleView = new TextView(context);
                titleView.setText("Новое значение: " + desc.getName());
                titleView.setTextSize(14);
                titleView.setPadding(0, 0, 0, 16);
                linear.addView(titleView);

                EditText editWeight = new EditText(context);
                editWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editWeight.setHint("Введите значение");
                editWeight.setTextSize(1, 20);
                linear.addView(editWeight);



                // Кнопка подтверждения
                Button confirmBtn = new Button(context);
                confirmBtn.setText("Перерасчитать");
                confirmBtn.setOnClickListener(v -> {

                    if(editWeight.getText().toString().isEmpty()){
                        editWeight.setHintTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
                        return;
                    }
                    float value = Float.valueOf(String.valueOf(editWeight.getText()));
                    Log.d("MainActivity1000", "value: " + value);
                    Log.d("MainActivity1000", "Старое: "+ desc.getWeight() +" / Новое: " + value);
                    recalculateDialog.dismiss();
                    mainDialog.dismiss();

                    Function3<Float, Float, Float, Float> recalculation = (W_old, W_new, X_old) -> X_old * (W_new / W_old);

                    float newWeight = Float.parseFloat(editWeight.getText().toString().trim());


                    data.stream().forEach(descriptions -> {
                            float recalculatedWeight = recalculation.invoke(Float.valueOf(oldWeight), newWeight, descriptions.getWeight());
                            descriptions.setWeight(recalculatedWeight); // сохраняем новый вес
                        });


                    notifyDataSetChanged();

                    Toast.makeText(context, "Перерасчёт выполнен", Toast.LENGTH_SHORT);
                });
                linear.addView(confirmBtn);

                recalculateDialog.setContentView(linear);
                recalculateDialog.show();
            });

            mainDialog.setContentView(mainLayout);
            mainDialog.show();

            return true;
        });

        String lang = myApp.getBaseLanguage();

        // переводим любую единицу в текущий язык
        String unitText = TextKey.unitTextByValue(desc.getUnit(), lang);

        holder.unit.setText(unitText);
    }

    private void addSheetItem(LinearLayout parent, Context context, String text, Runnable action) {
        TextView item = new TextView(context);
        item.setText(text);
        item.setTextSize(15);
        item.setPadding(48, 32, 48, 32);

        // Фон при нажатии
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
        item.setBackgroundResource(value.resourceId);

        item.setOnClickListener(v -> action.run());
        parent.addView(item);
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