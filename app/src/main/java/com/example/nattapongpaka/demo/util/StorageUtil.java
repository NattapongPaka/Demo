package com.example.nattapongpaka.demo.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;





/**
 * Created by Noth on 19/10/2559.
 */

public class StorageUtil {
    private static StorageUtil instance;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String CAMERA_DIR = "/dcim/";

    private String img_folder = "Demo";

    private Context context;

    public static StorageUtil getInstance() {
        if (instance == null) {
            instance = new StorageUtil();
        }
        return instance;
    }

    public StorageUtil() {
        context = Contextor.getInstance().getContext();
    }

    public String getPathInternalStorage() {
        String dirName = Environment.getExternalStorageDirectory() + "/" + img_folder;
        File dir = new File(dirName);
        if (dir.exists()) {
            return dirName;
        } else {
            dir.mkdir();
            return dirName;
        }
    }

    public String getFileName(String filePath) {
        try {
            return filePath.substring(filePath.lastIndexOf("/") + 1, filePath.indexOf(".")).trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getExtension(String filePath) {
        try {
            return filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getExtensionUpperCase(String filePath) {
        try {
            String result;
            if ((result = getExtension(filePath)) != null) {
                return result.toUpperCase();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean isFileDuplicate(String src) {
        String fileName = getFileName(src);
        String fileExt = getExtension(src);
        File file = new File(getPathInternalStorage(), fileName + "." + fileExt);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public File getFileInternalPath(String src) {
        if (isFileDuplicate(src)) {
            String fileName = getFileName(src);
            String fileExt = getExtension(src);
            File file = new File(getPathInternalStorage(), fileName + "." + fileExt);
            return file;
        }
        return null;
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        return File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
    }

    public File prepareFileImage(Bitmap bitmap) {
        FileOutputStream out = null;
        int bitmapQuality = 95;
        String extStorageDirectory = StorageUtil.getInstance().getPathInternalStorage();
        //String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "temp_filter";
        String fileExt = "png";
        File fileCache = new File(extStorageDirectory, fileName + "." + fileExt);
        try {
            OutputStream outStream = new FileOutputStream(fileCache);
            bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, outStream);
//            if (fileExt.toLowerCase().equals("png")) {
//                bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, outStream);
//            } else {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, bitmapQuality, outStream);
//            }
            outStream.flush();
            outStream.close();
            //if (bitmap != null) bitmap.recycle();
            return fileCache;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = getAlbumPath(img_folder);

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            //Log.v(ConfigSingleton.getInstantce().getImage_folder(), "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }

    public File getAlbumPath(String albumName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            return new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES
                    ),
                    albumName
            );
        } else {
            return new File(
                    Environment.getExternalStorageDirectory()
                            + CAMERA_DIR
                            + albumName
            );
        }
    }

    public void syncToGallery(File f) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(f)));
        }
    }


}
