import java.io.*;

/**
 * AgentInputStream is used to read a byte array from ObjectInputStream and
 * deserialize it into an agent object. For this purpose,
 * ObjectInputStream.resolveClass() was overwritten to search AgentClassLoader
 * for a given agent.
 */
public class AgentInputStream extends ObjectInputStream {
    private ClassLoader classloader;

    /**
     * The constructor initializes the InputStream super class and maintains
     * a class loader that includes the class of an agent to be read from
     * this input stream.
     *
     * @param in          an input stream to create an agent.
     * @param classloader a loader that includes the class of an agent to read.
     */
    public AgentInputStream(InputStream in, ClassLoader classloader)
            throws IOException {
        super(in);
        this.classloader = classloader;
    }

    /**
     * resolveClass() is automatically called when reading and deserializing
     * a new agent.
     *
     * @param agent the class found in ObjectInputStream
     */
    protected Class resolveClass(ObjectStreamClass agent)
            throws IOException {
        String className = agent.getName();

        try {
            Class loadedClass = classloader.loadClass(className);
            if (loadedClass != null) {
                return loadedClass;
            }
        } catch (Exception ignore) {
        }
        try {
            return super.resolveClass(agent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}