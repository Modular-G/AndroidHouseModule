package com.modular.mancha.housemodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.support.v7.internal.widget.AdapterViewCompat.*;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     */
    private CharSequence mTitle;
    private String ip ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));



    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    PlaceholderFragment ph = new PlaceholderFragment();
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        SharedPreferences sharedPref = getSharedPreferences("IPs", MODE_PRIVATE);
        ip = sharedPref.getString("host_ip","http://www.UPSSS.com");

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, ph.newInstance(position+1,ip))
                .commit();

    }


    public String getIP(){
        return ip;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        ph.goToAdmin();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public String getIp(){
        return ip;
    }


    /**
     * FRAGMENT DE WEBVIEW
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static String ip_fragment;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int position, String ip) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);
            ip_fragment = ip;
            return fragment;
        }

        public PlaceholderFragment() {
        }


        public void reload_profile(){
            WebView webprofile = (WebView) getActivity().findViewById(R.id.webview_profile);
            webprofile.getSettings().setJavaScriptEnabled(true);
            SharedPreferences sharedPref = getActivity().getSharedPreferences("IPs", getActivity().MODE_PRIVATE);
            String ip = sharedPref.getString("host_ip","http://www.UPSSS.com");
            webprofile.loadUrl(ip+"/getcurrentuser");
        }


        WebView webview;
        DrawerLayout drawerLayout;
        ListView options_view;
        Button alarm_button;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            final ArrayList options = new ArrayList();
            options.add(getActivity().getString(R.string.settings));
            options.add(getActivity().getString(R.string.rules));
            options.add(getActivity().getString(R.string.logout));
            options_view = (ListView) getActivity().findViewById(R.id.options_listview);
            drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1, options);
            options_view.setAdapter(adapter);

            options_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            if (options_view.getItemAtPosition(0).equals(getActivity().getString(R.string.settings))) {
                                webview.loadUrl(ip_fragment + "/admin");
                                options.set(0, getActivity().getString(R.string.home));
                                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, options);
                                options_view.setAdapter(adapter);
                            } else {
                                webview.loadUrl(ip_fragment);
                                options.set(0, getActivity().getString(R.string.settings));
                                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, options);
                                options_view.setAdapter(adapter);
                            }

                            break;
                        case 1:
                            webview.loadUrl(ip_fragment + "/autoluz");
                            options.set(0, getActivity().getString(R.string.home));
                            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, options);
                            options_view.setAdapter(adapter);
                            break;
                        case 2:
                            webview.loadUrl(ip_fragment + "/logout");
                            break;
                        default:
                            break;
                    }

                    drawerLayout.closeDrawers();
                }
            });

            options_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    intent.putExtra("ip", ip_fragment);
                    startActivityForResult(intent, 1);
                    drawerLayout.closeDrawers();
                    return true;
                }

            });





            //WEBVIEW PRINCIPAL

            webview = (WebView) rootView.findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadUrl(ip_fragment);
            webview.setWebViewClient(new WebViewClient() {
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progress);
                    pb.setVisibility(view.VISIBLE);
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progress);
                    pb.setVisibility(view.INVISIBLE);
                    reload_profile();
                }

                public void onPageFinished(WebView view, String url) {
                    ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progress);
                    pb.setVisibility(view.INVISIBLE);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    ErrorFragment error = new ErrorFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, error.newInstance(description, ip_fragment))
                            .commit();
                }
            });


            //ALARMA LISTVIEW
            alarm_button = (Button) getActivity().findViewById(R.id.alarm_button);

            alarm_button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    webview.loadUrl("javascript:(function(){document.getElementById('alarmita').click();})()");

                }
            });

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            ip_fragment = data.getStringExtra("ip");
            Context context = getActivity();
            SharedPreferences sharedPref = context.getSharedPreferences("IPs", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("host_ip", ip_fragment);
            editor.commit();
            webview.loadUrl(ip_fragment);
        }

        public void goToAdmin() {
            webview.loadUrl(ip_fragment + "/admin");
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }



    public static class ErrorFragment extends Fragment {

        private static String descripcion;
        private static String ip_fragment;
        public static ErrorFragment newInstance(String desc, String ip) {
            ErrorFragment fragment = new ErrorFragment();
            descripcion = desc;
            ip_fragment = ip;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_error, container, false);
            TextView txb = (TextView)rootView.findViewById(R.id.txb_error);
            txb.setText(descripcion);
            ListView options_view = (ListView) getActivity().findViewById(R.id.options_listview);
            options_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    intent.putExtra("ip", ip_fragment);
                    startActivityForResult(intent, 1);
                    return true;
                }

            });

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            ip_fragment =  data.getStringExtra("ip");
            Context context = getActivity();
            SharedPreferences sharedPref = context.getSharedPreferences("IPs", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("host_ip", ip_fragment);
            editor.commit();
            PlaceholderFragment ph = new PlaceholderFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ph.newInstance(1,ip_fragment))
                    .commit();
        }

    }
}
