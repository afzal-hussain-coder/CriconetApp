//package com.pb.criconet;
//
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.content.ContextCompat;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.RetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.material.navigation.NavigationView;
//import com.pb.criconet.Utills.Global;
//import com.pb.criconet.Utills.MytextviewBold;
//import com.pb.criconet.Utills.SessionManager;
//import com.pb.criconet.adapters.List_Adapter;
//import com.pb.criconet.fragments.WebView_Fragment;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import me.riddhimanadib.library.BottomBarHolderActivity;
//import me.riddhimanadib.library.NavigationPage;
//
//
//public class MainActivity2 extends BottomBarHolderActivity {
//
//    FragmentManager manager;
//    ListView list_nav;
//    TextView txt_nav_name, address;
//    ImageView loc;
//    List_Adapter list_adapter;
//
//    ActionBarDrawerToggle toggle;
//    SharedPreferences prefs;
//    RequestQueue queue;
//    ProgressDialog progress;
////    CircleImageView profile_pic;
//
//    @SuppressLint("WrongViewCast")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_home);
////        StatusBarGradient.setStatusBarGradiant(MainActivity.this);
////
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
////        manager = getSupportFragmentManager();
////        getSupportActionBar().setDisplayShowTitleEnabled(false);
////        queue = Volley.newRequestQueue(MainActivity2.this);
////        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity2.this);
////
//////        profile_pic = (CircleImageView) findViewById(R.id.profile_pic);
////        txt_nav_name = (MytextviewBold) findViewById(R.id.txt_nav_name);
////        address = (TextView) findViewById(R.id.address);
////
////        list_nav  = (ListView) findViewById(R.id.list_nav);
////        list_adapter = new List_Adapter(MainActivity2.this);
////        list_nav.setAdapter(list_adapter);
////        list_nav.addFooterView(new View(MainActivity2.this), null, true);
////
////
////        progress = new ProgressDialog(MainActivity2.this);
////        progress.setMessage(getString(R.string.loading));
////        progress.setCancelable(false);
////
////
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
////
////
////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
////
////        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
////            @Override
////            public void onDrawerClosed(View drawerView) {
////                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
////                super.onDrawerClosed(drawerView);
////            }
////
////            @Override
////            public void onDrawerOpened(View drawerView) {
////                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
////                super.onDrawerOpened(drawerView);
////
//////                txt_nav_name.setText(SessionManager.get_name(prefs));
//////                address.setText(SessionManager.get_address(prefs));
//////                if (SessionManager.get_address(prefs).equalsIgnoreCase(""))
//////                    loc.setVisibility(View.GONE);
//////                Glide.with(MainActivity.this).load(SessionManager.get_image(prefs)).into(profile_pic);
//////                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//////                imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
////
//////                list_adapter = new List_Adapter(MainActivity.this);
//////                list_adapter.notifyDataSetChanged();
//////                list_adapter.notifyDataSetChanged();
//////                list_nav.setAdapter(list_adapter);
////            }
////        };
////        drawer.setDrawerListener(toggle);
////        toggle.syncState();
////
////        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
////        txt_nav_name = (TextView) findViewById(R.id.txt_nav_name);
////        address = (TextView) findViewById(R.id.address);
////        loc = (ImageView) findViewById(R.id.loc);
////
//////        txt_nav_name.setText(SessionManager.get_name(prefs));
//////        address.setText(SessionManager.get_address(prefs));
//////        Glide.with(MainActivity.this).load(SessionManager.get_image(prefs)).into(profile_pic);
////
////
////        list_nav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                updatedisplay(position);
////            }
////        });
////
//////        getProfileDetails();
//////        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Dashboard()).commit();
////        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new WebView_Fragment()).commit();
//
//
//        // four navigation pages that would be displayed as four tabs
//        // contains title, icon and fragment instance
//        NavigationPage page1 = new NavigationPage("Home", ContextCompat.getDrawable(this, R.drawable.icon_home), new WebView_Fragment());
//        NavigationPage page2 = new NavigationPage("Watch Live", ContextCompat.getDrawable(this, R.drawable.icon_streaming), new WebView_Fragment());
//        NavigationPage page3 = new NavigationPage("Recent Matches", ContextCompat.getDrawable(this, R.drawable.icon_rec), new WebView_Fragment());
//        NavigationPage page4 = new NavigationPage("setting", ContextCompat.getDrawable(this, R.drawable.icon_settings), new WebView_Fragment());
//
//        // add them in a list
//        List<NavigationPage> navigationPages = new ArrayList<>();
//        navigationPages.add(page1);
//        navigationPages.add(page2);
//        navigationPages.add(page3);
//        navigationPages.add(page4);
//
//        // pass them to super method
//        super.setupBottomBarHolderActivity(navigationPages);
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            if (manager.getBackStackEntryCount() > 0) {
//                super.onBackPressed();
//            } else {
////                super.onBackPressed();
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//                alertDialog.setTitle("");
//                alertDialog.setMessage("Do you really want to Exit?");
//                alertDialog.setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        });
//                alertDialog.setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        });
//                alertDialog.show();
//            }
//        }
//    }
//
//    public void updatedisplay(int position) {
//        Fragment fragment = null;
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        }
//        switch (position) {
//            case 0:
////                fragment = new Dashboard();
////                fragment = new DashboardNew();
//                break;
//            case 1:
////                fragment = new EditProfile();
//                break;
//            case 2:
////                fragment = new ChangePassword();
////                Intent intent = new Intent(MainActivity.this, ChangePassword.class);
////                startActivity(intent);
//                break;
//            case 3:
////                fragment = new Users();
//                break;
//            case 4:
////                fragment = new PhotosVideos();
//                break;
//            case 5:
////                fragment = new Friends();
//                break;
//            case 6:
////                fragment = new PrivateChatList();
//                break;
//            case 7:
////                fragment = new CallsHistory();
//                break;
//            case 8:
////                fragment = new FriendRequest();
//                break;
//            case 9:
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//                alertDialog.setTitle("");
//                alertDialog.setMessage("Do you really want to logout?");
//                alertDialog.setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
////                                logout();
//                                SessionManager.dataclear(prefs);
//                                SessionManager.save_check_agreement(prefs, true);
//                                Intent intent = new Intent(MainActivity2.this, Login.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
//                alertDialog.setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        });
//                alertDialog.show();
//                break;
//        }
//        if (fragment != null) {
//            manager.beginTransaction()
//                    .replace(R.id.frame_container, fragment)
//                    .addToBackStack(null).commit();
//        }
//    }
//
//    public void logout() {
//        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
//        final JSONObject json = new JSONObject();
////        "action:logout
////        user_id:535"
//        try {
//            json.put("action", "logout");
//            json.put("user_id", SessionManager.get_user_id(prefs));
//            Log.e(" data  : ", "" + json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        progress.show();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST, Global.URL, json,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.v("login reponse", "" + response);
////                        {"status":"Success"}
//                        progress.dismiss();
//                        try {
//                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
//                            if (jsonObject.getString("status").equals("Success")) {
//                                SessionManager.dataclear(prefs);
//                                SessionManager.save_check_agreement(prefs, true);
//                                Intent intent = new Intent(MainActivity2.this, Login.class);
//                                startActivity(intent);
//                                finish();
//                            } else if (jsonObject.getString("status").equals("Fail")) {
//                                Global.msgDialog(MainActivity2.this, jsonObject.getString("msg"));
//                            } else {
//                                Global.msgDialog(MainActivity2.this, "Error in server");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                progress.dismiss();
//                Global.msgDialog(MainActivity2.this, "Internet connection is slow");
//            }
//        });
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        queue.add(jsonObjectRequest);
//    }
//
//    public void getProfileDetails() {
//        final JSONObject json = new JSONObject();
//        try {
//// "user_id:129,action:androidotherprofile,profile_id:129"
//            json.put("action", "androidotherprofile");
//            json.put("profile_id", SessionManager.get_user_id(prefs));
//            json.put("user_id", SessionManager.get_user_id(prefs));
//            Log.e("getProfileDetails : ", " data :  " + json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        progress.show();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST, Global.URL, json,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("getProfileDetails res :", "" + response);
//                        progress.dismiss();
//                        try {
//                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
//                            if (jsonObject.getString("status").equals("Success")) {
//
//                                jsonObject2 = jsonObject.getJSONObject("response");
//                                SessionManager.save_user_id(prefs, jsonObject2.getString("id"));
//                                SessionManager.save_name(prefs, jsonObject2.getString("firstName"));
//                                SessionManager.save_emailid(prefs, jsonObject2.getString("emailId"));
//                                SessionManager.save_password(prefs, jsonObject2.getString("password2"));
//                                SessionManager.save_address(prefs, jsonObject2.getString("address"));
//                                SessionManager.save_sex(prefs, jsonObject2.getString("gender"));
//                                SessionManager.save_firebaseId(prefs, jsonObject2.getString("firebaseId"));
//                                SessionManager.save_school(prefs, jsonObject2.getString("school"));
//                                SessionManager.save_studied(prefs, jsonObject2.getString("studied"));
//                                SessionManager.save_employment(prefs, jsonObject2.getString("employment"));
//                                SessionManager.save_Friends(prefs, jsonObject2.getString("Friend"));
//                                SessionManager.save_image(prefs, jsonObject2.getString("image"));
//
//                            } else if (jsonObject.getString("status").equals("Fail")) {
//                                Global.msgDialog(MainActivity2.this, jsonObject.getString("msg"));
//                            } else {
//                                Global.msgDialog(MainActivity2.this, "Error in server");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                progress.dismiss();
//                Global.msgDialog(MainActivity2.this, "Error from server");
//            }
//        });
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        queue.add(jsonObjectRequest);
//    }
//
//
//}
