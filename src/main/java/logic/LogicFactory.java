package logic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xiaodan Chen, Danping Tang, Gabreil Matte
 */
public abstract class LogicFactory {

    private static final String PACKAGE = "logic.";
    private static final String SUFFIX = "Logic";

    /**
     *
     */
    private LogicFactory() {
    }

    /**
     *
     * @param <T>
     * @param entityName
     * @return
     */
    //TODO this code is not complete, it is just here for sake of programe working. need to be changed ocmpletely
    public static < T> T getFor(String entityName) {
        try {
            T newInstance = getFor((Class< T>) Class.forName(PACKAGE + entityName + SUFFIX));
            return newInstance;
        } catch (ClassNotFoundException e) {

        }
        return null;
    }

    /**
     *
     * @param <T>
     * @param type
     * @return
     */
    public static <T> T getFor(Class<T> type) {
    
        try {
            Constructor<T> declaredConstructor = type.getDeclaredConstructor();
            T newInstance = declaredConstructor.newInstance();
            return newInstance;

        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(LogicFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
