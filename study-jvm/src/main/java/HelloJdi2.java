/**
 * javac -g HelloJdi2.java
 * <p>
 * java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8088  HelloJdi
 */
public class HelloJdi2 {

    public static void main(String[] args) {
        String str = "Hello jdi!";
        System.out.println(str);
    }

}