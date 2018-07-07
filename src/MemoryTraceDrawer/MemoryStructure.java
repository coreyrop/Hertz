package MemoryTraceDrawer;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import javax.swing.JOptionPane;


public class MemoryStructure
{
    private mxCell structureCell;
    private double structureCell_x, structureCell_width, structureCell_height, nextBoxX, nextBoxY, labelHeight;
    private DataStoreStyle storeStyle;
    private Frame frame;
    private static final String[] VARIABLE_STYLES = new String[VariableStyle.values().length];

    // Used to assign ID VARIABLE_STYLES to boxes for the Stack and Heap
    private static int ID_COUNT;

    static
    {
        ID_COUNT = 0;
        for (int i = 0; i < VARIABLE_STYLES.length; i++)
        {
            VARIABLE_STYLES[i] = VariableStyle.values()[i].toString();
        }
    }


    /**
     * Updates ID_COUNT to the passed value
     * used for when files are loaded
     *
     * @param count: the value to be set for ID_COUNT
     */
    protected static void setIdCount(int count)
    {
        ID_COUNT = count;
    }

    // Used to differentiate the styles for Stack and Heap
    public enum DataStoreStyle
    {
        STACK(Styles.getStackStyle(), "Frame's Name"), HEAP(Styles.getHeapStyle(), "Instance's Type");
        private String style;
        private String boxPrompt;

        public boolean contains(mxCell cell)
        {
            String cellId = cell.getId();
            return cellId.startsWith(name());
        }

        public String generateNewId()
        {
            int idNum = ID_COUNT;
            ID_COUNT += 1;
            return name() + idNum;
        }

        private String boxPrompt()
        {
            return boxPrompt;
        }

        public String style()
        {
            return style;
        }

        DataStoreStyle(String storeStyle, String boxesCalled)
        {
            style = storeStyle;
            boxPrompt = boxesCalled;
        }
    }

    /**
     * Used to manage the Reference and Primitive fields used in the graph
     * Tracks ID count for both Primitives and References
     * Allows the idCount to be set to a specified value, this is used for when files are loaded
     */
    public enum VariableStyle
    {
        REFERENCE("Reference", Styles.getReferenceStyle()), PRIMITIVE("Primitive", Styles.getPrimitiveStyle());
        private String displayName;
        private int idCount;
        private String style;

        public boolean instanceOf(mxCell cell)
        {
            String cellId = cell.getId();
            return cellId.startsWith(displayName);
        }

        public String getStyle()
        {
            return style;
        }

        VariableStyle(String _displayName, String _style)
        {
            displayName = _displayName;
            style = _style;
        }

        public String toString()
        {
            return displayName;
        }

        public String generateNewId()
        {
            int idNum = idCount;
            idCount += 1;
            return displayName + idNum;
        }

        public void setIdCount(int _idCount)
        {
            idCount = _idCount;
        }
    }

    /**
     * MemoryStructure class constructor
     * Initializes all the fields
     * Adds this MemoryStructure to the graph for visual display
     * Sets the style for this MemoryStructure in the graph
     *
     * @param frame:          the Frame instance for this MemoryStructure to be associated with
     * @param graph:          the graph that this MemoryStructure will be in
     * @param parent:         the parent of this MemoryStructure, needed for the graph
     * @param id:             the ID to be associated with this MemoryStructure
     * @param label:          the label that will be visually displayed on this MemoryStructure
     * @param x:              the x coordinate for this MemoryStructure
     * @param y:              the y coordinate for this MemoryStructure
     * @param width:          the width of this MemoryStructure
     * @param height:         the height of this MemoryStructure
     * @param dataStoreStyle: the style for this MemoryStructure to plot boxes, either STACK or HEAP
     */
    public MemoryStructure(Frame frame, mxGraph graph, Object parent, String id, Object label, double x, double y, double width, double height, DataStoreStyle dataStoreStyle)
    {
        this.frame = frame;
        structureCell_x = x;
        structureCell_width = width;
        structureCell_height = height;
        storeStyle = dataStoreStyle;
        nextBoxX = 0;
        nextBoxY = Styles.getDefaultRect().getHeight() + (mxConstants.LABEL_INSET * 2);
        labelHeight = Styles.getDefaultRect().getHeight() + (mxConstants.LABEL_INSET * 2);
        structureCell = (mxCell) graph.insertVertex(parent, id, label, x, y, width, height, dataStoreStyle.style());
        structureCell.setConnectable(false);
    }

    /**
     * Calls either addBoxStackStyle or addBoxHeapStyle depending on the storeStyle of this MemoryStructure
     *
     * @param _graph: the graph to add a box to
     * @param _x:     the x coordinate for the box to be added
     * @param _y:     the y coordinate for the box to be added
     * @return boolean: true if the box is added, false otherwise
     */
    protected boolean promptAddBox(mxGraph _graph, double _x, double _y)
    {
        String label = JOptionPane.showInputDialog(null, "What is " + storeStyle.boxPrompt());
        if (label != null)
        {
            boolean boxAdded = false;
            if (storeStyle == DataStoreStyle.STACK)
            {
                boxAdded = addBoxStackStyle(_graph, storeStyle.generateNewId(), label);
            }
            else
            {
                boxAdded = addBoxHeapStyle(_graph, _x, _y, storeStyle.generateNewId(), label);
            }
            if (boxAdded) Frame.projectChanged = true;
            return boxAdded;
        }
        return false;
    }

