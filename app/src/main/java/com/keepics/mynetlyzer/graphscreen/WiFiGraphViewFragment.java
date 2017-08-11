package com.keepics.mynetlyzer.graphscreen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.keepics.mynetlyzer.AppContext;
import com.keepics.mynetlyzer.MainActivity;
import com.keepics.mynetlyzer.R;

import com.keepics.mynetlyzer.scanner.ReportDataManager;
import com.keepics.mynetlyzer.scanner.WiFiDataCollector;
import com.keepics.mynetlyzer.util.AppSettings;
import com.keepics.mynetlyzer.scanner.BandChannelSet;
import com.keepics.mynetlyzer.scanner.ScannerBand;
import com.keepics.mynetlyzer.scanner.ScannerChannel;
import com.keepics.mynetlyzer.scanner.WiFiChannelAcessPointCount;
import com.keepics.mynetlyzer.scanner.ChannelRating;
import com.keepics.mynetlyzer.scanner.SignalStrength;
import com.keepics.mynetlyzer.scanner.SortBy;
import com.keepics.mynetlyzer.scanner.WiFiNetworkInfo;
import com.keepics.mynetlyzer.scanner.WiFiConnectionInfo;
import com.keepics.mynetlyzer.scanner.ScannerData;
import com.keepics.mynetlyzer.scanner.ScannerDataDetail;
import com.keepics.mynetlyzer.scanner.ScannerSignalData;
import com.keepics.mynetlyzer.scanner.ScannerNotifier;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static java.util.Collections.addAll;


public class WiFiGraphViewFragment extends Fragment implements ScannerNotifier {

    private OnFragmentInteractionListener mListener;
    AppContext appContext;
    private View currentView;
    private static final int VENDOR_LONG_MAX = 25;

    LineChart lineChart;
    ListView listViewNode;
    ArrayList<WiFiGraphViewFragment.Node> listNote;
    ArrayList<BandChannelSet> bandChannelSets;

    private int subBand;
    private int band;
    private boolean lockUpdating = false;

    private com.keepics.mynetlyzer.util.WiFiDataCollector wiFiDataCollector;
    private BandChannelSet currentBandChannel;
    private ChannelRating channelRate;

    private static final int MAX_CHANNELS_TO_DISPLAY = 10;

    Button backbutton;
    Button playPauseButton;
    Button buttonBand1;
    Button buttonBand2;
    Button buttonBand3;
    Button buttonBandSet1;
    Button buttonBandSet2;
    TextView channelRating;

    ColourPool colour_pool;

    public WiFiGraphViewFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bandChannelSets = com.keepics.mynetlyzer.util.WiFiDataCollector.getBandChannelSets();
        currentBandChannel = bandChannelSets.get(0);
        this.wiFiDataCollector = new com.keepics.mynetlyzer.util.WiFiDataCollector();
        appContext = AppContext.INSTANCE;
        appContext.getWiFiDataCollector().register(this);

        colour_pool = new WiFiGraphViewFragment.ColourPool();
        channelRate = new ChannelRating();

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onDestroy() {
        WiFiDataCollector wiFiDataCollector = AppContext.INSTANCE.getWiFiDataCollector();
        wiFiDataCollector.unregister(this);
        super.onDestroy();
    }

    private void refresh() {
        WiFiDataCollector wiFiDataCollector = AppContext.INSTANCE.getWiFiDataCollector();
        wiFiDataCollector.update();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_wi_fi_graph_view, container, false);
        listViewNode = (ListView)currentView.findViewById(R.id.wifi_graph_view_channel_list);

