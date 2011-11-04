package org.amplafi.tapestry4.components.ext;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

/**
 * Displays text (similar to Insert component).
 * <p/>
 * If however the title
 * parameter is used, the title will be displayed and the text will
 * popup when user hovers over the title.
 */
public abstract class ShowTitledText extends AbstractComponent {
    @Parameter(required = true)
    public abstract String getValue();

    @Parameter
    public abstract String getTitle();

    @Parameter
    public abstract String getPrefix();

    @Override
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        writer.begin(getTemplateTagName());
        renderInformalParameters(writer, cycle);
        if (getTitle()!=null) {
            writer.attribute("title", getValue());
            
            writePrefix(writer);
            writer.print(getTitle());
        } else {
            writePrefix(writer);
            writer.print(getValue());
        }
        writer.end();
    }

    private void writePrefix(IMarkupWriter writer) {
        if (getPrefix()!=null) {
            writer.print(getPrefix());
            writer.print(' ');
        }
    }
}
