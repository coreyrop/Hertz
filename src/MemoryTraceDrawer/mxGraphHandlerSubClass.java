package MemoryTraceDrawer;

import com.mxgraph.model.mxICell;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.mxGraphComponent;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;


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

    /*
        Overridden to prevent the moving of cells outside of their parents
     */
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

    /*
        Get the cell that is being dragged and save it as a field
     */
    @Override
    public void dragEnter(DropTargetDragEvent e)
    {
        int x = e.getLocation().x;
        int y = e.getLocation().y;
        draggedCell = (mxICell) graphComponent.getCellAt(x, y);
        super.dragEnter(e);
    }
}
