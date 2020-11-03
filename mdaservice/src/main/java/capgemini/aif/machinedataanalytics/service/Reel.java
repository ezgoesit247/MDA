package capgemini.aif.machinedataanalytics.service;

import javax.persistence.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
@Table(name="reel")
public class Reel implements MDADataObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reelndx",updatable=false,nullable=false)
    private Integer id;

	public Integer getId() {
		return id;
	}
    
    @Column(name="reelidentifier",columnDefinition="nvarchar",length=50,nullable=false,unique=true)
    private String reelidentifier;

	public String getReelidentifier() {
		return reelidentifier;
	}
	public void setReelidentifier(String reelidentifier) {
		this.reelidentifier = reelidentifier;
	}

	@OneToOne(fetch = FetchType.EAGER,cascade= {CascadeType.DETACH})
    @JoinColumn(name="workorderndx",updatable=false,nullable=true)
	protected WorkOrder workorder;

    public WorkOrder getWorkorder() {
		return workorder;
	}
	public void setWorkorder(WorkOrder workorder) {
		this.workorder = workorder;
	}

	@Column(name="type",columnDefinition="nvarchar",length=20,nullable=false)
	@Enumerated(EnumType.STRING)
    private ReelType type;

	public ReelType getType() {
		return type;
	}
	public void setType(ReelType type) {
		this.type = type;
	}
	public enum ReelType {
    	EXTRUDER, TAPER;
    }
    
    public Reel() {}

    public Reel(String reelidentifier, ReelType Type, WorkOrder workorder) {
        this.reelidentifier = reelidentifier;
        this.type = Type;
        this.workorder = workorder;
    }
    
    public String getUri() {
    	return "reel/"+this.getId().toString();
    }

    @Override
    public String toString() {
//        return "Reel{" +
//                "reelidentifier:"+reelidentifier +
//                ",type:"+type +
//                " }";
    	return getUri();
    }
	
	public String toJson() {
        final Gson gson = new GsonBuilder().create();
        String json = gson.toJson(this);
        return json;
	}

	
}