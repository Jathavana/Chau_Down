package com.example.yadu.chaudown;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Tom on 11/02/2015.
 */
public class AddIngredientDialogFragment extends DialogFragment {
    View dialogView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_ingredient_dialog, null);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialog));
        return alertDialog;
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            String ingredientName = "";
            String ingredientAmountStr = "";
            int ingredientAmount = 0;
            String ingredientCategory = "";
            String unitType = "";

            ExpandableListView expListView = (ExpandableListView) getActivity().findViewById(R.id.listViewPantry);
            ExpandableListAdapter expListAdapter = (ExpandableListAdapter) expListView.getExpandableListAdapter();
            EditText editName = (EditText) dialogView.findViewById(R.id.ingredientName);
            EditText editAmount = (EditText) dialogView.findViewById(R.id.ingredientAmount);
            Spinner spinnerCategory = (Spinner) dialogView.findViewById(R.id.ingredientCategory);
            Spinner spinnerUnit = (Spinner) dialogView.findViewById(R.id.ingredientUnit);

            ingredientName = (editName.getText().length() == 0) ? "" : editName.getText().toString().toLowerCase();
            ingredientAmountStr = (editAmount.getText().length() == 0) ? "0" : editAmount.getText().toString();
            ingredientAmount = Integer.parseInt(ingredientAmountStr);
            ingredientCategory = spinnerCategory.getSelectedItem().toString();
            unitType = spinnerUnit.getSelectedItem().toString();


            if (!ingredientName.isEmpty() && ingredientAmount > 0) {
                Ingredient ingredient = new Ingredient(ingredientName, ingredientCategory, ingredientAmount, unitType);
                SQLiteDatabase db = getActivity().openOrCreateDatabase("ChauDown.db", Context.MODE_PRIVATE, null);
                insertIngredientToDb(db, ingredient);
                Toast.makeText(getActivity().getApplicationContext(), "Added.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Please enter required fields.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertIngredientToDb(SQLiteDatabase db, Ingredient ingredient) {
        try {
            String query = String.format("INSERT INTO Ingredient VALUES(" +
                            "'%s', '%s', %s, '%s');",
                    ingredient.getName(), ingredient.getCategory(), Integer.toString(ingredient.getAmount()), ingredient.getUnit());
            db.execSQL(query);
        } catch (Exception e) {
            String query = String.format("UPDATE Ingredient SET Amount = Amount + %s WHERE Name = '%s'",
                    Integer.toString(ingredient.getAmount()), ingredient.getName());
            db.execSQL(query);
        }
    }
}

