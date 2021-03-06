package com.cen.complit.nitoumbrella;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public Boolean isAdmin = false;
    Bundle myArgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        isAdmin = getIntent().getBooleanExtra("rank", false);
        myArgs = new Bundle();
        myArgs.putBoolean("modStatus", isAdmin);

        if(isAdmin)
            Toast.makeText(getApplicationContext(), "Welcome, Moderator", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Welcome, User", Toast.LENGTH_LONG).show();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment objFragment = null;
        //Switches fragments depending on what is selected in the nav drawer.
        switch(position){
            case 0:
                objFragment = new menu1_Fragment();
                break;
            case 1:
                objFragment = new games_Fragment();
                break;
            case 2:
                objFragment = new rules_Fragment();
                break;
            case 3:
                objFragment = new aboutUs_Fragment();
                break;
            case 4:
                objFragment = new viewProfile_Fragment();
                break;
            case 5:
                objFragment = new clarifications_Fragment();
                break;
            case 6:
                objFragment = new updates_Fragment();
                break;
            case 7:
                objFragment = new liveMap_Fragment();
                break;
            case 8:
                objFragment = new reportKill_Fragment();
                break;
            case 9:
                objFragment = new viewMission_Fragment();
                break;
            case 10:
                objFragment = new enableGPS_Fragment();
                break;
            case 11:
                objFragment = new modHub_Fragment();
                objFragment.setArguments(myArgs);
                break;
            case 12:
                objFragment = new logout_Fragment();
                break;
//            case 11:
//                objFragment = new manageGames_Fragment();
//                break;
//            case 12:
//                objFragment = new manageMissions_Fragment();
//                break;
//            case 13:
//                objFragment = new geofence_Fragment();
//                break;
//            case 14:
//                objFragment = new users_Fragment();
//                break;
//            case 15:
//                objFragment = new modChat_Fragment();
//                break;
//            case 16:
//                objFragment = new logout_Fragment();
//                break;


        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objFragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "Home";
                break;
            case 2:
                mTitle = "Game";
                break;
            case 3:
                mTitle = "Rules";
                break;
            case 4:
                mTitle = "About Us";
                break;
            case 5:
                mTitle = "View Profile";
                break;
            case 6:
                mTitle = "Clarifications";
                break;
            case 7:
                mTitle = "Updates";
                break;
            case 8:
                mTitle = "Live Map";
                break;
            case 9:
                mTitle = "Report Kill";
                break;
            case 10:
                mTitle = "View Mission Info";
                break;
            case 11:
                mTitle = "Enable/Disable GPS";
                break;
            case 12:
                mTitle = "Mod Hub";
                break;
            case 13:
                mTitle = getString(R.string.title_section3);
                break;
//            case 12:
//                mTitle = "Manage Games";
//                break;
//            case 13:
//                mTitle = "Manage Missions";
//                break;
//            case 14:
//                mTitle = "GeoFences";
//                break;
//            case 15:
//                mTitle = "Users";
//                break;
//            case 16:
//                mTitle = "Moderator Chat";
//                break;
//            case 17:
//                mTitle = getString(R.string.title_section3);
//                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }



}
