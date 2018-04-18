package Testing;

import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CellPopupMenu extends JPopupMenu
{
    private Object cell;
    private mxCell mxcell;
    private mxGraphSubClass graph;

    public CellPopupMenu(mxGraphSubClass _graph, Object _cell)
    {
        this.cell = _cell;
        mxcell = (mxCell) this.cell;
        graph = _graph;

        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem renameItem = new JMenuItem("Rename");
        deleteItem.addActionListener(new DeleteActionListener());
        renameItem.addActionListener(new RenameActionListener());
        super.add(deleteItem);
        super.addSeparator();
        super.add(renameItem);
    }

    private class DeleteActionListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            graph.getModel().beginUpdate();
            try
            {
                graph.removeCells(new Object[]{cell});
            }
            finally
            {
                graph.getModel().endUpdate();
            }
        }
    }

    private class RenameActionListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            String label = JOptionPane.showInputDialog(null, "Enter New Label");
            if (label != null)
            {
                graph.getModel().beginUpdate();
                try
                {
                    graph.getModel().setValue(cell, label);

                    if (mxcell.getStyle().equals(Styles.getReferenceStyle()) || mxcell.getStyle().equals(Styles.getPrimitiveStyle()))
                    {
                        mxGeometry geometry = mxcell.getGeometry();
                        mxRectangle rect = mxUtils.getLabelSize(label, Styles.getPlaceHolder(), false, mxConstants.LINE_HEIGHT);
                        double textWidth = (rect.getWidth() + 5);
                        geometry.setWidth(textWidth);
                    }
                }
                finally
                {
                    graph.getModel().endUpdate();
                }
            }
        }
    }
}
