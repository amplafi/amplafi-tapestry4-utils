package org.amplafi.tapestry4.components.ext;

import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * An Any component that is also a form component - useful for gaining focus.
 */
public abstract class AnyForm extends AbstractFormComponent {
    @Override
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
        String element = getTemplateTagName();

        boolean rewinding = cycle.isRewinding();

        if (!rewinding) {
            writer.begin(element);

            renderInformalParameters(writer, cycle);

            // render tapestry generated id as the html id attribute if no overriding id is supplied.
            if (getId() != null && !isParameterBound("id")) {
                renderIdAttribute(writer, cycle);
            }
        }

        renderBody(writer, cycle);

        if (!rewinding) {
            writer.end();
        }
    }


    @Override
    @SuppressWarnings("unused")
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
    }
}
