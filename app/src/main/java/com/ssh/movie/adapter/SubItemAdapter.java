package com.ssh.movie.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.movie.R;
import com.ssh.movie.model.SubItem;

import java.util.List;


// 하위 어답터
public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {

    private List<SubItem> subItemList;

    SubItemAdapter(List<SubItem> subItemList) {
        this.subItemList = subItemList;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder subItemViewHolder, int i) {
        SubItem subItem = subItemList.get(i);
        subItemViewHolder.img_sub_item.setImageResource(subItem.getSubItemImage());
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {
        ImageView img_sub_item;

        SubItemViewHolder(View itemView) {
            super(itemView);
            img_sub_item = itemView.findViewById(R.id.img_sub_item);

        }
    }
}


