package com.YoKawaiiK.e_shop.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.YoKawaiiK.e_shop.Model.FavouritesClass;
import com.YoKawaiiK.e_shop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapterRecyclerView extends RecyclerView.Adapter<MyAdapterRecyclerView.ViewHolder> {
    private List<FavouritesClass> horizontalProductModelList;

    public MyAdapterRecyclerView(List<FavouritesClass> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavouritesClass horizontalProductModel = horizontalProductModelList.get(position);

        holder.producttitle.setText(horizontalProductModel.getProducttitle());
        holder.productprice.setText(horizontalProductModel.getProductprice());
        Picasso.get().load(horizontalProductModel.getProductimage()).into(holder.productImage);
        holder.checkBox.setImageResource(R.drawable.ic_baseline_favorite_24);
    }

    @Override
    public int getItemCount() {
        return horizontalProductModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView producttitle, productprice;
        ImageView checkBox;

        public ViewHolder(@NonNull View view) {
            super(view);
            productImage = view.findViewById(R.id.item_image);
            producttitle = view.findViewById(R.id.item_title);
            productprice = view.findViewById(R.id.item_Price);
            checkBox = view.findViewById(R.id.check_box);
        }
    }

}