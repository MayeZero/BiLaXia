package com.example.cameraalbumtest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShowPicture extends AppCompatActivity {
    ArrayList<File> fileList = new ArrayList<>();
    private static final String TAG = "ShowPicture";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private ImageView picture;
    private List<Picture> pictureList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

        initPictures();
        PictureAdapter adapter=new PictureAdapter(ShowPicture.this, R.layout.picture_item,pictureList);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View viewDiag = getLayoutInflater().inflate(R.layout.view,null);
                ImageView image = (ImageView)viewDiag.findViewById(R.id.picture2);
                image.setImageBitmap(BitmapFactory.decodeFile((String)pictureList.get(i).getImagePath()));
                new AlertDialog.Builder(ShowPicture.this).setView(viewDiag)
                        .setPositiveButton("确定",null).show();
            }
        });
    }

    private void initPictures(){
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        cursor.moveToNext();
        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Picture picture = new Picture(name, imagePath);
            pictureList.add(picture);
            MultipartBody.Builder builder=new MultipartBody.Builder();
            File file=new File(imagePath);
            builder.addFormDataPart(
                    "image",
                    file.getName(),
                    RequestBody.create(file,MediaType.parse("text/x-markdown; charset=utf-8")));
            HttpUtil.sendOkHttpRequestSendMultipart("http://39.106.231.142:9999/PA/doPost", builder, new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "onResponse: " + response);
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String url = "http://39.106.231.142:9999/PA/postFile";

                    OkhttpUtil.okHttpUploadListFile(url, fileList, "files", OkhttpUtil.FILE_TYPE_IMAGE, new CallBackUtil.CallBackString() {
                        @Override
                        public void onFailure(Call call, Exception e) {
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(ShowPicture.this, "Success", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: " + response);
                        }
                    });
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public class Picture{
        private String fileName;
        private String imagePath;

        public Picture(String fileName, String imagePath){
            this.fileName=fileName;
            this.imagePath=imagePath;
        }

        public String getName(){
            return fileName;
        }

        public String getImagePath(){
            return imagePath;
        }
    }

    public class PictureAdapter extends ArrayAdapter<Picture> {
        private int resourceId;
        public PictureAdapter(Context context, int textViewResourceId, List<Picture> objects){
            super(context,textViewResourceId,objects);
            resourceId=textViewResourceId;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Picture picture = getItem(position);
            View view;
            if(convertView==null){
                view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            }else{
                view=convertView;
            }
            TextView pictureName = (TextView)view.findViewById(R.id.picture_filename);
            pictureName.setText(picture.getName());
            return view;

        }
    }

//    private void displayImage(String imagePath) {
//        if (imagePath != null) {
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            picture.setImageBitmap(bitmap);
//        } else {
//            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
//        }
//    }
}

