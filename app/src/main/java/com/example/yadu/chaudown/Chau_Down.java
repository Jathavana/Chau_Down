package com.example.yadu.chaudown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import android.app.DialogFragment;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.ExpandableListView;

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.SearchManager;

import android.content.Context;
import android.view.MenuInflater;


public class Chau_Down extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chau__down);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);*/
        switch (item.getItemId()) {
            case R.id.action_search:
                // search action
                return true;
            case R.id.action_settings:
                // check for updates action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return PlaceholderFragment.newInstance(position + 1);
                case 1:
                    return PlaceholderFragment2.newInstance(position + 1);
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

            GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
            gridview.setAdapter(new ImageAdapter(getActivity()));

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent i = new Intent(getActivity().getApplicationContext(), Recipe.class);
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });

            TextView txtAddRecipe = (TextView) rootView.findViewById(R.id.txtAddRecipe);
            txtAddRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent i = new Intent(getActivity().getApplicationContext(), AddRecipe.class);
                    startActivity(i);
                }
            });

            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment2 extends Fragment {
        ExpandableListAdapter listAdapter;
        ExpandableListView expListView;
        List<String> listDataHeader;
        HashMap<String, List<Ingredient>> listDataChild;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment2 newInstance(int sectionNumber) {
            PlaceholderFragment2 fragment = new PlaceholderFragment2();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment2() {
        }

        /*
         * Preparing the list data
         */
        public void prepareListData(Context context) {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<Ingredient>>();

            // Adding child data
            listDataHeader.add(getString(R.string.dairy));
            listDataHeader.add(getString(R.string.grains));
            listDataHeader.add(getString(R.string.meats));
            listDataHeader.add(getString(R.string.produce));
            listDataHeader.add(getString(R.string.spices));

            // Adding child data
            List<Ingredient> dairy = new ArrayList<Ingredient>();
            List<Ingredient> grains = new ArrayList<Ingredient>();
            List<Ingredient> meats = new ArrayList<Ingredient>();
            List<Ingredient> produce = new ArrayList<Ingredient>();
            List<Ingredient> spices = new ArrayList<Ingredient>();

            SQLiteDBHelper dbHelper = new SQLiteDBHelper();
            SQLiteDatabase db = dbHelper.initDb(getActivity());

            Cursor resultSet = db.rawQuery("SELECT * FROM Ingredient", null);

            try {
                resultSet.moveToFirst();
                while (resultSet.isAfterLast() == false) {
                    Ingredient newIngredient = new Ingredient(resultSet.getString(0),
                            resultSet.getString(1),
                            resultSet.getInt(2),
                            resultSet.getString(3));
                    switch (newIngredient.getCategory()) {
                        case "Dairy":
                            dairy.add(newIngredient);
                            break;
                        case "Grains":
                            grains.add(newIngredient);
                            break;
                        case "Meats":
                            meats.add(newIngredient);
                            break;
                        case "Produce":
                            produce.add(newIngredient);
                            break;
                        case "Spices":
                            spices.add(newIngredient);
                            break;
                        default:
                            ;
                    }
                    resultSet.moveToNext();
                }
            } finally {
                resultSet.close();
            }

            // Header, Child data
            listDataChild.put(listDataHeader.get(0), dairy);
            listDataChild.put(listDataHeader.get(1), grains);
            listDataChild.put(listDataHeader.get(2), meats);
            listDataChild.put(listDataHeader.get(3), produce);
            listDataChild.put(listDataHeader.get(4), spices);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pantry, container, false);

            // get the listview
            expListView = (ExpandableListView) rootView.findViewById(R.id.listViewPantry);

            // preparing list data
            prepareListData(getActivity());

            listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

            // setting list adapter
            expListView.setAdapter(listAdapter);

            return rootView;
        }
    }

    public void writeToListView(View view) {
        DialogFragment dialog = new AddIngredientDialogFragment();
        dialog.show(getFragmentManager(), "IngredientDialogFragment");
    }
}
