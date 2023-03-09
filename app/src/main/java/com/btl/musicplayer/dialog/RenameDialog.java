package com.btl.musicplayer.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.btl.musicplayer.R;
import com.btl.musicplayer.databinding.DialogRenameBinding;
import com.btl.musicplayer.models.MusicManager;
import com.btl.musicplayer.utils.FilePath;
import com.haiprj.android_app_lib.ui.BaseDialog;

import java.io.File;

public class RenameDialog extends BaseDialog<DialogRenameBinding> {

    private MusicManager fileModel;
    @SuppressLint("StaticFieldLeak")
    private static RenameDialog instance;

    private RenameDialog(@NonNull Context context, Activity activity, OnActionDialogCallback onActionDialogCallback) {
        super(context, activity, onActionDialogCallback);
    }

    private RenameDialog(@NonNull Context context, int themeResId, Activity activity, OnActionDialogCallback onActionDialogCallback) {
        super(context, themeResId, activity, onActionDialogCallback);
    }

    private RenameDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, Activity activity, OnActionDialogCallback onActionDialogCallback) {
        super(context, cancelable, cancelListener, activity, onActionDialogCallback);
    }

    public static RenameDialog getInstance(@NonNull Context context, int themeResId, Activity activity, OnActionDialogCallback onActionDialogCallback) {
        if (instance == null) instance = new RenameDialog(context, themeResId, activity, onActionDialogCallback);
        return instance;
    }

    public static RenameDialog getInstance(@NonNull Context context, Activity activity, OnActionDialogCallback onActionDialogCallback) {
        if (instance == null) instance = new RenameDialog(context, activity, onActionDialogCallback);
        return instance;
    }

    public static RenameDialog getInstance(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, Activity activity, OnActionDialogCallback onActionDialogCallback) {
        if (instance == null) instance = new RenameDialog(context, cancelable, cancelListener, activity, onActionDialogCallback);
        return instance;
    }
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void addEvent() {
        binding.done.setOnClickListener(v -> {
            String name = binding.editText.getText().toString() + ".mp3";
            File file = new File(FilePath.getPath(getContext(), fileModel.getUri()));
            File newFile = new File(file.getParentFile(), name);
            boolean isSuccess;
            if (newFile.exists()) {
                Toast.makeText(getContext(), getContext().getString(R.string.file_exist), Toast.LENGTH_LONG).show();
                return;
            }
            else {

                isSuccess = file.renameTo(newFile);
            }
            onActionDialogCallback.callback("rename", isSuccess);
            dismiss();
        });

        binding.cancel.setOnClickListener(v -> {
            dismiss();
        });

        binding.clearText.setOnClickListener(v -> {
            binding.editText.setText("");
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
        binding.editText.setText(fileModel.getFileName().split("\\.")[0]);
    }

    public void setFileModel(MusicManager fileModel) {
        this.fileModel = fileModel;
    }

    public static void showUI(){
        instance.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_rename;
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        instance = null;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        instance = null;
    }
}
