import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TestFrameMouseListener implements MouseListener
{
    @Override
    public void mouseClicked(MouseEvent e)
    {
        ButtonManager.getInstance().undoSetComponent();
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}
