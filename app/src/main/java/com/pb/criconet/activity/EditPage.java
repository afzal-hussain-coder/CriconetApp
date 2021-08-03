package com.pb.criconet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MultipartRequest;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.PageModel;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;



public class EditPage extends AppCompatActivity {
    private static final int CAMERA_REQUESTid = 2015;
    private static final int PICK_IMAGEid = 100;
    private SharedPreferences prefs;
    private String email_String, name_String, address_String, company_string, phone_string;
    private EditText edttxt_desc, edttxt_name, edttxt_address, edttxt_company, edttxt_phone;
    private ImageView profile_image, cover_img, imageView, del_img;
    private RequestQueue queue;
    //    private ProgressWheel progress_wheel;
    private Button btn_login;
    private Cursor cursor;
    private int columnindex, i;
    private Uri URIid = null;
    private Uri selectedImageid, mCapturedImageURIid;
    private String file_pathid = "", image_pathid = "";
    private String imagepath = "";
    private String img_type = "";

    private PageModel userData;
    private ProgressDialog progress;
    private String page_id = "";
    private SlideUp slideUp;
    private View dim, rootViewPost;
    private View slideView;
    private TextView tv_camera;
    private TextView tvGallery;
    private TextView tvCancel;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_page);

        Toolbar toolbar = (Toolbar) EditPage.this.findViewById(R.id.toolbar);
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

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(R.string.update_page);

        prefs = PreferenceManager.getDefaultSharedPreferences(EditPage.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            page_id = bundle.getString("page_id");
        }


        queue = Volley.newRequestQueue(EditPage.this);
