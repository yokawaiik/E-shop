package com.YoKawaiiK.e_shop.UI.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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

import com.YoKawaiiK.e_shop.R;
import com.YoKawaiiK.e_shop.UI.MyCartFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

// Экран корзины
public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView mPerson_name;
    private CircleImageView mPerson_image;
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        fa=this;

        mToolbar = (Toolbar)findViewById(R.id.cartToolbar);
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("My Cart");
        getSupportActionBar().setTitle(R.string.cart_activity__my_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.Cartframe,new MyCartFragment()).commit();

    }



    @Override
    protected void onStart() {
        super.onStart();
        drawerLayout = (DrawerLayout) findViewById(R.id.cartDrawer);
        navigationView = (NavigationView) findViewById(R.id.cartNavigationViewer);

        //navigation header
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        mPerson_name = view.findViewById(R.id.persname);
        mPerson_image = view.findViewById(R.id.circimage);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getNavHeaderData();

        showCartIcon();
    }


    private void getNavHeaderData(){
        FirebaseAuth mAuth ;
        mAuth = FirebaseAuth.getInstance();
        String CurrentUser = mAuth.getCurrentUser().getUid();
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("users").child(CurrentUser);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String Name = snapshot.child("Name").getValue().toString();
                    String Image = snapshot.child("Image").getValue().toString();
                    mPerson_name.setText(Name);
                    if (Image.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(mPerson_image);
                    } else
                        Picasso.get().load(Image).placeholder(R.drawable.profile).into(mPerson_image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id== R.id.Home){
            startActivity(new Intent(CartActivity.this, MainActivity.class));
        }
        else if(id== R.id.Profile){
            startActivity(new Intent(CartActivity.this, UserProfileActivity.class));
        }
        else if(id == R.id.favourites){
            startActivity(new Intent(CartActivity.this, FavouritesActivity.class));
        }
        else if(id == R.id.MyOrders){
            startActivity(new Intent(CartActivity.this, OrderActivity.class));
        }
        else if(id== R.id.fruits){
            Intent intent =new Intent(CartActivity.this, CategoryActivity.class);
            intent.putExtra("Category Name","Fruits");
            startActivity(intent);
        }
        else if(id== R.id.vegetables){
            Intent intent =new Intent(CartActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Vegetables");
            startActivity(intent);
        }
        else if(id== R.id.meats){
            Intent intent =new Intent(CartActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Meats");
            startActivity(intent);
        }
        else if(id== R.id.electronics){
            Intent intent =new Intent(CartActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Electronics");
            startActivity(intent);
        }
        else if(id== R.id.Logout){
            CheckLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    private void CheckLogout(){
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(CartActivity.this);
        checkAlert.setMessage(R.string.message_do_you_want_to_logout)
                .setCancelable(false).setPositiveButton(R.string.message_button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(CartActivity.this, LogInActivity.class);
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

    private void showCartIcon(){
        //toolbar & cartIcon
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.buyer_toolbar,null);
        //actionBar.setCustomView(view);

        //************custom action items xml**********************
        CustomCartContainer = (RelativeLayout)findViewById(R.id.CustomCartIconContainer);
        PageTitle =(TextView)findViewById(R.id.PageTitle);
        PageTitle.setVisibility(View.GONE);
        CustomCartContainer.setVisibility(View.GONE);

    }

}