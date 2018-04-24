package MemoryTraceDrawer;

import com.mxgraph.util.mxCellRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class SaveImageActionListener implements ActionListener
{
    private Frame frame;

    /*
        SaveImageActionListener class constructor
        @ param Frame _frame: the Frame instance this SaveImageActionListener is associated with
     */
    public SaveImageActionListener(Frame _frame)
    {
        frame = _frame;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        promptSaveImage();
    }

    /*
        Prompts the user to select what to save the image as
        Saves the image as specified
     */
    private void promptSaveImage()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION)
        {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            if (fileChooser.getSelectedFile() != null)
            {
                BufferedImage image = mxCellRenderer.createBufferedImage(frame.getGraph(), null, 1, Color.WHITE, true, null);
                try
                {
                    ImageIO.write(image, "PNG", new File(fileToSave.getAbsolutePath() + ".png"));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
