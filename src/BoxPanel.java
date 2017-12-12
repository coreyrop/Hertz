import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.dnd.DropTarget;

public class BoxPanel extends JPanel implements Selectable
{
    private GridBagConstraints gbc;
    private Border actualBorder;
    private int gridY;
    public static BoxPanel makeBox(JPanel parent)
    {
        String label = JOptionPane.showInputDialog(null, "Enter Frame Label");
        if (label != null)
        {
            return new BoxPanel(label, parent);
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

//        setLayout(new GridLayout(0,1));
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;


        gridY = 0;

//        JButton left = new JButton("left");
//        JButton center = new JButton("center");
//        JButton right = new JButton("right");
//        left.setVisible(true);
//        center.setVisible(true);
//        right.setVisible(true);
//        add(left, gbc);
//        gbc.gridx = 1;
//        add(center, gbc);
//        gbc.gridx = 2;
//        add(right, gbc);

        gbc.gridx = 0;
        gridY = 1;
        gbc.gridwidth = 3;

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

    public void addUnderConstariants(JLabel _label)
    {
        add(_label, gbc);
        gbc.gridy++;
    }

//    public void gbcUpdateGridY()
//    {
//        gbc.gridy = gridY;
//        gridY++;
//    }
//
//    public GridBagConstraints getGbc()
//    {
//        return gbc;
//    }


}
