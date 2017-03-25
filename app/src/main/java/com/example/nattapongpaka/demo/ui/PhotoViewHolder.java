package com.example.nattapongpaka.demo.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.example.nattapongpaka.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nattapongpaka on 3/22/2017 AD.
 */

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.imageButton)
    ImageButton imageButton;

    private Context context;

    public PhotoViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);
    }

    public void setImageView(String path) {
        Glide.with(context)
                .load(path)
                .crossFade()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(300,300)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(imageView);
    }


    public interface IButton {
        void onClick();
    }

    public IButton iButton;

    public void setOnButtonClickListener(IButton iButton) {
        this.iButton = iButton;
    }

}
