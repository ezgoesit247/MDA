package capgemini.aif.machinedataanalytics.service;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.*;
import javax.validation.Valid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
@Table(name="telemetryheader")
public class Telemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="telemetryndx",columnDefinition="numeric(19, 0)",updatable=false,nullable=false)
    private Long id;
	public Long getId() {
		return id;
	}

//    @Column(name="variablendx")
//    private Integer variablendx;
//	public Integer getVariablendx() {
//		return variablendx;
//	}
//	public void setVariablendx(Integer variablendx) {
//		this.variablendx = variablendx;
//	}
	
	//TODO: FetchType Eager here is bad practice
	@ElementCollection(fetch=FetchType.EAGER,targetClass=TelemetryValue.class)
	@JoinTable(name="telemetryvariablevalues")
	private Set<TelemetryValue> variables;
	public Set<TelemetryValue> getVariables() {
		return variables;
	}
	public void setVariables(Set<TelemetryValue> variables) {
		this.variables = variables;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="reelndx",updatable=false,nullable=false)
    protected Reel reel;
	public Reel getReel() {
		return reel;
	}
	public void setReel(Reel reel) {
		this.reel = reel;
	}
    
//    @Column(name="value",nullable=true)
//    private Double value;
//	public Double getValue() {
//		return value;
//	}
//	public void setValue(Double value) {
//		this.value = value;
//	}

    @Column(name="timestamp",columnDefinition="datetime",updatable=true,nullable=true)
    private Timestamp timestamp;
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Column(name="sendstatus",columnDefinition="nvarchar",length=20)
	@Enumerated(EnumType.STRING)
    private SendStatus sendstatus;

	public SendStatus getSendstatus() {
		return sendstatus;
	}
	public void setSendstatus(SendStatus sendstatus) {
		this.sendstatus = sendstatus;
	}
	public enum SendStatus {
    	NEW, SENT, FAILED, NOSEND;
    }

	//TODO: Remove this from here and the database
	@Column(name="reelidentifier",columnDefinition="nvarchar",length=50,nullable=false)
    private String reelidentifier;
    
    public String getReelidentifier() {
		return reelidentifier;
	}
	public void setReelidentifier(String reelidentifier) {
		this.reelidentifier = reelidentifier;
	}
	public Telemetry() {}
	/**
	 * 
	 * @deprecated use {@link #Telemetry(Set, Reel, Timestamp)} instead
	 */
	@Deprecated
    public Telemetry(Integer variablendx, Reel reel, Double value, Timestamp timestamp) {
//    	this.variablendx = variablendx;
    	this.reel = reel;
//    	this.value = value;
    	this.timestamp = timestamp;
    	this.reelidentifier=reel.getReelidentifier();
    	this.sendstatus=SendStatus.NEW;
    }
    public Telemetry(Set<TelemetryValue> variables, Reel reel, Timestamp timestamp) {
    	this.variables = variables;
    	this.reel = reel;
    	this.timestamp = timestamp;
    	this.reelidentifier=reel.getReelidentifier();
    	this.sendstatus=SendStatus.NEW;
    }
    public Telemetry(Set<TelemetryValue> variables, Reel reel, Timestamp timestamp, SendStatus sendstatus) {
    	this.variables = variables;
    	this.reel = reel;
    	this.timestamp = timestamp;
    	this.reelidentifier=reel.getReelidentifier();
    	this.sendstatus=sendstatus;
    }

    public String getUri() {
    	return "telemetry/"+this.getId().toString();
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
