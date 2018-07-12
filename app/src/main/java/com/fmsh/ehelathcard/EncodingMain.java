package com.fmsh.ehelathcard;

/**
 * Created by haojie on 2018/7/4.
 */

public class EncodingMain {

    private static final char[] a = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final byte[] b = new byte[128];

    public EncodingMain() {
    }

    public static byte[] makeByteArray(byte[] bin, int len) {
        byte[] var2 = new byte[len];
        System.arraycopy(bin, 0, var2, 0, len);
        return var2;
    }

    public static String bin2hex(byte[] bin) {
        StringBuilder var1 = new StringBuilder(bin.length << 1);
        int var2 = (bin = bin).length;

        for(int var3 = 0; var3 < var2; ++var3) {
            byte var4 = bin[var3];
            var1.append(a[var4 >> 4 & 15]);
            var1.append(a[var4 & 15]);
        }

        return var1.toString();
    }

    public static String bin2hex(byte[] bin, int length) {
        StringBuilder var2 = new StringBuilder(bin.length << 1);

        for(int var3 = 0; var3 < length; ++var3) {
            var2.append(a[bin[var3] >> 4 & 15]);
            var2.append(a[bin[var3] & 15]);
        }

        return var2.toString();
    }

    public static byte[] hex2bin(String hex) {
        byte[] var1 = new byte[(hex = hex.toUpperCase()).length() / 2];

        for(int var2 = 0; var2 < hex.length(); var2 += 2) {
            byte var3 = b[hex.charAt(var2)];
            byte var4 = b[hex.charAt(var2 + 1)];
            var1[var2 / 2] = (byte)(var3 << 4 | var4);
        }

        return var1;
    }

    static {
        for(byte var0 = 0; var0 < a.length; b[a[var0]] = var0++) {
            ;
        }

    }
}