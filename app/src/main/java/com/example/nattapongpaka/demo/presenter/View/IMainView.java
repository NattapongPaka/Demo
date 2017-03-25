package com.example.nattapongpaka.demo.presenter.View;

import com.example.nattapongpaka.demo.model.PhotoDaos;

import java.util.List;

/**
 * Created by nattapongpaka on 3/25/2017 AD.
 */

public interface IMainView {
    void onLoadPhotosFromServerSuccess(List<PhotoDaos> photoDaosList);
    void onLoadPhotosFromCache(List<PhotoDaos> photoDaosList);
    void onLoadPhotosFromTakePhoto(String pathPhoto);
    void onLoadPhotosFromSelectGallery(List<PhotoDaos> photoDaosList);
}
