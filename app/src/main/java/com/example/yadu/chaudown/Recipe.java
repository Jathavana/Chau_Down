package com.example.yadu.chaudown;

import android.app.SearchManager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Recipe extends ActionBarActivity{

    // PUT YOUR API KEY HERE!
    private static final String API_KEY = "wT2XOfoaP8f0Q1akvhXjKg0wpqqkgSX_";

    public TextView recipeTitle;
    public TextView description;
    public TextView ingredientView;
    public TextView ingredientUnitsView;
    public TextView recipeSteps;
    public int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String textPos = Integer.toString(extras.getInt("position"));
        position = extras.getInt("position");
        Toast.makeText(this, "" + textPos, Toast.LENGTH_SHORT).show();



        setContentView(R.layout.activity_recipe);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Recipe");
        actionBar.setDisplayUseLogoEnabled(false);

        recipeTitle = (TextView) findViewById(R.id.RecipeTitle);
        ingredientView = (TextView) findViewById(R.id.item);
        description = (TextView) findViewById(R.id.description);
        ingredientUnitsView = (TextView) findViewById(R.id.itemQuantity);
        recipeSteps = (TextView) findViewById(R.id.recipeSteps);
        new GetRecipe().execute();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chau__down, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        // The below line returned null even though it was used in Google sample code
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        /*searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(getApplicationContext(), SearchResultsActivity.class)));*/
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                if (searchItem != null) {
                    searchItem.collapseActionView();
                }
                Toast.makeText(getApplicationContext(), query,
                        Toast.LENGTH_LONG).show();
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                // search action
                return true;
            case R.id.action_settings:
                // check for updates action
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class GetRecipe extends AsyncTask<Void, Void, Void> implements MongoAdapter {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Mongo.get(this, "Recipes", null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        // Method should return the name of the database you want to access
        public String dbName()
        {
            return "chau_down";
        }

        // Method should return the API Key as shown at the bottom of the MongoLab user page
        public String apiKey()
        {
            return API_KEY;
        }

        @Override
        public void processResult(String result) {
            try{
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(position);

                recipeTitle.setText(jsonObject.getString("Recipe"));
                description.setText(jsonObject.getString("Description"));
                ingredientView.setText(jsonObject.getString("Ingredients"));
                ingredientUnitsView.setText(jsonObject.getString("IngredientsUnits"));
                recipeSteps.setText(jsonObject.getString("Instructions"));

            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}