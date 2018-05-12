package MemoryTraceDrawer;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ApplicationCloseListener implements WindowListener
{
    private ActionListener saveActionListener;

    ApplicationCloseListener(ActionListener _saveActionListener)
    {
        saveActionListener = _saveActionListener;
    }
    @Override
    public void windowOpened(WindowEvent e)
    {

    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        if (Frame.projectChanged)
        {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to Save your Project Before Closing?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION)
            {
                saveActionListener.actionPerformed(null);
            }
            else if (dialogResult == JOptionPane.CANCEL_OPTION)
            {
                return;
            }
        }
        e.getWindow().dispose();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e)
    {

    }

    @Override
    public void windowIconified(WindowEvent e)
    {

    }

    @Override
    public void windowDeiconified(WindowEvent e)
    {

    }

    @Override
    public void windowActivated(WindowEvent e)
    {

    }

    @Override
    public void windowDeactivated(WindowEvent e)
    {

    }
}
