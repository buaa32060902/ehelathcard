package com.fmsh.ehelathcard;

/**
 * Created by haojie on 2018/7/4.
 */

public class SM3Main {

    public SM3Main() {
    }

    private static int a(int var0) {
        return var0 ^ (var0 << 9 | var0 >>> 23) ^ (var0 << 17 | var0 >>> 15);
    }

    public static void HashInit(int[] V) {
        int[] var1 = new int[]{1937774191, 1226093241, 388252375, -628488704, -1452330820, 372324522, -477237683, -1325724082};

        for(int var2 = 0; var2 < 8; ++var2) {
            V[var2] = var1[var2];
        }

    }

    public static void HashProcessBlock(int[] V, int[] Mblock) {
        int[] var12 = new int[68];
        int var3 = V[0];
        int var4 = V[1];
        int var5 = V[2];
        int var6 = V[3];
        int var7 = V[4];
        int var8 = V[5];
        int var9 = V[6];
        int var10 = V[7];

        int var2;
        for(var2 = 0; var2 < 16; ++var2) {
            var12[var2] = Mblock[var2];
        }

        int Mblock1;
        for(var2 = 16; var2 < 68; ++var2) {
            Mblock1 = (var12[var2 - 3] << 15 | var12[var2 - 3] >>> 17) ^ var12[var2 - 9] ^ var12[var2 - 16];
            var12[var2] = Mblock1 ^ (Mblock1 << 15 | Mblock1 >>> 17) ^ (Mblock1 << 23 | Mblock1 >>> 9) ^ (var12[var2 - 13] << 7 | var12[var2 - 13] >>> 25) ^ var12[var2 - 6];
        }

        int var11;
        for(var2 = 0; var2 < 16; ++var2) {
            Mblock1 = (Mblock1 = (var11 = var3 << 12 | var3 >>> 20) + var7 + (2043430169 << var2 | 2043430169 >>> 32 - var2)) << 7 | Mblock1 >>> 25;
            var11 ^= Mblock1;
            var11 = (var3 ^ var4 ^ var5) + var6 + var11 + (var12[var2] ^ var12[var2 + 4]);
            Mblock1 = (var7 ^ var8 ^ var9) + var10 + Mblock1 + var12[var2];
            var6 = var5;
            var5 = var4 << 9 | var4 >>> 23;
            var4 = var3;
            var3 = var11;
            var10 = var9;
            var9 = var8 << 19 | var8 >>> 13;
            var8 = var7;
            var7 = a(Mblock1);
        }

        for(var2 = 16; var2 < 64; ++var2) {
            var11 = var3 << 12 | var3 >>> 20;
            Mblock1 = var2 % 32;
            Mblock1 = (Mblock1 = var11 + var7 + (2055708042 << Mblock1 | 2055708042 >>> 32 - Mblock1)) << 7 | Mblock1 >>> 25;
            var11 ^= Mblock1;
            var11 = (var3 & var4 | var3 & var5 | var4 & var5) + var6 + var11 + (var12[var2] ^ var12[var2 + 4]);
            Mblock1 = (var7 & var8 | ~var7 & var9) + var10 + Mblock1 + var12[var2];
            var6 = var5;
            var5 = var4 << 9 | var4 >>> 23;
            var4 = var3;
            var3 = var11;
            var10 = var9;
            var9 = var8 << 19 | var8 >>> 13;
            var8 = var7;
            var7 = a(Mblock1);
        }

        V[0] ^= var3;
        V[1] ^= var4;
        V[2] ^= var5;
        V[3] ^= var6;
        V[4] ^= var7;
        V[5] ^= var8;
        V[6] ^= var9;
        V[7] ^= var10;
    }

    public static void BitTransfer(int[] pbSrc, byte[] pbDest) {
        int var3 = 0;

        for(int var2 = 0; var2 < 8; ++var2) {
            int var4 = pbSrc[var2];
            pbDest[var3] = (byte)(var4 >>> 24);
            pbDest[var3 + 1] = (byte)(var4 >>> 16);
            pbDest[var3 + 2] = (byte)(var4 >>> 8);
            pbDest[var3 + 3] = (byte)var4;
            var3 += 4;
        }

    }

