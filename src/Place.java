import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

/**
 * Mobile.Mobile.Place is the our mobile-agent execution platform that accepts an
 * agent transferred by Mobile.Agent.hop(), deserializes it, and resumes it
 * as an independent thread.
 *
 * @author Christopher Ijams and Munehiro Fukuda.
 */
public class Place extends UnicastRemoteObject implements PlaceInterface {
    private AgentLoader loader;
    private int agentSequencer;

    /**
     * This constructor instantiates a Mobile.AgentLoader object that
     * is used to define a new agent class coming remotely.
     */
    private Place() throws RemoteException {
        super();
        loader = new AgentLoader();
    }

    /**
     * main() starts an RMI registry in local, instantiates a Mobile.Mobile.Place
     * agent execution platform, and registers it into the registry.
     *
     * @param args receives a port, (i.e., 5001-65535).
     */
    public static void main(String[] args) {
        String usage = "usage: java <port>";

        if (args.length < 1) {
            System.out.println(usage);
            System.exit(-1);
        }

        if (Integer.parseInt(args[0]) < 5001 || Integer.parseInt(args[0]) > 65535) {
            System.out.println(usage);
            System.exit(-1);
        }

        try {
            int port = Integer.parseInt(args[0]);
            System.out.println("Place Ready...");
            startRegistry(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * startRegistry() starts an RMI registry process in local to this Mobile.Place.
     *
     * @param port the port to which this RMI should listen.
     */
    private static void startRegistry(int port) throws RemoteException {
        try {
            Registry registry =
                    LocateRegistry.getRegistry(port);
            registry.list();
        } catch (RemoteException e) {
            try {
                Registry registry =
                        LocateRegistry.createRegistry(port);

                Place place = new Place();
                registry.rebind("Place", place);                            // might need to be naming
                System.out.println(registry.toString());

            } catch (Exception ef) {
                ef.printStackTrace();
            }
        }
    }

    /**
     * deserialize() deserializes a given byte array into a new agent.
     *
     * @param buf a byte array to be deserialized into a new Agent object.
     * @return a deserialized Agent object
     */
    private Agent deserialize(byte[] buf)
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(buf);
        AgentInputStream input = new AgentInputStream(in, loader);
        return (Agent) input.readObject();
    }

    /**
     * transfer() accepts an incoming agent and launches it as an independent
     * thread.
     *
     * @param classname The class name of an agent to be transferred.
     * @param bytecode  The byte code of  an agent to be transferred.
     * @param entity    The serialized object of an agent to be transferred.
     * @return true if an agent was accepted in success, otherwise false.
     */
    public boolean transfer(String classname, byte[] bytecode, byte[] entity)
            throws RemoteException {
        Agent myAgent = null; // MyAgent agent
        loader.loadClass(classname, bytecode);
        try {
            myAgent = deserialize(bytecode);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //  myAgent.setId(5); // do some math
        Thread thread = new Thread(myAgent);
        thread.start();

        return true;
    }
}