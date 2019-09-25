import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellExcetors {

    public static void main(String[] args) {
        execOne("sh ping.sh " + args[0]);
    }

    public static void execOne(String shell) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(shell); // for Linux
            // process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            process.destroy();
        }
    }
}
