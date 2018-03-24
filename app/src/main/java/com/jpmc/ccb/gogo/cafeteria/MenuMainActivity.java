package com.jpmc.ccb.gogo.cafeteria;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jpmc.ccb.gogo.cafeteria.Adapter.MenuItemAdapter;
import com.jpmc.ccb.gogo.cafeteria.Applications.CafeteriaApplication;
import com.jpmc.ccb.gogo.cafeteria.Factory.CategoryFactory;
import com.jpmc.ccb.gogo.cafeteria.Observer.CafeteriaObserver;
import com.jpmc.ccb.gogo.cafeteria.model.Category;
import com.jpmc.ccb.gogo.cafeteria.model.FoodOrder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gaurav Shinde on 07-02-2018.
 */

public class MenuMainActivity extends AppCompatActivity implements CafeteriaObserver{

    private static final String EXTRA_CHOICE_CATEGORY = "com.jpmc.ccb.gogo.cafeteria.choice_category";
    public RecyclerView mRecyclerView;
    private  int category;
    private List<FoodOrder> mOrderlist;
    private RecyclerView.Adapter mAdapter;
    private CategoryFactory categoryFactory;
    private Menu menu;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_menu_fab)
    FloatingActionButton main_menu_fab;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.category_product_title)
    TextView backdroptitle;
    @BindView(R.id.category_product_subtitle)
    TextView backdropsubtitle;


    public static Intent newIntent(Context context, int category_id) {
        Intent i = new Intent(context, MenuMainActivity.class);
        i.putExtra(EXTRA_CHOICE_CATEGORY, category_id);
        return i;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CafeteriaApplication.getInstance().removeObserver(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        category = getIntent().getIntExtra(EXTRA_CHOICE_CATEGORY, 1);

        setContentView(R.layout.activity_mainmenu);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        createFoodMenu();

        initCollapsingToolbar();
        final CafeteriaApplication ca = CafeteriaApplication.getInstance();
        mAdapter = new MenuItemAdapter(this, mOrderlist, ca, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);


        changecoverimage();

        CafeteriaApplication.getInstance().registerObserver(this);
        this.dataChanged(CafeteriaApplication.getInstance().getTotalPrice());

    }


    private void createFoodMenu() {
        CategoryFactory.noItems = false;

        CafeteriaApplication ca = CafeteriaApplication.getInstance();
        if(ca.getFoodOrderList().size() == 0 ){
            CategoryFactory.noItems = true;
        }
        categoryFactory = CategoryFactory.getInstance();

        mOrderlist = categoryFactory.getCategory(category).getFoodOrderList();

    }

    @OnClick(R.id.main_menu_fab)
    public void onfabClick(View view){
        startActivity(new Intent(MenuMainActivity.this, FinalOrderActivity.class));
    }

    /************************APPEARENCE SETTINGS***********************************/

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initCollapsingToolbar() {

        collapsingToolbar.setTitle(" ");
        appBarLayout.setExpanded(true);
        changetoolbarcolor(category,collapsingToolbar);
        backdroptitle.setText("CAFETERIA");


        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, final int verticalOffset) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (scrollRange == -1) {
                            scrollRange = appBarLayout.getTotalScrollRange();
                        }
                        if (scrollRange + verticalOffset == 0) {
                            changetoolbartitle(category,collapsingToolbar,backdropsubtitle);
                            isShow = true;
                        } else if (isShow) {
                            collapsingToolbar.setTitle(" ");
                            isShow = false;
                        }
                    }
                });

            }
        });
    }

    private void changecoverimage() {
        //sets coverimage for collapsible toolbar

        Category c = categoryFactory.getCategory(category);
        int drawableId = getResources().getIdentifier("cover_"+c.getCategory_name().toLowerCase().replace(" ",""), "drawable", getPackageName());
        if(drawableId == 0){
            drawableId = getResources().getIdentifier("cover_default", "drawable", getPackageName());
        }
        Glide.with(this).load(drawableId).into((ImageView) findViewById(R.id.backdrop));


    }
    private void changetoolbartitle(int categoryId, CollapsingToolbarLayout collapsingToolbar, TextView backdropsubtitle) {
        //changing toolbar title
        Category c = categoryFactory.getCategory(categoryId);
        collapsingToolbar.setTitle(c.getCategory_name());
        backdropsubtitle.setText(c.getCategory_name());

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changetoolbarcolor(int categoryId, final CollapsingToolbarLayout collapsingToolbar) {
        Category c = categoryFactory.getCategory(categoryId);
        Log.d("issue", c.toString() );
        //get the image as cover_category_name
        //get login by default
        int drawableId = getResources().getIdentifier("cover_"+c.getCategory_name().toLowerCase().replace(" ",""), "drawable", getPackageName());
        if(drawableId == 0){
            drawableId = getResources().getIdentifier("cover_default", "drawable", getPackageName());
        }

        //generate palette colors, amber is default
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbar.setContentScrimColor(palette.getMutedColor(getResources().getColor(R.color.amber)));
                collapsingToolbar.setStatusBarScrimColor(palette.getDarkMutedColor(getResources().getColor(R.color.dark_amber)));
                getWindow().setStatusBarColor(palette.getDarkMutedColor(getResources().getColor(R.color.dark_amber)));
            }
        });



    }

    @Override
    public void dataChanged(double totalPrice) {
        if (totalPrice>0)
            main_menu_fab.show();
        else
            main_menu_fab.hide();

        if(menu!=null)
            onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemWalletBalance = menu.findItem(R.id.item_wallet_balance);
        itemWalletBalance.setTitle(getString(R.string.Rs) + String.valueOf(CafeteriaApplication.getInstance().getWalletAmount()));
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }
}

