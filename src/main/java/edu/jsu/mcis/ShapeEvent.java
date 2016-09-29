package edu.jsu.mcis;

public class ShapeEvent {
    
	public boolean hexagonSelected;
	public boolean octagonSelected;
	
    public ShapeEvent() {
        this(false);
    }
	
    public ShapeEvent(boolean selected) {
        this.hexagonSelected = hexagonSelected;
		this.octagonSelected = octagonSelected;
    }
	
    public boolean hexagonisSelected(){	return hexagonSelected;	}
	public boolean octagonisSelected(){	return octagonSelected;	}
}