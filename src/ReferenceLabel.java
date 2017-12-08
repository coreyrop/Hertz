import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ReferenceLabel extends JLabel implements Selectable
{
    private Border actualBorder;

    public static ReferenceLabel makeReferenceLabel()
    {
        String reference = JOptionPane.showInputDialog(null, "Enter reference value");
        if (reference != null)
        {
            return new ReferenceLabel(reference);
        }
        else
        {
            return null;
        }
    }

    private ReferenceLabel(String _reference)
    {
        setText(_reference);

        Border selectionBorder = BorderFactory.createLineBorder(UIManager.getColor("Panel.background"), 2);
        actualBorder = BorderFactory.createEtchedBorder();
        setBorder(BorderFactory.createCompoundBorder(selectionBorder, actualBorder));

        addMouseListener(new PrimitiveandReferenceMouseListener(ButtonManager.ComponentType.REFERENCE));
    }


    @Override
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
            this.setBackground(UIManager.getColor("Label.background"));
        }
    }
}
