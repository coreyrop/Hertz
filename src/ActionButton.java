import javax.swing.*;

enum ButtonType
{
    DELETE, CONNECT
}

public class ActionButton extends JButton
{
    ButtonType type;

    public ActionButton(String _text, ButtonType _type)
    {
        setText(_text);
        type = _type;
    }
}
