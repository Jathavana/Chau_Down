package com.example.yadu.chaudown;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Tom on 17/03/2015.
 */
public class SQLiteDBHelper {
    public static final String DB_NAME = "ChauDown.db";
    public static final String TABLE_NAME = "Ingredient";

    public SQLiteDatabase initDb(Activity a) {
        SQLiteDatabase db = a.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "Name VARCHAR PRIMARY KEY, " +
                "Category VARCHAR, " +
                "Amount INT, " +
                "Unit VARCHAR);");
        return db;
    }

    public boolean insertIntoIngredient(SQLiteDatabase db, Ingredient ingredient) {
        try {
            String query = String.format("INSERT INTO Ingredient VALUES(" +
                            "'%s', '%s', %s, '%s');",
                    ingredient.getName(), ingredient.getCategory(), Integer.toString(ingredient.getAmount()), ingredient.getUnit());
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            String query = String.format("UPDATE Ingredient SET Amount = Amount + %s WHERE Name = '%s'",
                    Integer.toString(ingredient.getAmount()), ingredient.getName());
            db.execSQL(query);
            return false;
        }
    }

    public Ingredient getIngredientByName(SQLiteDatabase db, String ingredientName) {
        Cursor resultSet = db.rawQuery("SELECT * FROM Ingredient WHERE Name = '" + ingredientName + "'", null);
        Ingredient ingredient = new Ingredient("", "", 0, "");

        try {
            resultSet.moveToFirst();
            while (resultSet.isAfterLast() == false) {
                ingredient = new Ingredient(resultSet.getString(0),
                        resultSet.getString(1),
                        resultSet.getInt(2),
                        resultSet.getString(3));
                resultSet.moveToNext();
            }
        } catch(Exception e) {
            resultSet.close();
        }
        return ingredient;
    }

    public void updateIngredient(SQLiteDatabase db, Ingredient ingredient) {
        String query = String.format("UPDATE Ingredient SET " +
                                    "Category = '%s', " +
                                    "Amount = %s, " +
                                    "Unit = '%s'" +
                                    "WHERE Name = '%s';",
                                    ingredient.getCategory(), Integer.toString(ingredient.getAmount()), ingredient.getUnit(), ingredient.getName());
        db.execSQL(query);
    }

    public void deleteFromIngredient(SQLiteDatabase db, String ingredientName) {
        String query = "DELETE FROM Ingredient WHERE Name = '" + ingredientName + "';";
        db.execSQL(query);
    }
}
