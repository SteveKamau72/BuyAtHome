package com.softtech.stevekamau.buyathome;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.activites.AdminPanel;
import com.softtech.stevekamau.buyathome.activites.Cart;
import com.softtech.stevekamau.buyathome.activites.Login;
import com.softtech.stevekamau.buyathome.activites.WishList;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.databaseHandlers.WishDB;
import com.softtech.stevekamau.buyathome.helper.SQLiteHandler;
import com.softtech.stevekamau.buyathome.helper.SessionManager;
import com.softtech.stevekamau.buyathome.interfaces.UpdateCartCount;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {
    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    CartDB myDb;
    WishDB favDB;
    View layout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private AdapterClass adapter;
    private RecyclerView recyclerView;
    private TextView txtName;
    private TextView txtEmail;
    private TextView welcome;
    private SQLiteHandler db;
    private SessionManager session;
    private FragmentDrawerListener drawerListener;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    public static List<Information> getData() {
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_home, R.drawable.ic_phone, R.drawable.ic_laptop, R.drawable.ic_phone_accessory, R.drawable.ic_laptop_accessory};
        String[] titles = {"Home", "Phones", "Laptops", "Phone accessories", "Laptop accessories"};

        for (int i = 0; i < titles.length && i < icons.length; i++) {
            Information current = new Information();
            current.iconId = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();

    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        layout.findViewById(R.id.linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Cart.class);
                startActivity(i);
            }
        });
        layout.findViewById(R.id.linear2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), WishList.class);
                startActivity(i);
            }
        });

        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm2);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new AdapterClass(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        myDb = new CartDB(getActivity());
        favDB = new WishDB(getActivity());

        TextView txt = (TextView) layout.findViewById(R.id.num);
        TextView txt2 = (TextView) layout.findViewById(R.id.num2);
        int profile_counts = myDb.numberOfRows();
        myDb.close();
        if (profile_counts > 0) {
            txt.setText(String.valueOf(profile_counts));
        } else {
            txt.setVisibility(View.INVISIBLE);
        }
        int profile_counts2 = favDB.numberOfRows();
        favDB.close();
        if (profile_counts2 > 0) {
            txt2.setText(String.valueOf(profile_counts2));
        } else {
            txt2.setVisibility(View.INVISIBLE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AdapterClass.selected_item = position;
                recyclerView.getAdapter().notifyDataSetChanged();
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        txtName = (TextView) layout.findViewById(R.id.textView10);
        txtEmail = (TextView) layout.findViewById(R.id.textView11);
        welcome = (TextView) layout.findViewById(R.id.textView12);
        db = new SQLiteHandler(getActivity());
        if (!getstatus()) {
            Intent intent = new Intent(getContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("message", "Successfully logged out");
            startActivity(intent);
        }

        txtName.setText(getuser_name());
        txtEmail.setText(getuser_email());

        LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.linear3);
        if (getuser_name().equalsIgnoreCase("Admin")) {
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), AdminPanel.class));
                }
            });
        } else {

            linearLayout.setVisibility(View.INVISIBLE);
        }
        return layout;
    }

    String getuser_name() {
        SharedPreferences spref2 = getContext().getSharedPreferences("ACCOUNT", getContext().MODE_PRIVATE);
        String user_name = spref2.getString("name", "");
        return user_name;
    }

    Boolean getstatus() {
        SharedPreferences spref2 = getContext().getSharedPreferences("ACCOUNT", getContext().MODE_PRIVATE);
        Boolean islogged = spref2.getBoolean("islogged", false);
        return islogged;
    }

    String getuser_email() {
        SharedPreferences spref2 = getContext().getSharedPreferences("ACCOUNT", getContext().MODE_PRIVATE);
        String user_email = spref2.getString("email", "");
        return user_email;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateCartCount event) {
        TextView txt = (TextView) layout.findViewById(R.id.num);
        int profile_counts = myDb.numberOfRows();
        myDb.close();
        if (profile_counts > 0) {
            txt.setText(String.valueOf(profile_counts));
        } else {
            txt.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView txt = (TextView) layout.findViewById(R.id.num);
        int profile_counts = myDb.numberOfRows();
        myDb.close();
        if (profile_counts > 0) {
            Log.d("on_resume_frag", "true"+String.valueOf(profile_counts));
            txt.setText(String.valueOf(profile_counts));
        } else {
            txt.setVisibility(View.INVISIBLE);
            Log.d("on_resume_frag", "true1");
        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }
}
