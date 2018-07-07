package MemoryTraceDrawer;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxEdgeStyle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ScrollPaneLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.Map;

public class Frame extends JFrame
{
    protected static boolean projectChanged = false;
    private MemoryStructure stack;
    private MemoryStructure heap;
    private mxGraphSubClass graph;

    /**
     * Frame class constructor
     * Creates menu bar and options
     * calls makeGraph()
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
        JMenuItem saveImageItem = new JMenuItem("Export Image");
        loadItem.addActionListener(new LoadActionListener(this));
        SaveProjectActionListener spal = new SaveProjectActionListener(this);
        saveProjectItem.addActionListener(spal);
        saveImageItem.addActionListener(new SaveImageActionListener(this));
        file.add(loadItem);
        file.add(saveProjectItem);
        file.add(saveImageItem);
        menuBar.add(file);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new ApplicationCloseListener(spal));
        setVisible(true);
    }

    public static void main(String[] args)
    {
        Frame frame = new Frame();
    }

    /**
     * Initializes JGraphX
     * creates graph and sets styles
     * adds Stack and Heap to the graph
     *
     * @param screenSize: the dimension of the display the program is run on
     */
    private void makeGraph(Dimension screenSize)
    {
        graph = new mxGraphSubClass();
        Object parent = graph.getDefaultParent();
        graph.setAllowDanglingEdges(false);
        Map<String, Object> edgeStyle = graph.getStylesheet().getDefaultEdgeStyle();
        edgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.SegmentConnector);
        graph.getModel().beginUpdate();
        try
        {
            stack = new MemoryStructure(this, graph, parent, "ROOTSTACK", "Stack", 0, 0, screenSize.width / 2, screenSize.height, MemoryStructure.DataStoreStyle.STACK);
            heap = new MemoryStructure(this, graph, parent, "ROOTHEAP", "Heap", (screenSize.width / 2) + 1, 0, screenSize.width / 2, screenSize.height, MemoryStructure.DataStoreStyle.HEAP);
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

    /**
     * returns stack or heap, whichever the provided cell is, returns null if it is neither
     *
     * @param cell: a cell in graph
     * @return Stack or Heap, else null if cell is neither
     */
    public MemoryStructure convertCell(Object cell)
    {
        if (cell == heap.getMemoryStructureCell())
        {
            return heap;
        }
        else if (cell == stack.getMemoryStructureCell())
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
