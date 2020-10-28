package com.tobe.fishking.v2.addon;

import java.util.Random;

public class RandomValueGen {
    private static final int PASSWD_SIZE = 10;
    private static final int CERT_CODE_SIZE = 6;
    private static String universe_char = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYG";
    private static String universe_int = "1234567890";

    public static String getCertCode() {
        StringBuffer sb = new StringBuffer();
        Random rnd = new Random();
        int seed1 = rnd.nextInt(1000);

        for (int i = 0; i < RandomValueGen.CERT_CODE_SIZE; i++) {
            int seed2 = rnd.nextInt(1000);
            if (seed2 % 3 == 0) {
                sb.append(universe_int.charAt(rnd.nextInt(seed1) * 3 % universe_int.length()));
            } else if (seed2 % 2 == 0) {
                sb.append(universe_int.charAt(rnd.nextInt(seed1) * 5 % universe_int.length()));
            } else {
                sb.append(universe_int.charAt(rnd.nextInt(seed1) * 7 % universe_int.length()));
            }
        }

        return sb.toString();
    }

    public static String getTempPasswd() {
        StringBuffer sb = new StringBuffer();
        Random rnd = new Random();
        int seed1 = rnd.nextInt(1000);

        for (int i = 0; i < RandomValueGen.PASSWD_SIZE; i++) {
            int seed2 = rnd.nextInt(1000);
            sb.append(universe_int.charAt(rnd.nextInt(seed1) % universe_int.length()));
			/*if (seed2 % 2 == 0) {
				sb.append(universe_char.charAt(rnd.nextInt(seed1) % universe_char.length()));
			} else {
				sb.append(universe_int.charAt(rnd.nextInt(seed1) % universe_int.length()));
			}*/
        }

        return sb.toString();
    }

}
