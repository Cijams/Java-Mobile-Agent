public class MyAgent extends Agent {
    private String destination = null;

    public MyAgent(String[] args) { // instantiated an ordinary object in local
        destination = args[0];
    }

    public void init() { // dispatched to a node specified with Mobile.Inject
        System.out.println("agent(" + getId() + ") invoked init: ");
        String[] args = new String[1];


        for (int i = 0; i < args.length; i++)
            System.out.println(args[i] + " args");
        System.out.println(args.length);

        args[0] = "Hello!";



        hop(destination, "step", args);
    }

    public void step(String[] args) { // dispatched to a user-specified node
        System.out.println("agent( " + getId() + ") invoked step: ");
    }
}