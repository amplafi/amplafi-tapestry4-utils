/*
 * Copyright 2009 by Amplafi. All rights reserved.
 * Confidential.
 */

package org.amplafi.tapestry4.components.ext;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import org.amplafi.tapestry4.BaseComponentTestCase;
import org.amplafi.tapestry4.components.ext.CommentInsert;

/**
 * Test {@link CommentInsert}.
 * @author Andreas Andreou
 */
public class TestCommentInsert extends BaseComponentTestCase {

    @DataProvider(name = "data")
    protected Object[][] data() {
    	String separator = System.getProperty("line.separator");
        return new String[][] {
                {"", ""},
                {"normal", "<!-- normal -->" + separator},
                {"<!-- hmm ------->", "<!-- - hmm - -->" + separator},
                {"{\"path\":\"=HTML{--#_temp_amp_0_html--}/BODY{--#_temp_amp_1_body--}/DIV{--#_temp_amp_5_div--}/P{--#_temp_amp_7_p--}/{----}\",\"lookupKey\":\"ampmep_74\"}",
                    "<!-- {\"path\":\"=HTML{-#_temp_amp_0_html-}/BODY{-#_temp_amp_1_body-}/DIV{-#_temp_amp_5_div-}/P{-#_temp_amp_7_p-}/{-}\",\"lookupKey\":\"ampmep_74\"} -->" + separator}
        };
    }

    @Test(dataProvider = "data")
    public void testCommentRender(String input, String output) {
        CommentInsert pager = newInstance(CommentInsert.class, "renderedAsComment", true, "value", input);
        pager.renderComponent(newBufferWriter(), newCycle(false));
        assertBuffer(output);
    }
}
