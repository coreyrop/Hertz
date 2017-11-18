import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

public class PanelTransferHandler extends TransferHandler
{

    public boolean canImport(TransferHandler.TransferSupport support) {
        // for the demo, we will only support drops (not clipboard paste)
        if (!support.isDrop()) {
            return false;
        }

        // we only import Strings
        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }

        // check if the source actions contain the desired action -
        // either copy or move, depending on what was specified when
        // this instance was created
        boolean actionSupported = (COPY & support.getSourceDropActions()) == COPY;
        if (!actionSupported) {
            return false;
        }


        // fetch the data and bail if this fails
        String data;
        try {
            data = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            System.err.println(data);
            if (data.equals("Add Box")) {
                support.setDropAction(COPY);
                return true;
            }
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (java.io.IOException e) {
            return false;
        }

        // the desired action is not supported, so reject the transfer
        return false;
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        // if we cannot handle the import, say so
        if (!canImport(support)) {
            return false;
        }

        JPanel panel = (JPanel)support.getComponent();
        String label = getLabelInput("Enter Frame Label");
        throw new UnsupportedOperationException();
        //BoxPanel box = new BoxPanel(panel);
        //box.addMouseListener(new PanelMouseListener(panel.getParent(), box));
        //panel.add(box);
        //return true;
    }

    /*
   * creates a pop-up window displaying the specified message
   * and reads in a string which gets returned
   *
   * @param _message, String message to display to the user
   *
   * @return String label, string of the user's input
   */
    private String getLabelInput(String _message)
    {
        String label = JOptionPane.showInputDialog(null, _message);
        if(label != null)
        {
            return label;
        }
        else
        {
            //figure out how to handle this case
            return null;
        }
    }
}
