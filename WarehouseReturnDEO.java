package thw.edu.javaII.port.warehouse.model.deo;

import java.io.Serializable;

public class WarehouseReturnDEO implements Serializable{
	private static final long serialVersionUID = 5607081847323905913L;
	private Object data;
	private String message;
	private Status status;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public WarehouseReturnDEO(Object data, String message, Status status) {
		super();
		this.data = data;
		this.message = message;
		this.status = status;
	}

	public WarehouseReturnDEO() {

	}

}