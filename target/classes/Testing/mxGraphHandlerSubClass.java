package MemoryTraceDrawer;

import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Level;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxCell;

public class mxGraphHandlerSubClass extends mxGraphHandler
{
    private mxICell draggedCell;

    /**
     * @param graphComponent
     */
    public mxGraphHandlerSubClass(mxGraphComponent graphComponent)
    {
        super(graphComponent);
    }

    @Override
    public void drop(DropTargetDropEvent e)
    {
        int x = e.getLocation().x;
        int y = e.getLocation().y;
        mxICell dropCell = (mxICell) graphComponent.getCellAt(x, y);
        if (draggedCell != null && dropCell == draggedCell.getParent())
        {
            super.drop(e);
        }
        else
        {
            e.rejectDrop();
            MouseEvent event = createEvent(e);
            event.consume();
            mouseReleased(event);
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent e)
    {
        int x = e.getLocation().x;
        int y = e.getLocation().y;
        draggedCell = (mxICell) graphComponent.getCellAt(x, y);
        super.dragEnter(e);
    }
}
