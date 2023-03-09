package com.btl.musicplayer.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.btl.musicplayer.R;
import com.btl.musicplayer.databinding.DialogDetailsBinding;
import com.btl.musicplayer.models.DetailsModel;
import com.btl.musicplayer.models.MusicManager;
import com.btl.musicplayer.ui.adapter.ItemDetailAdapter;
import com.btl.musicplayer.utils.FilePath;
import com.haiprj.android_app_lib.ui.BaseDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DetailsDialog extends BaseDialog<DialogDetailsBinding> {

    private ItemDetailAdapter adapter;

    private MusicManager fileModel;

    public DetailsDialog(@NonNull Context context, Activity activity, OnActionDialogCallback onActionDialogCallback, MusicManager fileModel) {
        super(context, activity, onActionDialogCallback);
        this.fileModel = fileModel;
    }

    public DetailsDialog(@NonNull Context context, int themeResId, Activity activity, OnActionDialogCallback onActionDialogCallback, MusicManager fileModel) {
        super(context, themeResId, activity, onActionDialogCallback);
        this.fileModel = fileModel;
    }

    public DetailsDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, Activity activity, OnActionDialogCallback onActionDialogCallback, MusicManager fileModel) {
        super(context, cancelable, cancelListener, activity, onActionDialogCallback);
        this.fileModel = fileModel;
    }

    @Override
    protected void addEvent() {
        binding.done.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    protected void initView() {
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        adapter = new ItemDetailAdapter(getContext(), getList());
        binding.rcvDetails.setAdapter(adapter);
    }

    private List<DetailsModel> getList() {
        File file = new File(Objects.requireNonNull(FilePath.getPath(getContext(), fileModel.getUri())));
        List<DetailsModel> list = new ArrayList<>();
        @SuppressLint("DefaultLocale") String fileLength = String.format("%.2f", (fileModel.getSize() / (1024f * 1024f))) + "MB";
        @SuppressLint("SimpleDateFormat") String lastModified = new SimpleDateFormat("dd/MM/yyyy").format(new Date(file.lastModified()));
        list.add(new DetailsModel("Path: ", file.getPath()));
        list.add(new DetailsModel("Size: ", fileLength));
        list.add(new DetailsModel("Last Modified: ", lastModified));
        return list;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_details;
    }
}
