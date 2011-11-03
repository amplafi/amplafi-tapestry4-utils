package org.amplafi.tapestry4.bindings;


import org.apache.tapestry.binding.AbstractBindingFactory;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.hivemind.Location;

/**
 * Constructs instances of {@link org.amplafi.tapestry4.bindings.RandomMessageBinding}.
 *
 * @author Andreas Andreou
 */
public class RandomMessageBindingFactory extends AbstractBindingFactory {

    public IBinding createBinding(IComponent root, String description, String expression, Location location) {
        return new RandomMessageBinding(description, getValueConverter(), location, root, expression);
    }
}