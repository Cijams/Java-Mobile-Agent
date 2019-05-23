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
        System.out.println("hop count = " + hopCount);
        System.out.println("next destination = " + destination[hopCount]);
        hopCount++;

        String[] args = new String[1];
        args[0] = 56778+"";
        hop(destination[1], "step", args);
    }

    public void step() {
        System.out.println("agent(" + getId() + ") invoked step: ");
        System.out.println("hop count = " + hopCount);
        System.out.println("next destination = " + destination[hopCount]);
        hopCount++;

        String[] args = new String[1];
        args[0] = 56779+"";
        hop(destination[2], "jump", args);
    }

    public void jump() {
        System.out.println("agent(" + getId() + ") invoked jump: ");
        System.out.println("hop count = " + hopCount);
        hopCount++;
    }
}