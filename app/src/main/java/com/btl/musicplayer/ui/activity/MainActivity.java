package com.btl.musicplayer.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.btl.musicplayer.R;
import com.btl.musicplayer.databinding.ActivityMainBinding;
import com.btl.musicplayer.dialog.DeleteDialog;
import com.btl.musicplayer.dialog.DetailsDialog;
import com.btl.musicplayer.dialog.RenameDialog;
import com.btl.musicplayer.models.MusicManager;
import com.btl.musicplayer.ui.adapter.MusicAdapter;
import com.btl.musicplayer.utils.AppUtils;
import com.btl.musicplayer.utils.FilePath;
import com.haiprj.android_app_lib.ui.BaseActivity;
import com.haiprj.android_app_lib.ui.BaseDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private MusicAdapter musicAdapter;
    @Override
    protected void initView() {
        requestPermissionApp();
        musicAdapter = new MusicAdapter(this, new MusicAdapter.MusicItemListener() {
            @Override
            public void callback(String key, Object... objects) {
                if (Objects.equals(key, MusicAdapter.CLICK_MORE)) {
                    showMore(objects);
                }
                if (Objects.equals(key, MusicAdapter.CLICK)) {
                    PlayMusicActivity.start(MainActivity.this, (MusicManager) objects[0]);
                }
            }
        });
        binding.rcvListMusics.setAdapter(musicAdapter);

    }

    @SuppressLint("NonConstantResourceId")
    private void showMore(Object[] objects) {
        MusicManager fileModel = (MusicManager) objects[0];
        PopupMenu popup = new PopupMenu(MainActivity.this, (View) objects[1]);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.more_popup, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.share:
                    AppUtils.shareFile(MainActivity.this, fileModel.getUri());
                    break;
                case R.id.rename:
                    RenameDialog.getInstance(MainActivity.this, MainActivity.this, (keys, objectsF) -> {
                        if (Objects.equals(keys, "rename")) {
                            boolean isSuccess = (boolean) objectsF[0];
                            if (isSuccess){
                                new LoadFileMusic().execute();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Error Rename", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).setFileModel(fileModel);
                    RenameDialog.showUI();
                    break;
                case R.id.details:
                    viewDetails(fileModel);
                    break;
                case R.id.delete:
                    DeleteDialog deleteDialog = new DeleteDialog(this, this, new BaseDialog.OnActionDialogCallback() {
                        @Override
                        public void callback(String key, Object... objects) {
                            if (Objects.equals(key, DeleteDialog.DELETE)) {
                                File file = new File(Objects.requireNonNull(FilePath.getPath(MainActivity.this, fileModel.getUri())));
                                if (file.delete()) {
                                    new LoadFileMusic().execute();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Error Delete", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                    deleteDialog.show();
                    break;

            }

            return true;
        });

        popup.show();
    }

    private void viewDetails(MusicManager musicManager) {
        DetailsDialog detailsDialog = new DetailsDialog(this, this, new BaseDialog.OnActionDialogCallback() {
            @Override
            public void callback(String key, Object... objects) {

            }
        }, musicManager);
        detailsDialog.show();
    }

    private void requestPermissionApp() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            new LoadFileMusic().execute();
        }
        else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        requestPermissionApp();
    }

    @Override
    protected void addEvent() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadFileMusic extends AsyncTask<String, Void, List<MusicManager>> {

        @Override
        protected List<MusicManager> doInBackground(String... strings) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_STARTED));
            try {
                Thread.sleep(500);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED));

            } catch (Exception ignored) {

            }
            return getListFileModelAudio();
        }

        @Override
        protected void onPostExecute(List<MusicManager> list) {
            super.onPostExecute(list);
            musicAdapter.update(list);
        }
        private List<MusicManager> getListFileModelAudio() {
            List<MusicManager> list = new ArrayList<>();
            Uri collection;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            } else {
                collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            String[] projection = new String[] {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.SIZE
            };
            String selection = MediaStore.Audio.Media.DURATION +
                    " >= ?";
            String[] selectionArgs = new String[] {
                    String.valueOf(TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES))
            };
            String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

            try (Cursor cursor = MainActivity.this.getApplicationContext().getContentResolver().query(
                    collection,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
            )) {
                // Cache column indices.
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                int nameColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                int durationColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);

                while (cursor.moveToNext()) {
                    // Get values of columns for a given video.
                    long id = cursor.getLong(idColumn);
                    String name = cursor.getString(nameColumn);
                    int duration = cursor.getInt(durationColumn);
                    int size = cursor.getInt(sizeColumn);

                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                    // Stores column values and the contentUri in a local object
                    // that represents the media file.
                    list.add(new MusicManager(name,contentUri, size, duration));
                }
            }
            return list;
        }
    }
}