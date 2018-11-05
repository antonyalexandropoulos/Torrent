import java.net.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.lang.IllegalArgumentException;
import java.util.Iterator;
import java.util.Arrays;

class PeerConnection implements Runnable {

    public List<Piece> pieces;
    private Peer peer;
    private Torrent t;
    private TreeSet<Integer> peerAvailability=new TreeSet<Integer>();
    private Boolean choked= true;
    public boolean requesting = false;

    PeerConnection(Peer peer,List<Piece> pieces,Torrent t) {
    	this.peer = peer;
        this.pieces = pieces;
        this.t = t;
    }
    public synchronized void setPieceIndex(int index,int value){
    	this.pieces.get(index).setPieceIndex(value);

    }
    public synchronized void add(Integer piece){
    	this.peerAvailability.add(piece);

    }
    public synchronized boolean get(Integer piece){
    	return this.peerAvailability.contains(piece);
    }
    public synchronized void setChoked(Boolean choke){
    	this.choked = choke;
    }
    public synchronized void writePiece(Integer index,Integer offset,byte[] piece){
    	Piece currentPiece = pieces.get(index);
    	System.out.println(index);
    	try{
    		currentPiece.writeBytes(piece,offset);
    		System.out.println(Thread.currentThread().getId()+" "+ this.peer+" "+currentPiece.isDone());
    	}
    	catch(IOException e){
    		System.out.println(e+" writing piece failed");
    	}
    	
    }
    public synchronized Boolean isChoked(){
    	return this.choked;
    }
    public static String MessageMap(int id){
		switch(id){
			case 0: return "Choke";
			case 1: return "Unchoke";
			case 2: return "Interested";
			case 3: return "Not Interested";
			case 4:	return "Have";
			case 5: return "Bitfield";
			case 6: return "Request";
			case 7: return "Piece";
			case 8: return "Cancel";
			case 9: return "Port";

		}
		return "";

	}
	public  void showAvailable(){
		Iterator<Integer> i = this.peerAvailability.iterator(); 
        while (i.hasNext()) 
            System.out.println(i.next());
        System.out.println(this.pieces.size());
	}
    public void run(){
    	try
    	{
			System.out.println(peer);
			Messenger m = new Messenger();
			Socket sock = new Socket(peer.getIp(), peer.getPort());
			Thread test = new Thread(new Send(peer,this.t,sock,this));
			Thread test2 = new Thread(new Receive(peer,this.t,sock,this));
			test.start();
			System.out.println(test.getId());
			test2.start();
		 	//System.out.println( handshake);

		    //sock.close();
		   // System.out.println("done");

    	}
    	catch(IOException e)
    	{
    		System.out.println("Exception occurred "+e);
    	}
    	
    }
        
}

class Receive implements Runnable{
	private Peer peer;
    private Torrent t;
    private Socket socket;
    private PeerConnection connection;

	Receive(Peer peer,Torrent t,Socket socket,PeerConnection connection){
		this.peer = peer;
		this.t = t;
		this.socket = socket;
		this.connection= connection;

	}
	private synchronized void ProcessMessage(String message,byte[] data){

		switch(message){

			case "Bitfield":
				this.ProcessBitfield(data);
				break;
			case "Have":
				this.ProcessHave(data);
				break;
			case "Choke":
				this.ProcessChoke(true);
				break;
			case "Unchoke":
				this.ProcessChoke(false);
				break;
			case "Piece":
				this.ProcessPiece(data);
				break;
			default:
				throw new IllegalArgumentException("Invalid message: " + message);
		}
		
	}
	private void ProcessBitfield(byte[] data){
		Integer pieceIndex=0;
		for(int i=0;i<data.length;++i){
			for(int j=7;j>=0;--j){
				if((data[i] & (1<<j))==1){
					this.connection.add(pieceIndex);
				}
				pieceIndex+=1;
			}
		}

		//this.connection.showAvailable();
        

	}
	private void ProcessHave(byte[] data){
		Integer pieceNumber = Utils.bytesToInt(data);
		connection.add(pieceNumber);
		
	}

	private void ProcessChoke(boolean choked){
		this.connection.setChoked(choked);

	}

	

	private void ProcessPiece(byte[] data){
		byte [] idx = {data[0],data[1],data[2],data[3]};
		byte [] off= {data[4],data[5],data[6],data[7]}; 
		Integer index = Utils.bytesToInt(idx);
		Integer offset= Utils.bytesToInt(off);
		byte[] piece = Arrays.copyOfRange(data, 8, data.length);
		this.connection.writePiece(index,offset,piece);
		this.connection.requesting= false;


	}

