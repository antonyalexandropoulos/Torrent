
import java.util.List;

public class TrackerResponse{

	private  String failureReason;
  private  String warningMessage;
	private  Integer interval;
  private  Integer minInterval;
  private  String trackerId;
  private  Integer complete;
  private  Integer incomplete;
  private  List<Peer> peerList;


  //setters
	public void setFailureReason(String failureReason){
		this.failureReason = failureReason;
	}

	public void setWarningMessage(String warningMessage){
		this.warningMessage = warningMessage;
	}

	public void setInterval(Integer interval){
		this.interval = interval;
	}

	public void setMinInterval(Integer minInterval){
		this.minInterval = minInterval;
	}

	public void setTrackerId(String trackerId){
		this.trackerId = trackerId;
	}

	public void setComplete(Integer complete){
		this.complete = complete;
	}

	public void setIncomplete(Integer incomplete){
		this.incomplete = incomplete;
	}

	public void setPeerList(List<Peer> peerList){
		this.peerList= peerList;
	}

  //getters

  public String getFailureReason(){
    return this.failureReason;
  }

  public String getWarningMessage(){
    return this.warningMessage;
  }
	
  public Integer getInterval(){
    return this.interval;
  }

  public Integer getMinInterval(){
    return this.minInterval;
  }

  public String getTrackerId(){
    return this.trackerId;
  }

  public Integer getComplete(){
    return this.complete;
  }

  public Integer incomplete(){
    return this.incomplete;
  }

  public List<Peer> getPeerList(){
    return this.peerList;
  }
}