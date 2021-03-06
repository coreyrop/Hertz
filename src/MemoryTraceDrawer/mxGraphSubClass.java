package MemoryTraceDrawer;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class mxGraphSubClass extends mxGraph
{
    /**
     * Checks if the cell is a valid source for an edge.
     * Is only true if the source cell is a reference component and does not already have an outgoing edge
     *
     * @param cell: the cell that is being considered as a potential source
     * @return boolean: true if the cell is a valid source, false otherwise
     */
    @Override
    public boolean isValidSource(Object cell)
    {
        if (cell != null)
        {
            mxCell mxcell = (mxCell) cell;
            return super.isValidSource(cell) && MemoryStructure.VariableStyle.REFERENCE.instanceOf(mxcell) && mxcell.getEdgeCount() <= 1;
        }
        return false;
    }

    /**
     * Checks if the cell is a valid target for an edge
     * Is only true if the cell is a box in the Heap
     *
     * @param cell: the cell that is being considered as a potential target
     * @return boolean: true if the cell is a valid target, false otherwise
     */
    @Override
    public boolean isValidTarget(Object cell)
    {
        mxCell thisCell = (mxCell) cell;
        return super.isValidSource(cell) && MemoryStructure.DataStoreStyle.HEAP.contains(thisCell);
    }
}