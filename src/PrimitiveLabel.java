import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class PrimitiveLabel extends JLabel implements Selectable
{
    private String type, name, value;
    private Border actualBorder;

    public static PrimitiveLabel makePrimitiveLabel()
    {
        String type = JOptionPane.showInputDialog(null, "Enter primitive type");
        String name = JOptionPane.showInputDialog(null, "Enter variable name");
        String value = JOptionPane.showInputDialog(null, "Enter primitive value");
        if (type != null && name != null && value != null)
        {
            return new PrimitiveLabel(type, name, value);
        }
        else
        {
            return null;
        }
    }


    private PrimitiveLabel(String _type, String _name, String _value)
    {
        type = _type;
        name = _name;
        value = _value;
        setText(type + " " + name + " = " + value);

        Border selectionBorder = BorderFactory.createLineBorder(UIManager.getColor("Panel.background"), 2);
        actualBorder = BorderFactory.createEtchedBorder();
        setBorder(BorderFactory.createCompoundBorder(selectionBorder, actualBorder));

        addMouseListener(new PrimitiveandReferenceMouseListener(ButtonManager.ComponentType.PRIMITIVE));
    }

    public void updateText(String _value)
    {
        value = _value;
        setText(type + " " + name + " = " + value);
    }

    @Override
    public void setSelected(boolean _isSelected)
    {
        if (_isSelected)
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
