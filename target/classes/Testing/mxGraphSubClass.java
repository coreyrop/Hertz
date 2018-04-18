package Testing;
import com.mxgraph.view.mxGraph;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxCell;

public class mxGraphSubClass extends mxGraph
{
    private Frame frame;

    public mxGraphSubClass(Frame _frame)
    {
        frame = _frame;
    }

    @Override
    public boolean isValidSource(Object cell)
    {
        if (cell != null)
        {
            mxCell mxcell = (mxCell) cell;
            String cellId = mxcell.getId();
            return super.isValidSource(cell) && cellId.startsWith(MemoryStructure.variableStyle.REFERENCE.toString()) && mxcell.getEdgeCount() <= 1;
        }
        return false;
    }

    @Override
    public boolean isValidTarget(Object cell)
    {
        mxCell thisCell = (mxCell) cell;
        String cellId = thisCell.getId();
        return super.isValidSource(cell) && cellId.startsWith(MemoryStructure.dataStoreStyle.HEAP.name());
    }

}