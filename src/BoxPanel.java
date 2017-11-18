import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;

public class BoxPanel extends JPanel
{

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
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), label));
        setLayout(new GridLayout(0,1));

        addMouseListener(new PanelMouseListener(this));
        DropTarget boxDrop = new DropTarget(this, new BoxDrop(this, parent));
        setDropTarget(boxDrop);

    }
}
