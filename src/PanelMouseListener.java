import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PanelMouseListener implements MouseListener
{
    private JPanel panel;
    private Color defaultColor;

    PanelMouseListener(JPanel _panel)
    {
        panel = _panel;
        defaultColor = panel.getBackground();
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        panel.setBackground(new Color(0.0f, 0.9f, 0.0f, 0.5f));
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        panel.setBackground(defaultColor);
    }

}
