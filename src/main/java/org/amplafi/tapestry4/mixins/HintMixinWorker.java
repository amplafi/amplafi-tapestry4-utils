package org.amplafi.tapestry4.mixins;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;

import org.apache.tapestry.enhance.EnhancementWorker;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.EnhanceUtils;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.spec.ParameterSpecification;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.dojo.form.AbstractFormWidget;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.hivemind.service.MethodSignature;

/**
 * Enhances {@link AbstractFormComponent}s so that they accept an additional 'hint'
 * parameter.
 */
public class HintMixinWorker implements EnhancementWorker {
    public static final String HINT_PARAMETER = "hint";
    /** renderFormComponent method of {@link AbstractFormComponent}. */
    protected static final String RENDER_COMPONENT_METHOD = "renderFormComponent";
    /** renderFormWidget method of {@link org.apache.tapestry.dojo.form.AbstractFormWidget}. */
    protected static final String RENDER_WIDGET_METHOD = "renderFormWidget";
    /** Parameters to renderFormComponent method in {@link AbstractFormComponent}. */
    protected static final Class<?>[] RENDER_PARAMS = { IMarkupWriter.class, IRequestCycle.class };
    /** Injected script source. */
    protected IScriptSource scriptSource;

    @SuppressWarnings("unchecked")
    @Override
    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec) {
        // Only on form components
        if (!AbstractFormComponent.class.isAssignableFrom(op.getBaseClass())) {
            return;
        }

        String methodName = AbstractFormWidget.class.isAssignableFrom(op.getBaseClass()) ?
                RENDER_WIDGET_METHOD : RENDER_COMPONENT_METHOD;

        if (spec.getParameter(HINT_PARAMETER) == null) {
            // So the parameter is part of spec and won't be rendered (as informal)
            IParameterSpecification ps = new ParameterSpecification();
            ps.setParameterName(HINT_PARAMETER);
            ps.setPropertyName(HINT_PARAMETER);
            ps.setLocation(spec.getLocation());
            spec.addParameter(ps);
        }

        Method renderMethod;
        try {
            renderMethod = op.getBaseClass().getDeclaredMethod(methodName, RENDER_PARAMS);
        } catch (NoSuchMethodException e) {
            return;
        }

        MethodSignature scriptSourceAccessor = createScriptSourceAccessor(op, spec);

        MethodSignature enhancedMethod = new MethodSignature(renderMethod);
        op.addMethod(renderMethod.getModifiers(), enhancedMethod,
                     buildMethodCallBack(enhancedMethod, scriptSourceAccessor),
                     spec.getLocation());
    }

    protected MethodSignature createScriptSourceAccessor(EnhancementOperation op,
                                                         IComponentSpecification spec) {
        String name = "enhScriptSource";
        String fieldName = "_$" + name;
        op.claimProperty(fieldName);
        String injName = op.addInjectedField(fieldName, IScriptSource.class, this.scriptSource);
        String methodName = EnhanceUtils.createAccessorMethodName(name);

        MethodSignature returnval = new MethodSignature(IScriptSource.class, methodName, null, null);
        op.addMethod(Modifier.PUBLIC, returnval, "return " + injName + ";", spec.getLocation());

        return returnval;
    }

    private String buildMethodCallBack(MethodSignature method, MethodSignature scriptSourceAccessor) {
        StringBuilder buf = new StringBuilder("{\n");
        buf.append(MessageFormat.format("super.{0}($$);\n", method.getName()));

        buf.append(" if (isParameterBound(\"")
        .append(HINT_PARAMETER).append("\")) {\n")
        .append("Object hint = ")
        .append("getBinding(\"").
        append(HINT_PARAMETER).append("\").getObject();\n");

        // begin param bound check
        buf.append(" if (hint != null) { ")
        .append(HintMixinWorker.class.getName()).append(".renderHint(")
        .append("$1, ")
        .append("$2, ")
        .append(scriptSourceAccessor.getName()).append("(), ")
        .append(" this, hint.toString());\n")
        .append(" } \n");
        // end param bound check

        buf.append("}");

        // end method subclassing
        buf.append("}\n");
        return buf.toString();
    }

    public void setScriptSource(IScriptSource scriptSource) {
        this.scriptSource = scriptSource;
    }
    /**
     * referenced in generated code in {@link #buildMethodCallBack(MethodSignature, MethodSignature)}
     * @param writer
     * @param cycle
     * @param scriptSource
     * @param comp
     * @param hint
     */
    @SuppressWarnings("unused")
    public static void renderHint(IMarkupWriter writer, IRequestCycle cycle, IScriptSource scriptSource,
            IComponent comp, String hint) {
        writer.begin("span");
        writer.attribute("class", "fieldhint");
        writer.printRaw(hint);
        writer.end();
    }
}
