import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

public class BoxDrop implements DropTargetListener {
    private static final String ADD_BOX = "Add Box";
    private static final String ADD_PRIMITIVE = "Add Primitive";

    private BoxPanel box;
    private JPanel containingPanel;
    private Color defaultColor;

    public BoxDrop(BoxPanel _box, JPanel parent)

    {
        box = _box;
        containingPanel = parent;
        defaultColor = box.getBackground();
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        Transferable data = dtde.getTransferable();
        try {
            if (data.getTransferData(data.getTransferDataFlavors()[0]).equals(ADD_BOX)) {
                containingPanel.setBackground(new Color(0.0f, 0.9f, 0.0f, 0.5f));
            } else {
                box.setBackground(new Color(0.0f, 0.9f, 0.0f, 0.5f));
            }
        } catch (Exception e)
        {

        }
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        containingPanel.setBackground(defaultColor);
        box.setBackground(defaultColor);
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        Transferable data = dtde.getTransferable();
        try {
            if (data.getTransferData(data.getTransferDataFlavors()[0]).equals(ADD_BOX)) {
                BoxPanel newPanel = BoxPanel.makeBox(containingPanel);
                if (newPanel != null) {
                    box.add(newPanel);
                }
            } else if (data.getTransferData(data.getTransferDataFlavors()[0]).equals(ADD_PRIMITIVE)) {
                box.add(new PrimitiveLabel());
            } else {
                box.add(new ReferenceLabel());
            }
            box.revalidate();
        } catch (Exception e) {

        }
        box.setBackground(defaultColor);
    }
}
