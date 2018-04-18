package Testing;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxEdgeStyle;

import javax.swing.*;

import java.awt.*;

import java.util.Map;

public class Frame extends JFrame
{
    private MemoryStructure stack;
    private MemoryStructure heap;
    private mxGraphSubClass graph;

    public Frame()
    {
        super("Memory");
        setLayout(new FlowLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        makeGraph(screenSize);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(new LoadActionListener(this));
        file.add(loadItem);
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new SaveActionListener(this));
        file.add(saveItem);
        menuBar.add(file);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        Frame frame = new Frame();
    }

    private void makeGraph(Dimension _screenSize)
    {
        graph = new mxGraphSubClass(this);
        Object parent = graph.getDefaultParent();
        graph.setAllowDanglingEdges(false);
        Map<String, Object> edgeStyle = graph.getStylesheet().getDefaultEdgeStyle();
        edgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.SegmentConnector);
        graph.getModel().beginUpdate();
        try
        {
            stack = new MemoryStructure(this, graph, parent, "ROOTSTACK", "Stack", 0, 0, _screenSize.width / 2, _screenSize.height, MemoryStructure.dataStoreStyle.STACK);
            heap = new MemoryStructure(this, graph, parent, "ROOTHEAP", "Heap", (_screenSize.width / 2) + 1, 0, _screenSize.width / 2, _screenSize.height, MemoryStructure.dataStoreStyle.HEAP);
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        final mxGraphComponent graphComponent = new mxGraphComponentSubClass(graph);
        ScrollPaneLayout spl = new ScrollPaneLayout();
        spl.setHorizontalScrollBarPolicy(ScrollPaneLayout.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spl.setVerticalScrollBarPolicy(ScrollPaneLayout.VERTICAL_SCROLLBAR_AS_NEEDED);
        graphComponent.setLayout(spl);
        getContentPane().add(graphComponent);
        graphComponent.getGraphControl().addMouseListener(new Listener(this, graph, graphComponent));
        graphComponent.getGraphHandler().setRemoveCellsFromParent(false);
    }

    public MemoryStructure convertCell(Object cell)
    {
        if (cell == heap.getMemoryStructureCellObject())
        {
            return heap;
        }
        else if (cell == stack.getMemoryStructureCellObject())
        {
            return stack;
        }
        else
        {
            return null;
        }
    }

    public MemoryStructure getHeap()
    {
        return heap;
    }

    public MemoryStructure getStack()
    {
        return stack;
    }

    public mxGraphSubClass getGraph()
    {
        return graph;
    }
}
