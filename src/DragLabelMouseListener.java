import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DragLabelMouseListener implements MouseListener
{
    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        ButtonManager.getInstance().undoSetComponent();

        JComponent comp = (JComponent) e.getSource();
        TransferHandler handler = comp.getTransferHandler();
        handler.exportAsDrag(comp, e, TransferHandler.COPY);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }
}
