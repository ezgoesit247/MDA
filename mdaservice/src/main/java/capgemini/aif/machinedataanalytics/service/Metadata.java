package capgemini.aif.machinedataanalytics.service;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
@Table(name="telemetrymetadata")
public class Metadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="variablendx",columnDefinition="integer",updatable=false,nullable=false)
    private Integer id;
	public Integer getId() {
		return id;
	}

    @Column(name="variablename",columnDefinition="nvarchar",length=50,updatable=false,nullable=false,unique=true)
    private String variablename;
	public String getVariablename() {
		return variablename;
	}
	public void setVariablename(String variableName) {
		this.variablename = variableName;
	}

    @Column(name="variabletype",columnDefinition="nvarchar",length=20,updatable=false,nullable=false)
	@Enumerated(EnumType.STRING)
    private VariableType type;
	public VariableType getType() {
		return type;
	}
	public void setType(VariableType type) {
		this.type = type;
	}
	public enum VariableType {
    	DEFAULT,DIGITAL,TENSION,REEL_CAPACITY,SPEED;
    }

    @Column(name="variableshortname",columnDefinition="nvarchar",length=20,updatable=true,nullable=false,unique=true)
    private String shortname;
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

    @Column(name="linemachine",columnDefinition="nvarchar",length=50,updatable=false,nullable=true)
    private String linemachine;
	public String getLinemachine() {
		return linemachine;
	}
	public void setLinemachine(String linemachine) {
		this.linemachine = linemachine;
	}

    @Column(name="equipment",columnDefinition="nvarchar",length=20,updatable=false,nullable=true)
    private String equipment;
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
    
    public Metadata() {}
    public Metadata(String variableName, VariableType type, String shortname, String linemachine, String equipment) {
    	this.variablename=variableName;
    	this.type=type;
    	this.shortname=shortname;
    	this.linemachine=linemachine;
    	this.equipment=equipment;
    }
    public Metadata(String variableName, String shortname, VariableType type) {
    	this.variablename=variableName;
    	this.type=type;
    	this.shortname=shortname;
    }
    public Metadata(String variableName, String shortname) {
    	this.variablename=variableName;
    	this.shortname=shortname;
    	this.type=VariableType.DEFAULT;
    }

	public String getUri() {
		return "metadata/"+this.id.toString();
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
