import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.dnd.DropTarget;

public class BoxPanel extends JPanel implements Selectable
{
    private Border actualBorder;
    public static BoxPanel makeBox(JPanel parent)
    {
        String label = JOptionPane.showInputDialog(null, "Enter Frame Label");
        if (label != null)
        {
            BoxPanel retVal = new BoxPanel(label, parent);
            return retVal;
        }
        else
        {
            return null;
        }
    }

    private BoxPanel(String label, JPanel parent)
    {
        Border selectionBorder = BorderFactory.createLineBorder(UIManager.getColor("Panel.background"), 2);
        actualBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), label);
        setBorder(BorderFactory.createCompoundBorder(selectionBorder, actualBorder));

        setLayout(new GridLayout(0,1));

        addMouseListener(new BoxPanelMouseListener(this));
        DropTarget boxDrop = new DropTarget(this, new BoxDrop(this, parent));
        setDropTarget(boxDrop);
    }

    public void setSelected(boolean isSelected)
    {
        if (isSelected)
        {
            Border selectionBorder = BorderFactory.createLineBorder(UIManager.getColor(Color.GRAY), 2);
            setBorder(BorderFactory.createCompoundBorder(selectionBorder, actualBorder));
        }
        else
        {
            Border selectionBorder = BorderFactory.createLineBorder(UIManager.getColor("Panel.background"), 2);
            setBorder(BorderFactory.createCompoundBorder(selectionBorder, actualBorder));
        }
    }

    @Override
    public void setEntered(boolean _isEntered)
    {
        if (_isEntered)
        {
            this.setBackground(Color.GREEN);
        }
        else
        {
            this.setBackground(UIManager.getColor("Panel.background"));
        }
    }
}
