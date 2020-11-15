/**
 * javac -g HelloJdi.java
 *
 * java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8088  HelloJdi
 *
 *
 */
public class HelloJdi {

    public static void main(String[] args) {
        while (true) {
            String str = String.format("Hello jdi : %s", String.valueOf(System.currentTimeMillis()));
            System.out.println(str);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}