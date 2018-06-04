package com.pac.masternodeapp.View;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pac.masternodeapp.Controller.SQLiteHandler;
import com.pac.masternodeapp.Model.Masternode;
import com.pac.masternodeapp.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by PACcoin Team on 3/14/2018.
 */

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MasternodesListFragment.OnListFragmentInteractionListener {

    public static boolean menuCreated = false;
    public static boolean visibleSearch = false;
    public static boolean buttonAction = true;
    public static FloatingActionButton actionButton;
    public static FragmentManager fragmentManager;
    public static String homeFragment;
    private static String infoFragment;
    public static Menu main_menu;
    public static boolean checkedList = false;
    public static String passcodeSetup;
    Typeface regularFont;

    SharedPreferences preferences;
    String allFragment;
    static String aliasFragment = "";
    Toolbar searchToolbar;
    Menu search_menu;
    MenuItem item_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final String ADD_MN_INTENT = "android.intent.action.addmn";
//        final String SETTINGS_MN_INTENT = "android.intent.action.settings";

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        regularFont = ResourcesCompat.getFont(this, R.font.karla_regular);

        final Typeface regularFont = ResourcesCompat.getFont(this, R.font.karla_regular);
        fragmentManager = getFragmentManager();

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(60, 10, 60, 10);

        final EditText edittext = new EditText(this);
        edittext.setLayoutParams(lp);
        edittext.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        edittext.setHint(R.string.mn_add_alias_placeholder_no_optional);
        edittext.setMaxLines(1);
        edittext.setClipToOutline(true);
        edittext.setTypeface(regularFont);
        final int maxLength = 25;
        edittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        container.addView(edittext);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this)
                .setTitle(R.string.dialog_add_mn_title)
                .setMessage(R.string.mn_add_alias_description_single)
                .setPositiveButton(R.string.dialog_button_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Masternode> checkedList = MasternodesAddListRecyclerViewAdapter.GetCheckedList();
                        String alias = edittext.getText().toString();
                        if (alias.equals(""))
                            checkedList.get(0).setAlias(checkedList.get(0).getIp());
                        else
                            checkedList.get(0).setAlias(edittext.getText().toString());

                        SQLiteHandler sqLiteHandler = new SQLiteHandler(HomeActivity.this);
                        long addedRows = sqLiteHandler.addMasternode(checkedList.get(0));
                        if (addedRows > 0)
                            Snackbar.make(findViewById(R.id.home_action_button), getResources().getString(R.string.mn_add_success), Snackbar.LENGTH_LONG).show();
                        else
                            Snackbar.make(findViewById(R.id.home_action_button), getResources().getString(R.string.mn_add_error), Snackbar.LENGTH_LONG).show();

                        MasternodesAddListRecyclerViewAdapter.ClearCheckedList();
                        HomeActivity.fragmentManager.popBackStack(HomeActivity.homeFragment, 0);
                        edittext.setText("");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setView(container);

        final AlertDialog addDialog = dialogBuilder.create();

        addDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                addDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(getApplicationContext(),
                                R.color.buttonConfirm));
                addDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(regularFont);

                addDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(getApplicationContext(),
                                R.color.buttonCancel));
                addDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(regularFont);
            }
        });


        actionButton = findViewById(R.id.home_action_button);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonAction) {
                    item_search.collapseActionView();
                    Fragment masternodesAddListFragment = new MasternodesAddListFragment();
                    allFragment = masternodesAddListFragment.getClass().getName();
                    changeFragment(masternodesAddListFragment, null);
                } else {
                    List<Masternode> checkedlist = MasternodesAddListRecyclerViewAdapter.GetCheckedList();
                    for (int i = 0; i < checkedlist.size(); i++)
                        Log.d("CheckedListItem: " + i, checkedlist.get(i).getIp());
                    if (checkedlist.isEmpty()) {
                        Snackbar.make(view, getString(R.string.mn_add_empty_error), Snackbar.LENGTH_LONG).show();
                    } else if (checkedlist.size() == 1) {
                        item_search.collapseActionView();
                        addDialog.show();
                    } else {
                        item_search.collapseActionView();
                        main_menu.findItem(R.id.action_search).setVisible(false);
                        checkedList = true;
                        Fragment masternodesAddAliasListFragment = new MasternodesAddAliasListFragment();
                        aliasFragment = masternodesAddAliasListFragment.getClass().getName();
                        changeFragment(masternodesAddAliasListFragment, null);
                    }
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.pacYellow));

        NavigationView navigationView = findViewById(R.id.nav_view);

        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        navigationView.setNavigationItemSelectedListener(this);

        Fragment listFragment = new MasternodesListFragment();
        homeFragment = listFragment.getClass().getName();
        changeFragment(listFragment, null);

        setSearchToolbar();

