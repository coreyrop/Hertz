package MemoryTraceDrawer;

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

public class SaveProjectActionListener implements ActionListener
{
    private Frame frame;

    /*
        SaveProjectActionListener class constructor
        @param _frame: the Frame instance associated with this SaveProjectActionListener
     */
    public SaveProjectActionListener(Frame _frame)
    {
        frame = _frame;
    }

    /*
        Gets the HashMaps of boxes from the Stack and Heap
        Passes them as an argument to save
     */
    public void actionPerformed(ActionEvent e)
    {
        ArrayList<MemoryStructure> elders = getMemoryStructures();
        String fileName = promptGetSaveFileName();
        if (fileName != null)
        {
            save(elders, fileName);
        }
    }

    /*
        Gets the MemoryStructures in frame and returns them in an ArrayList
        @return ArrayList<MemoryStructure>: Collection of all MemoryStructures in frame
     */
    private ArrayList<MemoryStructure> getMemoryStructures()
    {
        ArrayList<MemoryStructure> elders = new ArrayList<>();
        elders.add(frame.getStack());
        elders.add(frame.getHeap());
        return elders;
    }

    /*
        Prompts the user to specify the file path to save to
        @return String: specified file path, or null if cancelled or invalid path selected
     */
    private String promptGetSaveFileName()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION)
        {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            return fileToSave.getAbsolutePath();
        }
        return null;
    }

    /*
        Saves all of the data reachable from the Collection's elements into a file with the specified file name.
        @param _eldestStructures: a collection of all the HashMaps for the MemoryStructures used to hold the program's data
     */
    private void save(Collection<MemoryStructure> _eldestStructures, String fileName)
    {

                try
                {
                    PrintWriter writer = new PrintWriter(fileName + ".mt", "UTF-8");

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


    /*
        Prints all the required values to the file specified by the user
        @param _writer: the writer used to write to the file
        @param _eldestStructures: a collection of all the HashMaps for the Stack and Heap

        @throws JSONException
     */
    private void printAll(PrintWriter _writer, Collection<MemoryStructure> _eldestStructures) throws JSONException
    {

        // write all boxes in the MemoryStructures to file
        for (MemoryStructure root : _eldestStructures)
        {
            mxCell mxroot = root.getMemoryStructureCell();
            JSONObject rootObj = displayInfoToJSONObject(mxroot);
            JSONArray boxes = generateBoxesArray(root);
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
                JSONObject box = boxComponentsToJSONObject(root, node);
                boxes.put(box);
            }
        }
        boxes.write(_writer);
        _writer.println();
    }

    /*
        Creates a new JSONObject and stores the provided boxes components in the JSONObject
        @param _root: The memory containing the box
        @param _node: the box who's data we are putting in the returned JSONObject

        @throws JSONException
        @return JSONObject: JSONObject storing all the components of the given box
     */
    private JSONObject boxComponentsToJSONObject(MemoryStructure _root, mxCell _node) throws JSONException
    {
        JSONObject box = new JSONObject();
        box.put("ID", _node.getId());
        JSONArray fields = new JSONArray();
        for (mxCell leaf : _root.getBoxes().get(_node))
        {
            JSONObject field = displayInfoToJSONObject(leaf);
            if (leaf.getEdgeCount() > 0)
            {
                field.put("Edge", leaf.getEdgeAt(0).getTerminal(false).getId());
            }
            fields.put(field);
        }
        box.put("Fields", fields);
        return box;
    }

    /*
        Creates a JSONArray storing all of the JSONObjects for all the boxes inside of the given MemoryStructure
        @param _root: The MemoryStructure containing the desired boxes

        @throws JSONException
        @return JSONArray: JSONArray containing all of the JSONObjects for all of the boxes in _root
     */
    private JSONArray generateBoxesArray(MemoryStructure _root) throws JSONException
    {
        JSONArray boxes = new JSONArray();
        for (mxCell node : _root.getBoxes().keySet())
        {
            JSONObject box = displayInfoToJSONObject(node);
            boxes.put(box);
        }
        return boxes;
    }

    /*
        Gets all of the values needed for the given cell and stores them in a JSONObject
        @param _mxcell: the cell to have values stored in JSONObject

        @throws JSONException
        @return JSONObject: the JSONObject containing all the required values for the cell
     */
    private JSONObject displayInfoToJSONObject(mxCell _mxcell) throws JSONException
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

