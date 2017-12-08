import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PrimitiveandReferenceMouseListener implements MouseListener
{

    private ButtonManager.ComponentType type;

    public PrimitiveandReferenceMouseListener(ButtonManager.ComponentType _type)
    {
        type = _type;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        Selectable comp = (Selectable) e.getSource();
        ButtonManager.getInstance().setComponent(comp, type);
        comp.setSelected(true);
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        Selectable comp = (Selectable) e.getSource();
        comp.setEntered(true);
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        Selectable comp = (Selectable) e.getSource();
        comp.setEntered(false);
    }
}
