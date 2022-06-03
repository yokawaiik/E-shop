package com.YoKawaiiK.e_shop.UI.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.YoKawaiiK.e_shop.Adapters.GridProductAdapter;
import com.YoKawaiiK.e_shop.Adapters.MyAdapter;
import com.YoKawaiiK.e_shop.Model.FavouritesClass;
import com.YoKawaiiK.e_shop.Model.HorizontalProductModel;
import com.YoKawaiiK.e_shop.Model.Model;
import com.YoKawaiiK.e_shop.Model.Offers;
import com.YoKawaiiK.e_shop.Model.User;
import com.YoKawaiiK.e_shop.R;
import com.YoKawaiiK.e_shop.Utils.Constants;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// constants


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private CircleImageView image;
    private TextView mperson_name;
    private FirebaseAuth mAuth;
    private String Uid, name, photo;
    private FirebaseUser CurrentUser;
    private NavigationView navigationView;
    private ViewPager pager;
    private MyAdapter adapter;
    private List<Model> ModelStandarts;
    private DatabaseReference m;
    private View mnavigationview;
    private static List<FavouritesClass> favourites;
    //Custom Xml Views (cart Icon)
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;
    private TextView CustomCartNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        Uid = CurrentUser.getUid();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        mnavigationview = navigationView.getHeaderView(0);
        mperson_name = mnavigationview.findViewById(R.id.persname);
        image = mnavigationview.findViewById(R.id.circimage);
        drawerLayout = findViewById(R.id.drawer);

        mToolBar = findViewById(R.id.main_TooBar);
        setSupportActionBar(mToolBar);
        mtoggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onStart() {
        super.onStart();

        //Получить данные пользователя представления заголовка
        navigationViewHeaderData();
        //Получить избранное
        retrieveFav();
        // FirstView
        retrieveElectroncis();
        // SecondView
        retrieveFruits();
        //Third View
        retrieveMeats();
        // Fourth View
        retrieveVegatables();
        // OFFERS
        retrieveOffers();
        //Обновить CartIcon
        showCartIcon();
        HandleTotalPriceToZeroIfNotExist();
    }

    // Блок товаров для категории электроника
    public void retrieveElectroncis() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText(R.string.electronics);
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridProductAdapter my_adapter;
        my_adapter = new GridProductAdapter(lastmodels, favourites,MainActivity.this);

        m = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("product").child("Electronics");

        m.limitToLast(Constants.COUNT_ITEMS_IN_GRID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User myUser = new User();
                    myUser = ds.getValue(User.class);
                    myUser.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(myUser.getImage(), myUser.getCategory(), myUser.getPrice(), false,myUser.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra(getString(R.string.intentStringExtraCategoryName), getString(R.string.intentStringExtraCategoryElectronics));
                startActivity(intent);
            }
        });

    }

    // Получение избранных товаров
    public void retrieveFav() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favourites")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        favourites = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    FavouritesClass fav = new FavouritesClass();

                    FavouritesClass fav = new FavouritesClass(
                            String.valueOf(ds.child("productimage").getValue()),
                            String.valueOf(ds.child("producttitle").getValue()),
                            String.valueOf(ds.child("productprice").getValue()),
                            String.valueOf(ds.child("expiredDate").getValue()),
                            Boolean.valueOf(String.valueOf(ds.child("checked").getValue()))
                    );

//                    fav = ds.getValue(FavouritesClass.class);
                    favourites.add(fav);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    // Блок товаров для категории фруктов
    public void retrieveFruits() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView2);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
