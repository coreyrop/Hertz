import java.util.ArrayList;

public class ButtonManager
{
    private TestFrame frame;

    enum ComponentType
    {
        BOX, REFERENCE, PRIMITIVE
    }
    private static ButtonManager theInstance = new ButtonManager();

    public static ButtonManager getInstance() {
        return theInstance;
    }

    private ArrayList<ActionButton> buttons = new ArrayList<ActionButton>();
    private Selectable component;

    private ButtonManager() { }

    public void addButton(ActionButton _button)
    {
        buttons.add(_button);
    }

    public void setComponent(Selectable _component, ComponentType _type)
    {
        if (component != null)
        {
            component.setSelected(false);
        }
        component = _component;
        enableButtons(_type);
        component.setSelected(true);
    }

    public void undoSetComponent()
    {
        if (component != null)
        {
            component.setSelected(false);
        }
        component = null;
    }

    private void enableButtons(ComponentType _component)
    {
        if (_component == ComponentType.BOX)
        {
            enable(ButtonType.DELETE);
            enable(ButtonType.CONNECT);
        }
        else if (_component == ComponentType.PRIMITIVE)
        {
            enable(ButtonType.DELETE);
        }
        else
        {
            enable(ButtonType.DELETE);
        }
        frame.revalidate();
    }

    private void enable(ButtonType _type)
    {
        for (ActionButton button: buttons)
        {
            if (button.type == _type)
            {
                button.setEnabled(true);
            }
        }
    }

    public void disableAllButtons()
    {
        for (ActionButton button : buttons)
        {
            button.setEnabled(false);
        }
        frame.revalidate();
    }

    public void setFrame(TestFrame _frame)
    {
        frame = _frame;
    }

    public Object getComponent()
    {
        return component;
    }
}
