package com.example.nattapongpaka.demo.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nattapongpaka.demo.model.PhotoDaos;
import com.example.nattapongpaka.demo.R;
import com.example.nattapongpaka.demo.presenter.MainPresenter;

import java.util.List;

/**
 * Created by nattapongpaka on 3/22/2017 AD.
 */

public class RecycleAdapter extends RecyclerView.Adapter<PhotoViewHolder> implements PhotoViewHolder.IButton {

    private Context context;
    private List<PhotoDaos> photoDaosList;
    private MainPresenter mainPresenter;

    public RecycleAdapter(Context context,MainPresenter mainPresenter, List<PhotoDaos> photoDaosList) {
        this.context = context;
        this.mainPresenter = mainPresenter;
        this.photoDaosList = photoDaosList;
    }

    public void updateData(List<PhotoDaos> newPhotoDaosList){
        this.photoDaosList = newPhotoDaosList;
        notifyDataSetChanged();
    }

    public void appendData(PhotoDaos photoDaos){
        this.photoDaosList.add(photoDaos);
        notifyDataSetChanged();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(context, view);
        return photoViewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, final int position) {
        PhotoDaos photoDaos = photoDaosList.get(position);
        holder.setImageView(photoDaos.getUrl());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDaosList.remove(position);
                notifyDataSetChanged();
                mainPresenter.pushPhotoDaosCache(photoDaosList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoDaosList != null && !photoDaosList.isEmpty() ? photoDaosList.size() : 0;
    }

    @Override
    public void onClick() {
        //TODO Remove image

    }
}
