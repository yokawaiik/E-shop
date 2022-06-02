package com.YoKawaiiK.e_shop.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.YoKawaiiK.e_shop.Adapters.AdminOfferAdapter;
import com.YoKawaiiK.e_shop.Adapters.AdminProductAdapter;
import com.YoKawaiiK.e_shop.Model.AdminProduct;
import com.YoKawaiiK.e_shop.R;
import com.YoKawaiiK.e_shop.UI.Activities.AdminAddProductActivity;
import com.YoKawaiiK.e_shop.UI.Activities.EditProductActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String mParam1;
    private String mParam2;

    //my variables
    private RecyclerView ProductsRecycler;
    private AdminProductAdapter adapter;
    private FloatingActionButton ProductsFloatingActionButton;
    private List<AdminProduct> adminProducts;
    private DatabaseReference mDataBaseRef;
    private ProgressBar bar;


    public ProductsFragment() {
        // Required empty public constructor
    }

    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_products, container, false);

        ProductsRecycler = (RecyclerView) view.findViewById(R.id.ProductsRecycler);
        ProductsFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.SalesFloatingBtnId);

        bar = view.findViewById(R.id.productProgressBar);

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("product");
        adminProducts = new ArrayList<>();

        adapter = new AdminProductAdapter(getActivity(), adminProducts);
        ProductsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ProductsRecycler.setAdapter(adapter);

        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminProducts.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())//category
                {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren())//name
                    {
                        adminProducts.add(new AdminProduct(snapshot2.getKey(),
                                snapshot1.getKey(),
                                snapshot2.child("expired").getValue(String.class),
                                snapshot2.child("image").getValue(String.class),
                                snapshot2.child("price").getValue(String.class),
                                snapshot2.child("quantity").getValue(String.class)));
                    }
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter.setOnItemClickListener(new AdminOfferAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent i = new Intent(getActivity(), EditProductActivity.class);
                Bundle b = new Bundle();
                b.putString("img", adminProducts.get(pos).getImage());
                b.putString("name", adminProducts.get(pos).getName());
                b.putString("category", adminProducts.get(pos).getCategory());
                b.putString("expired", adminProducts.get(pos).getExpired());
                b.putString("price", adminProducts.get(pos).getPrice());
                b.putString("quantity", adminProducts.get(pos).getQuantity());
                i.putExtras(b);
                startActivity(i);
            }
        });

        adapter.setOnLongClickListener(new AdminOfferAdapter.onLongClickListener() {
            @Override
            public void onItemLongClick(final int pos) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()).setTitle("Confirmation").setMessage("Are You Sure You Want To Delete ?!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = mDataBaseRef.child(adminProducts.get(pos).getCategory()).child(adminProducts.get(pos).getName());
                        reference.removeValue();
                        StorageReference z = FirebaseStorage.getInstance().getReference("offers").child(adminProducts.get(pos).getName() + ".jpg");
                        z.delete();
                        StorageReference x = FirebaseStorage.getInstance().getReference("offers").child(adminProducts.get(pos).getName());
                        x.delete();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();
            }
        });


        //on clicking to adding button
        ProductsFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here add button
                startActivity(new Intent(getActivity(), AdminAddProductActivity.class));

            }
        });


        return view;
    }


}