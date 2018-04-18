package Testing;

import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class mxGraphComponentSubClass extends mxGraphComponent
{
    /**
     * @param graph
     */
    public mxGraphComponentSubClass(mxGraph graph)
    {
        super(graph);
    }

    @Override
    protected mxGraphHandler createGraphHandler()
    {
        return new mxGraphHandlerSubClass(this);
    }
}
