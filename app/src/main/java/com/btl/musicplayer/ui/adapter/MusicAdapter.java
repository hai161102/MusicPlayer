package com.btl.musicplayer.ui.adapter;

import static com.btl.musicplayer.utils.AppUtils.convertDuration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.btl.musicplayer.R;
import com.btl.musicplayer.databinding.ItemMusicBinding;
import com.btl.musicplayer.models.MusicManager;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    public static final String CLICK_MORE = "click_more";
    public static final String CLICK = "click";
    private Context context;
    private MusicItemListener listener;
    private final List<MusicManager> list = new ArrayList<>();

    public MusicAdapter(Context context, MusicItemListener listener) {
        this.context = context;
        this.listener = listener;
    }


    public void update(List<MusicManager> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_music, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.ViewHolder holder, int position) {
        holder.load(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemMusicBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void load(MusicManager musicManager) {
            binding.musicName.setText(musicManager.getFileName());

            binding.musicDuration.setText(convertDuration(musicManager.getDuration()));
            binding.musicMore.setOnClickListener(v -> {
                listener.callback(CLICK_MORE, musicManager, binding.musicMore);
            });
            binding.getRoot().setOnClickListener(v -> {
                listener.callback(CLICK, musicManager);
            });
        }

    }

    public interface MusicItemListener {
        void callback(String key, Object... objects);
    }
}