    /**
     * Prompts the user to input the VARIABLE_STYLES to be set for the component added
     * If not all VARIABLE_STYLES are provided then the component is not added
     *
     * @param graph: the graph to add the component to
     * @param box:   the box to add the component to
     * @return boolean: true if the component is added, false otherwise
     */
    protected boolean promptAddComponent(mxGraph graph, mxCell box)
    {
        if (boxInStructure(box))
        {
            String type = (String) JOptionPane.showInputDialog(null, "Select Component Type", "Component Selection", JOptionPane.PLAIN_MESSAGE, null, VARIABLE_STYLES, "Reference");
            if (type != null)
            {
                VariableStyle varStyle = VariableStyle.valueOf(type.toUpperCase());
                String label = JOptionPane.showInputDialog(null, "Enter variable name");
                if (label != null)
                {
                    addComponent(graph, box, label, varStyle.getStyle(), varStyle.generateNewId());
                    Frame.projectChanged = true;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds the user specified component to the ArrayList of components for the corresponding box
     *
     * @param graph: the graph to add the component to
     * @param box:   the box to add the component to
     * @param label: the label that will be visually displayed on this component
     * @param style: the style to be used for the components cell on the graph
     * @param id:    the ID to be associated with the component to be added
     */
    private void addComponent(mxGraph graph, mxCell box, String label, String style, String id)
    {
        mxRectangle rect = mxUtils.getLabelSize(label, Styles.getPlaceHolder(), false, mxConstants.LINE_HEIGHT);
        double textWidth = (rect.getWidth() + 5);
        double componentHeight = rect.getHeight();
        Object newComponent = graph.insertVertex(box, id, label, 5, (box.getChildCount() * componentHeight) + 5, textWidth, componentHeight, style);
    }

    /**
     * Adds a box to this MemoryStructure in the style of a Stack, that is one element on top of the other in descending order
     *
     * @param graph:   the graph to add the box to
     * @param idLabel: the label of the ID for the box to be added, this will be concatenated with the proper number
     * @param label:   the label to be placed on the box
     * @return boolean: true if box is added, false otherwise
     */
    private boolean addBoxStackStyle(mxGraph graph, String idLabel, String label)
    {
        mxCell newBox = (mxCell) graph.insertVertex(structureCell, idLabel, label, nextBoxX, nextBoxY, structureCell_width, structureCell_height / 10, Styles.getStackBoxStyle());
        nextBoxY += (structureCell_height / 10) + labelHeight;
        nextBoxX = 0; // never change

        return true;
    }

    /**
     * Adds a box to tis MemoryStructure in the style of a Heap, that is add the box where specified with no strict order
     *
     * @param graph:   the graph to add the box to
     * @param x:       the x coordinate for the box to be added
     * @param y:       the y coordinate for the bos to be added
     * @param idLabel: the label of the ID for the box to be added, this will be concatenated with the proper number
     * @param label:   the label to be placed on the box
     * @return boolean: true if box is added, false otherwise
     */
    private boolean addBoxHeapStyle(mxGraph graph, double x, double y, String idLabel, String label)
    {
        double newBoxX = x - structureCell_x;
        mxCell newBox = (mxCell) graph.insertVertex(structureCell, idLabel, label, newBoxX, y, structureCell_width / 10, structureCell_height / 10, Styles.getHeapBoxStyle());

        return true;
    }


    /**
     * Moves boxes in this structure cell up to fill any gaps created by the deletion of a cell, only used if this
     * structure cell uses Stack StoreStyle
     *
     * @param _deletedBoxY: the y coordinate of the box that was deleted
     */
    protected void shiftBoxesUp(double _deletedBoxY)
    {
        for (int i = 0; i < structureCell.getChildCount(); i++)
        {
            mxCell mxcell = (mxCell) structureCell.getChildAt(i);
            mxGeometry mxGeo = mxcell.getGeometry();
            if (mxGeo.getY() > _deletedBoxY)
            {
                frame.getGraph().translateCell(mxcell, 0, -((structureCell_height / 10) + labelHeight));
            }
        }
        nextBoxY -= ((structureCell_height / 10) + labelHeight);
    }

    /**
     * Sets the y coordinate to add the next box, this is used with Stack StoreStyle
     * Called when a file is loaded
     */
    protected void calcNextBoxY()
    {
        for (int i = 0; i < structureCell.getChildCount(); i++)
        {
            mxCell mxcell = (mxCell) structureCell.getChildAt(i);
            mxGeometry mxGeo = mxcell.getGeometry();
            if (mxGeo.getY() > nextBoxY)
            {
                nextBoxY = mxGeo.getY();
            }
        }
        nextBoxY += ((structureCell_height / 10) + labelHeight);
    }

    /**
     * Checks if the given cell is in this structure cell on the graph
     *
     * @param box: the cell that is being checked
     * @return boolean: true if the cell is in this structure cell, false otherwise
     */
    protected boolean boxInStructure(mxCell box)
    {
        if (box != null)
        {
            mxCell parent = (mxCell) box.getParent();
            if (parent == structureCell)
            {
                return true;
            }
        }
        return false;
    }

    protected mxCell getMemoryStructureCell()
    {
        return structureCell;
    }
}
