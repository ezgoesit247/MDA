package capgemini.aif.machinedataanalytics.service;

import java.sql.Timestamp;

import javax.persistence.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
@Table(name="workorder")
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="workorderndx",updatable=false,nullable=false)
    private Integer id;
    
	public Integer getId() {
		return id;
	}
	
	@Column(name="workorderidentifier",columnDefinition="nvarchar",length=50,nullable=false,unique=true)
    private String workorderidentifier;

    public String getWorkorderidentifier() {
		return workorderidentifier;
	}
	public void setWorkorderidentifier(String workorderidentifier) {
		this.workorderidentifier = workorderidentifier;
	}
	
    @Column(name="timestamp",columnDefinition="datetime",nullable=false)
    private Timestamp timestamp;

	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public WorkOrder() {}
	
	public WorkOrder(String workorderidentifier, Timestamp timestamp) {
		this.workorderidentifier = workorderidentifier;
		this.timestamp = timestamp;
	}
	public String getUri() {
		return "workorder/"+this.getId().toString();
	}
	@Override
	public String toString() {
		return getUri();
	}
	public String toJson() {
        final Gson gson = new GsonBuilder().create();
        String json = gson.toJson(this);
        return json;
	}

}
