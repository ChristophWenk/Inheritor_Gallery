import service.JShellService;

public class Test {

    private static JShellService jShellService = JShellService.getInstance();

    public static void main(String[] args) {
        String input = "Person p = new Person();";
        String s = jShellService.processInput(input);
        System.out.println(s);
    }
}
