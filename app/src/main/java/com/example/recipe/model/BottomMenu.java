package com.example.recipe.model;

import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.recipe.R;

public class BottomMenu {

    private final View indicator;

    private final View homeButton;
    private final View categoryButton;

    private ImageButton addRecipe;

    private final ImageView homeIcon;
    private final ImageView categoryIcon;

    private final TextView homeText;
    private final TextView categoryText;



    public BottomMenu(@NonNull View rootView) {

        indicator = rootView.findViewById(R.id.tabIndicator);

        homeButton = rootView.findViewById(R.id.homeButton);
        categoryButton = rootView.findViewById(R.id.categoryButton);
        addRecipe = rootView.findViewById(R.id.AddRecipeButton);
        homeIcon = homeButton.findViewById(R.id.homeIcon);
        categoryIcon = categoryButton.findViewById(R.id.categoryIcon);

        homeText = homeButton.findViewById(R.id.homeText);
        categoryText = categoryButton.findViewById(R.id.categoryText);

        homeButton.setOnClickListener(v -> selectHome());

        categoryButton.setOnClickListener(v -> selectCategory());



        selectHome();
    }

    private void selectHome() {

        indicator.animate()
                .translationX(0)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(300)
                .start();

        animateSelected(homeButton);
        animateUnselected(categoryButton);

        homeText.setAlpha(1f);
        categoryText.setAlpha(0.6f);

        homeIcon.setAlpha(1f);
        categoryIcon.setAlpha(0.6f);
    }

    private void selectCategory() {

        float target = homeButton.getWidth();

        indicator.animate()
                .translationX(target)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(300)
                .start();

        animateSelected(categoryButton);
        animateUnselected(homeButton);

        categoryText.setAlpha(1f);
        homeText.setAlpha(0.6f);

        categoryIcon.setAlpha(1f);
        homeIcon.setAlpha(0.6f);
    }

    private void animateSelected(View view) {

        view.animate()
                .scaleX(1.08f)
                .scaleY(1.08f)
                .setDuration(200)
                .start();
    }

    private void animateUnselected(View view) {

        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(200)
                .start();
    }


    public View getHomeButton() {
        return homeButton;
    }

    public View getCategoryButton() {
        return categoryButton;
    }

    public ImageButton getAddRecipe(@NonNull View rootView) {
        if(addRecipe == null){
            addRecipe = rootView.findViewById(R.id.AddRecipeButton);
        }
        return addRecipe;
    }
}