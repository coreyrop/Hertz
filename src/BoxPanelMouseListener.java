import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoxPanelMouseListener implements MouseListener
{
    private Selectable panel;

    BoxPanelMouseListener(Selectable _panel)
    {
        panel = _panel;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        ButtonManager.getInstance().setComponent(panel, ButtonManager.ComponentType.BOX);
        ((BoxPanel)panel).setSelected(true);
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        panel.setEntered(true);
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        panel.setEntered(false);
    }

}
