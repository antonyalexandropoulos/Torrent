

public class TrackerRequest{

	private String infoHash;
	private String peerId;
	private int port;
	private int uploaded;
	private int downloaded;
	private int left;
	private enum compact{0,1};
	private enum event{"started","stopped","completed"};
	
	//optional
	private String ip;
	private int numwant;
	private String key
	private String trackerId;

	public void setInfoHash(String infoHash){
		this.infoHash = infoHash;
	}

	public void setPeerId(String peerId){
		this.peerId = peerId;
	}

	public void setPort(int port){
		this.port = port;
	}

	public void setUploaded(int uploaded){
		this.uploaded = uploaded;
	}

	public void setUploaded(int downloaded){
		this.downloaded = downloaded;
	}

	public void setLeft(int left){
		this.left = left;
	}
	

	public String getRequest{
		return ""
	}
}