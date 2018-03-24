package com.jpmc.ccb.gogo.cafeteria;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jpmc.ccb.gogo.cafeteria.Applications.CafeteriaApplication;
import com.jpmc.ccb.gogo.cafeteria.Observer.CafeteriaObserver;
import com.jpmc.ccb.gogo.cafeteria.Observer.CategoryObserver;
import com.jpmc.ccb.gogo.cafeteria.Others.PrefManager;
import com.jpmc.ccb.gogo.cafeteria.model.Category;
import com.jpmc.ccb.gogo.cafeteria.startscreens.IntroActivity;
import com.jpmc.ccb.gogo.cafeteria.startscreens.LoginActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CafeteriaObserver {

    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private Menu menu;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;


    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_YOUR_ORDERS = "your_orders";
    private static final String TAG_HISTORY = "history";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_FEEDBACK = "feedback";
    private static final String TAG_ABOUT_US = "aboutus";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private NavigationView navigationView;
    private View navheader;

    private Handler mHandler;
    private Runnable mPendingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //firebase authentication
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        };

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_drawer_labels);
        mHandler = new Handler();
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //load nav menu header data
        loadNavHeader();

        //when the app loads for the first time
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }


        CafeteriaApplication.getInstance().registerObserver(this);
        this.dataChanged(CafeteriaApplication.getInstance().getTotalPrice());

    }


    private void loadNavHeader() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navheader = navigationView.getHeaderView(0);
        TextView txtCompany = (TextView) navheader.findViewById(R.id.company);
        TextView txtmail = (TextView) navheader.findViewById(R.id.mail_id);
        txtmail.setText(auth.getCurrentUser().getEmail());
        txtCompany.setText("JPMC");
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        startActivity(new Intent(MainActivity.this, FinalOrderActivity.class));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //before signing out also restart the application
        switch (id) {
            case R.id.action_sign_out:
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                signOut();
                break;


        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        //Check to see which item was being clicked and perform appropriate action
        switch (id) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_home:
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                break;

            case R.id.orders:
                navItemIndex = 1;
                CURRENT_TAG = TAG_YOUR_ORDERS;
                break;

            case R.id.nav_history:
                navItemIndex = 2;
                CURRENT_TAG = TAG_HISTORY;
                break;

            case R.id.nav_settings:
                navItemIndex = 3;
                CURRENT_TAG = TAG_SETTINGS;
                break;

            case R.id.nav_help:
                navItemIndex = 4;
                CURRENT_TAG = TAG_SETTINGS;
                break;

            case R.id.nav_feedback:
                navItemIndex = 5;
                CURRENT_TAG = TAG_FEEDBACK;
                break;

            case R.id.nav_about_us:
                // launch new intent instead of loading fragment
                navItemIndex = 6;
                CURRENT_TAG = TAG_ABOUT_US;
                break;

            default:
                navItemIndex = 0;
        }

        //Checking if the item is in checked state or not, if not make it in checked state
        if (menuItem.isChecked()) {
            menuItem.setChecked(false);
        } else {
            menuItem.setChecked(true);
        }
        //menuItem.setChecked(true);

        loadHomeFragment();

        return true;
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

         mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                //        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {

        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // your orders
                YourOrdersFragment yourOrdersFragment = new YourOrdersFragment();
                return yourOrdersFragment;

            /*case 2:
                // settings fragment
                CustomerSettingsFragment customerSettingsFragment = new CustomerSettingsFragment();
                return customerSettingsFragment;
            */
            case 4:
                // settings fragment
                PrefManager prefManager = new PrefManager(this);
                prefManager.setFirstTimeLaunch(true);
                Intent i = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(i);
            default:
                return new HomeFragment();
        }
    }

    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void signOut() {
        auth.signOut();
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemWalletBalance = menu.findItem(R.id.item_wallet_balance);
        itemWalletBalance.setTitle(getString(R.string.Rs) + String.valueOf(CafeteriaApplication.getInstance().getWalletAmount()));
        return true;
    }

    @Override
    public void dataChanged(double totalPrice) {
        if (totalPrice > 0)
            fab.show();
        else
            fab.hide();

        if(menu!=null)
            onPrepareOptionsMenu(menu);
    }



}
