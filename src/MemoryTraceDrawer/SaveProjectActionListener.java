package MemoryTraceDrawer;

import com.mxgraph.model.mxCell;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JFileChooser;
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

    /**
     * SaveProjectActionListener class constructor
     *
     * @param frame: the Frame instance associated with this SaveProjectActionListener
     */
    protected SaveProjectActionListener(Frame frame)
    {
        this.frame = frame;
    }

    /**
     * Gets the HashMaps of boxes from the Stack and Heap
     * Passes them as an argument to save
     */
    public void actionPerformed(ActionEvent e)
    {
        ArrayList<MemoryStructure> elders = getMemoryStructures();
        String fileName = promptGetSaveFileName();
        if (fileName != null)
        {
            save(elders, fileName);
            Frame.projectChanged = false;
        }
    }

    /**
     * Gets the MemoryStructures in frame and returns them in an ArrayList
     *
     * @return ArrayList<MemoryStructure>: Collection of all MemoryStructures in frame
     */
    private ArrayList<MemoryStructure> getMemoryStructures()
    {
        ArrayList<MemoryStructure> elders = new ArrayList<>();
        elders.add(frame.getStack());
        elders.add(frame.getHeap());
        return elders;
    }

    /**
     * Prompts the user to specify the file path to save to
     *
     * @return String: specified file path, or null if cancelled or invalid path selected
     */
    private String promptGetSaveFileName()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION)
        {
            File fileToSave = new File(FilenameUtils.removeExtension(fileChooser.getSelectedFile().getAbsolutePath()) + ".mt");
            return fileToSave.getAbsolutePath();
        }
        return null;
    }

    /**
     * Saves all of the data reachable from the Collection's elements into a file with the specified file name.
     *
     * @param eldestStructures: a collection of all the HashMaps for the MemoryStructures used to hold the program's data
     */
    private void save(Collection<MemoryStructure> eldestStructures, String fileName)
    {
        try
        {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");

            printAll(writer, eldestStructures);
            writer.close();

            File file = new File(fileName);
            Encrypter.encrypt(file, file);
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

    /**
     * Prints all the required values to the file specified by the user
     *
     * @param _writer:           the writer used to write to the file
     * @param eldestStructures: a collection of all the HashMaps for the Stack and Heap
     * @throws JSONException
     */
    private void printAll(PrintWriter _writer, Collection<MemoryStructure> eldestStructures) throws JSONException
    {

        // write all boxes in the MemoryStructures to file
        for (MemoryStructure root : eldestStructures)
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
        for (MemoryStructure root : eldestStructures)
        {
            mxCell rootCell = root.getMemoryStructureCell();
            for (int i = 0; i < rootCell.getChildCount(); i++)
            {
                mxCell node = (mxCell) rootCell.getChildAt(i);
                JSONObject box = boxComponentsToJSONObject(node);
                boxes.put(box);
            }
        }
        boxes.write(_writer);
        _writer.println();
    }

    /**
     * Creates a new JSONObject and stores the provided boxes components in the JSONObject
     *
     * @param node: the box who's data we are putting in the returned JSONObject
     * @return JSONObject: JSONObject storing all the components of the given box
     * @throws JSONException
     */
    private JSONObject boxComponentsToJSONObject(mxCell node) throws JSONException
    {
        JSONObject box = new JSONObject();
        box.put("ID", node.getId());
        JSONArray fields = new JSONArray();
        for (int i = 0; i < node.getChildCount(); i++)
        {
            mxCell leaf = (mxCell) node.getChildAt(i);
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

    /**
     * Creates a JSONArray storing all of the JSONObjects for all the boxes inside of the given MemoryStructure
     *
     * @param root: The MemoryStructure containing the desired boxes
     * @return JSONArray: JSONArray containing all of the JSONObjects for all of the boxes in root
     * @throws JSONException
     */
    private JSONArray generateBoxesArray(MemoryStructure root) throws JSONException
    {
        JSONArray boxes = new JSONArray();
        mxCell rootCell = root.getMemoryStructureCell();
        for (int i = 0; i < rootCell.getChildCount(); i++)
        {
            mxCell node = (mxCell) rootCell.getChildAt(i);
            if (!node.isEdge()) // JGraphX treats edges as children, we do not want them evaluated here.
            {
                JSONObject box = displayInfoToJSONObject(node);
                boxes.put(box);
            }
        }
        return boxes;
    }

    /**
     * Gets all of the values needed for the given cell and stores them in a JSONObject
     *
     * @param mxCell: the cell to have values stored in JSONObject
     * @return JSONObject: the JSONObject containing all the required values for the cell
     * @throws JSONException
     */
    private JSONObject displayInfoToJSONObject(mxCell mxCell) throws JSONException
    {
        JSONObject jobj = new JSONObject();

        jobj.put("ID", mxCell.getId());
        jobj.put("Label", mxCell.getValue());
        jobj.put("X", mxCell.getGeometry().getX());
        jobj.put("Y", mxCell.getGeometry().getY());
        jobj.put("Width", mxCell.getGeometry().getWidth());
        jobj.put("Height", mxCell.getGeometry().getHeight());

        return jobj;
    }
}

