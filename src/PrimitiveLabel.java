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

        Color boring = UIManager.getColor("Panel.background");
        Color boringer = new Color(boring.getRed(), boring.getGreen(), boring.getBlue(), 0);
        Border selectionBorder = BorderFactory.createLineBorder(boringer, 2);
        setBorder(selectionBorder);

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
            setBorder(selectionBorder);
        }
        else
        {
            Color boring = UIManager.getColor("Panel.background");
            Color boringer = new Color(boring.getRed(), boring.getGreen(), boring.getBlue(), 0);
            Border selectionBorder = BorderFactory.createLineBorder(boringer, 2);
            setBorder(selectionBorder);
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
