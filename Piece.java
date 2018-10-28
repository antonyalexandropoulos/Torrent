
public class Piece{
	private String hash;
	private byte[] hashBlob;
	private byte[] data;
	private int number;
	private int index=0;
	private Long pieceSize;


	public Piece(String hash,Long pieceSize,int number,byte[] hashBlob){
		this.hash = hash;
		this.pieceSize = pieceSize;
		this.number = number;
		this.hashBlob =  hashBlob;
		this.data = new byte[6];

	}

	public boolean verify(){
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