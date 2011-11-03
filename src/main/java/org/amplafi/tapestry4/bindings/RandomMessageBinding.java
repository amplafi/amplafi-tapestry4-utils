package org.amplafi.tapestry4.bindings;

import org.apache.tapestry.binding.MessageBinding;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.IComponent;
import org.apache.hivemind.Location;


/**
 * Similar to {@link org.apache.tapestry.binding.MessageBinding}.
 * Returns a random message for the given key by appending suffixes like -0, -1,
 * e.t.c. to the key. The key passed must end with slash and a number which is the
 * number of the available alternatives for that message.<p/>
* The binding is typically registered with the prefix "random".
 *
 * @author Andreas Andreou
 */
public class RandomMessageBinding extends MessageBinding {

    private int count;
    private String key;

    protected RandomMessageBinding(String description, ValueConverter valueConverter, Location location, IComponent component, String key) {
        super(description, valueConverter, location, component, key);
        int pos = key.indexOf("/");
        if (pos<0) {
            throw new IllegalArgumentException("Random Message Key must end with /number, i.e. /4 if there are 4 alternatives. actual string="+key);
        }
        this.key = key.substring(0, pos);
        this.count = Integer.parseInt(key.substring(pos+1));
    }

    @Override
    public Object getObject() {
        IComponent comp = (IComponent) getComponent();
        return getMessage(comp, key, count);
    }

    @Override
    public boolean isInvariant() {
        return false;
    }

    /**
     * A random message for the given key.
     * @param comp
     * @param key
     * @return
     */
    public static String getMessage(IComponent comp, String key, int count) {
        int pos = (int) (Math.random() * count);
        return comp.getMessages().getMessage(key + "-" + pos);
    }

    public static String format(IComponent comp, String key, int count,
                                String...arguments) {
        int pos = (int) (Math.random() * count);
        return comp.getMessages().format(key + "-" + pos, arguments);
    }

}