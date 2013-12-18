package md.varoinform.sequrity;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public enum  MAC {
    instance;

    private String name;
    public String getMacAddressAsString() {
        return StringUtils.bytesToHex(getMacAddress());
    }
    public byte[] getMacAddress() {
        byte[] mac = null;
        String name = getName();
        if (name != null) {
            try {
                mac = getMacByName();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        if (mac == null){
            try {
                mac = findMac();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        return mac;
    }


    private byte[] findMac() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface anInterface = networkInterfaces.nextElement();
            byte[] mac = anInterface.getHardwareAddress();
            if (mac != null) {
                setName(anInterface.getName());
                return mac;
            }
        }
        return null;
    }

    private String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private byte[] getMacByName() throws SocketException {
        NetworkInterface anInterface = NetworkInterface.getByName(name);
        if (anInterface != null) {
            return anInterface.getHardwareAddress();
        }
        return null;
    }


}