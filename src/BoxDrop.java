import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

public class BoxDrop implements DropTargetListener
{
    private static final String ADD_BOX = "Add Box";
    private static final String ADD_PRIMITIVE = "Add Primitive";
    private static final String ADD_REFERENCE = "Add Reference";

    private BoxPanel box;
    private BoxPanel.ParentType parentType;
    private JPanel containingPanel;
    private Color defaultColor;

    public BoxDrop(BoxPanel _box, JPanel parent)
    {
        box = _box;
        parentType = box.getType();
        containingPanel = parent;
        defaultColor = box.getBackground();
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) { }

    @Override
    public void dragOver(DropTargetDragEvent dtde)
    {
        Transferable data = dtde.getTransferable();
        try
        {
            if (data.getTransferData(data.getTransferDataFlavors()[0]) == ADD_PRIMITIVE)
            {
                box.setBackground(Color.GREEN);
            }
            else if (data.getTransferData(data.getTransferDataFlavors()[0]) == ADD_REFERENCE)
            {
                if (parentType == BoxPanel.ParentType.STACK)
                {
                    box.setBackground(Color.GREEN);
                }
                else
                {
                    dtde.rejectDrag();
                }
            }
            else
            {
                dtde.rejectDrag();
            }
        }
        catch(Exception e)
        {
            // This "can never happen"
        }
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) { }

    @Override
    public void dragExit(DropTargetEvent dte)
    {
        containingPanel.setBackground(defaultColor);
        box.setBackground(defaultColor);
    }

    @Override
    public void drop(DropTargetDropEvent dtde)
    {
        Transferable data = dtde.getTransferable();
        try
        {
            if (data.getTransferData(data.getTransferDataFlavors()[0]).equals(ADD_PRIMITIVE))
            {
                box.add(PrimitiveLabel.makePrimitiveLabel());
            }
            else
            {
                if (parentType == BoxPanel.ParentType.STACK)
                {
                    box.add(ReferenceLabel.makeReferenceLabel());
                }
            }
            box.revalidate();
        }
        catch (Exception e) { }
        box.setBackground(defaultColor);
    }
}

