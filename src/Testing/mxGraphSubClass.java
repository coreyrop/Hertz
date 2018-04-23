package Testing;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class mxGraphSubClass extends mxGraph
{
    /*
        Checks if the cell is a valid source for an edge.
        Is only true if the source cell is a reference component and does not already have an outgoing edge
        @ param Object _cell: the cell that is being considered as a potential source

        @ return boolean: true if the cell is a valid source, false otherwise
     */
    @Override
    public boolean isValidSource(Object _cell)
    {
        if (_cell != null)
        {
            mxCell mxcell = (mxCell) _cell;
            String cellId = mxcell.getId();
            return super.isValidSource(_cell) && cellId.startsWith(MemoryStructure.variableStyle.REFERENCE.toString()) && mxcell.getEdgeCount() <= 1;
        }
        return false;
    }

    /*
        Checks if the cell is a valid target for an edge
        Is only true if the cell is a box in the Heap
        @ param Object _cell: the cell that is being considered as a potential target

        @ return boolean: true if the cell is a valid target, false otherwise
     */
    @Override
    public boolean isValidTarget(Object _cell)
    {
        mxCell thisCell = (mxCell) _cell;
        String cellId = thisCell.getId();
        return super.isValidSource(_cell) && cellId.startsWith(MemoryStructure.dataStoreStyle.HEAP.name());
    }

}