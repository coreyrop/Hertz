import javax.swing.*;
import java.awt.*;

public class HeapPanel extends JPanel
{
    private Color boring, boringer;

    public HeapPanel()
    {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 3), "heap"));
        setTransferHandler(new PanelTransferHandler());
        boring = UIManager.getColor("Panel.background");
        boringer = new Color(boring.getRed(), boring.getGreen(), boring.getBlue(), 0);
    }

}
