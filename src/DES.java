import java.math.BigInteger;
import java.sql.Array;
import java.util.Arrays;

public class DES {
    public static String runDES(String message, String key) {
        String permutedKey = permuteKey(key);
        getAuxKeys(permutedKey);
        getFinalKeys();
        String permutedMessage = initialPermute(message);
        getAuxMessages(permutedMessage);
        String encodedMessage = getEncodedMessage();
        return encodedMessage;
    }

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


    private static int[][] S1 = {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
    };

    private static int[][] S2 = {
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}

    };
    private static int[][] S3 = {
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
    };
    private static int[][] S4 = {
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
    };
    private static int[][] S5 = {
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
    };
    private static int[][] S6 = {
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13},
    };
    private static int[][] S7 = {
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
    };
    private static int[][] S8 = {
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11},
    };

    private static int[] p = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    private static int[] ip_inv = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
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

    static void getFinalKeys() {
        for (int i = 0; i < auxKeys.length; i++) {
            String auxKey = auxKeys[i][0] + auxKeys[i][1];
            StringBuilder key = new StringBuilder();
            for (int j = 0; j < pc_2.length; j++) {
                key.append(auxKey.charAt(pc_2[j] - 1));
            }
            keys[i] = key.toString();
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
        String permutedMessage;
        String binary = hexToBinary(message);
        if (binary.length() < 64){
            int padding = 63 - binary.length();
            StringBuilder binMessage = new StringBuilder();
            for (int i = 0; i < padding; i++) {
                binMessage.append("0");
            }
            binMessage.append(binary);
            permutedMessage = binMessage.toString();
        }else{
            permutedMessage = binary.substring(0, 64);
        }
        StringBuilder initialMessage = new StringBuilder();
        for (int i = 0; i < ip.length; i++) {
            initialMessage.append(permutedMessage.charAt(ip[i] - 1));
        }
        return initialMessage.toString();
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

    static String getEncodedMessage() {
        String RL = auxMessage[16][1] + auxMessage[16][0];
        StringBuilder finalStr = new StringBuilder();
        for (int j = 0; j < ip_inv.length; j++) {
            finalStr.append(RL.charAt(ip_inv[j] - 1));
        }
        BigInteger val = new BigInteger(finalStr.toString(), 2);
        return val.toString(16);
    }

    static void getRight(int index) {
        long Ln_1 = Long.parseLong(auxMessage[index - 1][0], 2);
        String function = func(index);
        long functionToBin = Long.parseLong(function, 2);
        long valueBin = Ln_1 ^ functionToBin;
        String valueStr = Long.toBinaryString(valueBin);
        if (valueStr.length() < 32) {
            int padding = 32 - valueStr.length();
            for (int i = 0; i < padding; i++) {
                valueStr = "0" + valueStr;
            }
        } else if (valueStr.length() > 32) {
            int excess = valueStr.length() - 32;
            valueStr = valueStr.substring(excess, valueStr.length() - 1);
        }
        auxMessage[index][1] = valueStr;
    }

    static String checkBinLen(String bin) {
        switch (bin.length()) {
            case 1:
                return "000" + bin;
            case 2:
                return "00" + bin;
            case 3:
                return "0" + bin;
        }
        return bin;
    }

    static String func(int index) {
        StringBuilder returnString = new StringBuilder();
        StringBuilder e_Rn_1 = new StringBuilder();
        for (int i = 0; i < selection.length; i++) {
            e_Rn_1.append(auxMessage[index - 1][1].charAt(selection[i] - 1));
        }
        long dec_e_Rn_1 = Long.parseLong(e_Rn_1.toString(), 2);
        long dec_key = Long.parseLong(keys[index], 2);
        long xor = dec_e_Rn_1 ^ dec_key;
        StringBuilder binary = new StringBuilder(Long.toBinaryString(xor));
        if (binary.length() < 48) {
            int padding = 48 - binary.length();
            for (int i = 0; i < padding; i++) {
                binary.insert(0, '0');
            }
        }
        String[] binStore = new String[8];
        int start = 0;
        int end = 6;
        for (int i = 0; i < binStore.length; i++) {
            binStore[i] = binary.substring(start, end);
            start = start + 6;
            end = end + 6;
        }
        for (int i = 0; i < binStore.length; i++) {
            StringBuilder rowStr = new StringBuilder();
            String tempStr = binStore[i];
            rowStr.append(tempStr.charAt(0)).append(tempStr.charAt(5));
            int row = Integer.parseInt(rowStr.toString(), 2);
            String columnStr = tempStr.substring(1, 5);
            int column = Integer.parseInt(columnStr, 2);
            int value;
            String binValue;
            switch (i) {
                case 0:
                    value = S1[row][column];
                    binValue = Integer.toBinaryString(value);
                    binValue = checkBinLen(binValue);
                    returnString.append(binValue);
                    break;
                case 1:
                    value = S2[row][column];
                    binValue = Integer.toBinaryString(value);
                    binValue = checkBinLen(binValue);
                    returnString.append(binValue);
                    break;
                case 2:
                    value = S3[row][column];
                    binValue = Integer.toBinaryString(value);
                    binValue = checkBinLen(binValue);
                    returnString.append(binValue);
                    break;
                case 3:
                    value = S4[row][column];
                    binValue = Integer.toBinaryString(value);
                    binValue = checkBinLen(binValue);
                    returnString.append(binValue);
                    break;
                case 4:
                    value = S5[row][column];
                    binValue = Integer.toBinaryString(value);
                    binValue = checkBinLen(binValue);
                    returnString.append(binValue);
                    break;
                case 5:
                    value = S6[row][column];
                    binValue = Integer.toBinaryString(value);
                    binValue = checkBinLen(binValue);
                    returnString.append(binValue);
                    break;
                case 6:
                    value = S7[row][column];
                    binValue = Integer.toBinaryString(value);
                    binValue = checkBinLen(binValue);
                    returnString.append(binValue);
                    break;
                case 7:
                    value = S8[row][column];
                    binValue = Integer.toBinaryString(value);
                    binValue = checkBinLen(binValue);
                    returnString.append(binValue);
                    break;
            }
        }
        String pTableInput = returnString.toString();
        StringBuilder pTableOutput = new StringBuilder();
        for (int i = 0; i < p.length; i++) {
            pTableOutput.append(pTableInput.charAt(p[i] - 1));
        }
        return pTableOutput.toString();
    }


    public static void main(String[] args) {
        System.out.println("The encoded message is: " + runDES("0123456789ABCDEF", "133457799BBCDFF1"));
    }

}
