public class Main {

    public static void main(String[] args) {
        generateScaleTable2();
    }

    private static void generateScaleTable() {
        System.out.println("scale_table:");
        double scale = 0x100;
        double horizon = 1;
        double yWorld = 8;
        for (int yScreen = 63, n = 0; yScreen >= 0; yScreen--, n++) {
            double z = yWorld / (yScreen + horizon);
            String hex = toHex((int) Math.round(z * scale), 4);
            System.out.print((n % 8 == 0 ? "       data " : "") + hex + (n % 8 == 7 ? "\n" : ","));
        }
        System.out.println();
    }

    private static void generateScaleTable2() {
        System.out.println("scale_table:");
        double start = 0x0020;
        double factor = 0.0;
        for (int yScreen = 0; yScreen < 64; yScreen++) {
            double value = start + yScreen * factor + (yScreen * yScreen) / 32.0;
            String hex = toHex((int) Math.round(value), 4);
            System.out.print((yScreen % 8 == 0 ? "       data " : "") + hex + (yScreen % 8 == 7 ? "\n" : ","));
        }
        System.out.println();
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
}
