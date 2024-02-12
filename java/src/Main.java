public class Main {
    public static void main(String[] args) {
        System.out.println("z_table:");
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
