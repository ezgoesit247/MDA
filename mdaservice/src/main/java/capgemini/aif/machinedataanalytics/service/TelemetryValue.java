package capgemini.aif.machinedataanalytics.service;

import javax.persistence.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
@Table(name="telemetryvalue")
public class TelemetryValue implements MDADataObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="telemetryvaluendx",columnDefinition="numeric(19, 0)",updatable=false,nullable=false)
    private Long id;
	public Long getId() {
		return id;
	}

	@ManyToOne(cascade={CascadeType.DETACH},targetEntity=Metadata.class)
    @JoinColumn(name="variablendx",updatable=false,nullable=false)
    private Metadata variable;
	public Metadata getVariable() {
		return variable;
	}
	public void setVariable(Metadata variable) {
		this.variable = variable;
	}
    
    private Double value;
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	public TelemetryValue() {}
	public TelemetryValue(Metadata variable, Double value) {
		this.variable=variable;
		this.value=value;
	}

    public String getUri() {
    	return "telemetryvalue/"+this.getId().toString();
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
