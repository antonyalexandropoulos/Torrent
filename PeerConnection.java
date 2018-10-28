import java.net.*;
import java.io.*;
import java.util.List;

class PeerConnection implements Runnable {

    private List<Piece> pieces;
    private Peer peer;
    private Torrent t;


    PeerConnection(Peer peer,List<Piece> pieces,Torrent t) {
    	this.peer = peer;
        this.pieces = pieces;
        this.t = t;
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
			System.out.println(sock.isConnected());
			byte [] handshake = m.HandShake(t.getInfoHash());
			OutputStream os = sock.getOutputStream();
		    os.write(handshake, 0, handshake.length);
		    os.flush();

		 	System.out.println( handshake);

		    InputStream in = sock.getInputStream();
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
		      		byte [] currBlob = new byte[currlen];
		      		
		      		System.out.println(currentMessage);
		      		i+=3;
		      	}
		      	else{
		      		if(byteindex==0){
		      			currentMessage = MessageMap(buffer[i]);
		      		}
		      		System.out.println(currentMessage);
		      		byteindex++;
		      		if(byteindex==currlen){
		      			currlen = 0;
		      			byteindex = 0;
		      			currentMessage = "";

		      		}
		      		//currBlob[byteindex++]= buffer[i];

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
		    sock.close();
		    System.out.println("done");

    	}
    	catch(IOException e)
    	{
    		System.out.println("Exception occurred");
    	}
    	
    }
        
}