    public static void SM3Hash(byte[] pbSrc, int cbSrc, int var2, byte[] pbHash, int[] cbHash) {
        byte[] var15 = new byte[10000];
        byte[] var5 = new byte[128];
        int[] var9 = new int[8];
        int[] var10 = new int[8];
        int[] var11 = new int[16];
        int var6 = (cbSrc + 9 + 63) / 64 << 6;
        HashInit(var9);

        int var7;
        int var8;
        for(var8 = 0; var8 < var6; var8 += 64) {
            if(var8 + 64 <= cbSrc) {
                System.arraycopy(pbSrc, var8, var15, 0, pbSrc.length - var8);
            } else if(var8 > cbSrc) {
                System.arraycopy(var5, 64, var15, 0, var5.length - 64);
            } else {
                for(var2 = 0; var2 < 128; ++var2) {
                    var5[var2] = 0;
                }

                if((var7 = cbSrc - var8) > 0) {
                    System.arraycopy(pbSrc, var8, var5, 0, var7);
                }

                var5[var7] = -128;
                var7 = var6 - var8 - 4;
                var5[var7++] = (byte)(cbSrc << 3 >>> 24);
                var5[var7++] = (byte)(cbSrc << 3 >>> 16);
                var5[var7++] = (byte)(cbSrc << 3 >>> 8);
                var5[var7] = (byte)(cbSrc << 3);
                var15 = var5;
            }

            for(var7 = 0; var7 < 64; var7 += 4) {
                var11[var7 / 4] = (var15[var7] & 255) << 24 | (var15[var7 + 1] & 255) << 16 | (var15[var7 + 2] & 255) << 8 | var15[var7 + 3] & 255;
            }

            HashProcessBlock(var9, var11);
        }

        boolean pbSrc1 = true;
        var2 = var9[0];
        int var16 = var9[1];
        var6 = var9[2];
        var7 = var9[3];
        var8 = var9[4];
        int var17 = var9[5];
        int var18 = var9[6];
        int pbSrc2 = var9[7];
        var10[0] = var2;
        var10[1] = var16;
        var10[2] = var6;
        var10[3] = var7;
        var10[4] = var8;
        var10[5] = var17;
        var10[6] = var18;
        var10[7] = pbSrc2;
        cbHash[0] = 32;
        BitTransfer(var9, pbHash);
    }

    public static void SoftWare_SM3_Compress(byte[] pData, int cbDataLen, byte[] pDigest) {
        int[] var3 = new int[]{32};
        SM3Hash(pData, cbDataLen, 0, pDigest, var3);
    }

    public static void printHexString(byte[] b) {
        for(int var1 = 0; var1 < b.length; ++var1) {
            String var2;
            if((var2 = Integer.toHexString(b[var1] & 255)).length() == 1) {
                var2 = "0" + var2;
            }

            System.out.print(var2.toUpperCase() + " ");
        }

    }

    public static void printHexString(int[] l) {
        for(int var1 = 0; var1 < l.length; ++var1) {
            String var2 = Integer.toHexString(l[var1]);
            var2 = "0x" + var2;
            System.out.print(var2.toUpperCase() + " ");
        }

    }

    public static void printInt() {
        int[] var0 = new int[]{1937774191, 1226093241, 388252375, -628488704, -1452330820, 372324522, -477237683, -1325724082};

        for(int var1 = 0; var1 < var0.length; ++var1) {
            String var2 = Integer.toHexString(var0[var1]);
            var2 = "0x" + var2;
            System.out.print(var2.toUpperCase() + " ");
        }

    }

    public static String digest(String hex) {
        byte[] hex1 = EncodingMain.hex2bin(hex);
        byte[] var1 = new byte[32];
        SoftWare_SM3_Compress(hex1, hex1.length, var1);
        return EncodingMain.bin2hex(var1);
    }

    public static String digest(byte[] data) {
        byte[] var1 = new byte[32];
        SoftWare_SM3_Compress(data, data.length, var1);
        return EncodingMain.bin2hex(var1);
    }

    public static void main(String[] var0) {
        System.out.println(digest("c1c2c3c4c5c6c7c8"));
    }
}
