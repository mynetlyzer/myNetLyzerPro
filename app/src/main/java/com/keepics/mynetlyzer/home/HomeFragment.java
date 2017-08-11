package com.keepics.mynetlyzer.home;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.keepics.mynetlyzer.MainActivity;
import com.keepics.mynetlyzer.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    ListView listViewNode;
    ArrayList<HomeFragment.Node> listNote;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public  void onResume(){
        super.onResume();
        listViewNode.requestFocus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listViewNode = (ListView)view.findViewById(R.id.home_fragment_item_list);
        listViewNode.requestFocus();

        return view;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Resources resources = this.getResources();
        listNote = new ArrayList<HomeFragment.Node>();

        HomeFragment.Node currentWiFiNode = new HomeFragment.Node(R.drawable.wifi_analyze, 0,
                resources.getString(R.string.action_current_wifi), resources.getString(R.string.action_current_wifi_description));

        HomeFragment.Node networkSpeedTestNode = new HomeFragment.Node(R.drawable.speed_test, 1,
                resources.getString(R.string.action_network_speed_test), resources.getString(R.string.action_network_speed_test_description));

        HomeFragment.Node networkScanNode = new HomeFragment.Node(R.drawable.device_scan, 2,
                resources.getString(R.string.action_network_scan), resources.getString(R.string.action_network_scan_description));

        HomeFragment.Node supportNode = new HomeFragment.Node(R.drawable.support, 3,
                resources.getString(R.string.action_support), resources.getString(R.string.action_support_description));

        HomeFragment.Node settingsNode = new HomeFragment.Node(R.drawable.settings, 4,
                resources.getString(R.string.action_settings), resources.getString(R.string.action_settings_description));

        HomeFragment.Node aboutNode = new HomeFragment.Node(R.drawable.info, 5,
                resources.getString(R.string.action_about), resources.getString(R.string.action_about_description));

        listNote.add(currentWiFiNode);
        listNote.add(networkSpeedTestNode);
        listNote.add(networkScanNode);
        listNote.add(supportNode);
        listNote.add(settingsNode);
        listNote.add(aboutNode);

        HomeFragment.MenuItemListAdapter adapter =
                new HomeFragment.MenuItemListAdapter(
                        getActivity(),
                        listNote);
        listViewNode.setAdapter(adapter);

        listViewNode.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listViewNode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeFragment.Node node = listNote.get(i);
                if(node.num == 0){
                    MainActivity.getInstance().gotoWifiGraphScreen("Current WiFi");
                }else if(node.num == 1){
                    //leave it empty
                }else if(node.num == 2){
                    //leave it empty
                }else if(node.num == 4){
                    MainActivity.getInstance().gotoSettingsScreen();
                }else if(node.num == 5){
                    MainActivity.getInstance().gotoAboutScreen();
                }else if(node.num == 3){
                    //leave it empty
                }
            }
        });

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void onHiddenChanged (boolean hidden){
        Log.e("","");
    }


    class Node {
        int iconId;
        int num;
        String itemName;
        String itemDescription;

        Node(int ico, int num, String name, String description){
            this.iconId = ico;
            this.num = num;
            this.itemName = name;
            this.itemDescription = description;
        }
    }

    public class MenuItemListAdapter extends ArrayAdapter {
        public MenuItemListAdapter(Context context, ArrayList<HomeFragment.Node> list) {
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HomeFragment.Node itemNode = (HomeFragment.Node)getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_content_list_item, parent, false);
            }

            TextView itemName = (TextView) convertView.findViewById(R.id.home_content_list_name);
            TextView itemDescription = (TextView) convertView.findViewById(R.id.home_content_list_description);
            ImageView icon = (ImageView) convertView.findViewById(R.id.home_content_list_item_icon);

            itemName.setText(itemNode.itemName);
            itemDescription.setText(itemNode.itemDescription);
            icon.setImageDrawable(HomeFragment.this.getContext().getDrawable(itemNode.iconId));

            return convertView;
        }

    }
}
