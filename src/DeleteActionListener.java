import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteActionListener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JComponent component = (JComponent) ButtonManager.getInstance().getComponent();
        Container parent = component.getParent();
        Container root = component.getRootPane();
        parent.remove(component);
        parent.revalidate();
        parent.repaint();
        ButtonManager.getInstance().disableAllButtons();
    }
}
