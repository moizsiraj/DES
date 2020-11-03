import java.math.BigInteger;
import java.util.Arrays;

public class DES {
    private static int[] pc_1 = new int[]{57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4};


    private static int[] pc_2 = new int[]{14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32};

    private static int[] ip = new int[]{
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    private static int[] selection = new int[]{
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    static int[] left_shift = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    static String[][] auxKeys = new String[17][2];
    static String[] keys = new String[17];

    static String[][] auxMessage = new String[17][2];


    //input 16 character hex key
    static String permuteKey(String key) {
        String bin = hexToBinary(key);
        StringBuilder p_key = new StringBuilder();
        for (int i = 0; i < pc_1.length; i++) {
            p_key.append(bin.charAt(pc_1[i] - 1));
        }
        return p_key.toString();
    }

    static String hexToBinary(String hex) {
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < hex.length(); i++) {
            String char_temp = String.valueOf(hex.charAt(i));
            String bin_temp = new BigInteger(char_temp, 16).toString(2);
            switch (bin_temp.length()) {
                case 1:
                    bin_temp = "000" + bin_temp;
                    binary.append(bin_temp);
                    break;
                case 2:
                    bin_temp = "00" + bin_temp;
                    binary.append(bin_temp);
                    break;
                case 3:
                    bin_temp = "0" + bin_temp;
                    binary.append(bin_temp);
                    break;
                case 4:
                    binary.append(bin_temp);
                    break;
            }
        }
        return binary.toString();
    }

    static String[] splitKey(String key) {
        String C = key.substring(0, 28);
        String D = key.substring(28, 56);
        return new String[]{C, D};
    }

    static String leftShift(String key, int shift) {
        return key.substring(shift) + key.substring(0, shift);
    }

    static void getAuxKeys(String key) {
        String[] keySplit = splitKey(key);
        auxKeys[0][0] = keySplit[0];
        auxKeys[0][1] = keySplit[1];
        for (int i = 1; i <= 16; i++) {
            auxKeys[i][0] = leftShift(auxKeys[i - 1][0], left_shift[i - 1]);
            auxKeys[i][1] = leftShift(auxKeys[i - 1][1], left_shift[i - 1]);
        }
    }

    static void getKey() {
        for (int i = 0; i < auxKeys.length; i++) {
            String auxKey = auxKeys[i][0] + auxKeys[i][1];
            StringBuilder key = new StringBuilder();
            for (int j = 0; j < pc_2.length; j++) {
                key.append(auxKey.charAt(pc_2[j] - 1));
            }
            keys[i] = key.toString();
            System.out.println(i + ":" + keys[i]);
        }
    }

    static String stringToHex(String message) {
        StringBuilder hexString = new StringBuilder();
        char[] charArray = message.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            String hexOut = Integer.toHexString(charArray[i]);
            hexString.append(hexOut);
        }
        return hexString.toString();
    }

    static String initialPermute(String message) {
        String hexMessage = stringToHex(message);
//        String binary = hexToBinary(hexMessage);
        String binary = "0000000100100011010001010110011110001001101010111100110111101111";
//        if (binary.length() < 64){
//            int padding = 63 - binary.length();
//            StringBuilder binMessage = new StringBuilder();
//            for (int i = 0; i < padding; i++) {
//                binMessage.append("0");
//            }
//            binMessage.append(binary);
//            return binMessage.toString();
//        }else{
//            return binary.substring(0, 64);
//        }
        StringBuilder intialMessage = new StringBuilder();
        for (int i = 0; i < ip.length; i++) {
            intialMessage.append(binary.charAt(ip[i] - 1));
        }
        return intialMessage.toString();
    }

    static String[] splitMessage(String message) {
        String C = message.substring(0, 32);
        String D = message.substring(32, 64);
        return new String[]{C, D};
    }

    static void getAuxMessages(String message) {
        String[] messageSplit = splitMessage(message);
        auxMessage[0][0] = messageSplit[0];
        auxMessage[0][1] = messageSplit[1];
        for (int i = 1; i <= 16; i++) {
            auxMessage[i][0] = auxMessage[i - 1][1];
            getRight(i);
        }
    }

    static void getRight(int index) {
        int Ln_1 = Integer.parseInt(auxMessage[index - 1][0], 2);


//        auxMessage[index][1] =;
    }

    static void func(int index) {
        StringBuilder e_Rn_1 = new StringBuilder();
        for (int i = 0; i < selection.length; i++) {
            e_Rn_1.append(auxMessage[index - 1][1].charAt(selection[i] - 1));
        }
        int dec_e_Rn_1 = Integer.parseInt(e_Rn_1.toString(), 2);
//        int dec_key = Integer.parseInt(keys[index])


    }


    public static void main(String[] args) {
        getAuxKeys("11110000110011001010101011110101010101100110011110001111");
        getKey();
    }

}
