package com.example.yadu.chaudown;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
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

/**
 * Created by Tom on 11/02/2015.
 */
public class EditIngredientDialogFragment extends DialogFragment {
    View dialogView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.edit_ingredient_dialog, null);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        SQLiteDBHelper dbHelper = new SQLiteDBHelper();
        SQLiteDatabase db = dbHelper.initDb(getActivity());
        Cursor resultSet = db.rawQuery("SELECT * FROM Ingredient WHERE Name = '" + getArguments().getString("itemName") + "';", null);
        try {
            resultSet.moveToFirst();
            TextView banner = (TextView) dialogView.findViewById(R.id.banner);
            EditText editAmount = (EditText) dialogView.findViewById(R.id.ingredientAmount);
            Spinner spinnerCategory = (Spinner) dialogView.findViewById(R.id.ingredientCategory);
            Spinner spinnerUnit = (Spinner) dialogView.findViewById(R.id.ingredientUnit);

            banner.setText(banner.getText() + " " + resultSet.getString(0));
            editAmount.setText(resultSet.getString(2));
            spinnerCategory.setSelection(getSpinnerIndex(spinnerCategory, resultSet.getString(1)));
            spinnerUnit.setSelection(getSpinnerIndex(spinnerUnit, resultSet.getString(3)));
        } finally {
            resultSet.close();
        }

        builder.setView(dialogView)
                .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
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
            String ingredientName = getArguments().getString("itemName");
            String ingredientAmountStr = "";
            int ingredientAmount = 0;
            String ingredientCategory = "";
            String unitType = "";

            ExpandableListView expListView = (ExpandableListView) getActivity().findViewById(R.id.listViewPantry);
            ExpandableListAdapter expListAdapter = (ExpandableListAdapter) expListView.getExpandableListAdapter();
            EditText editAmount = (EditText) dialogView.findViewById(R.id.ingredientAmount);
            Spinner spinnerCategory = (Spinner) dialogView.findViewById(R.id.ingredientCategory);
            Spinner spinnerUnit = (Spinner) dialogView.findViewById(R.id.ingredientUnit);

            ingredientAmountStr = (editAmount.getText().length() == 0) ? "0" : editAmount.getText().toString();
            ingredientAmount = Integer.parseInt(ingredientAmountStr);
            ingredientCategory = spinnerCategory.getSelectedItem().toString();
            unitType = spinnerUnit.getSelectedItem().toString();

            if (!ingredientName.isEmpty() && ingredientAmount > 0) {
                Ingredient ingredient = new Ingredient(ingredientName, ingredientCategory, ingredientAmount, unitType);
                SQLiteDBHelper dbHelper = new SQLiteDBHelper();
                SQLiteDatabase db = dbHelper.initDb(getActivity());

                Ingredient oldIngredient = dbHelper.getIngredientByName(db, ingredientName);
                dbHelper.updateIngredient(db, ingredient);

                expListAdapter.addChild(ingredient.getCategory(), ingredient);
                expListAdapter.removeChild(oldIngredient.getCategory(), getArguments().getInt("childPosition"));
                expListAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity().getApplicationContext(), "Edited.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Please enter required fields.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                index = i;
                break;
            }
        }
        return index;
    }
}

