import java.util.Hashtable;

/**
 * Mobile.AgentLoader defines the class of an incoming agent and registers
 * it into its local class hash.
 *
 * @author Christopher Ijams and Munehiro Fukuda
 */
public class AgentLoader extends ClassLoader {
    private Hashtable<String, Class> classHash = new Hashtable<>();

    /**
     * Mobile.AgentLoader defines the class of an incoming agent and registers
     * it into its local class hash.
     *
     * @param name     the name of a given agent.
     * @param bytecode the byte code of a given agent.
     * @return the new class of a given agent.
     */
    public Class loadClass(String name, byte[] bytecode) {
        Class newClass = findLoadedClass(name);
        if (newClass == null)
            try {
                newClass = super.loadClass(name);
            } catch (Exception ignore) {}
        if (newClass == null)
            newClass = classHash.get(name);
        if (newClass == null) {
            newClass = defineClass(name, bytecode, 0, bytecode.length);
            classHash.put(name, newClass);
        }
        return newClass;
    }
}