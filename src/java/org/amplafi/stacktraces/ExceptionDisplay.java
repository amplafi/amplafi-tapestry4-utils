package org.amplafi.stacktraces;

import java.util.List;


public abstract class ExceptionDisplay extends org.apache.tapestry.html.ExceptionDisplay {
    public abstract List getLinkablePackages();

    public boolean isInClickable() {
        return exists(getTrace(), getLinkablePackages());
    }

    public String getActivationJs() {
        String trace = getTrace();
        int pos = trace.lastIndexOf('(');
        if (pos<0)
            return "";
        int at = trace.indexOf(':', pos);
        int end = trace.indexOf(')', pos);
        if (at<0)
            return "";
        String file = trace.substring(pos + 1, at);
        if (end<0)
            return "Activator.doOpen('file?file=" + file + "')";
        else
            return "Activator.doOpen('file?file=" + file + "&line=" +
                    trace.substring(at+1, end) + "')";
    }

    public boolean isInPackage() {
        return exists(getTrace(), getPackages());
    }

    private boolean exists(String trace, List packages) {
        if (packages == null)
            return false;


        for (int i=0; i<packages.size(); i++)
        {
            if (trace.startsWith((String)packages.get(i)))
                return true;
        }
        return false;
    }
}
