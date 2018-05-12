package MemoryTraceDrawer;

import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Listener extends MouseAdapter
{
    private Frame frame;
    private mxGraphComponent graphComponent;
    private boolean popupTrigger;

    /*
        Listener class constructor
        @param _frame: the Frame instance that is connected to this listener
        @param _graphComponent: the mxGraphComponent associated with the graph used in Frame,
        used to get the mxCell Objects that are selected
     */
    public Listener(Frame _frame, mxGraphComponent _graphComponent)
    {
        frame = _frame;
        graphComponent = _graphComponent;
        popupTrigger = false;
    }


    /*
        Handles mouseClicked events.
        Gets the mxCell Object that has been clicked, based on the coordinates of the event.
        Once the proper mxCell Object is obtained the correct respective popup action is executed:
            If the click is a popupTrigger: open rename / delete option menu
            For Stack and Heap: add a box to the MemoryStructure
            For box in Stack or Heap: add a field to the box
     */
    public void mouseClicked(MouseEvent e)
    {
        mxGraphSubClass graph = frame.getGraph();
        final Object cell = graphComponent.getCellAt(e.getX(), e.getY());
        if (cell != null)
        {
            // Event is a popupTrigger, if mxCell Object is valid then display popup to rename / delete mxCell
            if (popupTrigger)
            {
                MemoryStructure cellMemoryStructure = frame.convertCell(cell);
                if (cellMemoryStructure == null)
                {
                    // We have a box or variable -- something which has a popup menu
                    Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), graphComponent);
                    CellPopupMenu menu = new CellPopupMenu(graph, cell, frame);
                    menu.show(graphComponent, pt.x, pt.y);
                }
            }
            else
            {
                MemoryStructure cellMemoryStructure = frame.convertCell(cell);

                // Check if mxCell Object is Stack or Heap
                if (cellMemoryStructure != null)
                {
                    graph.getModel().beginUpdate();
                    try
                    {
                        cellMemoryStructure.promptAddBox(graph, e.getX(), e.getY());
                    }
                    finally
                    {
                        graph.getModel().endUpdate();
                    }
                }
                // mxCell Object is not Stack or Heap
                else
                {
                    mxCell mxcell = (mxCell) cell;
                    mxICell mxiparent = mxcell.getParent();
                    MemoryStructure parentStruct = frame.convertCell(mxiparent);

                    if (parentStruct != null)
                    {
                        graph.getModel().beginUpdate();
                        try
                        {
                            parentStruct.promptAddComponent(graph, mxcell);
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

    /*
        sets popupTrigger flag to be the case of the Event
     */
    public void mousePressed(MouseEvent e)
    {
        popupTrigger = e.isPopupTrigger();
    }
}
