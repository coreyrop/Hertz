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

    /*
        SaveActionListener class constructor
        @ param Frame _frame: the Frame instance associated with this SaveActionListener
     */
    public SaveActionListener(Frame _frame)
    {
        frame = _frame;
    }

    /*
        Gets the HashMaps of boxes from the Stack and Heap
        Passes them as an argument to save
     */
    public void actionPerformed(ActionEvent e)
    {
        ArrayList<MemoryStructure> elders = new ArrayList<MemoryStructure>();
        elders.add(frame.getStack());
        elders.add(frame.getHeap());
        save(elders);
    }

    /*
        Prompts the user to select what to save the file as
        Calls printAll with writer and _eldestStructures as arguments
        @ param Collection<MemoryStructure> _eldestStructures: a collection of all the HashMaps for the Stack and Heap
     */
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

    /*
        Prints all the required values to the file specified by the user
        @ param PrintWriter _writer: the writer used to write to the file
        @ param Collection<MemoryStructure> _eldestStructures: a collection of all the HashMaps for the Stack and Heap

        @ throws JSONException
     */
    private void printAll(PrintWriter _writer, Collection<MemoryStructure> _eldestStructures) throws JSONException
    {

        // write all boxes in the Stack and Heap to file
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
            rootObj.write(_writer);
            _writer.println();
        }

        // write out all the components to file
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
        boxes.write(_writer);
        _writer.println();
    }

    /*
        Gets all of the values needed for the given cell and stores them in a JSONObject
        @ param mxCell _mxcell: the cell to have values stored in JSONObject

        @ throws JSONException
        @ return JSONObject: the JSONObject containing all the required values for the cell
     */
    private JSONObject generateJObj(mxCell _mxcell) throws JSONException
    {
        JSONObject jobj = new JSONObject();

        jobj.put("ID", _mxcell.getId());
        jobj.put("Label", _mxcell.getValue());
        jobj.put("X", _mxcell.getGeometry().getX());
        jobj.put("Y", _mxcell.getGeometry().getY());
        jobj.put("Width", _mxcell.getGeometry().getWidth());
        jobj.put("Height", _mxcell.getGeometry().getHeight());

        return jobj;
    }

}

