package com.YoKawaiiK.e_shop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.YoKawaiiK.e_shop.Model.MyOrderModel;
import com.YoKawaiiK.e_shop.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<MyOrderModel> orderItemList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView orderDate, orderNums, orderPrice, orderProducts, OrderCheck;
        private Button ScanQrCode;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderNums = itemView.findViewById(R.id.orderNums);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderProducts = itemView.findViewById(R.id.orderProducts);
            OrderCheck = itemView.findViewById(R.id.OrderCheck);
        }
    }


    public OrderAdapter(Context context, List<MyOrderModel> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderItemview = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        return new ViewHolder(orderItemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyOrderModel model = orderItemList.get(position);

        holder.orderDate.setText(model.getDate());
        holder.orderNums.setText(model.getOrderNums());
        holder.orderPrice.setText(model.getOrderPrice());
        holder.orderProducts.setText(model.getOrderProducts());

        if(model.getOrderCheck().equalsIgnoreCase("false")){
            holder.OrderCheck.setText("Order: Pending");
        }
        else{
            holder.OrderCheck.setText("Order: Received");
            holder.ScanQrCode.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

}
