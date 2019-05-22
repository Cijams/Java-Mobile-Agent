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
        int port = 0;
        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
                if (port < 5001 || port > 65535)
                    throw new Exception();
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.err.println(usage);
            System.exit(-1);
        }

        try {
            startRegistry(port);
            System.out.println("Place ready.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            Place place = new Place();
            Naming.rebind("rmi://localhost:" + port + "/Place", place);
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
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list();
        } catch (RemoteException re) {
            Registry registry = LocateRegistry.createRegistry(port);
            registry.list();
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
        System.out.println(loader.loadClass(classname, bytecode));

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

    public void testMe() {
        System.out.println("I, THE MIGHT TESTME HAVE BEEN CALLED, MORTALS");
    }
}