        channelRating = (TextView)currentView.findViewById(R.id.wifi_graph_channel_rating);
        backbutton = (Button)currentView.findViewById(R.id.wifi_graph_back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = MainActivity.getInstance().getSupportFragmentManager();
                if(fragmentManager.getBackStackEntryCount()> 0){
                    fragmentManager.popBackStack();
                }else {
                    MainActivity.getInstance().gotoHomeScreen("Home");
                }
            }
        });
        backbutton.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Drawable drawableRes = getResources().getDrawable(R.drawable.home);
                    backbutton.setBackground(drawableRes);
                }else{
                    Drawable drawableRes = getResources().getDrawable(R.drawable.home_focus);
                    backbutton.setBackground(drawableRes);
                }
            }
        });
        backbutton.requestFocus();

        playPauseButton = (Button)currentView.findViewById(R.id.play_button);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appContext.getWiFiDataCollector().isRunning()){
                    appContext.getWiFiDataCollector().pause();
                }else{
                    appContext.getWiFiDataCollector().resume();
                }
                updatePlayPauseButton();
            }
        });

        playPauseButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(appContext.getWiFiDataCollector().isRunning()){
                        Drawable drawableRes = getResources().getDrawable(R.drawable.pause);
                        playPauseButton.setBackground(drawableRes);
                    }else{
                        Drawable drawableRes = getResources().getDrawable(R.drawable.play);
                        playPauseButton.setBackground(drawableRes);
                    }

                }else{
                    if(appContext.getWiFiDataCollector().isRunning()){
                        Drawable drawableRes = getResources().getDrawable(R.drawable.pause_focus);
                        playPauseButton.setBackground(drawableRes);
                    }else{
                        Drawable drawableRes = getResources().getDrawable(R.drawable.play_focus);
                        playPauseButton.setBackground(drawableRes);
                    }
                }
            }
        });

        buttonBand1 = (Button)currentView.findViewById(R.id.wifi_graph_5g_band1);
        buttonBand1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBandChannel = bandChannelSets.get(1);
                band = 1;
                subBand = 1;
                switchBand(currentBandChannel);
            }
        });

        buttonBand2 = (Button)currentView.findViewById(R.id.wifi_graph_5g_band2);
        buttonBand2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBandChannel = bandChannelSets.get(2);
                band = 1;
                subBand = 2;
                switchBand(currentBandChannel);
            }
        });

        buttonBand3 = (Button)currentView.findViewById(R.id.wifi_graph_5g_band3);
        buttonBand3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBandChannel = bandChannelSets.get(3);
                band = 1;
                subBand = 3;
                switchBand(currentBandChannel);
            }
        });

        buttonBandSet1 = (Button)currentView.findViewById(R.id.wifi_graph_band_set1);
        buttonBandSet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(band == 1){
                    currentBandChannel = bandChannelSets.get(0);
                    band = 0;
                    subBand = 0;
                    switchBand(currentBandChannel);
                }

            }
        });

        buttonBandSet2 = (Button)currentView.findViewById(R.id.wifi_graph_band_set2);
        buttonBandSet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(band == 0){
                    currentBandChannel = bandChannelSets.get(1);
                    band = 1;
                    subBand = 1;
                    switchBand(currentBandChannel);
                }

            }
        });

        channelRating.setTextColor(Color.BLACK);


        switchBand(currentBandChannel);
        prepareChannelSideList();
        updatePlayPauseButton();

        return currentView;
    }

    public void updatePlayPauseButton(){
        if(appContext.getWiFiDataCollector().isRunning()){
            if(playPauseButton.hasFocus()){
                playPauseButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.pause_focus) );
            }else{
                playPauseButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.pause) );
            }
        }else{
            if(playPauseButton.hasFocus()){
                playPauseButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.play_focus) );
            }else{
                playPauseButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.play) );
            }

        }
    }

    public void switchBand(BandChannelSet band){
        setupLineChart(band);
        if(this.band == 0){
            buttonBandSet1.setTextColor(Color.BLUE);
            buttonBandSet2.setTextColor(Color.GRAY);

            buttonBand1.setVisibility(View.INVISIBLE);
            buttonBand2.setVisibility(View.INVISIBLE);
            buttonBand3.setVisibility(View.INVISIBLE);
        }else{
            buttonBandSet1.setTextColor(Color.GRAY);
            buttonBandSet2.setTextColor(Color.BLUE);

            buttonBand1.setVisibility(View.VISIBLE);
            buttonBand2.setVisibility(View.VISIBLE);
            buttonBand3.setVisibility(View.VISIBLE);
            buttonBand1.setTextColor(subBand==1?Color.BLUE:Color.GRAY);
            buttonBand2.setTextColor(subBand==2?Color.BLUE:Color.GRAY);
            buttonBand3.setTextColor(subBand==3?Color.BLUE:Color.GRAY);
        }

        ReportDataManager.band = this.band;
    }

    public void setupLineChart(BandChannelSet band){
        lineChart = (LineChart) currentView.findViewById(R.id.line_chart);
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        Description des = new Description();
        des.setText("Channels");
        lineChart.setDescription(des);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(true);
        lineChart.setVisibleXRangeMinimum(1f);//lineChart.setVisibleXRangeMaximum(5f);
        lineChart.setVisibleYRangeMaximum(100f, YAxis.AxisDependency.LEFT);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(8f);
        xAxis.setAxisMinimum(band.startChannel - 2);
        xAxis.setAxisMaximum(band.lastChannel);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(band.lastChannel - band.startChannel + 3);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        //xAxis.setTextColor(Color.RED);
        //xAxis.setSpaceMax(40);

        WiFiGraphViewFragment.ChannelValueFormatter formatterX = new WiFiGraphViewFragment.ChannelValueFormatter();
        xAxis.setValueFormatter(formatterX);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawLabels(true);
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(true);
        yAxis.setDrawZeroLine(true);
        yAxis.setAxisMinimum(-100);
        yAxis.setAxisMaximum(-10);
        yAxis.setLabelCount(10);
        yAxis.setTextSize(8f);
        yAxis.setDrawLimitLinesBehindData(true);
        WiFiGraphViewFragment.SignalStrengthValueFormatter formatterY = new WiFiGraphViewFragment.SignalStrengthValueFormatter();
        yAxis.setValueFormatter(formatterY);
    }


    public void updateWiFiGraphDataSet(ArrayList<WiFiGraphViewFragment.Node> series){

//        if (lineChart.getData() != null &&
//                lineChart.getData().getDataSetCount() > 0) {
//
//            for (WiFiGraphViewFragment.Node node:series) {
//                LineDataSet set = (LineDataSet)lineChart.getData().getDataSetByLabel(node.name, true);
//                if(set != null){
//                    set.setValues(node.lineDataSet.getValues());
//                    set.notifyDataSetChanged();
//                }else{
//                    LineData data = lineChart.getData();
//                    data.addDataSet(node.lineDataSet);
//                }
//            }
//
//        } else {
            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
            for (WiFiGraphViewFragment.Node node:series) {
                lineDataSets.add(node.lineDataSet);
                lineChart.setData(new LineData(lineDataSets));
            }
//        }

        lineChart.getData().notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();

    }

    private void prepareChannelSideList(){
        listNote = new ArrayList<WiFiGraphViewFragment.Node>();

        WiFiGraphViewFragment.ChannelItemListAdapter adapter =
                new WiFiGraphViewFragment.ChannelItemListAdapter(
                        getActivity(),
                        listNote);
        listViewNode.setAdapter(adapter);

        listViewNode.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listViewNode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WiFiGraphViewFragment.Node node = listNote.get(i);
            }
        });
    }

    private void updateChannelSideList(ArrayList<WiFiGraphViewFragment.Node> series){
        listNote.clear();

        for (WiFiGraphViewFragment.Node node:series) {
            listNote.add(node);
        }

        ((WiFiGraphViewFragment.ChannelItemListAdapter)(listViewNode.getAdapter())).notifyDataSetChanged();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void update(@NonNull ScannerData scannerData){

        try{

            ScannerDataDetail connection = scannerData.getConnection();
            ScannerSignalData scannerSignalData = connection.getScannerSignalData();
            SignalStrength signalStrength = scannerSignalData.getStrength();
            WiFiNetworkInfo wiFiNetworkInfo = connection.getWiFiNetworkInfo();
            WiFiConnectionInfo wiFiConnectionInfo = wiFiNetworkInfo.getWiFiConnectionInfo();

            AppSettings appSettings = AppContext.INSTANCE.getAppSettings();

            ScannerBand scannerBand;
            if(band == 0){
                scannerBand = ScannerBand.GHZ2;
            }else{
                scannerBand = ScannerBand.GHZ5;
            }

            String countryCode = appSettings.getCountryCode();
            List<ScannerChannel> scannerChannels = scannerBand.getChannels().getAvailableChannels(countryCode);
            addAll(scannerChannels);
            channelRate.setScannerDataDetails(scannerData.getWiFiDetails(scannerBand, SortBy.STRENGTH));

            String  alternativeBandString = ReportDataManager.getInstance().alternativeBand;
            String  bestChannelString = ReportDataManager.getInstance().alternativeChannelString;

            Resources resources = this.getActivity().getResources();
            String prefix = resources.getText(R.string.channel_rating_best).toString();
            if(!alternativeBandString.equalsIgnoreCase("-1")){
                channelRating.setText(prefix + bestChannelString + "," + "N/A");
            }else{
                channelRating.setText(prefix + bestChannelString);
            }

            TextView textLevel = (TextView) currentView.findViewById(R.id.wifi_graph_header_signal_db);
            textLevel.setText(String.format(Locale.ENGLISH, "%ddBm", scannerSignalData.getLevel()));
            textLevel.setTextColor(ContextCompat.getColor(this.getContext(), signalStrength.colorResource()));

            ImageView imageView = (ImageView) currentView.findViewById(R.id.wifi_graph_header_signal_icon);
            imageView.setImageResource(signalStrength.imageResource());
            imageView.setColorFilter(ContextCompat.getColor(this.getContext(), signalStrength.colorResource()));

            ((TextView) currentView.findViewById(R.id.wifi_graph_header_signal_distance))
                    .setText(String.format(Locale.ENGLISH, "%5.1fm", scannerSignalData.getDistance()));

            ((TextView) currentView.findViewById(R.id.wifi_graph_header_ssid))
                    .setText(  connection.getTitle() + "," + wiFiConnectionInfo.getIpAddress()
                            + "," + wiFiConnectionInfo.getLinkSpeed() + WifiInfo.LINK_SPEED_UNITS);

            String channelText = scannerSignalData.getChannelDisplay();
            String frequency = String.format(Locale.ENGLISH, "%d%s",
                    scannerSignalData.getPrimaryFrequency(), ScannerSignalData.FREQUENCY_UNITS);
            String frequencyRange = scannerSignalData.getFrequencyStart() + " - " + scannerSignalData.getFrequencyEnd();
            String vendor = wiFiNetworkInfo.getVendorName();
            String vendorText = vendor.substring(0, Math.min(VENDOR_LONG_MAX, vendor.length()));

            ((TextView) currentView.findViewById(R.id.wifi_graph_header_channel_text))
                    .setText( "Channel" + " "
                            + channelText + ", "
                            + frequency + ", "
                            + frequencyRange + " "
                            + "(" + scannerSignalData.getChannelWidth().getFrequencyWidth() + ScannerSignalData.FREQUENCY_UNITS + ")"
                            + vendorText + "\n" + connection.getCapabilities());

            List<ScannerDataDetail> scannerDataDetails = scannerData.getWiFiDetails(currentBandChannel.scannerBand, SortBy.CHANNEL);//By signalStrength or channel
            Set<ScannerDataDetail> newSeries = wiFiDataCollector.getNewSeries(scannerDataDetails, currentBandChannel.wiFiChannelPair);
            ArrayList<WiFiGraphViewFragment.Node> dataNodes = getListNote(newSeries);

            updateWiFiGraphDataSet(dataNodes);
            updateChannelSideList(dataNodes);
        }catch (Exception ex){
            Log.e("wifianalyzer", ex.getLocalizedMessage());
        }


    }

    ArrayList<WiFiGraphViewFragment.Node> getListNote(Set<ScannerDataDetail> series){
        ArrayList<WiFiGraphViewFragment.Node> ret = new ArrayList<WiFiGraphViewFragment.Node>();

        colour_pool.resetPool();
        int index = 0;
        for (ScannerDataDetail data: series) {
            int channel = data.getScannerSignalData().getCenterWiFiChannel().getChannel();
            int width = data.getScannerSignalData().getChannelWidth().getFrequencyWidth();
            int level = data.getScannerSignalData().getLevel();
            WiFiGraphViewFragment.Node node =
                    new WiFiGraphViewFragment.Node(index, colour_pool.getNextColor(), data.getSSID(),"",channel, level,width);
            ret.add(node);
            index++;
        }

        return ret;
    }


    class Node {
        int color;
        String name;
        String moreDetail;
        int id;
        int centerChannel;
        int level;
        int width;
        ArrayList<Entry> points = new ArrayList<>();
        int baseY = -100; //bottom value
        LineDataSet lineDataSet;


        Node(int num, int color, String name, String description, int channel, int levelValue, int widthValue){
            this.id = num;
            this.name = name;
            this.moreDetail = description;
            this.color = color;
            this.centerChannel = channel;
            this.level = levelValue;
            this.width = widthValue/10;

            points.add(new Entry(channel - width/2 - 1, baseY));
            points.add(new Entry(channel - width/2, level));
            points.add(new Entry(channel + width/2, level));
            points.add(new Entry(channel + width/2 + 1, baseY));

            lineDataSet = new LineDataSet(points,name);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setColor(this.color);
            lineDataSet.setLineWidth(1);
            lineDataSet.setDrawValues(false);

            Drawable drawable_blue = ContextCompat.getDrawable(WiFiGraphViewFragment.this.getActivity(), R.drawable.fade_blue);
            lineDataSet.setFillDrawable(drawable_blue);
        }

    }


    public class ColourPool{
        int startIndex = 0;
        int numOfColor = 16;
        int[] max = new int[numOfColor];

        ColourPool(){
            max[0] = Color.argb(0xff,0xff,0x00,0x00);
            max[1] = Color.argb(0xff,0x00,0xff,0x00);
            max[2] = Color.argb(0xff,0x00,0x00,0xff);

            max[3] = Color.argb(0xff,0x80,0x00,0x00);
            max[4] = Color.argb(0xff,0x00,0x80,0x00);
            max[5] = Color.argb(0xff,0x00,0x00,0x80);

            max[6] = Color.argb(0xff,0xff,0xff,0x00);
            max[7] = Color.argb(0xff,0x00,0xff,0xff);
            max[8] = Color.argb(0xff,0xff,0x00,0xff);

            max[9] = Color.argb(0xff,0x80,0x80,0x00);
            max[10] = Color.argb(0xff,0x80,0x00,0x80);
            max[11] = Color.argb(0xff,0x00,0x80,0x80);

            max[12] = Color.argb(0xff,0x80,0xff,0x00);
            max[13] = Color.argb(0xff,0x80,0x00,0xff);
            max[14] = Color.argb(0xff,0xff,0x80,0x00);
            max[15] = Color.argb(0xff,0x00,0xff,0x80);
        }

        public void resetPool(){
            startIndex = 0;
        }

        public int getNextColor(){
            startIndex++;
            if(startIndex >= numOfColor){
                startIndex = 0;
            }
            int value = max[startIndex];
            return value;
        }
    }

    public class SignalStrengthValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public SignalStrengthValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mFormat.format(value) + " dB";
        }

        /** this is only needed if numbers are returned, else return 0 */
        /*@Override
        public int getDecimalDigits() { return 1; }*/
    }

    public class ChannelValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public ChannelValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if(value > 0){
                return mFormat.format(value);
            }else{
                return "";
            }
        }
    }

    public class ChannelItemListAdapter extends ArrayAdapter {
        public ChannelItemListAdapter(Context context, ArrayList<WiFiGraphViewFragment.Node> list) {
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WiFiGraphViewFragment.Node itemNode = (WiFiGraphViewFragment.Node)getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.wifi_graph_channel_list_content, parent, false);
            }

            TextView itemNum = (TextView) convertView.findViewById(R.id.wifi_graph_channel_list_item_number);
            TextView itemName = (TextView) convertView.findViewById(R.id.wifi_graph_channel_list_item_name);

            itemNum.setText(Integer.toString(itemNode.id + 1));
            itemNum.setBackgroundColor(itemNode.color);

            itemName.setText(itemNode.name);
            itemName.setTextColor(Color.WHITE);

            return convertView;
        }

    }


    void bestChannels(@NonNull ScannerBand scannerBand, @NonNull List<ScannerChannel> scannerChannels) {
        List<WiFiChannelAcessPointCount> wiFiChannelAcessPointCounts = channelRate.getBestChannels(scannerChannels);
        int channelCount = 0;
        StringBuilder result = new StringBuilder();
        Resources resources = getContext().getResources();
        String prefix = resources.getText(R.string.channel_rating_best).toString();
        result.append(prefix);
        for (WiFiChannelAcessPointCount wiFiChannelAcessPointCount : wiFiChannelAcessPointCounts) {
            if (channelCount > MAX_CHANNELS_TO_DISPLAY) {
                result.append("...");
                break;
            }
            if (result.length() > prefix.length()) {
                result.append(", ");
            }
            result.append(wiFiChannelAcessPointCount.getScannerChannel().getChannel());
            channelCount++;
        }
        if (channelCount > 0) {
            channelRating.setText(result.toString());
            channelRating.setTextColor(Color.BLACK);
        } else {

            StringBuilder message = new StringBuilder(prefix + resources.getText(R.string.channel_rating_best_none));
            if (ScannerBand.GHZ2.equals(scannerBand)) {
                message.append(resources.getText(R.string.channel_rating_best_alternative));
                message.append(" ");
                message.append(ScannerBand.GHZ5.getBand());
            }
            channelRating.setText(message);
            channelRating.setTextColor(Color.RED);
        }
    }
}
