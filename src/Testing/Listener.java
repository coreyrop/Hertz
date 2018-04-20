package Testing;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Listener extends MouseAdapter
{
    private Frame frame;
    private mxGraphSubClass graph;
    private mxGraphComponent graphComponent;
    private mxCell draggedCell;
    private boolean popupTrigger;

    public Listener(Frame _frame, mxGraphSubClass _graph, mxGraphComponent _graphComponent)
    {
        frame = _frame;
        graph = _graph;
        graphComponent = _graphComponent;
        popupTrigger = false;
    }


    public void mouseClicked(MouseEvent e)
    {
        final Object cell = graphComponent.getCellAt(e.getX(), e.getY());
        if (cell != null)
        {
            if (popupTrigger)
            {
                MemoryStructure cellMemoryStructure = frame.convertCell(cell);
                if (cellMemoryStructure == null)
                {
                    // We have a box or variable -- something which has a popup menu
                    Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), graphComponent);
                    CellPopupMenu menu = new CellPopupMenu(graph, cell);
                    menu.show(graphComponent, pt.x, pt.y);
                }
            }
            else
            {
                MemoryStructure cellMemoryStructure = frame.convertCell(cell);
                if (cellMemoryStructure != null)
                {
                    graph.getModel().beginUpdate();
                    try
                    {
                        cellMemoryStructure.addBox(graph, cell, e.getX(), e.getY());
                    }
                    finally
                    {
                        graph.getModel().endUpdate();
                    }
                }
                else
                {
                    mxICell mxicell = (mxICell) cell;
                    mxICell mxiparent = mxicell.getParent();
                    MemoryStructure parentStruct = frame.convertCell(mxiparent);

                    if (parentStruct != null)
                    {
                        graph.getModel().beginUpdate();
                        try
                        {
                            parentStruct.promptAddComponent(graph, cell);
                        }
                        finally
                        {
                            graph.getModel().endUpdate();
                        }
                    }
                }
            }
        }
    }

    public void mousePressed(MouseEvent e)
    {
        popupTrigger = e.isPopupTrigger();
    }
}
