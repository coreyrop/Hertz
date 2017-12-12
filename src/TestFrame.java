import javax.swing.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.MouseListener;

public class TestFrame extends JFrame
{
    public TestFrame()
    {
        super("TestFrame");
        JPanel heapPanel = createPanel(Color.BLUE, "heap");
        JPanel stackPanel = createPanel(Color.RED, "stack");
        JToolBar buttonBar = createToolBar();

        getContentPane().setLayout(new GridLayout());

        DropTarget stackDrop = new DropTarget(stackPanel, new StackandHeapDrop(stackPanel));
        DropTarget heapDrop = new DropTarget(heapPanel, new StackandHeapDrop(heapPanel));
        stackPanel.setDropTarget(stackDrop);
        heapPanel.setDropTarget(heapDrop);

        add(stackPanel);
        add(heapPanel);
        add(buttonBar);

        ButtonManager.getInstance().setFrame(this);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MouseListener ml = new TestFrameMouseListener();
        addMouseListener(ml);
        buttonBar.addMouseListener(ml);
    }

    public static void main(String[] args)
    {
        new TestFrame();
    }
    
    /*
    * creates a JPanel with the provided label and color border
    *
    * @param _color, the color for the border
    * @param _label, String used for JPanel label
    *
    * @return a newly created JPanel with the specified fields
    */
    private JPanel createPanel(Color _color, String _label)
    {
        JPanel createdPanel = new JPanel();
        createdPanel.setLayout(new GridLayout(0, 1));
        createdPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(_color, 3), _label));
        createdPanel.setTransferHandler(new PanelTransferHandler());
        return createdPanel;
    }

    /*
     * Calls createButton() to make the desired buttons
     * then puts them inside of a new JPanel() which
     *
     * @return JPanel that holds JButtons
     */
    private JToolBar createToolBar()
    {
        JToolBar createdToolBar = new JToolBar("Tool Bar");
        createdToolBar.setLayout(new BoxLayout(createdToolBar, BoxLayout.Y_AXIS));

        JLabel addBoxLabel = new JLabel("Add Box");
        JLabel addPrimitiveLabel = new JLabel("Add Primitive");
        JLabel addReferenceLabel = new JLabel("Add Reference");

        ActionButton deleteButton = new ActionButton("Delete Component", ButtonType.DELETE);
        deleteButton.addActionListener(new DeleteActionListener());
        ButtonManager.getInstance().addButton(deleteButton);

        addBoxLabel.setTransferHandler(new TransferHandler("text"));
        addPrimitiveLabel.setTransferHandler(new TransferHandler("text"));
        addReferenceLabel.setTransferHandler(new TransferHandler("text"));

        addBoxLabel.setDropTarget(null);
        addPrimitiveLabel.setDropTarget(null);
        addReferenceLabel.setDropTarget(null);

        addBoxLabel.addMouseListener(new DragLabelMouseListener());
        addPrimitiveLabel.addMouseListener(new DragLabelMouseListener());
        addReferenceLabel.addMouseListener(new DragLabelMouseListener());

        createdToolBar.add(addBoxLabel);
        createdToolBar.add(addPrimitiveLabel);
        createdToolBar.add(addReferenceLabel);
        createdToolBar.add(deleteButton);

        deleteButton.setEnabled(false);
        return createdToolBar;
    }


}
