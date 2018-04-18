package Testing;

import com.mxgraph.model.mxCell;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

public class SaveActionListener implements ActionListener
{
    private Frame frame;

    public SaveActionListener(Frame _frame)
    {
        frame = _frame;
    }

    public void actionPerformed(ActionEvent e)
    {
        ArrayList<MemoryStructure> elders = new ArrayList<MemoryStructure>();
        elders.add(frame.getStack());
        elders.add(frame.getHeap());
        save(elders);
    }

    private void save(Collection<MemoryStructure> _eldestStructures)
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
                try
                {
                    PrintWriter writer = new PrintWriter(fileToSave.getAbsolutePath(), "UTF-8");

                    printAll(writer, _eldestStructures);
                    writer.close();
                }
                catch (FileNotFoundException e)
                {
                    System.err.println("File not found");
                }
                catch (UnsupportedEncodingException e)
                {
                    System.err.println("Unsupported encoding");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void printAll(PrintWriter writer, Collection<MemoryStructure> _eldestStructures) throws JSONException
    {

        for (MemoryStructure root : _eldestStructures)
        {
            mxCell mxroot = root.getMemoryStructureCell();
            JSONObject rootObj = generateJObj(mxroot);
            JSONArray boxes = new JSONArray();
            for (mxCell node : root.getBoxes().keySet())
            {
                JSONObject box = generateJObj(node);
                boxes.put(box);
            }
            rootObj.put("boxes", boxes);
            rootObj.write(writer);
            writer.println();
        }
        JSONArray boxes = new JSONArray();
        for (MemoryStructure root : _eldestStructures)
        {
            for (mxCell node : root.getBoxes().keySet())
            {
                JSONObject box = new JSONObject();
                box.put("ID", node.getId());
                JSONArray fields = new JSONArray();
                for (mxCell leaf : root.getBoxes().get(node))
                {
                    JSONObject field = generateJObj(leaf);
                    if (leaf.getEdgeCount() > 0)
                    {
                        field.put("Edge", leaf.getEdgeAt(0).getTerminal(false).getId());
                    }
                    fields.put(field);
                }
                box.put("Fields", fields);
                boxes.put(box);
            }
        }
        boxes.write(writer);
        writer.println();
    }

    private JSONObject generateJObj(mxCell mxcell) throws JSONException
    {
        JSONObject jobj = new JSONObject();

        jobj.put("ID", mxcell.getId());
        jobj.put("Label", mxcell.getValue());
        jobj.put("X", mxcell.getGeometry().getX());
        jobj.put("Y", mxcell.getGeometry().getY());
        jobj.put("Width", mxcell.getGeometry().getWidth());
        jobj.put("Height", mxcell.getGeometry().getHeight());

        return jobj;
    }

}

