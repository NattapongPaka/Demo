package com.example.nattapongpaka.demo.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nattapongpaka.demo.BuildConfig;
import com.example.nattapongpaka.demo.presenter.MainPresenter;
import com.example.nattapongpaka.demo.presenter.View.IMainView;
import com.example.nattapongpaka.demo.util.Constance;
import com.example.nattapongpaka.demo.util.LogUtil;
import com.example.nattapongpaka.demo.model.PhotoDaos;
import com.example.nattapongpaka.demo.R;
import com.example.nattapongpaka.demo.manager.HttpManager;
import com.example.nattapongpaka.demo.util.NetworkUtils;
import com.example.nattapongpaka.demo.util.PreferenceUtil;
import com.example.nattapongpaka.demo.util.StorageUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nattapongpaka on 3/22/2017 AD.
 */

public class MainFragment extends Fragment implements EasyPermissions.PermissionCallbacks, IMainView {

    public static String TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.btnAddImage)
    Button btnAddImage;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    private StaggeredGridLayoutManager gaggeredGridLayoutManager;

    private List<PhotoDaos> photoDaosList;
    private RecycleAdapter recycleAdapter;

    private final int REQ_CAMERA = 1111;
    private final int REQ_GALLERY = 2222;

    private ArrayList<PhotoDaos> selectPhotos = new ArrayList<>();
    private MainPresenter mainPresenter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mainPresenter.stop();
    }

    private void initView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(llm);
        recycleView.setHasFixedSize(true);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    @OnClick(R.id.btnAddImage)
    public void setBtnAddImage() {
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Please Select");
        builder.setItems(new String[]{"Take photo", "Open gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                if (pos == 0) {
                    //TODO Select take a photo
                    startCameraPicker();
                } else if (pos == 1) {
                    //TODO Select open gallery
                    startGallery();
                }
            }
        });
        builder.create().show();
    }


    private void startGallery() {
        String[] params = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), params)) {
            PhotoPicker.builder()
                    .setPhotoCount(10)
                    .setShowCamera(false)
                    .setPreviewEnabled(false)
                    .start(getContext(), this, PhotoPicker.REQUEST_CODE);
        } else {
            EasyPermissions.requestPermissions(this, "Allow Read External", REQ_GALLERY, params);
        }
    }

    private void startCameraPicker() {
        String[] params = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            File mFile = null;
            //thePic = null;
            try {
                mFile = StorageUtil.getInstance().createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ContentValues values = new ContentValues();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", mFile);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQ_CAMERA);
            } else {
                values.put(MediaStore.Images.Media.TITLE, timeStamp);
                Uri mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                startActivityForResult(intentPicture, REQ_CAMERA);
            }
        } else {
            EasyPermissions.requestPermissions(this, "Allow Camera", REQ_CAMERA, params);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQ_GALLERY) {
            startGallery();
        } else if (requestCode == REQ_CAMERA) {
            startCameraPicker();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        photoDaosList = new ArrayList<>();
        mainPresenter = new MainPresenter(getActivity(), this);
        recycleAdapter = new RecycleAdapter(getContext(), mainPresenter, photoDaosList);
        recycleView.setAdapter(recycleAdapter);

        if (NetworkUtils.getInstance().isNetworkConnected()) {
            mainPresenter.fatchImageFromServer();
        } else {
            mainPresenter.getPhotoDaosCache();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CAMERA) {
                //TODO Camera
                mainPresenter.getOriginalImagePath();
            } else if (requestCode == PhotoPicker.REQUEST_CODE) {
                //TODO Gallery
                mainPresenter.getImageFromGallery(data);
            }
        }
    }


    @Override
    public void onLoadPhotosFromServerSuccess(List<PhotoDaos> photoDaosList) {
        selectPhotos.addAll(photoDaosList);
        recycleAdapter.updateData(selectPhotos);
    }

    @Override
    public void onLoadPhotosFromCache(List<PhotoDaos> photoDaosList) {
        selectPhotos.addAll(photoDaosList);
        recycleAdapter.updateData(selectPhotos);
    }

    @Override
    public void onLoadPhotosFromTakePhoto(String pathPhoto) {
        PhotoDaos photoDaos = mainPresenter.generatePhotoDaos(pathPhoto);
        selectPhotos.add(photoDaos);
        recycleAdapter.updateData(selectPhotos);
        pushCache();
    }

    @Override
    public void onLoadPhotosFromSelectGallery(List<PhotoDaos> photoDaosList) {
        selectPhotos.addAll(photoDaosList);
        recycleAdapter.updateData(selectPhotos);
        pushCache();
    }

    private void pushCache() {
        mainPresenter.pushPhotoDaosCache(selectPhotos);
    }

    /**
     * Logic
     */


}
