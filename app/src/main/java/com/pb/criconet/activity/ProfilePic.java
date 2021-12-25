package com.pb.criconet.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.ImageFilePath;
import com.pb.criconet.Utills.SessionManager;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;

/**
 * Created by user1 on 3/23/2016.
 */
public class ProfilePic extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1, SELECT_FILE = 2;
    ImageView imageView1;
    FileBody imagefile;
    String screentype = "";
    SharedPreferences prefs;
    private AQuery aQuery;
    private String selectedImagePath = "";

    public static Bitmap rotateImage(Bitmap bmp, String imageUrl) {
        if (bmp != null) {
            ExifInterface ei;
            int orientation = 0;
            try {
                ei = new ExifInterface(imageUrl);
                orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
            }

            int bmpWidth = bmp.getWidth();
            int bmpHeight = bmp.getHeight();
            Matrix matrix = new Matrix();
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;
                default:
                    break;
                // etc.
            }

            Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmpWidth,
                    bmpHeight, matrix, true);
            return resizedBitmap;
        } else {
            return bmp;
        }
    }

    public static void rotateImageNew(String imageUrl) {

        ExifInterface ei;
        int orientation = 0;
        try {
            ei = new ExifInterface(imageUrl);
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }


        Matrix matrix = new Matrix();
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;

            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            default:
                break;
            // etc.
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_pic);

        if (!Global.isOnline(ProfilePic.this)) {
            Global.showDialog(ProfilePic.this);
            return;
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(ProfilePic.this);
        aQuery = new AQuery(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbartext);
        toolbartext.setText(R.string.upload_picture);

//        screentype = getIntent().getStringExtra("user_type");

        imageView1 = (ImageView) findViewById(R.id.imageView1);


        aQuery.id(R.id.btngalary).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
            }
        });

        aQuery.id(R.id.camera).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        aQuery.id(R.id.tviSkip).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (SessionManager.get_select_type(prefs).equals("M")) {
//                    Intent intent = new Intent(ProfilePic.this, SignupMerchantDetails.class);
//                    startActivity(intent);
//                    finish();
//                } else {
                Intent in = new Intent(ProfilePic.this, MainActivity.class);
                startActivity(in);
                finish();
//                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).contains("my_image")) {
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove("my_image").commit();
        }
        Intent in = new Intent(ProfilePic.this, MainActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                selectedImagePath = String.valueOf(destination);
                try {
                    Glide.with(ProfilePic.this).load(selectedImagePath).into(imageView1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                Bitmap bm = null;
                if (data != null) {
                    try {
                        Uri selectedImageUri = data.getData();
                        selectedImagePath = ImageFilePath.getPath(getBaseContext(), selectedImageUri);
                        try {
                            Glide.with(ProfilePic.this).load(selectedImagePath).into(imageView1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("my_image", selectedImagePath).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            new MyAsyncTask().execute();
        }
    }

    private String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }


    //RAhul Singh Edited

    public Bitmap decodeFile(String path) {//you can provide file path here
        int orientation;
        try {
            if (path == null) {
                return null;
            }
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 0;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bm = BitmapFactory.decodeFile(path, o2);
            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);

            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Log.e("ExifInteface .........", "rotation =" + orientation);

//          exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

            Log.e("orientation", "" + orientation);
            Matrix m = new Matrix();

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
//              m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                // if(m.preRotate(90)){
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            }
            return bitmap;
        } catch (Exception e) {
            return null;
        }

    }

    private class MyAsyncTask extends AsyncTask<Void, String, String> {
        private final ProgressDialog dialog = new ProgressDialog(ProfilePic.this);
        String response = "";
        StringBuilder s = new StringBuilder();

        @Override
        protected void onPreExecute() {

            // dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.show();
            dialog.setCancelable(false);
//            dialog.setContentView(R.layout.my_pb);
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                selectedImagePath = PreferenceManager.getDefaultSharedPreferences(ProfilePic.this).getString("my_image", "");
                imagefile = new FileBody(new File(selectedImagePath));

                URL url = new URL(Global.URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setDoInput(true);
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                entity.addPart("action", new StringBody("changeProfilePic"));
                entity.addPart("userId", new StringBody(PreferenceManager.getDefaultSharedPreferences(ProfilePic.this).getString("user_id", "0")));
                entity.addPart("userType", new StringBody(getIntent().getStringExtra("user_type")));

                if (selectedImagePath.length() > 1) {
                    entity.addPart("image", imagefile);
                }
                connection.addRequestProperty("Content-length", entity.getContentLength() + "");
                connection.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());
                OutputStream os = connection.getOutputStream();
                entity.writeTo(connection.getOutputStream());
                os.close();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    s = new StringBuilder(readStream(connection.getInputStream()));
                    return s.toString();
                }


            } catch (Exception e) {
                e.printStackTrace();
                s = new StringBuilder(e.toString());
            }

            return s.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            this.dialog.dismiss();

            if (result != "" || result != null) {

                try {
                    JSONObject json = new JSONObject(result);
                    Log.e("response    ", "" + json);
                    if (json.has("status")) {
                        if (json.getString("status").equals("Success")) {
                            JSONObject response = json.getJSONObject("response");

                            SessionManager.save_image(PreferenceManager.getDefaultSharedPreferences(ProfilePic.this), response.getString("profileImage"));
//                            if (SessionManager.get_select_type(prefs).equals("M")) {
//                                Intent intent = new Intent(ProfilePic.this, SignupMerchantDetails.class);
//                                startActivity(intent);
//                            } else {
                            Intent in = new Intent(ProfilePic.this, MainActivity.class);
                            in.putExtra("intent", "success");
                            startActivity(in);
                            finish();
//                            }

                        }
                    } else {
                        Global.msgDialog(ProfilePic.this, json.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Global.msgDialog(ProfilePic.this, getResources().getString(R.string.error_server));
            }
        }
    }


}
