package org.amplafi.tapestry4.components.ext;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;

/**
 * Displays a clickable url.
 */
@ComponentClass(reservedParameters="href")
public abstract class InsertUrl extends AbstractComponent {
    /**
     * @return
     * The url to display.
     */
    @Parameter(required = true)
    public abstract String getUrl();

    /**
     * @return
     * The text to present for this url. If not specified, the url is used.
     */
    @Parameter
    public abstract String getText();
    /**
    *
    * @return true means no "target=_blank", false means no target attribute.
    */
    @Parameter
    public abstract boolean isUsingCurrent();

    @Override
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        writer.begin("a");
        writer.attribute("href", getUrl());
        if ( !isUsingCurrent()) {
            writer.appendAttribute("target", "_blank");
        }
        renderInformalParameters(writer, cycle);
        if (getText()!=null) {
            writer.print(getText());
        } else {
            writer.print(getUrl());
        }
        writer.end();
    }
}
