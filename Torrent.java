
import java.util.List;

public class Torrent{
	private  String announce;
	private  String name;
	private  byte[] infoHash;
  private  String urlInfoHash;
	private  String comment;
	private  String createdBy;
	private  Long pieceLength;
	private  Long totalSize;
	private  Long creationDate;
	private  List<String> pieces;
	private  List<String> announceList;
	private  List<TorrentFile> fileList;
	private  byte[] piecedata;
	
	private boolean singlefile;

  	public void setAnnounce(String announce){
  		this.announce = announce;
  	}

  	public void setName(String name){
  		this.name = name;
  	}

  	public void setPieceLength(Long pieceLength){
  		this.pieceLength = pieceLength;
  	}

  	public void setTotalSize(Long totalSize){
  		this.totalSize = totalSize;
  	}
  	public void setPieces(List<String> pieces){
  		this.pieces = pieces;
  	}

  	public void setPieceData(byte [] piecedata){
  		this.piecedata = piecedata;
  	}

  	public void setComment(String comment){
  		this.comment = comment;
  	}

  	public void setCreatedBy(String createdBy){
  		this.createdBy = createdBy;
  	}

  	public void setCreationDate(Long creationDate){
  		this.creationDate= creationDate;
  	}

  	public void setAnnounceList(List<String> announceList){
  		this.announceList = announceList;
  	}

  	public void setInfoHash(byte[] infoHash){
  		this.infoHash = infoHash;
  	}

    public void setUrlInfoHash(String urlInfoHash){
      this.urlInfoHash = urlInfoHash;
    }

  	public void setSingleFile(boolean singlefile){
  		this.singlefile = singlefile;
  	}

  	public void setFileList(List<TorrentFile> fileList){
  		this.fileList = fileList;
  	}


  	public String getAnnounce(){
  		return this.announce;
  	}

  	public String getName(){
  		return this.name;
  	}

  	public Long getPieceLength(){
  		return this.pieceLength;
  	}

  	public Long getTotalSize(){
		return this.totalSize;
	}

  	public List<String> getAnnounceList(){
  		return this.announceList;
  	}
  	public List<String> getPieces(){
  		return this.pieces;
  	}

  	public byte [] getPieceData(){
  		return this.piecedata;
  	}

  	public String getComment(){
  		return this.comment;
  	}
	
	public String getCreatedBy(){
		return this.createdBy;
	}

	public Long getCreationDate(){
		return this.creationDate;
	}

	public byte[] getInfoHash(){
		return this.infoHash;
	}

  public String getUrlInfoHash(){
    return this.urlInfoHash;
  }

	public List<TorrentFile> getFileList(){
		return this.fileList;
	}
	public boolean isSingleFile(){
		return this.singlefile;
	}
}