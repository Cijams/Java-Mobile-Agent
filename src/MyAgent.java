public class MyAgent extends Agent {
    private String[] destination = new String[10];
    int hopCount = 0;

    public MyAgent() {
        System.out.println("agent ran");
        init();
    }

    public MyAgent(String[] args) {
        for (int i = 0; i < args.length; i++)
            destination[i] = args[i];

        for(int i = 0; i < args.length; i++)
            System.out.println(args[i] + " " +i);

    }

    public void init() {
        System.out.println("agent(" + getId() + ") invoked init: ");
        hop();
        String[] args = new String[5];
        args[0] = 56777+"";
        hop(destination[1], "step", args);
    }

    public void step() { // dispatched to a user-specified node
        System.out.println("agent( " + getId() + ") invoked step: ");
        String[] args = new String[5];
        args[0] = 56778+"";
        System.out.println(++hopCount);
        hop(destination[2], "end", args);
    }

    public void end() {
        System.out.println(++hopCount);
        System.out.println("I ended");
    }
}