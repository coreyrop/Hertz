package MemoryTraceDrawer;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class LoadActionListener implements ActionListener
{
    private Frame frame;
    private int greatestReferenceID, greatestPrimitiveID;

    /*
        LoadActionListener class constructor
        sets Frame field frame to the param passed in
        @param _frame: the Frame instance this LoadActionListener is associated with
     */
    public LoadActionListener(Frame _frame)
    {
        frame = _frame;
    }

    /*
        Prompts JFileChooser to allow the user to select the file that they would like to load
        Handles any file load exceptions
        If file is valid then calls load to display with the parsed values
     */
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to load");

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION)
        {
            File fileToLoad = fileChooser.getSelectedFile();
            if (fileChooser.getSelectedFile() != null)
            {
                try
                {
                    Encrypter.decrypt(fileToLoad, fileToLoad);
                    Reader reader = new FileReader(fileToLoad);
                    JSONTokener parser = new JSONTokener(reader);
                    JSONObject stack = (JSONObject) parser.nextValue();
                    JSONObject heap = (JSONObject) parser.nextValue();
                    JSONArray boxes = (JSONArray) parser.nextValue();
                    loadToDisplay(stack, heap, boxes);
                }
                catch (FileNotFoundException fnfe)
                {
                    System.err.println("File not found");
                }
                catch (JSONException jsone)
                {
                    jsone.printStackTrace();
                }
                finally
                {
                    Encrypter.encrypt(fileToLoad, fileToLoad);
                }
            }
            Frame.projectChanged = false;
        }
    }

    /*
        Clears the display of the current structures and replaces them with the ones that were parsed from the loaded file.
        Updates the ID counters to match with what the loaded file would require.
        Updates the HashMaps of boxes and components for both the Stack and the Heap.
        @param _stack: the parsed values of the boxes in the stack
        @param _heap: the parsed values of the boxes in the heap
        @param _allComponents: the parsed values of all the fields in every box for both the Stack and Heap
     */
    private void loadToDisplay(JSONObject _stack, JSONObject _heap, JSONArray _allComponents)
    {
        // Initialize values needed, set ID trackers to be initially 0
        clearGraph();
        mxGraphSubClass graph = frame.getGraph();
        int greatestFrameID = 0;
        greatestReferenceID = 0;
        greatestPrimitiveID = 0;

        graph.getModel().beginUpdate();
        try
        {
            // Add all boxes for the Stack to the HashMap as keys and, to the graph for visual display
            JSONArray stackBoxes = getStructureBoxes(_stack, graph, frame.getStack().getMemoryStructureCell());
            if (stackBoxes.length() > 0)
            {
                JSONObject lastStackBox = stackBoxes.getJSONObject(stackBoxes.length() - 1);
                greatestFrameID = Integer.parseInt(lastStackBox.getString("ID").substring(5, lastStackBox.getString("ID").length()));
            }
            // Add all boxes for the Heap to the HashMap as keys, and to the graph for visual display
            JSONArray heapInstances = getStructureBoxes(_heap, graph, frame.getHeap().getMemoryStructureCell());
            if (heapInstances.length() > 0)
            {
                JSONObject lastHeapInstance = heapInstances.getJSONObject(heapInstances.length() - 1);
                int greatestHeapFrameID = Integer.parseInt(lastHeapInstance.getString("ID").substring(4, lastHeapInstance.getString("ID").length()));
                if (greatestHeapFrameID > greatestFrameID)
                {
                    greatestFrameID = greatestHeapFrameID;
                }
            }

            // Add all the fields to the proper ArrayList for the correct cell parent as the key in the proper HashMap
            mxGraphModel model = (mxGraphModel) graph.getModel();
            populateBoxesInGraph(_allComponents, graph, model);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        finally
        {
            graph.getModel().endUpdate();
            frame.getStack().calcNextBoxY();
            MemoryStructure.VariableStyle.PRIMITIVE.setIdCount(greatestPrimitiveID + 1);
            MemoryStructure.VariableStyle.REFERENCE.setIdCount(greatestReferenceID + 1);
            MemoryStructure.setIdCount(greatestFrameID + 1);

        }
    }

    /*
        Puts all the components in the proper box for all boxes
        @param _allComponents: the components to be added to the given HashMaps
        @param _graph: the graph that the components will be added to
        @param _model: the model associated with _graph

        @throws JSONException
     */
    private void populateBoxesInGraph(JSONArray _allComponents, mxGraphSubClass _graph, mxGraphModel _model) throws JSONException
    {
        for (int k = 0; k < _allComponents.length(); k++)
        {
            JSONObject components = _allComponents.getJSONObject(k);
            mxCell parent = (mxCell) _model.getCell(components.getString("ID"));
            JSONArray parentsComponents = components.getJSONArray("Fields");

            populateArrayLists(_graph, _model, parent, parentsComponents);
        }
    }

    /*
        Adds all the parent's components to the parent and tracks Reference and Primitive ID numbers
        @param _graph: the graph the components will be added to
        @param _model: the model associated with _graph
        @param _parent: the parent who's components we are adding
        @param _parentsComponents: the components to add to the parent in the HashMapt

        @throws JSONException
     */
    private void populateArrayLists(mxGraphSubClass _graph, mxGraphModel _model, mxCell _parent, JSONArray _parentsComponents) throws JSONException
    {
        String style;
        for (int l = 0; l < _parentsComponents.length(); l++)
        {
            JSONObject component = _parentsComponents.getJSONObject(l);
            if (component.getString("ID").startsWith("Reference"))
            {
                style = MemoryStructure.VariableStyle.REFERENCE.getStyle();
                int refIDNUM = Integer.parseInt(component.getString("ID").substring(9, component.getString("ID").length()));
                if (greatestReferenceID < refIDNUM)
                {
                    greatestReferenceID = refIDNUM;
                }
            }
            else
            {
                style = MemoryStructure.VariableStyle.PRIMITIVE.getStyle();
                int primIDNUM = Integer.parseInt(component.getString("ID").substring(9, component.getString("ID").length()));
                if (greatestPrimitiveID < primIDNUM)
                {
                    greatestPrimitiveID = primIDNUM;
                }
            }

            // If the field has a reference then add the edge the graph for visual display
            mxCell newComponent = (mxCell) _graph.insertVertex(_parent, component.getString("ID"), component.getString("Label"), component.getDouble("X"), component.getDouble("Y"), component.getDouble("Width"), component.getDouble("Height"), style);
            if (component.has("Edge"))
            {
                _graph.insertEdge(null, null, null, newComponent, _model.getCell(component.getString("Edge")));
            }
        }
    }

    /*
        Puts all of the boxes for a memory structure into a JSONArray and visually displays them on the graph
        @param _structure: the MemoryStructure's JSONObject that contains the boxes
        @param _graph: the graph the boxes will be added to
        @param _memoryStructureMap: the HashMap for the MemoryStructure where the boxes will be added as keys
        @param _memoryStructureCell: the mxCell of the MemoryStructure, will be set as the boxes parent

        @throws JSONException
     */
    private JSONArray getStructureBoxes(JSONObject _structure, mxGraphSubClass _graph, mxCell _memoryStructureCell) throws JSONException
    {
        JSONArray structureBoxes = (JSONArray) _structure.get("boxes");
        for (int j = 0; j < structureBoxes.length(); j++)
        {
            JSONObject instance = structureBoxes.getJSONObject(j);
            mxCell newBox = (mxCell) _graph.insertVertex(_memoryStructureCell, instance.getString("ID"), instance.getString("Label"), instance.getDouble("X"), instance.getDouble("Y"), instance.getDouble("Width"), instance.getDouble("Height"), Styles.getHeapBoxStyle());
        }
        return structureBoxes;
    }


    /*
        Removes all the cells from the graph
     */
    private void clearGraph()
    {
        mxCell stackCell = frame.getStack().getMemoryStructureCell();
        mxCell heapCell = frame.getHeap().getMemoryStructureCell();
        ArrayList<Object> allCells = new ArrayList<>();
        for (int i = 0; i < stackCell.getChildCount(); i++)
        {
            allCells.add(stackCell.getChildAt(i));
        }
        for (int j = 0; j < heapCell.getChildCount(); j++)
        {
            allCells.add(heapCell.getChildAt(j));
        }
        frame.getGraph().removeCells(allCells.toArray());
    }

}
