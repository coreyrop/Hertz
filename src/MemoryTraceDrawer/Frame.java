package MemoryTraceDrawer;

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

    /*
        Frame class constructor
        Creates menu bar and options
        calls makeGraph()
     */
    public Frame()
    {
        super("Memory");
        setLayout(new FlowLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        makeGraph(screenSize);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load Project");
        JMenuItem saveProjectItem = new JMenuItem("Save Project");
        JMenuItem saveImageItem = new JMenuItem("Save Image");
        loadItem.addActionListener(new LoadActionListener(this));
        saveProjectItem.addActionListener(new SaveProjectActionListener(this));
        saveImageItem.addActionListener(new SaveImageActionListener(this));
        file.add(saveImageItem);
        file.add(loadItem);
        file.add(saveProjectItem);
        menuBar.add(file);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        Frame frame = new Frame();
    }

    /*
        Initializes JGraphX
        creates graph and sets styles
        adds Stack and Heap to the graph
        @param _screenSize: the dimension of the display the program is run on
     */
    private void makeGraph(Dimension _screenSize)
    {
        graph = new mxGraphSubClass();
        Object parent = graph.getDefaultParent();
        graph.setAllowDanglingEdges(false);
        Map<String, Object> edgeStyle = graph.getStylesheet().getDefaultEdgeStyle();
        edgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.SegmentConnector);
        graph.getModel().beginUpdate();
        try
        {
            stack = new MemoryStructure(this, graph, parent, "ROOTSTACK", "Stack", 0, 0, _screenSize.width / 2, _screenSize.height, MemoryStructure.DataStoreStyle.STACK);
            heap = new MemoryStructure(this, graph, parent, "ROOTHEAP", "Heap", (_screenSize.width / 2) + 1, 0, _screenSize.width / 2, _screenSize.height, MemoryStructure.DataStoreStyle.HEAP);
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
        graphComponent.getGraphControl().addMouseListener(new Listener(this, graphComponent));
        graphComponent.getGraphHandler().setRemoveCellsFromParent(false);
    }

    /*
        @param _cell: a cell in graph
        @return Stack or Heap, else null if _cell is neither
     */
    public MemoryStructure convertCell(Object _cell)
    {
        if (_cell == heap.getMemoryStructureCell())
        {
            return heap;
        }
        else if (_cell == stack.getMemoryStructureCell())
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
