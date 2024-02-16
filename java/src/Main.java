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
            double value = scaleFunc1(yScreen);
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

    private static double scaleFunc1(int yScreen) {
        double x = yScreen + 8;
        double a = 1.0 / 16;
        double b = 0;
        double c = 0x0020;
        return a * Math.pow(x, 2) + b * x + c;
    }

    private static double scaleFunc2(int yScreen) {
        double x = 64 - yScreen;
        return 0x0020 + 400 / (x + 8);
    }

    private static double scaleFunc3(int yScreen) {
        double x = yScreen + 24;
        double a = 1.0 / 32;
        double b = 0;
        double c = 0x0020;
        return a * Math.pow(0.25 * x, 3) + b * x + c;
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