//        try {
//            if (ADD_MN_INTENT.equals(getIntent().getAction())) {
//                Fragment masternodesAddListFragment = new MasternodesAddListFragment();
//                allFragment = masternodesAddListFragment.getClass().getName();
//                changeFragment(masternodesAddListFragment, null);
//            }
//            if (SETTINGS_MN_INTENT.equals(getIntent().getAction())) {
//                Fragment settingsFragmentFragment = new SettingsFragment();
//                allFragment = settingsFragmentFragment.getClass().getName();
//                changeFragment(settingsFragmentFragment, null);
//            }
//        }
//        catch (Exception e){
//            Log.d("INTENT", e.toString());
//        }
    }

    @Override
    public void onBackPressed() {
        main_menu.findItem(R.id.action_search).setVisible(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchToolbar.getVisibility() < 4) {
            item_search.collapseActionView();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            if (!visibleSearch)
                main_menu.findItem(R.id.action_search).setVisible(false);
            fragmentManager.popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        main_menu = menu;
        menuCreated = true;

        preferences = getSharedPreferences("active_passcode", 0);
        Boolean pinIsActive = preferences.getBoolean("active_passcode", false);
        MenuItem app_lock = menu.findItem(R.id.action_lock_app);
        if (pinIsActive) {
            app_lock.setVisible(true);
        } else {
            app_lock.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.search_toolbar, 1, true, true);
                else
                    searchToolbar.setVisibility(View.VISIBLE);
                item_search.expandActionView();
                return true;
            case R.id.action_settings:
                Fragment settingsFragment = new SettingsFragment();
                changeFragment(settingsFragment, null);
                return true;
            case R.id.action_lock_app:
                switchActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            main_menu.findItem(R.id.action_search).setVisible(true);
            Fragment masternodesListFragment = new MasternodesListFragment();
            changeFragment(masternodesListFragment, null);
        } else if (id == R.id.nav_add_list) {
            buttonAction = false;
            actionButton.setImageResource(R.mipmap.ic_check_black);
            main_menu.findItem(R.id.action_search).setVisible(true);
            Fragment masternodesAddListFragment = new MasternodesAddListFragment();
            allFragment = masternodesAddListFragment.getClass().getName();
            changeFragment(masternodesAddListFragment, null);
        } else if (id == R.id.nav_settings) {
            Fragment settingsFragment = new SettingsFragment();
            changeFragment(settingsFragment, null);

        } else if (id == R.id.nav_about) {
            Fragment masternodeAboutFragment = new MasternodesAboutFragment();
            changeFragment(masternodeAboutFragment, null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public FragmentManager getFragmentManager() {
        return super.getFragmentManager();
    }

    @Override
    public void onListFragmentInteraction(Masternode item) {
        item_search.collapseActionView();
        main_menu.findItem(R.id.action_search).setVisible(false);
        Fragment masternodeInfoFragment = new MasternodeInformationFragment();
        infoFragment = masternodeInfoFragment.getClass().getName();
        changeFragment(masternodeInfoFragment, item);
    }

    public static void changeFragment(Fragment fragment, Masternode mn) {
        fragmentManager.popBackStackImmediate(aliasFragment, 1);
        String fragmentTag = fragment.getClass().getName();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(fragmentTag, 0);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        Fragment existingFragment = fragmentManager.findFragmentByTag(fragmentTag);

        if (!fragmentPopped && existingFragment == null) {
            if (fragmentTag.equals(infoFragment)) {
                MasternodeInformationFragment masternodeInfoFragment = (MasternodeInformationFragment) fragment;
                transaction.replace(R.id.fragment_home, masternodeInfoFragment.newInstance(mn), fragmentTag);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(fragmentTag).commit();
            } else {
                transaction.replace(R.id.fragment_home, fragment, fragmentTag);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(fragmentTag).commit();
            }
        }
    }

    public void setSearchToolbar() {
        searchToolbar = findViewById(R.id.search_toolbar);
        if (searchToolbar != null) {
            searchToolbar.inflateMenu(R.menu.menu_search);
            search_menu = searchToolbar.getMenu();

            searchToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.search_toolbar, 1, true, false);
                    else
                        searchToolbar.setVisibility(View.GONE);
                }
            });

            item_search = search_menu.findItem(R.id.search_filter);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.search_toolbar, 1, true, false);
                    } else
                        searchToolbar.setVisibility(View.GONE);
                    if (buttonAction) {
                        MasternodesListFragment fragment = (MasternodesListFragment) fragmentManager.findFragmentByTag(homeFragment);
                        fragment.Reset();
                    } else {
                        MasternodesAddListFragment fragment = (MasternodesAddListFragment) fragmentManager.findFragmentByTag(allFragment);
                        fragment.Reset();
                    }
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });
            initSearchView();
        } else
            Log.d("toolbar", "setSearchtollbar: NULL");
    }

    public void initSearchView() {

        final SearchView searchView = (SearchView) search_menu.findItem(R.id.search_filter).getActionView();
        searchView.setSubmitButtonEnabled(true);

        final ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.mipmap.ic_clear_cross_black);

        EditText txtSearch = searchView.findViewById(R.id.search_src_text);
        txtSearch.setHint("Search..");
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.colorPrimary));

        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                //Do searching
                Log.i("query", "" + query);
                if (buttonAction) {
                    MasternodesListFragment fragment = (MasternodesListFragment) fragmentManager.findFragmentByTag(homeFragment);
                    fragment.Search(query);
                    if (!fragment.isRecyclerScrollable())
                        actionButton.show();
                } else {
                    MasternodesAddListFragment fragment = (MasternodesAddListFragment) fragmentManager.findFragmentByTag(allFragment);
                    fragment.Search(query);
                    if (!fragment.isRecyclerScrollable())
                        actionButton.show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = findViewById(viewID);

        int width = myView.getWidth();

        if (posFromRight > 0)
            width -= (posFromRight * getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        if (containsOverflow)
            width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        anim.setDuration((long) 220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if (isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();
    }

    private void applyFontToMenuItem(MenuItem mi) {
        final Typeface regularFont = ResourcesCompat.getFont(this, R.font.karla_regular);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", regularFont), 0, mNewTitle.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public void appLockVisible(Boolean isVisible) {
        HomeActivity.main_menu.findItem(R.id.action_lock_app).setVisible(isVisible);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Boolean isActive = preferences.getBoolean("active_passcode", false);
        if (isActive) {
            menu.findItem(R.id.action_lock_app).setVisible(true);
            menu.getItem(2).setEnabled(true);
        } else {
            menu.findItem(R.id.action_lock_app).setVisible(false);
            menu.getItem(2).setEnabled(false);
        }
        return true;
    }

    private void switchActivity() {
        Intent intent = new Intent(getApplicationContext(), PasscodeActivity.class);
        startActivity(intent);
    }


}
