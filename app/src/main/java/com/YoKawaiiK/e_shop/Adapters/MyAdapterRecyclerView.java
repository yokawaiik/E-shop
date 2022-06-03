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

    private onItemClickListener itemListener;

    private List<FavouritesClass> horizontalProductModelList;

    public interface onItemClickListener {
        void onItemClick(int pos);
    }

//    public void setOnItemClickListener(MyAdapterRecyclerView.onItemClickListener listener) {
//        itemListener = listener;
//    }


    public MyAdapterRecyclerView(MyAdapterRecyclerView.onItemClickListener itemListener,
                                 List<FavouritesClass> horizontalProductModelList) {
        this.itemListener = itemListener;
        this.horizontalProductModelList = horizontalProductModelList;
    }

    public void setOnItemClickListener(MyAdapterRecyclerView.onItemClickListener listener) {
        itemListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, null);
        return new ViewHolder(v, itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavouritesClass horizontalProductModel = horizontalProductModelList.get(position);

//        Log.i("FavoritesClass-getProductTitle", horizontalProductModel.getProductTitle());
//        Log.i("FavoritesClass-getProductPrice", horizontalProductModel.getProductPrice());
//        Log.i("FavoritesClass-getProductImage", horizontalProductModel.getProductImage());
//        Log.i("FavoritesClass-getProductExpiryDate", horizontalProductModel.getProductExpiryDate());
//        Log.i("FavoritesClass-isFavorite", String.valueOf(horizontalProductModel.isFavorite()));

        holder.producttitle.setText(horizontalProductModel.getProductTitle());
        holder.productprice.setText(horizontalProductModel.getProductPrice());
        Picasso.get().load(horizontalProductModel.getProductImage()).into(holder.productImage);
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

        public ViewHolder(@NonNull View view,
                          final MyAdapterRecyclerView.onItemClickListener itemlistener
        ) {
            super(view);
            productImage = view.findViewById(R.id.item_image);
            producttitle = view.findViewById(R.id.item_title);
            productprice = view.findViewById(R.id.item_Price);
            checkBox = view.findViewById(R.id.check_box);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemlistener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            itemlistener.onItemClick(position);
                        }
                    }
                }
            });
        }


    }

}