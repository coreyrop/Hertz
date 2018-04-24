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
import java.util.HashMap;

public class LoadActionListener implements ActionListener
{
    private Frame frame;

    /*
        LoadActionListener class constructor
        sets Frame field frame to the param passed in
        @ param Frame _frame: the Frame instance this LoadActionListener is associated with
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
            System.out.println("Load file: " + fileToLoad.getAbsolutePath());
            if (fileChooser.getSelectedFile() != null)
            {
                try
                {
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
            }
        }
    }

    /*
        Clears the display of the current structures and replaces them with the ones that were parsed from the loaded file.
        Updates the ID counters to match with what the loaded file would require.
        Updates the HashMaps of boxes and components for both the Stack and the Heap.
        @ param JSONObject _stack: the parsed values of the boxes in the stack
        @ param JSONObject _heap: the parsed values of the boxes in the heap
        @ param JSONArray _allComponents: the parsed values of all the fields in every box for both the Stack and Heap
     */
    private void loadToDisplay(JSONObject _stack, JSONObject _heap, JSONArray _allComponents)
    {
        // Initialize values needed, set ID trackers to be initially 0
        mxGraphSubClass graph = frame.getGraph();
        int greatestFrameID = 0;
        int greatestReferenceID = 0;
        int greatestPrimitiveID = 0;
        double greatestStackBoxY = 0;

        // Delete all the current boxes and fields in the graph, initialize the values that will take their place
        frame.getStack().clearBoxes();
        frame.getHeap().clearBoxes();
        HashMap<mxCell, ArrayList<mxCell>> stackBoxMap = new HashMap<>();
        HashMap<mxCell, ArrayList<mxCell>> heapBoxMap = new HashMap<>();

        graph.getModel().beginUpdate();
        try
        {
            // Add all boxes for the Stack to the HashMap as keys and, to the graph for visual display
            JSONArray stackBoxes = (JSONArray) _stack.get("boxes");
            for (int i = 0; i < stackBoxes.length(); i++)
            {
                JSONObject frame = stackBoxes.getJSONObject(i);
                double newFrameY = frame.getDouble("Y");
                mxCell newFrame = (mxCell) graph.insertVertex(this.frame.getStack().getMemoryStructureCell(), frame.getString("ID"), frame.getString("Label"), frame.getDouble("X"), newFrameY, frame.getDouble("Width"), frame.getDouble("Height"), Styles.getStackBoxStyle());
                stackBoxMap.put(newFrame, new ArrayList<>());

                int frameIDNUM = Integer.parseInt(frame.getString("ID").substring(5, frame.getString("ID").length()));
                if (greatestFrameID < frameIDNUM)
                {
                    greatestFrameID = frameIDNUM;
                }

                if (greatestStackBoxY < newFrameY)
                {
                    greatestStackBoxY = newFrameY;
                }
            }

            // Add all boxes for the Heap to the HashMap as keys, and to the graph for visual display
            JSONArray heapInstances = (JSONArray) _heap.get("boxes");
            for (int j = 0; j < heapInstances.length(); j++)
            {
                JSONObject instance = heapInstances.getJSONObject(j);
                mxCell newInstance = (mxCell) graph.insertVertex(frame.getHeap().getMemoryStructureCell(), instance.getString("ID"), instance.getString("Label"), instance.getDouble("X"), instance.getDouble("Y"), instance.getDouble("Width"), instance.getDouble("Height"), Styles.getHeapBoxStyle());
                heapBoxMap.put(newInstance, new ArrayList<>());
                int boxIDNUM = Integer.parseInt(instance.getString("ID").substring(4, instance.getString("ID").length()));
                if (greatestFrameID < boxIDNUM)
                {
                    greatestFrameID = boxIDNUM;
                }
            }

            // Add all the fields to the proper ArrayList for the correct cell parent as the key in the proper HashMap
            mxGraphModel model = (mxGraphModel) graph.getModel();
            String style;
            for (int k = 0; k < _allComponents.length(); k++)
            {
                JSONObject components = _allComponents.getJSONObject(k);
                mxCell parent = (mxCell) model.getCell(components.getString("ID"));
                JSONArray parentsComponents = components.getJSONArray("Fields");

                // Get the proper HashMap depending if parent is in the Stack or the Heap
                HashMap<mxCell, ArrayList<mxCell>> properMap;
                if (stackBoxMap.containsKey(parent))
                {
                    properMap = stackBoxMap;
                }
                else
                {
                    properMap = heapBoxMap;
                }

                // Adds each field to the appropriate ArrayList and to the graph for visual display
                for (int l = 0; l < parentsComponents.length(); l++)
                {
                    JSONObject component = parentsComponents.getJSONObject(l);
                    if (component.getString("ID").startsWith("Reference"))
                    {
                        style = Styles.getReferenceStyle();
                        int refIDNUM = Integer.parseInt(component.getString("ID").substring(9, component.getString("ID").length()));
                        if (greatestReferenceID < refIDNUM)
                        {
                            greatestReferenceID = refIDNUM;
                        }
                    }
                    else
                    {
                        style = Styles.getPrimitiveStyle();
                        int primIDNUM = Integer.parseInt(component.getString("ID").substring(9, component.getString("ID").length()));
                        if (greatestPrimitiveID < primIDNUM)
                        {
                            greatestPrimitiveID = primIDNUM;
                        }
                    }
                    // If the field has a reference then add the edge the graph for visual display
                    mxCell newComponent = (mxCell) graph.insertVertex(parent, component.getString("ID"), component.getString("Label"), component.getDouble("X"), component.getDouble("Y"), component.getDouble("Width"), component.getDouble("Height"), style);
                    if (component.has("Edge"))
                    {
                        graph.insertEdge(null, null, null, newComponent, model.getCell(component.getString("Edge")));
                    }
                    properMap.get(parent).add(newComponent);
                }

            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        finally
        {
            graph.getModel().endUpdate();
            frame.getStack().setBoxes(stackBoxMap);
            frame.getStack().setNextBoxY(greatestStackBoxY);
            frame.getHeap().setBoxes(heapBoxMap);
            MemoryStructure.variableStyle.PRIMITIVE.setIdCount(greatestPrimitiveID + 1);
            MemoryStructure.variableStyle.REFERENCE.setIdCount(greatestReferenceID + 1);
            MemoryStructure.setIdCount(greatestFrameID + 1);

        }
    }

}
