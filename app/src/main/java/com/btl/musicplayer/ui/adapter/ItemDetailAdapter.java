package com.btl.musicplayer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.btl.musicplayer.R;
import com.btl.musicplayer.databinding.ItemDetailsBinding;
import com.btl.musicplayer.models.DetailsModel;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder> {

    private Context context;
    private final List<DetailsModel> list = new ArrayList<>();

    public ItemDetailAdapter(Context context, List<DetailsModel> list) {
        this.context = context;
        this.list.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_details, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.load(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemDetailsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void load(DetailsModel model) {
            binding.header.setText(model.getHeader());
            binding.body.setText(model.getBody());
        }
    }
}
