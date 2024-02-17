import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        StringBuilder sb = generateScaleTable();
        try {
            saveScaleTable(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static StringBuilder generateScaleTable() {
        StringBuilder sb = new StringBuilder("scale_table:\n");
        StringBuilder sums = new StringBuilder();
        double sum = 0;
        for (int yScreen = 0; yScreen < 64; yScreen++) {
            double value = scaleFunc2(yScreen);
            String hex = toHex((int) Math.round(value), 4);
            sb.append(yScreen % 8 == 0 ? "       data " : "").append(hex).append(yScreen % 8 == 7 ? "\n" : ",");
            double oldSum = sum;
            sum += value;
            if (oldSum > 0) {
                sums.append(sum / oldSum).append("\n");
            }
        }
        sb.append("\n");
        System.out.println(sb);
        // System.out.println(sums);
        return sb;
    }

    // 2nd degree polynomial
    private static double scaleFunc1(int yScreen) {
        double x = yScreen + 8;
        double a = 1.0 / 16;
        double b = 0;
        double c = 0x0020;
        return a * Math.pow(x, 2) + b * x + c;
    }

    // 1 / x
    private static double scaleFunc2(int yScreen) {
        double x = 64 - yScreen;
        return 1024.0 / x;
    }

    // 3rd degree polynomial
    private static double scaleFunc3(int yScreen) {
        double x = yScreen + 24;
        double a = 1.0 / 32;
        double b = 0;
        double c = 32.0;
        return a * Math.pow(0.25 * x, 3) + b * x + c;
    }

    // Exponential
    private static double scaleFunc4(int yScreen) {
        double x = yScreen + 66;
        return Math.pow(1.05, x);
    }

    // Linear
    private static double scaleFunc5(int yScreen) {
        return 16.0 + yScreen * 2;
    }

    // Constant
    private static double scaleFunc6(int yScreen) {
        return 256.0;
    }

    private static String toHex(int n, int len) {
        String hex = Integer.toHexString(n);
        if (hex.length() < len) {
            while (hex.length() < len) {
                hex = "0" + hex;
            }
        }
        else if (hex.length() > len) {
            hex = hex.substring(hex.length() - len);
        }
        return ">" + hex;
    }

    private static void saveScaleTable(StringBuilder sb) throws IOException {
        FileWriter fw = new FileWriter("src/scale-table.a99");
        fw.write(sb.toString());
        fw.close();
    }
}
