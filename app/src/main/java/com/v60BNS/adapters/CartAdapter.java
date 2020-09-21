package com.v60BNS.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.v60BNS.R;
import com.v60BNS.activities_fragments.activity_cart.CartActivity;
import com.v60BNS.models.StoryModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Cart_Holder> {
    private List<StoryModel.Data> orderlist;
    private Context context;
    private int i = -1;


    public CartAdapter(List<StoryModel.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
    }

    @Override
    public Cart_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_row, viewGroup, false);
        return new Cart_Holder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final Cart_Holder holder, final int position) {


        holder.imgIncrease.setOnClickListener(v -> {
                    int count = Integer.parseInt(holder.tvAmount.getText().toString()) + 1;
                    holder.tvAmount.setText(String.valueOf(count));

                    //   notifyItemChanged(holder.getAdapterPosition());
                }

        );
        holder.imgDecrease.setOnClickListener(v -> {

                    int count = Integer.parseInt(holder.tvAmount.getText().toString());
                    if (count > 1) {
                        count = count - 1;
                        holder.tvAmount.setText(String.valueOf(count));

                        //     notifyItemChanged(holder.getAdapterPosition());            }
                    }
                }

        );
        holder.imDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartActivity activity=(CartActivity)context;
                activity.remove(holder.getLayoutPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class Cart_Holder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvCost, tvAmount;
        private RoundedImageView image;
        private ImageView imgIncrease, imgDecrease, imDelete;
        public ConstraintLayout consBackground, consForeground;
        public LinearLayout llLeft, llRight;


        public Cart_Holder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgIncrease = itemView.findViewById(R.id.imgIncrease);
            imgDecrease = itemView.findViewById(R.id.imgDecrease);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            imDelete = itemView.findViewById(R.id.icon);
            consForeground = itemView.findViewById(R.id.consForeground);


        }


    }

}
