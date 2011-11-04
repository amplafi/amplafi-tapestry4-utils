package org.amplafi.tapestry4.components.ext;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.annotations.Parameter;

import static com.amplafi.util.Utils.*;

/**
 * Like the Insert component, produces the result as a html comment. Useful to help with debugging without affecting the layout.
 */
public abstract class CommentInsert extends AbstractComponent {

    /**
     * @return
     * The text to present.
     */
    @Parameter(required = true)
    public abstract String getValue();

    /**
     * Useful for making debug output visible in a development environment.
     * @return true render as html comment. false render as the Insert component would.
     */
    @Parameter(defaultValue="true")
    public abstract boolean isRenderedAsComment();

    /**
     *
     * @return true if nothing should be outputted.
     */
    @Parameter
    public abstract boolean isHidden();

    @Override
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        String value;
        if (cycle.isRewinding() || isHidden() || isBlank(value = getValue())) {
            return;
        }

        value = value.replaceAll("<!--", "-");
        value = value.replaceAll("-->", "-");
        value = value.replaceAll("--+", "-");

        if ( isRenderedAsComment()) {
            writer.comment(value);
        } else {
            writer.printRaw(value);
        }
    }

}
