package MemoryTraceDrawer;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;

import java.util.HashMap;
import java.util.Map;

public class Styles
{
    private static Styles self = new Styles();

    @Deprecated
    public static Styles getInstance()
    {
        return self;
    }

    private String StackStyle, HeapStyle, StackBoxStyle, HeapBoxStyle, ReferenceStyle, PrimitiveStyle;
    private mxRectangle defaultRect;
    private Map<String, Object> placeHolder;

    /**
     * Contains all of the styles to be used for the cells in the graph
     */
    private Styles()
    {
        placeHolder = new HashMap<>();
        defaultRect = mxUtils.getLabelSize("sampleTxt", placeHolder, false, mxConstants.LINE_HEIGHT);
        final String assign = "=";
        final String end = ";";
        final String one = "1";
        final String zero = "0";

        HeapStyle = mxConstants.STYLE_MOVABLE + assign + zero + end + mxConstants.STYLE_RESIZABLE + assign + zero + end + mxConstants.STYLE_FOLDABLE + assign + zero + end + mxConstants.STYLE_DELETABLE + assign + zero + end + mxConstants.STYLE_EDITABLE + assign + zero + end + mxConstants.STYLE_VERTICAL_LABEL_POSITION + assign + mxConstants.ALIGN_MIDDLE + end + mxConstants.STYLE_FILLCOLOR + assign + "C933FF" + end + mxConstants.STYLE_OPACITY + assign + "25" + end + mxConstants.STYLE_NOEDGESTYLE + assign + one + end;

        StackStyle = mxConstants.STYLE_MOVABLE + assign + zero + end + mxConstants.STYLE_RESIZABLE + assign + zero + end + mxConstants.STYLE_FOLDABLE + assign + zero + end + mxConstants.STYLE_DELETABLE + assign + zero + end + mxConstants.STYLE_EDITABLE + assign + zero + end + mxConstants.STYLE_VERTICAL_LABEL_POSITION + assign + mxConstants.ALIGN_MIDDLE + end + mxConstants.STYLE_FILLCOLOR + assign + "FF7A00" + end + mxConstants.STYLE_OPACITY + assign + "25" + end + mxConstants.STYLE_NOEDGESTYLE + assign + one + end;

        StackBoxStyle = mxConstants.STYLE_MOVABLE + assign + zero + end + mxConstants.STYLE_DELETABLE + assign + one + end + mxConstants.STYLE_EDITABLE + assign + zero + end + mxConstants.STYLE_FOLDABLE + assign + zero + end + mxConstants.STYLE_VERTICAL_LABEL_POSITION + assign + mxConstants.ALIGN_TOP + end + mxConstants.STYLE_VERTICAL_ALIGN + assign + mxConstants.ALIGN_BOTTOM + end + mxConstants.STYLE_SPACING_BOTTOM + assign + zero + end + mxConstants.STYLE_ALIGN + assign + mxConstants.ALIGN_LEFT + end + mxConstants.STYLE_FONTSTYLE + assign + mxConstants.FONT_BOLD + end;

        HeapBoxStyle = mxConstants.STYLE_MOVABLE + assign + one + end + mxConstants.STYLE_DELETABLE + assign + one + end + mxConstants.STYLE_EDITABLE + assign + zero + end + mxConstants.STYLE_FOLDABLE + assign + zero + end + mxConstants.STYLE_VERTICAL_LABEL_POSITION + assign + mxConstants.ALIGN_TOP + end + mxConstants.STYLE_VERTICAL_ALIGN + assign + mxConstants.ALIGN_BOTTOM + end + mxConstants.STYLE_SPACING_BOTTOM + assign + zero + end + mxConstants.STYLE_ALIGN + assign + mxConstants.ALIGN_LEFT + end + mxConstants.STYLE_FONTSTYLE + assign + mxConstants.FONT_BOLD + end;

        ReferenceStyle = mxConstants.STYLE_EDITABLE + assign + zero + end + mxConstants.STYLE_DELETABLE + assign + one + end + mxConstants.STYLE_NOEDGESTYLE + assign + one + end + mxConstants.STYLE_OPACITY + assign + zero + end + mxConstants.STYLE_MOVABLE + assign + zero + end + mxConstants.STYLE_RESIZABLE + assign + zero + end + mxConstants.STYLE_FOLDABLE + assign + zero + end + mxConstants.STYLE_VERTICAL_LABEL_POSITION + assign + mxConstants.ALIGN_MIDDLE + end + mxConstants.STYLE_ALIGN + assign + mxConstants.ALIGN_LEFT + end + mxConstants.STYLE_PORT_CONSTRAINT + assign + mxConstants.DIRECTION_EAST + end;

        PrimitiveStyle = mxConstants.STYLE_EDITABLE + assign + zero + end + mxConstants.STYLE_DELETABLE + assign + one + end + mxConstants.STYLE_NOEDGESTYLE + assign + one + end + mxConstants.STYLE_MOVABLE + assign + zero + end + mxConstants.STYLE_RESIZABLE + assign + zero + end + mxConstants.STYLE_OPACITY + assign + zero + end + mxConstants.STYLE_FOLDABLE + assign + zero + end + mxConstants.STYLE_VERTICAL_LABEL_POSITION + assign + mxConstants.ALIGN_MIDDLE + end + mxConstants.STYLE_ALIGN + assign + mxConstants.ALIGN_LEFT + end + mxConstants.STYLE_PORT_CONSTRAINT + assign + mxConstants.DIRECTION_EAST + end;
    }

    protected static String getHeapBoxStyle()
    {
        return self.HeapBoxStyle;
    }

    protected static String getHeapStyle()
    {
        return self.HeapStyle;
    }

    protected static String getPrimitiveStyle()
    {
        return self.PrimitiveStyle;
    }

    protected static String getReferenceStyle()
    {
        return self.ReferenceStyle;
    }

    protected static String getStackBoxStyle()
    {
        return self.StackBoxStyle;
    }

    protected static String getStackStyle()
    {
        return self.StackStyle;
    }

    protected static mxRectangle getDefaultRect()
    {
        return self.defaultRect;
    }

    protected static Map<String, Object> getPlaceHolder()
    {
        return self.placeHolder;
    }

}
