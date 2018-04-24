package MemoryTraceDrawer;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MemoryStructure
{
    private mxCell structureCell;
    private double structureCell_x, structureCell_width, structureCell_height, nextBoxX, nextBoxY, labelHeight;
    private dataStoreStyle storeStyle;
    private Frame frame;
    private HashMap<mxCell, ArrayList<mxCell>> boxes;

    // Used to assign ID values to boxes for the Stack and Heap
    private static int ID_COUNT;

    static
    {
        ID_COUNT = 0;
    }

    /*
        Updates ID_COUNT to the passed value
        used for when files are loaded
        @ param int _count: the value to be set for ID_COUNT
     */
    public static void setIdCount(int _count)
    {
        ID_COUNT = _count;
    }

    // Used to differentiate the styles for Stack and Heap
    public enum dataStoreStyle
    {
        STACK, HEAP
    }

    /*
        Used to manage the Reference and Primitive fields used in the graph
        Tracks ID count for both Primitives and References
        Allows the idCount to be set to a specified value, this is used for when files are loaded
     */
    public enum variableStyle
    {
        REFERENCE("Reference"), PRIMITIVE("Primitive");
        private String displayName;
        private int idCount;

        variableStyle(String _displayName)
        {
            displayName = _displayName;
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

    /*
        MemoryStructure class constructor
        Initializes all the fields
        Adds this MemoryStructure to the graph for visual display
        Sets the style for this MemoryStructure in the graph
        @ param Frame _frame: the Frame instance for this MemoryStructure to be associated with
        @ param mxGraph _graph: the graph that this MemoryStructure will be in
        @ param Object _parent: the parent of this MemoryStructure, needed for the graph
        @ param String _id: the ID to be associated with this MemoryStructure
        @ param Object _label: the label that will be visually displayed on this MemoryStructure
        @ param double _x: the x coordinate for this MemoryStructure
        @ param double _y: the y coordinate for this MemoryStructure
        @ param double _width: the width of this MemoryStructure
        @ param double _height: the height of this MemoryStructure
        @ param dataStoreStyle _storeStyle: the style for this MemoryStructure to plot boxes, either STACK or HEAP
     */
    public MemoryStructure(Frame _frame, mxGraph _graph, Object _parent, String _id, Object _label, double _x, double _y, double _width, double _height, dataStoreStyle _storeStyle)
    {
        frame = _frame;
        structureCell_x = _x;
        structureCell_width = _width;
        structureCell_height = _height;
        storeStyle = _storeStyle;
        nextBoxX = 0;
        nextBoxY = Styles.getDefaultRect().getHeight() + (mxConstants.LABEL_INSET * 2);
        labelHeight = Styles.getDefaultRect().getHeight() + (mxConstants.LABEL_INSET * 2);
        boxes = new HashMap<>();

        if (storeStyle == dataStoreStyle.STACK)
        {
            structureCell = (mxCell) _graph.insertVertex(_parent, _id, _label, _x, _y, _width, _height, Styles.getStackStyle());
        }
        else
        {
            structureCell = (mxCell) _graph.insertVertex(_parent, _id, _label, _x, _y, _width, _height, Styles.getHeapStyle());
        }
        structureCell.setConnectable(false);

    }

    /*
        Calls either addBoxStackStyle or addBoxHeapStyle depending on the storeStyle of this MemoryStructure
        @ param mxGraph _graph: the graph to add a box to
        @ param Object _parent: the parent cell for the box to be added
        @ param double _x: the x coordinate for the box to be added
        @ param double _y: the y coordinate for the box to be added

        @ returns boolean: true if the box is added, false otherwise
     */
    public boolean promptAddBox(mxGraph _graph, Object _parent, double _x, double _y)
    {
        if (storeStyle == dataStoreStyle.STACK)
        {
            return addBoxStackStyle(_graph, _parent, storeStyle.name());
        }
        else
        {
            return addBoxHeapStyle(_graph, _parent, _x, _y, storeStyle.name());
        }
    }

    /*
        Prompts the user to input the values to be set for the component added
        If not all values are provided then the component is not added
        @ param mxGraph _graph: the graph to add the component to
        @ param Object _box: the box to add the component to

        @ returns boolean: true if the component is added, false otherwise
     */
    public boolean promptAddComponent(mxGraph _graph, Object _box)
    {
        if (boxInStructure(_box))
        {
            String[] values = new String[variableStyle.values().length];
            for (int i = 0; i < values.length; i++)
            {
                values[i] = variableStyle.values()[i].toString();
            }
            String type = (String) JOptionPane.showInputDialog(null, "Select Component Type", "Component Selection", JOptionPane.PLAIN_MESSAGE, null, values, "Reference");
            if (type != null)
            {
                String label = JOptionPane.showInputDialog(null, "Enter Component Label");
                if (label != null)
                {
                    String style, idString;
                    if (type.equals(variableStyle.REFERENCE.toString()))
                    {
                        style = Styles.getReferenceStyle();
                        idString = variableStyle.REFERENCE.generateNewId();
                    }
                    else
                    {
                        style = Styles.getPrimitiveStyle();
                        idString = variableStyle.PRIMITIVE.generateNewId();
                    }
                    addComponent(_graph, _box, label, style, idString);
                    return true;
                }
            }
        }
        return false;
    }

    /*
        Adds the user specified component to the ArrayList of components for the corresponding box
        @ param mxGraph _graph: the graph to add the component to
        @ param Object _box: the box to add the component to
        @ param String _label: the label that will be visually displayed on this component
        @ param String _style: the style to be used for the components cell on the graph
        @ param String _id: the ID to be associated with the component to be added
     */
    private void addComponent(mxGraph _graph, Object _box, String _label, String _style, String _id)
    {
        mxRectangle rect = mxUtils.getLabelSize(_label, Styles.getPlaceHolder(), false, mxConstants.LINE_HEIGHT);
        double textWidth = (rect.getWidth() + 5);
        ArrayList<mxCell> components = boxes.get(_box);
        double componentHeight = rect.getHeight();
        Object newComponent = _graph.insertVertex(_box, _id, _label, 5, (components.size() * componentHeight) + 5, textWidth, componentHeight, _style);
        components.add((mxCell) newComponent);
    }

    /*
        Adds a box to this MemoryStructure in the style of a Stack, that is one element on top of the other in descending order
        @ param mxGraph _graph: the graph to add the box to
        @ param Object _parent: the parent cell for the box to be added
        @ param String _idLabel: the label of the ID for the box to be added, this will be concatenated with the proper number

        @ returns boolean: true if box is added, false otherwise
     */
    private boolean addBoxStackStyle(mxGraph _graph, Object _parent, String _idLabel)
    {
        if (_parent != structureCell)
        {
            System.err.println("caught exception");
            return false;
        }

        String label = JOptionPane.showInputDialog(null, "Enter Frame Label");
        if (label != null)
        {
            mxCell newBox = (mxCell) _graph.insertVertex(structureCell, _idLabel + Integer.toString(ID_COUNT), label, nextBoxX, nextBoxY, structureCell_width, structureCell_height / 10, Styles.getStackBoxStyle());
            nextBoxY += (structureCell_height / 10) + labelHeight;
            nextBoxX = 0; // never change
            ID_COUNT += 1;
            ArrayList<mxCell> components = new ArrayList<>();
            boxes.put(newBox, components);
            return true;
        }
        return false;
    }

    /*
        Adds a box to tis MemoryStructure in the style of a Heap, that is add the box where specified with no strict order
        @ param mxGraph _graph: the graph to add the box to
        @ param Object _parent: the parent of the box to be added
        @ param double _x: the x coordinate for the box to be added
        @ param double _y: the y coordinate for the bos to be added
        @ param String _idLabel: the label of the ID for the box to be added, this will be concatenated with the proper number

        @ returns boolean: true if box is added, false otherwise
     */
    private boolean addBoxHeapStyle(mxGraph _graph, Object _parent, double _x, double _y, String _idLabel)
    {
        if (_parent != structureCell)
        {
            System.err.println("caught exception");
            return false;
        }
        String label = JOptionPane.showInputDialog(null, "Enter Instance Label");

        if (label != null)
        {
            double newBoxX = _x - structureCell_x;
            mxCell newBox = (mxCell) _graph.insertVertex(structureCell, _idLabel + Integer.toString(ID_COUNT), label, newBoxX, _y, structureCell_width / 10, structureCell_height / 10, Styles.getHeapBoxStyle());
            newBox.setSource(null);
            ID_COUNT += 1;
            ArrayList<mxCell> components = new ArrayList<>();
            boxes.put(newBox, components);
            return true;
        }
        return false;
    }

    /*
        Sets the y coordinate to add the next box, this is used with Stack StoreStyle
        Called when a file is loaded
        @ param double _greatestBoxY: the y coordinate of the last box added to this MemoryStructure
     */
    public void setNextBoxY(double _greatestBoxY)
    {
        nextBoxY = _greatestBoxY + (structureCell_height / 10) + labelHeight;
    }

    public boolean boxInStructure(Object _box)
    {
        return _box != null && boxes.containsKey(_box);
    }

    public mxCell getMemoryStructureCell()
    {
        return structureCell;
    }

    public HashMap<mxCell, ArrayList<mxCell>> getBoxes()
    {
        return boxes;
    }

    public void setBoxes(HashMap<mxCell, ArrayList<mxCell>> _boxes)
    {
        boxes = _boxes;
    }

    public void removeBox(Object _cell)
    {
        boxes.remove(_cell);
    }

    public void clearBoxes()
    {
        mxCell[] instanceBoxes = boxes.keySet().toArray(new mxCell[boxes.size()]);
        frame.getGraph().removeCells(instanceBoxes);
        boxes = new HashMap<>();
    }
}
