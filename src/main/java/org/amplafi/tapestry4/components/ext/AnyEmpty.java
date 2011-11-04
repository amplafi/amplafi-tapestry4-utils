package org.amplafi.tapestry4.components.ext;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * A component that can substitute for any empty HTML element, like
 * input, img, br, hr, e.t.c.
 */
public abstract class AnyEmpty extends AbstractComponent {

    @Override
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String element = getTemplateTagName();
        boolean rewinding = cycle.isRewinding();

        if (!rewinding) {
            writer.beginEmpty(element);

            renderInformalParameters(writer, cycle);

            if (getId() != null && !isParameterBound("id")) {
                renderIdAttribute(writer, cycle);
            }

            writer.closeTag();
        }
    }
}
