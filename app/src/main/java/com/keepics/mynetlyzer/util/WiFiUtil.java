package com.keepics.mynetlyzer.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Enumeration;

public class WiFiUtil {
    public static String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }

    public static String ethernetIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }

    public static String getIpAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface )(en.nextElement());
                String name = intf.getName();
                Log.e("IP address",""+name);
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress)(enumIpAddr.nextElement());
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address && name.contains("eth")) {
                        String ipAddress=inetAddress.getHostAddress().toString();
                        Log.e("IP address",""+ipAddress);
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("wifianaylzer", ex.toString());
        }
        return null;
    }

    private static final int VENDOR_NAME_MAX = 50;

    static String cleanVendorName(String name) {
        if (StringUtils.isBlank(name) || name.contains("<") || name.contains(">")) {
            return StringUtils.EMPTY;
        }
        String result = name
                .replaceAll("[^a-zA-Z0-9]", " ")
                .replaceAll(" +", " ")
                .trim()
                .toUpperCase();

        return result.substring(0, Math.min(result.length(), VENDOR_NAME_MAX));
    }
}