//        progress_wheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
        progress = new ProgressDialog(EditPage.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        edttxt_name = (EditText) findViewById(R.id.edttxt_fname);
        edttxt_desc = (EditText) findViewById(R.id.edttxt_desc);
        edttxt_address = (EditText) findViewById(R.id.edttxt_address);
        edttxt_company = (EditText) findViewById(R.id.edttxt_company);
        edttxt_phone = (EditText) findViewById(R.id.edttxt_phone);
        profile_image = (ImageView) findViewById(R.id.profile_pic);
        cover_img = (ImageView) findViewById(R.id.cover_img);
        rootViewPost = findViewById(R.id.root_view);
        slideView = findViewById(R.id.slideView);
        dim = findViewById(R.id.dim);
        tv_camera=findViewById(R.id.tv_camera);
        tvGallery=findViewById(R.id.tvGallery);
        tvCancel=findViewById(R.id.tvCancel);
        slideUp = new SlideUpBuilder(slideView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                        if (percent < 100) {
                            // slideUp started showing

                        }
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();
        tvCancel.setOnClickListener(v -> {
            slideUp.hide();
        });

//        imageView = (ImageView) findViewById(R.id.imageView);
        del_img = (ImageView) findViewById(R.id.del_img);
        btn_login = (Button) findViewById(R.id.btn_login);

        getPageDetails();

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_type = "profile";
                selectImagee();
            }
        });
        cover_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_type = "cover";
                selectImagee();
            }
        });
        del_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditPage.this);
                alertDialog.setTitle("");
                alertDialog.setMessage("Do you really want to Delete this Page?");
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deletePage();
                            }
                        });
                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMethod();
            }
        });
    }
    private void selectImagee() {
        slideUp.show();
        tv_camera.setOnClickListener(v -> {
            slideUp.hide();
            openCameraid();
        });
        tvGallery.setOnClickListener(v -> {
            slideUp.hide();
            openGalleryid();
        });
        tvCancel.setOnClickListener(v -> {
            slideUp.hide();
        });
    }


    private void checkMethod() {

        name_String = edttxt_name.getText().toString().trim();
        email_String = edttxt_desc.getText().toString().trim();
        company_string = edttxt_company.getText().toString().trim();
        address_String = edttxt_address.getText().toString().trim();
        phone_string = edttxt_phone.getText().toString().trim();

        if (!Global.validateLength(name_String, 3)) {
            Toast.makeText(EditPage.this, "Enter Page Name", Toast.LENGTH_SHORT).show();
        } else if (!Global.validateLength(email_String, 3)) {
            Toast.makeText(EditPage.this, "Enter Page Description", Toast.LENGTH_SHORT).show();
        } else if (!Global.validateLength(company_string, 3)) {
            Toast.makeText(EditPage.this, "Enter Page Company", Toast.LENGTH_SHORT).show();
        } else if (!Global.validateLength(address_String, 3)) {
            Toast.makeText(EditPage.this, "Enter Page Address", Toast.LENGTH_SHORT).show();
//        } else if (!Global.validateLength(phone_string, 9)) {
//            Toast.makeText(EditPage.this, "Enter Page Phone", Toast.LENGTH_SHORT).show();
        } else {
            if (Global.isOnline(EditPage.this)) {
                editPageTask();
            } else {
                Toast.makeText(EditPage.this, R.string.no_internet, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(EditPage.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    openCameraid();
                } else if (items[item].equals("Gallery")) {
                    openGalleryid();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCameraid() {

        try {
            String fileName = "profile.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURIid = EditPage.this.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURIid);
            startActivityForResult(intent, CAMERA_REQUESTid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGalleryid() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGEid);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGEid) {
            try {
                selectedImageid = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                cursor = EditPage.this.getContentResolver().query(selectedImageid, filePathColumn, null, null, null);
                cursor.moveToFirst();
                columnindex = cursor.getColumnIndex(filePathColumn[0]);
                file_pathid = cursor.getString(columnindex);
                // Log.e("Attachment Path:", attachmentFile);
                URIid = Uri.parse("file://" + file_pathid);
                imagepath = file_pathid;

                cursor.close();

                if (resultCode == 0) {
//                    dialog_camera.dismiss();
                } else {
//                    dialog_camera.dismiss();
                    System.out.println("cccccccc   " + imagepath);
                    if (img_type.equals("profile")) {
                        profile_image.setImageURI(Uri.parse(imagepath));
                    } else {
                        cover_img.setImageURI(Uri.parse(imagepath));
                    }
                    updateImageTask(imagepath);
//                    try {
//                        Glide.with(EditProfile.this).load(Uri.parse(imagepath)).into(profile_image);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    textViewidproof.setText(image_pathid);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAMERA_REQUESTid) {
            try {
                String[] projection = {MediaStore.Images.Media.DATA};
                @SuppressWarnings("deprecation")
                Cursor cursor = EditPage.this.managedQuery(mCapturedImageURIid, projection, null, null, null);
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String capturedImageFilePath = cursor.getString(column_index_data);
                imagepath = capturedImageFilePath;

                if (resultCode == 0) {
//                    dialog_camera.dismiss();
                } else {
//                    dialog_camera.dismiss();
//                    profile_image.setImageURI(Uri.parse(imagepath));
                    if (img_type.equals("profile")) {
                        Glide.with(EditPage.this).load(imagepath).into(profile_image);
                    } else {
                        Glide.with(EditPage.this).load(imagepath).into(cover_img);
                    }
//                    textViewidproof.setText(image_pathid);

                    System.out.println("cccccccc   " + imagepath);
                    updateImageTask(imagepath);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = EditPage.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void getPageDetails() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_page_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                JSONObject object = jsonObject.getJSONObject("page_data");

                                userData = PageModel.fromJson(object);

                                setData();

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(EditPage.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(EditPage.this, "Error in server");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.dismiss();
                        Global.msgDialog(EditPage.this, "Error from server");
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("page_profile_id", page_id);
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void setData() {

        edttxt_name.setText(userData.getPage_title());
        edttxt_desc.setText(userData.getPage_description());
        edttxt_company.setText(userData.getCompany());
        edttxt_address.setText(userData.getAddress());
        edttxt_phone.setText(userData.getPhone());
        Glide.with(EditPage.this).load(userData.getAvatar()).into(profile_image);
        Glide.with(EditPage.this).load(userData.getCover()).into(cover_img);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.del_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.del:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditPage.this);
                alertDialog.setTitle("");
                alertDialog.setMessage("Do you really want to Delete this Page?");
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deletePage();
                            }
                        });
                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);

    }

    private void deletePage() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "delete_page",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                finish();
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(EditPage.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(EditPage.this, "Error in server");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.dismiss();
                        Global.msgDialog(EditPage.this, "Error from server");
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("page_id", page_id);
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    private void editPageTask() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "update_page",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                Global.msgDialog(EditPage.this, "Page Updated Successfully");
                                getPageDetails();
//                                JSONObject object = jsonObject.getJSONObject("user_data");
//
//                                userData = UserData.fromJson(object);
//                                SessionManager.save_user_id(prefs, userData.getUser_id());
//                                SessionManager.save_name(prefs, userData.getUsername());
//                                SessionManager.save_emailid(prefs, userData.getEmail());
//                                SessionManager.save_sex(prefs, userData.getGender());
//                                SessionManager.save_address(prefs, userData.getAddress());
//                                SessionManager.save_image(prefs, userData.getAvatar());

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(EditPage.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(EditPage.this, "Error in server");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.dismiss();
                        Global.msgDialog(EditPage.this, "Error from server");
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "user_id:1735
//                page_id:
//                s:4d9076ab6b1b5254aa8de046c65fd63a5908ee22134451b41ee035cc8eaa0aa8d1de0443860666293f78fa1cdb0e2fda88c2a935950ffdf1
//                page_name:create news
//                page_title:pageeae
//                phone:7532866377
//                page_description:Page description
//                website:https://mywebsite.com
//                company:Criconet online
//                address:Gurgaon
//                facebook:https://facebook.com
//                twitter:https://twitter.in
//                linkedin:https:.//linkedin.com
//                instagram:https://instagram
//                youtube:https://youtube"

                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("page_id", page_id);
                //                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));

                param.put("page_title", edttxt_name.getText().toString());
                param.put("page_description", edttxt_desc.getText().toString());
                param.put("company", edttxt_company.getText().toString());
                param.put("address", edttxt_address.getText().toString());
                param.put("phone", edttxt_phone.getText().toString().trim());

                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    public void updateImageTask(String path) {
        progress.show();
        try {
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
            entity.addPart("page_id", new StringBody(page_id));
            if (!(path.equals("") || path == null)) {
                File file = new File(path);
                FileBody fileBody = new FileBody(file);
                if (img_type.equals("profile")) {
//                    entity.addPart("image_type", new StringBody("avatar"));
                    entity.addPart("avatar", fileBody);
                } else {
//                    entity.addPart("image_type", new StringBody("cover"));
                    entity.addPart("cover", fileBody);
                }
            }
            MultipartRequest req = new MultipartRequest(Global.URL + "update_page_image", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        progress.dismiss();
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                            getPageDetails();
//                            Global.msgDialog(EditProfile.this, "Profile Saved Successfully");
//                            JSONObject object = jsonObject.getJSONObject("user_data");

//                            userData = UserData.fromJson(object);
//                            SessionManager.save_user_id(prefs, userData.getUser_id());
//                            SessionManager.save_name(prefs, userData.getUsername());
//                            SessionManager.save_emailid(prefs, userData.getEmail());
//                            SessionManager.save_sex(prefs, userData.getGender());
//                            SessionManager.save_address(prefs, userData.getAddress());
//                            SessionManager.save_image(prefs, userData.getAvatar());

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Global.msgDialog(EditPage.this, jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(EditPage.this, "Error in server");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    error.printStackTrace();
                }
            }, entity);

            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            queue.add(req);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
