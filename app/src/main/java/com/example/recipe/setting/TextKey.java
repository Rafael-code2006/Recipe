package com.example.recipe.setting;

public enum TextKey {

        MAIN("Рецепты", "Recipes", "Рецепттер"),
        ADD("Добавить рецепт", "Add Recipe", "Рецепт қосу"),
        SHOW("Рецепт", "Recipe", "Рецепт"),
        EDIT("Изменить", "Edit", "Өзгерту"),

        SETTINGS("Настройки", "Settings", "Параметрлер"),
        EXPORT("Экспорт", "Export", "Экспорт"),
        IMPORT("Импорт", "Import", "Импорт"),
        LANGUAGE("Язык", "Language", "Тіл"),
        SAVE("Сохранить", "Save", "Сақтау"),

        RECIPE_NAME("Название рецепта", "Recipe name", "Рецепт атауы"),
        INGREDIENTS("Ингредиенты", "Ingredients", "Ингредиенттер"),
        INSTRUCTION("Инструкция", "Instruction", "Нұсқаулық"),

        NAME("Название", "Name", "Атауы"),
        WEIGHT("Вес", "Weight", "Салмақ"),


        KG("кг", "kg", "кг"),
        GR("гр", "gr", "гр"),
        L("л", "l", "л"),
        ML("мл", "ml", "мл"),
        TBSP("мсл", "tbsp", "өақ"),
        TSP("мчл", "tsp", "өшқ");

    public static String unitTextByValue(String value, String lang) {
        for (TextKey key : new TextKey[]{KG, GR, L, ML, TBSP, TSP}) {
            if (key.ru.equals(value) || key.eng.equals(value) || key.kaz.equals(value)) {
                return key.getText(lang);
            }
        }
        // если не нашли — возвращаем исходное значение
        return value;
    }

        private final String ru;
        private final String eng;
        private final String kaz;

        TextKey(String ru, String eng, String kaz) {
            this.ru = ru;
            this.eng = eng;
            this.kaz = kaz;
        }

        public String getText(String lang) {

            switch (lang) {
                case "Eng":
                    return eng;
                case "Каз":
                    return kaz;
                default:
                    return ru;
            }
        }
}
