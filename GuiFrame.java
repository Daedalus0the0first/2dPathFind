import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GuiFrame extends JFrame implements MouseMotionListener, ActionListener{
    private static Color currentColor = Color.lightGray;
    private static Color defaultColor = Color.lightGray;
    private static CanvasPanel canvasPanel;
    private LeftButtonPanel leftButtonPanel;
    public GuiFrame() {
    	int x = 89;
    	int y = 55;
    	int startX = 8;
    	int startY = 5;
    	int endX = 45;
    	int endY = 35;
    	canvasPanel=new CanvasPanel(x, y, startX, startY, endX, endY);
    	leftButtonPanel = new LeftButtonPanel();
        gui();
    }
    
    public class LeftButtonPanel extends JPanel{
        public LeftButtonPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JButton obstacle = new JButton("Obstacle");
            obstacle.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e) {
    				currentColor = Color.BLACK;
    			}
            });
            JButton start = new JButton("Start");
            start.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e) {
    				currentColor = Color.GREEN;
    			}
            });
            JButton end = new JButton("End");
            end.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e) {
    				currentColor = Color.RED;
    			}
            });
            JButton beginSimulation = new JButton("Dikstra's Algorithm");
            beginSimulation.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e) {
    				CanvasPanel.dikstraStart();
    				canvasPanel.repaint();
    			}
            });

            JButton reset = new JButton("Reset");
            reset.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e) {
    				CanvasPanel.reset();
    				canvasPanel.repaint();
    			}
            });
            add(obstacle, gbc);
            add(start, gbc);
            add(end, gbc);
            add(beginSimulation, gbc);
            add(reset, gbc);
        }
    }
    
    private void gui() {
        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());

        c.add(canvasPanel, BorderLayout.CENTER);
        c.add(leftButtonPanel, BorderLayout.WEST);
        
        canvasPanel.setPreferredSize(new Dimension(canvasPanel.getNumberofSquareX()*canvasPanel.getSideLength(), canvasPanel.getNumberofSquareY()*canvasPanel.getSideLength()));
        this.setVisible(true);
        pack();
    }

    public static Color getCurrentColor() {
    	return currentColor;
    }


    public static void main(String[] args) {
    	GuiFrame gFrame=new GuiFrame();

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}