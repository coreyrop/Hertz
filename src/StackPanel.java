import javax.swing.*;
import java.awt.*;

public class StackPanel extends JPanel
{
    private JPanel filler;
    private GridBagConstraints gbcFiller;
    private GridBagLayout gbl;

    public StackPanel()
    {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED, 3), "stack"));
        gbl = new GridBagLayout();
        setLayout(gbl);
        setTransferHandler(new PanelTransferHandler());
        filler = new JPanel();
        Color boring = UIManager.getColor("Panel.background");
        Color boringer = new Color(boring.getRed(), boring.getGreen(), boring.getBlue(), 0);
        filler.setBackground(boringer);
        gbcFiller = makeGBCFiller();
        super.add(filler, gbcFiller);
    }

    @Override
    public Component add(Component comp)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = getComponentCount()-1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbcFiller.gridy = gbc.gridy+1;
        gbl.setConstraints(filler, gbcFiller);
        super.add(comp, gbc);
        revalidate();
        return comp;
    }

    private GridBagConstraints makeGBCFiller()
    {
        GridBagConstraints retVal = new GridBagConstraints();
        retVal.gridx = 0;
        retVal.gridy = GridBagConstraints.RELATIVE;
        retVal.weightx = 1;
        retVal.weighty = 1;
        retVal.fill = GridBagConstraints.BOTH;
        return retVal;
    }
}