	public void run(){
		try
		{
		    InputStream in = this.socket.getInputStream();
		    BufferedInputStream bis = new BufferedInputStream(in);
		    ByteArrayOutputStream tmp = new ByteArrayOutputStream();
		    BufferedOutputStream bos = new BufferedOutputStream(tmp);
		    //byte[] buffer = new byte[1024];
		  

		    //System.out.println(new String(buffer));
		    
		    //sock.setSoTimeout(20);
		    int count;
		    int byteindex = 0;
		    int currlen = 0;
		    String currentMessage = "";
		    int start = 0;
		    byte[] buffer = new byte[8192]; // or 4096, or more
		   	List<Byte> blob = new ArrayList<Byte>();
		    while ((count = bis.read(buffer,0,buffer.length)) > 0)
		    {
		    
		      
		     // System.out.println(count);
		      for(int i=0;i<count;++i,++start){
		      	if(start<68)continue;
		      	if(currlen==0){
		      		if(i+3>=count)
		      			throw new Error("Invalid socket input");
		      		byte [] prefix = {buffer[i],buffer[i+1],buffer[i+2],buffer[i+3]};
		      		//System.out.println(buffer[i]);
				    //System.out.println(buffer[i+1]);
				    //System.out.println(buffer[i+2]);
				    //System.out.println(buffer[i+3]);
				    //System.out.println(buffer[i+4]);
		      		currlen = Utils.bytesToInt(prefix);
		      		//System.out.println(currlen);
		      		if (currlen==0)
		      			this.connection.requesting=false;
		      		
		      		//System.out.println(currentMessage);
		      		i+=3;
		      	}
		      	else{
		      		if(byteindex==0){
		      			currentMessage = PeerConnection.MessageMap(buffer[i]);
		      		}
		      		else
		      			blob.add(buffer[i]);
		      		//System.out.println(currentMessage);
		      		//System.out.println(buffer[i]);
		      		
		      		byteindex++;
		      		if(byteindex==currlen){
		      			currlen = 0;
		      			byteindex = 0;
		      			if (currentMessage=="Unchoke"){
		      				Messenger m = new Messenger();
		      				OutputStream os = this.socket.getOutputStream();
		      				
		      			}
		      			ProcessMessage(currentMessage,Utils.byteUnwrap(blob));
		      			//System.out.println(this.connection.isChoked());
		      			currentMessage = "";
		      			blob.clear();

		      		}
		      		
		      		//currBlob[byteindex-1]= buffer[i];

		      	}

		      }
		      bos.write(buffer, 0, count);
		      /*
		      System.out.println(buffer[0]);
		      System.out.println(buffer[1]);
		      System.out.println(buffer[2]);
		      System.out.println(buffer[3]);
		      System.out.println(buffer[4]);
		      
		      System.out.println("");
		     // break;
		     */
		     
		     
		    }
		    bos.close();
		    bis.close();
		    
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

	}
}

class Send implements Runnable{
	private Peer peer;
    private Torrent t;
    private Socket socket;
    private PeerConnection connection;

	Send(Peer peer,Torrent t,Socket socket,PeerConnection connection){
		this.peer = peer;
		this.t = t;
		this.socket = socket;
		this.connection=connection;
	}

	public void run(){
		System.out.println(this.peer);
		Messenger m = new Messenger();
		
		System.out.println(this.socket.isConnected());
		byte [] handshake = m.HandShake(t.getInfoHash());
		try
		{
			OutputStream os = this.socket.getOutputStream();
		    os.write(handshake, 0, handshake.length);
		    os.flush();
		    byte[] f = m.Interested();
		    os.write(f, 0, f.length);
		    os.flush();
		    byte[] c= m.UnChoke();
		    os.write(c, 0, c.length);
		    os.flush();
		    int requested=0;
		    while(true){
		    	if(this.connection.isChoked()==false && !this.connection.requesting ){
		    		requested=0;
		    		for(Piece piece:this.connection.pieces){
		    			if(requested>0)break;
		    			if(  this.connection.pieces.get(7).isDone())continue;
		    			if(this.connection.get(piece.getPieceNumber())){
		  
		    				byte [] request = m.Request(7,this.connection.pieces.get(7).getPieceIndex(),16384);
		    				//System.out.println(this.t.getPieceLength().intValue());
		    				System.out.println("requesting "+7+" "+ piece.isDownloading() );
		    				//for(byte b:request)System.out.println(b);
		    				os.write(request, 0, request.length);
		    				os.flush();
		    				piece.setDownloading(true);
		    				System.out.println("ITS NOT FALSE: "+piece.isDownloading());
		    				this.connection.requesting=true;
		    				requested+=1;
		    				
		    			}
		    		}
		    	}
		    }
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

	}
}
