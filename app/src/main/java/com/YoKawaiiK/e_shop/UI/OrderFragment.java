package com.YoKawaiiK.e_shop.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.YoKawaiiK.e_shop.Adapters.OrderAdapter;
import com.YoKawaiiK.e_shop.Model.MyOrderModel;
import com.YoKawaiiK.e_shop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    ArrayList<MyOrderModel> orderItemList;
    OrderAdapter adapter ;
    private FirebaseAuth mAuth;
    private String CurrentUser;
    private DatabaseReference m , root;
    public static Activity fa;
    public OrderFragment() {
        // Required empty public constructor
    }
    private RecyclerView OrderItemRecyclerView;

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order, container, false);
        fa= getActivity();
        mAuth=FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();

        OrderItemRecyclerView =  view.findViewById(R.id.orderrecycler);
        orderItemList = new ArrayList<MyOrderModel>();
        adapter = new OrderAdapter(getActivity(),orderItemList);

        OrderItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrderItemRecyclerView.setAdapter(adapter);

        DatabaseReference roott= FirebaseDatabase.getInstance().getReference();
        DatabaseReference x = roott.child("order").child(CurrentUser);
        ValueEventListener valueEventListener1 =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String Date = dataSnapshot.child("Date").getValue().toString();
                        int nums = ((int)(dataSnapshot.child("orderproducts").getChildrenCount()));
                        String totalPrice = dataSnapshot.child("totalPrice").getValue().toString();
                        String OrderCheck = dataSnapshot.child("IsChecked").getValue().toString();

                        String products="Products :\n";
                        for (DataSnapshot data : dataSnapshot.child("orderproducts").getChildren())
                        {
                            products+= "    #"+data.getKey() + "\n        Price: " + data.child("productPrice").getValue().toString() + " EGP\n        Quantity: " + data.child("quantity").getValue().toString()+"\n";
                        }

                        orderItemList.add( new MyOrderModel(dataSnapshot.getKey(),"   Date :  " + Date ,"   Products Number :  "+String.valueOf(nums),"   Total Price :  "+ totalPrice+" EGP" , "   "+products,OrderCheck));
                    }
                }
                else{
                    orderItemList.clear();
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        x.addListenerForSingleValueEvent(valueEventListener1);

        return view;
    }
}