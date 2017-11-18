import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ReferenceLabel extends JLabel
{
    public ReferenceLabel()
    {
        String reference = JOptionPane.showInputDialog(null, "Enter reference value");
        setText(reference);
        //setBorder(new MatteBorder(1,3,1,1, Color.BLACK));
    }


}
