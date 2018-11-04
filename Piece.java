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
	private int times=0;

	public Piece(String hash,Long pieceSize,int number,byte[] hashBlob){
		this.hash = hash;
		this.pieceSize = pieceSize;
		this.number = number;
		this.hashBlob =  hashBlob;
		this.data = new ByteArrayOutputStream() ;

	}
	public void writeBytes(byte[] bytes) throws IOException{
		data.write(bytes);
		this.index+= bytes.length;
		this.times+=1;
		System.out.println("Succesfully wrote "+bytes.length+" bytes :piece index "+this.index+" "+this.number+" "+this.times);
		if(this.index==pieceSize){
			System.out.println("DONE"+this.number);
			verify();
		}
		this.downloading=false;

	}
	public int getPieceNumber(){
		return this.number;
	}
	public synchronized int getPieceIndex(){
		return this.index;
	}
	public void  setPieceIndex(int index){
		this.index = index;
	}
	public synchronized void setDownloading(boolean downloading){
		this.downloading = downloading;
	}
	public synchronized boolean isDownloading(){
		return this.downloading;
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