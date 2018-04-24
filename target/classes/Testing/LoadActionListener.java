package MemoryTraceDrawer;

import com.mxgraph.model.mxCell;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.mxgraph.model.mxGraphModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import com.mxgraph.model.mxICell;

public class LoadActionListener implements ActionListener
{
    private Frame frame;
    private mxGraphSubClass graph;

    public LoadActionListener(Frame _frame)
    {
        frame = _frame;
        graph = frame.getGraph();
    }

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

    private void loadToDisplay(JSONObject _stack, JSONObject _heap, JSONArray _allFields)
    {
        frame.getStack().clearBoxes();
        frame.getHeap().clearBoxes();
        HashMap<mxCell, ArrayList<mxCell>> stackBoxMap = new HashMap<mxCell, ArrayList<mxCell>>();
        HashMap<mxCell, ArrayList<mxCell>> heapBoxMap = new HashMap<mxCell, ArrayList<mxCell>>();
        graph.getModel().beginUpdate();
        try
        {

            JSONArray stackBoxes = (JSONArray) _stack.get("boxes");
            for (int i = 0; i < stackBoxes.length(); i++)
            {
                JSONObject box = stackBoxes.getJSONObject(i);
                mxCell newBox = (mxCell)graph.insertVertex(frame.getStack().getMemoryStructureCell(), box.getString("ID"), box.getString("Label"), box.getDouble("X"), box.getDouble("Y"), box.getDouble("Width"), box.getDouble("Height"), Styles.getStackBoxStyle());
                stackBoxMap.put(newBox, new ArrayList<mxCell>());
            }


            JSONArray heapInstances = (JSONArray) _heap.get("boxes");
            for (int j = 0; j < heapInstances.length(); j++)
            {
                JSONObject instance = heapInstances.getJSONObject(j);
                mxCell newBox = (mxCell)graph.insertVertex(frame.getHeap().getMemoryStructureCell(), instance.getString("ID"), instance.getString("Label"), instance.getDouble("X"), instance.getDouble("Y"), instance.getDouble("Width"), instance.getDouble("Height"), Styles.getHeapBoxStyle());
                heapBoxMap.put(newBox, new ArrayList<mxCell>());
            }

            mxGraphModel model= (mxGraphModel)graph.getModel();
            String style;
            for (int k = 0; k < _allFields.length(); k++)
            {
                JSONObject fields = _allFields.getJSONObject(k);
                mxCell parent = (mxCell)model.getCell(fields.getString("ID"));
                JSONArray theseFields = fields.getJSONArray("Fields");

                HashMap<mxCell, ArrayList<mxCell>> properMap;
                if (stackBoxMap.containsKey(parent))
                {
                    properMap = stackBoxMap;
                }
                else
                {
                    properMap = heapBoxMap;
                }

                for (int l = 0; l < theseFields.length(); l++)
                {
                    JSONObject field = theseFields.getJSONObject(l);
                    if (field.getString("ID").startsWith("Reference"))
                    {
                        style = Styles.getReferenceStyle();
                    }
                    else
                    {
                        style = Styles.getPrimitiveStyle();
                    }
                   mxCell newComponent = (mxCell)graph.insertVertex(parent, field.getString("ID"), field.getString("Label"), field.getDouble("X"), field.getDouble("Y"), field.getDouble("Width"), field.getDouble("Height"), style);
                    if (field.has("Edge"))
                    {
                        graph.insertEdge(null, null, null, newComponent, model.getCell(field.getString("Edge")));
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
            frame.getHeap().setBoxes(heapBoxMap);
        }
    }

}
