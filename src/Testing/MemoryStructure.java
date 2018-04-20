package Testing;

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
    private double structureCell_x, structureCell_y, structureCell_width, structureCell_height, nextBoxX, nextBoxY, labelHeight;
    private dataStoreStyle storeStyle;
    private Frame frame;
    private HashMap<mxCell, ArrayList<mxCell>> boxes;

    private static int ID_COUNT;

    static
    {
        ID_COUNT = 0;
    }

    public static void setIdCount(int _count)
    {
        ID_COUNT = _count;
    }

    public enum dataStoreStyle
    {
        STACK, HEAP
    }

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

        public void setIdCount(int _latestIDCount)
        {
            idCount = _latestIDCount;
        }
    }

    public MemoryStructure(Frame _frame, mxGraph graph, Object parent, String id, Object value, double x, double y, double width, double height, dataStoreStyle _storeStyle)
    {
        frame = _frame;
        structureCell_x = x;
        structureCell_y = y;
        structureCell_width = width;
        structureCell_height = height;
        storeStyle = _storeStyle;
        nextBoxX = 0;
        nextBoxY = Styles.getDefaultRect().getHeight() + (mxConstants.LABEL_INSET * 2);
        labelHeight = Styles.getDefaultRect().getHeight() + (mxConstants.LABEL_INSET * 2);
        boxes = new HashMap<>();

        if (storeStyle == dataStoreStyle.STACK)
        {
            structureCell = (mxCell) graph.insertVertex(parent, id, value, x, y, width, height, Styles.getStackStyle());
        }
        else
        {
            structureCell = (mxCell) graph.insertVertex(parent, id, value, x, y, width, height, Styles.getHeapStyle());
        }
        structureCell.setConnectable(false);

    }

    public boolean addBox(mxGraph graph, Object _cell, double _x, double _y)
    {
        if (storeStyle == dataStoreStyle.STACK)
        {
            return addBoxStackStyle(graph, _cell, _x, _y, storeStyle.name());
        }
        else
        {
            return addBoxHeapStyle(graph, _cell, _x, _y, storeStyle.name());
        }
    }

    public void promptAddComponent(mxGraph _graph, Object _box)
    {
        if (boxInStructure(_box))
        {
            String[] values = new String[variableStyle.values().length];
            for (int i = 0; i < values.length; i++ ) {
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
                }
            }
        }
    }

    public void addComponent(mxGraph _graph, Object _box, String _label, String _style, String type)
    {
        mxRectangle rect = mxUtils.getLabelSize(_label, Styles.getPlaceHolder(), false, mxConstants.LINE_HEIGHT);
        double textWidth = (rect.getWidth() + 5);
        ArrayList<mxCell> components = boxes.get(_box);
        double componentHeight = rect.getHeight();
        Object newComponent = _graph.insertVertex(_box, type, _label, 5, (components.size() * componentHeight) + 5, textWidth, componentHeight, _style);
        components.add((mxCell)newComponent);
    }

    private boolean addBoxStackStyle(mxGraph graph, Object _cell, double _x, double _y, String idLabel)
    {
        if (_cell != structureCell)
        {
            System.err.println("caught exception");
            return false;
        }

        String label = JOptionPane.showInputDialog(null, "Enter Frame Label");
        if (label != null)
        {
            mxCell newBox = (mxCell) graph.insertVertex(structureCell, idLabel+Integer.toString(ID_COUNT), label, nextBoxX, nextBoxY, structureCell_width, structureCell_height / 10, Styles.getStackBoxStyle());
            nextBoxY += (structureCell_height / 10) + labelHeight;
            nextBoxX = 0; // never change
            ID_COUNT += 1;
            ArrayList<mxCell> components = new ArrayList<>();
            boxes.put(newBox, components);
            return true;
        }
        return false;
    }

    private boolean addBoxHeapStyle(mxGraph graph, Object _cell, double _x, double _y, String idLabel)
    {
        if (_cell != structureCell)
        {
            System.err.println("caught exception");
            return false;
        }
        String label = JOptionPane.showInputDialog(null, "Enter Instance Label");

        if (label != null)
        {
            double newBoxX = _x - structureCell_x;
            mxCell newBox = (mxCell) graph.insertVertex(structureCell, idLabel+Integer.toString(ID_COUNT), label, newBoxX, _y, structureCell_width / 10, structureCell_height / 10, Styles.getHeapBoxStyle());
            newBox.setSource(null);
            ID_COUNT += 1;
            ArrayList<mxCell> components = new ArrayList<>();
            boxes.put(newBox, components);
            return true;
        }
        return false;
    }

    public boolean boxInStructure(Object _box)
    {
        return _box != null && boxes.containsKey(_box);
    }

    public mxCell getMemoryStructureCell()
    {
        return structureCell;
    }

    public Object getMemoryStructureCellObject()
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

    public void clearBoxes()
    {
        mxCell[] instanceBoxes = boxes.keySet().toArray(new mxCell[boxes.size()]);
        frame.getGraph().removeCells(instanceBoxes);
        boxes = new HashMap<>();
    }

    public void setNextBoxY(double _greatestBoxY)
    {
        nextBoxY = _greatestBoxY + (structureCell_height / 10) + labelHeight;
    }
}
