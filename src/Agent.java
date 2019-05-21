import java.io.*;

/**
 * Base class for user-define mobile agents. Inherit to gain access
 * to the agent's identifier, along with the next host's IP address
 * and port number. Additional information encapsulated includes
 * the next host, args passed to this agent anlong with its class
 * name and byte code. Actual agent withh run as an independent thread
 * that invokes a given function upon migrating to the next host.
 *
 * @author Christopher Ijams and Munehiro Fukuda.
 */
public class Agent implements Serializable, Runnable {
    // the live data to carry with the agent upon a migration.
    protected int agentId = -1;         // this agent's identifier.
    private String _hostname = null;    // the next host name to migrate.
    private String _function = null;    // the function to invoke upon a move.
    private int _port = 0;              // the next host's port to migrate.
    private String[] _arguments = null; // arguments pass to _function.
    private String _classname = null;   // this agent's class name.
    private byte[] _bytecode = null;    // this agent's byte code.

    /**
     * getByteCode() reads a byte code from the file whose name is given in
     * "classname.class".
     *
     * @param classname the name of a class to read from local disk.
     * @return a byte code of a given class.
     */
    public static byte[] getByteCode(String classname) {
        String filename = classname + ".class";
        File file = new File(filename);
        byte[] bytecode = new byte[(int) file.length()];
        try {
            BufferedInputStream bis =
                    new BufferedInputStream(new FileInputStream(filename));
            bis.read(bytecode, 0, bytecode.length);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytecode;
    }

    /**
     * setPort() sets a port that is used to contact a remote Mobile.Mobile.Place.
     *
     * @param port a port to be set.
     */
    public void setPort(int port) {
        this._port = port;
    }

    /**
     * getId() returns this agent identifier: agentId.
     */
    public int getId() {
        return agentId;
    }

    /**
     * getByteCode() reads this agent's byte code from the corresponding file.
     *
     * @return a byte code of this agent.
     */
    public byte[] getByteCode() {
        if (_bytecode != null)
            return _bytecode;

        _classname = this.getClass().getName();
        _bytecode = getByteCode(_classname);
        return _bytecode;
    }

    /**
     * run() is the body of Mobile.Agent that is executed upon an injection
     * or a migration as an independent thread. run() identifies the method
     * with a given function name and arguments and invokes it. The invoked
     * method may include hop() that transfers this agent to a remote host or
     * simply returns back to run() that terminates the agent.
     */
    @Override
    public void run() {
        try {
            this.getClass().getMethod("hop").invoke("hop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * hop() transfers this agent to a given host and invokes a given
     * function of this agent.
     *
     * @param hostname the IP name of the next host machine to migrate
     * @param function the name of a function to invoke upon a migration
     */
    public void hop(String hostname, String function) {
        hop(hostname, function, null);
    }

    /**
     * hop() transfers this agent to a given host, and invoeks a given
     * function of this agent as passing given arguments to it.
     *
     * @param hostname the IP name of the next host machine to migrate
     * @param function the name of a function to invoke upon a migration
     * @param args     the arguments passed to a function called upon a
     *                 migration.
     */
    public void hop(String hostname, String function, String[] args) {
        // TODO Finish Impl.
    }

    /**
     * serialize() serializes this agent into a byte array.
     *
     * @return a byte array to contain this serialized agent.
     */
    private byte[] serialize() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(this);
            return out.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
