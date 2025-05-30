package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

public class DemoModel implements Serializable {
	private static final long serialVersionUID = -474679583809542535L;
	private int id;
	private static final String PRINT_FORMAT = "[%-20s - %-50s]";
	private String name;
	public static final int columnCount = 2;
	
	public DemoModel() {
		
	}

	public DemoModel(int id, String name) {
		this.id=id;
		this.name = name;
	}
	
	public String toListString() {
		return String.format(PRINT_FORMAT, id, name);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Object getValueAtColumn(int column) {
		switch (column) {
		case 0:
			return getId();
		case 1:
			return getName();
		default:
			return null;
		}
	}

}
