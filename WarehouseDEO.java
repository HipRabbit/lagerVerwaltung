package thw.edu.javaII.port.warehouse.model.deo;

import java.io.Serializable;

public class WarehouseDEO implements Serializable{
	private static final long serialVersionUID = 4403645819092074274L;
	private Object data;
	private Zone zone;
	private Command command;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public WarehouseDEO(Object data, Zone zone, Command command) {
		super();
		this.data = data;
		this.zone = zone;
		this.command = command;
	}
	
	public WarehouseDEO() {
		
	}

}