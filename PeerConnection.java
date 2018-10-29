import java.net.*;
import java.io.*;
import java.util.List;
import java.util.HashSet;

class PeerConnection implements Runnable {

    private List<Piece> pieces;
    private Peer peer;
    private Torrent t;
    private HashSet<Integer> peerAvailable;
    private Boolean choked= true;


    PeerConnection(Peer peer,List<Piece> pieces,Torrent t) {
    	this.peer = peer;
        this.pieces = pieces;
        this.t = t;
    }
    public synchronized void add(Integer piece){
    	this.peerAvailable.add(piece);

    }
    public synchronized void get(Integer piece){
    	this.peerAvailable.contains(piece);
    }
    public synchronized void setChoked(Boolean choke){
    	this.choked = choke;
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

    public void run(){
    	try
    	{
			System.out.println(peer);
			Messenger m = new Messenger();
			Socket sock = new Socket(peer.getIp(), peer.getPort());
			Thread test = new Thread(new Send(peer,this.t,sock));
			Thread test2 = new Thread(new Receive(peer,this.t,sock));
			test.start();
			System.out.println(test.getId());
			//test2.start();
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

	Receive(Peer peer,Torrent t,Socket socket){
		this.peer = peer;
		this.t = t;
		this.socket = socket;

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
		    byte [] currBlob;
		    while ((count = bis.read(buffer,0,buffer.length)) > 0)
		    {
		    
		      
		      System.out.println(count);
		      for(int i=0;i<count;++i,++start){
		      	if(start<68)continue;
		      	if(currlen==0){
		      		if(i+3>=count)
		      			throw new Error("Invalid socket input");
		      		byte [] prefix = {buffer[i],buffer[i+1],buffer[i+2],buffer[i+3]};
		      		System.out.println(buffer[i]);
				    System.out.println(buffer[i+1]);
				    System.out.println(buffer[i+2]);
				    System.out.println(buffer[i+3]);
				    System.out.println(buffer[i+4]);
		      		currlen = Utils.bytesToInt(prefix);
		      		System.out.println(currlen);
		      		currBlob = new byte[currlen];
		      		
		      		System.out.println(currentMessage);
		      		i+=3;
		      	}
		      	else{
		      		if(byteindex==0){
		      			currentMessage = PeerConnection.MessageMap(buffer[i]);
		      		}
		      		System.out.println(currentMessage);
		      		//System.out.println(buffer[i]);
		      		byteindex++;
		      		if(byteindex==currlen){
		      			currlen = 0;
		      			byteindex = 0;
		      			currentMessage = "";

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

	Send(Peer peer,Torrent t,Socket socket){
		this.peer = peer;
		this.t = t;
		this.socket = socket;

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
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		

	 	System.out.println( new String(handshake));

	}
}