//        gridlayouttitle.setText("Fruits");
        gridlayouttitle.setText(R.string.fruits);
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridProductAdapter my_adapter;
        my_adapter = new GridProductAdapter(lastmodels, favourites,MainActivity.this);
        m = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("product").child("Fruits");

        m.limitToLast(Constants.COUNT_ITEMS_IN_GRID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User my_user = new User();
                    my_user = ds.getValue(User.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(), my_user.getPrice(), false, my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra(getString(R.string.intentStringExtraCategoryName), getString(R.string.intentStringExtraCategoryFruits));
                startActivity(intent);
            }
        });
    }

    // Блок товаров для категории мясо
    public void retrieveMeats() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView3);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText(R.string.meats);
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridProductAdapter my_adapter;
        my_adapter = new GridProductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Meats");
        m.limitToLast(Constants.COUNT_ITEMS_IN_GRID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User my_user = new User();
                    my_user = ds.getValue(User.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(), my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra(getString(R.string.intentStringExtraCategoryName), getString(R.string.intentStringExtraCategoryMeats));
                startActivity(intent);
            }
        });

    }

    // Блок товаров для категории овощей
    public void retrieveVegatables() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView4);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText(R.string.vegetables);
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridProductAdapter my_adapter;
        my_adapter = new GridProductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Vegetables");
        m.limitToLast(Constants.COUNT_ITEMS_IN_GRID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User my_user = new User();
                    my_user = ds.getValue(User.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(),my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra(getString(R.string.intentStringExtraCategoryName), getString(R.string.intentStringExtraCategoryVegetables));
                startActivity(intent);
            }
        });
    }

    // Блок товаров для предложения
    public void retrieveOffers() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("offers");
        ModelStandarts = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Offers offer = new Offers();
                    offer = ds.getValue(Offers.class);
                    offer.setTitle(ds.getKey().toString());
                    ModelStandarts.add(new Model(offer.getImg(), offer.getTitle(), offer.getDescribtion()));
                    adapter = new MyAdapter(ModelStandarts, MainActivity.this);
                    pager = findViewById(R.id.cardview);
                    pager.setAdapter((PagerAdapter) adapter);
                    pager.setPadding(130, 0, 130, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    // Прослушиватель для обработки событий в элементах навигации
    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Profile) {
            startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
        }
        else if(id == R.id.favourites){
            startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
        }
        else if(id == R.id.Cart){
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        }
        else if(id == R.id.MyOrders){
            startActivity(new Intent(MainActivity.this, OrderActivity.class));
        }

        else if(id== R.id.fruits){
            Intent intent =new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra(getString(R.string.intentStringExtraCategoryName), getString(R.string.intentStringExtraCategoryFruits));
            startActivity(intent);
        }
        else if(id== R.id.vegetables){
            Intent intent =new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra(getString(R.string.intentStringExtraCategoryName),getString(R.string.intentStringExtraCategoryVegetables));
            startActivity(intent);
        }
        else if(id== R.id.meats){
            Intent intent =new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra(getString(R.string.intentStringExtraCategoryName),getString(R.string.intentStringExtraCategoryMeats));
            startActivity(intent);
        }
        else if(id== R.id.electronics){
            Intent intent =new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra(getString(R.string.intentStringExtraCategoryName),getString(R.string.intentStringExtraCategoryElectronics));
            startActivity(intent);
        }
        else if (id == R.id.Logout) {
            CheckLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //выход
    private void CheckLogout(){
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(MainActivity.this);
        checkAlert.setMessage(R.string.message_do_you_want_to_logout)
                .setCancelable(false).setPositiveButton(R.string.message_button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton(R.string.message_button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = checkAlert.create();
        alert.setTitle(R.string.alert_dialog__logout);
        alert.show();

    }


    @Override
    public void onStop() {
        super.onStop();
    }

    public void navigationViewHeaderData() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("users").child(Uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("Name").getValue().toString();
                    photo = dataSnapshot.child("Image").getValue().toString();
                    if (photo.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(image);
                    } else
                        Picasso.get().load(photo).placeholder(R.drawable.profile).into(image);
                    mperson_name.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    // Показать иконку корзины
    private void showCartIcon(){
        //toolbar & cartIcon
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.buyer_toolbar,null);
        actionBar.setCustomView(view);

        //************custom action items xml**********************
        CustomCartContainer = (RelativeLayout)findViewById(R.id.CustomCartIconContainer);
        PageTitle =(TextView)findViewById(R.id.PageTitle);
        CustomCartNumber = (TextView)findViewById(R.id.CustomCartNumber);
        setNumberOfItemsInCartIcon();

        CustomCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

    }

    // Установить число товаров рядом с иконкой корзины
    private void setNumberOfItemsInCartIcon(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("cart").child(Uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(dataSnapshot.getChildrenCount()==1){
                        CustomCartNumber.setVisibility(View.GONE);
                    }
                    else {
                        CustomCartNumber.setVisibility(View.VISIBLE);
                        CustomCartNumber.setText(String.valueOf(dataSnapshot.getChildrenCount()-1));
                    }
                }
                else{
                    CustomCartNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    // ЕсПроверка общей цены товаров в корзине на ноль
    private void HandleTotalPriceToZeroIfNotExist(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("cart").child(Uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference().child("cart").child(Uid).child("totalPrice").setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        m.addListenerForSingleValueEvent(eventListener);

    }

}
