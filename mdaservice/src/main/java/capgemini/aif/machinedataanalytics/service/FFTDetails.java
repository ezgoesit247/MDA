package capgemini.aif.machinedataanalytics.service;

import java.sql.Timestamp;

import javax.persistence.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
@Table(name="fftdetails")
public class FFTDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fftdetailsndx",columnDefinition="numeric(19, 0)",updatable=false,nullable=false)
    private Long id;
	
	public Long getId() {
		return id;
	}

	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH},targetEntity=WorkOrder.class)
    @JoinColumn(name="workorderndx",updatable=false,nullable=false)
	protected WorkOrder workorder;
    
    public WorkOrder getWorkorder() {
		return workorder;
	}
	public void setWorkorder(WorkOrder workorder) {
		this.workorder = workorder;
	}

	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH},targetEntity=Reel.class)
    @JoinColumn(name="reelndx",updatable=false,nullable=false)
	protected Reel reel;
    
    public Reel getReel() {
		return reel;
	}
	public void setReel(Reel reel) {
		this.reel = reel;
	}


	@Column(name="d1freq",columnDefinition="numeric(19, 0)",nullable=true)
    private Long d1frequency;

    public Long getD1frequency() {
		return d1frequency;
	}
	public void setD1frequency(Long d1frequency) {
		this.d1frequency = d1frequency;
	}
    @Column(name="d3freq",columnDefinition="numeric(19, 0)",nullable=true)
    private Long d3frequency;

	public Long getD3frequency() {
		return d3frequency;
	}
	public void setD3frequency(Long d3frequency) {
		this.d3frequency = d3frequency;
	}
    @Column(name="ccoldfreq",columnDefinition="numeric(19, 0)",nullable=true)
    private Long ccoldfrequency;

	public Long getCcoldfrequency() {
		return ccoldfrequency;
	}
	public void setCcoldfrequency(Long ccoldfrequency) {
		this.ccoldfrequency = ccoldfrequency;
	}

    @Column(name="d1amp",columnDefinition="int",nullable=true)
    private Integer d1amplitude;

	public Integer getD1amplitude() {
		return d1amplitude;
	}
	public void setD1amplitude(Integer d1amplitude) {
		this.d1amplitude = d1amplitude;
	}
    @Column(name="d3amp",columnDefinition="int",nullable=true)
    private Integer d3amplitude;

	public Integer getD3amplitude() {
		return d3amplitude;
	}
	public void setD3amplitude(Integer d3amplitude) {
		this.d3amplitude = d3amplitude;
	}
    @Column(name="ccoldamp",columnDefinition="int",nullable=true)
    private Integer ccoldamplitude;

    
	public Integer getCcoldamplitude() {
		return ccoldamplitude;
	}
	public void setCcoldamplitude(Integer ccoldamplitude) {
		this.ccoldamplitude = ccoldamplitude;
	}

	@Column(name="timestamp",columnDefinition="datetime",nullable=false)
    private Timestamp timestamp;

	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Column(name="starttime",columnDefinition="datetime",nullable=false)
    private Timestamp starttime;

	public Timestamp getStarttime() {
		return starttime;
	}
	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	@Column(name="endtime",columnDefinition="datetime",nullable=false)
    private Timestamp endtime;
	
	public Timestamp getEndtime() {
		return endtime;
	}
	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
	}
	public FFTDetails() {}
	
	@Override
	public String toString() {
        return getUri();
	}
	public String getUri() {
        return "fftresult/"+this.getId().toString();
	}
	
	public String toJson() {
        final Gson gson = new GsonBuilder().create();
        String json = gson.toJson(this);
        return json;
	}

}
