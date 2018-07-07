package MemoryTraceDrawer;

import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class mxGraphComponentSubClass extends mxGraphComponent
{
    /**
     * @param graph: graph instance that this instance will be associated with
     */
    protected mxGraphComponentSubClass(mxGraph graph)
    {
        super(graph);
    }

    /**
     * Implemented to allow use of the mxGraphHandlerSubClass
     */
    @Override
    protected mxGraphHandler createGraphHandler()
    {
        return new mxGraphHandlerSubClass(this);
    }
}
