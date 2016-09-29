package edu.jsu.mcis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class CustomWidget extends JPanel implements MouseListener {
    private java.util.List<ShapeObserver> observers;
    
    
    private final Color SELECTED_HEXAGON_COLOR = Color.green;
	private final Color SELECTED_OCTAGON_COLOR = Color.red;
    private final Color DEFAULT_COLOR = Color.white;
    private boolean selected;  
	private boolean hexagonSelected;
	private boolean octagonSelected;
    private Point[] hexagonVertex;
	private Point[] octagonVertex;

    
    public CustomWidget() {
        observers = new ArrayList<>();
        
        selected = false;
		hexagonSelected = true;
		octagonSelected = false;
		
        hexagonVertex = new Point[6];
		octagonVertex = new Point[8];
        for(int i = 0; i < hexagonVertex.length; i++) { 
			hexagonVertex[i] = new Point(); 
		}
		for(int j = 0; j < octagonVertex.length; j++){
			octagonVertex[j] = new Point(); 
		}
        Dimension dim = getPreferredSize();
        calculateVertices(dim.width, dim.height);
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(this);
    }

    
    public void addShapeObserver(ShapeObserver observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }
    public void removeShapeObserver(ShapeObserver observer) {
        observers.remove(observer);
    }
    private void notifyObservers() {
        ShapeEvent event = new ShapeEvent(selected);
        for(ShapeObserver obs : observers) {
            obs.shapeChanged(event);
        }
    }    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    private void calculateVertices(int width, int height) {
        // Square size should be half of the smallest dimension (width or height).
        int side = Math.min(width, height) / 3;
        //Point[] sign = {new Point(-1, -1), new Point(1, -1), new Point(1, 1), new Point(-1, 1)};
        double theta;
		for(int i = 0; i < hexagonVertex.length; i++) {
			theta = 2 * Math.PI/hexagonVertex.length;
			hexagonVertex[i].setLocation((width/3) + Math.cos(theta * i) * 100, height/2 + Math.sin(theta * i) * 100);
        }
		for(int j = 0; j < octagonVertex.length; j++){
			theta = 2 * Math.PI/octagonVertex.length;
			octagonVertex[j].setLocation(((width/3) * 2.3 ) + Math.cos(theta * j) * 100, height/2 + Math.sin(theta * j) * 100);
		}
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        calculateVertices(getWidth(), getHeight());
		Shape[] shapes = getShapes();
        g2d.setColor(Color.black);
        g2d.draw(shapes[0]);
		g2d.draw(shapes[1]);
        
		if(hexagonSelected) {
            g2d.setColor(SELECTED_HEXAGON_COLOR);
            g2d.fill(shapes[0]);
            g2d.setColor(DEFAULT_COLOR);
            g2d.fill(shapes[1]);
        }
		if(octagonSelected){
            g2d.setColor(SELECTED_OCTAGON_COLOR);
            g2d.fill(shapes[1]);
			g2d.setColor(DEFAULT_COLOR);
            g2d.fill(shapes[0]);
		}
    }

    public void mouseClicked(MouseEvent event) {
        Shape[] shapes = getShapes();
        if(shapes[0].contains(event.getX(), event.getY())) {
            selected = !selected;
			hexagonSelected = !hexagonSelected;
            notifyObservers();
        }
		if(shapes[1].contains(event.getX(), event.getY())) {
            selected = !selected;
			octagonSelected = !octagonSelected;
            notifyObservers();
        }
        repaint(shapes[0].getBounds());
		repaint(shapes[1].getBounds());
    }
    public void mousePressed(MouseEvent event) {}
    public void mouseReleased(MouseEvent event) {}
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}
    
    public Shape[] getShapes() {
        int[] x = new int[hexagonVertex.length];
        int[] y = new int[hexagonVertex.length];
		int[] a = new int[octagonVertex.length];
		int[] b = new int[octagonVertex.length];
		
        for(int i = 0; i < hexagonVertex.length; i++) {
            x[i] = hexagonVertex[i].x;
            y[i] = hexagonVertex[i].y;
        }
		for(int i = 0; i < octagonVertex.length; i++){
			a[i] = octagonVertex[i].x;
			b[i] = octagonVertex[i].y;
		}
			
		Shape[] shapes = new Shape[2];
        shapes[0] = new Polygon(x, y, hexagonVertex.length);
		shapes[1] = new Polygon(a, b, octagonVertex.length);
		
        return shapes;
    }

    public boolean isSelected() {	return selected;	}
	public boolean isHexagonSelected() {	return hexagonSelected;	}
	public boolean isOctagonSelected() {		return octagonSelected;	}

	public static void main(String[] args) {
		JFrame window = new JFrame("Custom Widget");
        window.add(new CustomWidget());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(300, 300);
        window.setVisible(true);
	}
}
