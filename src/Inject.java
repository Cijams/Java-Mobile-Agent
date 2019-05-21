import java.lang.reflect.*;

/**
 * Mobile.Inject reads a given agent class from local disk, instantiates a new
 * object from it, and transfers this agent to a given destination IP where
 * the agent starts with the init( ) function.
 *
 * @authors Christopher Ijams and Munehiro Fukuda.
 */
public class Inject {
    public static String usage =
            "usage: java -cp Mobile.jar Mobile.Inject host port agent (arg1...N)";

    /**
     * Reads an agent byte code from the local disk, instantiates a new
     * object from it, and transfers this agent to a given destination IP
     * where the agent starts with init() function.
     *
     * @param args consists of host name, port, agent class name, and 0 or more
     *             arguments passed to an agent to be injected.
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println(usage);
            System.exit(-1);
        }
        String hostname = args[0];
        int port = 0;
        try {
            port = Integer.parseInt(args[1]);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(usage);
            System.exit(-1);
        }
        String agentClassName = args[2];
        String[] arguments = (args.length == 3) ?
                null : new String[args.length - 3];
        if (arguments != null)
            for (int i = 0; i < arguments.length; i++)
                arguments[i] = args[3 + i];

        byte[] bytecode = Agent.getByteCode(agentClassName);
        try {
            AgentLoader loader = new AgentLoader();
            Class agentClass = loader.loadClass(agentClassName, bytecode);

            Agent agent = null;
            if (arguments == null) {
                agent = (Agent) (agentClass.newInstance());
            } else {
                Object[] constructorArgs = new Object[]{arguments};
                Constructor agentConst
                        = agentClass.getConstructor(new Class[]{String[].class});
                agent = (Agent) (agentConst.newInstance(constructorArgs));
            }
            agent.setPort(port);
            agent.hop(hostname, "init");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}