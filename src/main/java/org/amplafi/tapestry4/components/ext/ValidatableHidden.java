package org.amplafi.tapestry4.components.ext;

import org.apache.tapestry.form.Hidden;
import org.apache.tapestry.form.ValidatableField;
import org.apache.tapestry.form.ValidatableFieldSupport;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.valid.ValidatorException;
import org.apache.tapestry.annotations.Parameter;
import org.apache.hivemind.ApplicationRuntimeException;

/**
 * A hidden form component that can be validated.
 */
public abstract class ValidatableHidden extends Hidden implements ValidatableField {

    @Override
    @Parameter
    public abstract Object getValue();

    @Parameter
    public abstract Object getValidators();

    @Override
    @Parameter
    public abstract IActionListener getListener();

    @Override
    @Parameter
    public abstract boolean getEncode();

    public abstract ValidatableFieldSupport getValidatableFieldSupport();

    @Override
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
        super.renderFormComponent(writer, cycle);

        getValidatableFieldSupport().renderContributions(this, writer, cycle);
    }


    @Override
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
        String parameter = cycle.getParameter(getName());

        Object value = parameter;

        if (getEncode()) {
            try {
                value = getDataSqueezer().unsqueeze(parameter);
            }
            catch (Exception ex) {
                throw new ApplicationRuntimeException(ex.getMessage(), this, null, ex);
            }
        }

        try {
            getValidatableFieldSupport().validate(this, writer, cycle, value);
            setValue(value);
        }
        catch (ValidatorException e) {
            getForm().getDelegate().record(e);
        }

        // A listener is not always necessary ... it's easy to code
        // the synchronization as a side-effect of the accessor method.

        getListenerInvoker().invokeListener(getListener(), this, cycle);
    }
}
