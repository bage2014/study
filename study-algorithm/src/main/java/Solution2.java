import java.lang.reflect.Method;

class Solution2 {
    public static void main(String[] args) throws Exception {
        int result = new Solution2().solution("add",2,2);
        System.out.println(result);
    }
    public int solution(String methodName, int a, int b) throws Exception {
        Method method = Calculator.class.getClass().getDeclaredMethod(methodName,new Class[]{Integer.class,Integer.class});
            if(method.getName().equals(methodName)){
                Object result = method.invoke,new Object[]{a,b});
                return Integer.parseInt(String.valueOf(result));
            }
        throw new NoSuchMethodException();
    }
}

class Calculator {
    public static int add(int a, int b) {
        return a + b;
    }

    public static int sub(int a, int b) {
        return a - b;
    }

    public static int mul(int a, int b) {
        return a * b;
    }

    public static int div(int a, int b) {
        return a / b;
    }
}