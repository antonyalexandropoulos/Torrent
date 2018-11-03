import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Piece{
	private String hash;
	private ByteArrayOutputStream data;
	private byte[] hashBlob;
	private int number;
	private int index=0;
	private Long pieceSize;
	private boolean downloading = false;
	private boolean done= false;


	public Piece(String hash,Long pieceSize,int number,byte[] hashBlob){
		this.hash = hash;
		this.pieceSize = pieceSize;
		this.number = number;
		this.hashBlob =  hashBlob;
		this.data = new ByteArrayOutputStream() ;

	}
	private void writeBytes(byte[] bytes) throws IOException{
		data.write(bytes);
		this.index+= bytes.length;
		if(this.index==pieceSize-1)
			verify();
	}
	public int getPieceNumber(){
		return this.number;
	}
	public int getPieceIndex(){
		return this.index;
	}
	public void  setPieceIndex(int index){
		this.index = index;
	}
	public void setDownloading(boolean downloading){
		this.downloading = downloading;
	}
	public void setDone(boolean done){
		this.done = done;
	}
	public boolean verify(){
		System.out.println("verifying : "+this.number);
		return true;
	}

	@Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
        sb.append("\n(");
        sb.append(hash);
        sb.append(",");
        sb.append(Integer.toString(number));
        sb.append(",");
        sb.append(Integer.toString(index));
        sb.append(")");


        return sb.toString();
    }

}