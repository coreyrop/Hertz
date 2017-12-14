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
        setAlignmentY(0);
        setAlignmentX(1);

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

    @Override
    public Component add(Component comp)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        super.add(comp, gbc);
        return comp;
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
