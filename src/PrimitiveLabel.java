import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class PrimitiveLabel extends JLabel
{
    private String type, name, value;



    private PrimitiveLabel()
    {
        type = JOptionPane.showInputDialog(null, "Enter primitive type");
        name = JOptionPane.showInputDialog(null, "Enter variable name");
        value = JOptionPane.showInputDialog(null, "Enter primitive value");
        setText(type + " " + name + " = " + value);
    }

    public void updateText(String _value)
    {
        value = _value;
        setText(type + " " + name + " = " + value);
    }
}
