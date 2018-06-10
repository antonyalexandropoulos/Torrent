import java.net.*;
import java.io.*;

public class TrackerRequestHandler{
	private Torrent torrent;
	private TrackerResponse response;
	
	private int port = 6881;

	private Long uploaded;
	private Long downloaded;
	private Long left;

	public TrackerRequestHandler(Torrent torrent)throws UnsupportedEncodingException{
		this.torrent = torrent;
		//System.out.println(URLEncoder.encode(torrent.getInfoHash(),"ISO-8859-1"));
	}

	public TrackerRequest createRequest()throws UnsupportedEncodingException{
		TrackerRequest request = new TrackerRequest();
		request.setAnnounce(torrent.getAnnounce());
		request.add("info_hash",new String(this.torrent.getUrlInfoHash().getBytes(),"ISO-8859-1"));
		request.add("peer_id",new String("-TO0042-0ab8e8a31019"));
		//%f8%d0%2c%c3c%ca%da%93%12R%ec%bd%b2%91%b8%88l%1b%dd%c0
		//f8d02cc363cada931252ecbdb291b8886c1bddc0
		//85922fbee6dce5e2f5491e16bcdd9e6e427ba5aa
		//%85%92%2f%be%e6%dc%e5%e2%f5I%1e%16%bc%dd%9enB%7b%a5%aa
		//R%5b%c4%a5Pwg%26%1c%03%a0w%1b%c9%3b%dd%a5K%e2%ec
		//R%5b%c4%a5Pwg%26%1c%03%a0w%1b%c9%3b%dd%a5K%e2%ec

		//36234f25405fa31127d61c29954a4352e44e8797
		//6%23O%25%40_%a3%11%27%d6%1c%29%95JCR%e4N%87%97
		request.add("port",Integer.toString(port));
		request.add("event","started");
		request.add("uploaded","0");
		request.add("downloaded","0");
		request.add("left","6666");
		request.add("compact","1");
		request.add("no_peer_id","0");
		System.out.println(this.torrent.getInfoHash());
		System.out.println(request.getRequest());
		return request;
	}


	public TrackerResponse getTrackerResponse() throws MalformedURLException,IOException{
		URL test = new URL(createRequest().getRequest());
		HttpURLConnection conn = (HttpURLConnection)test.openConnection();
		conn.setRequestMethod("GET");
		InputStream t = conn.getInputStream();

		byte[] buffer = new byte[8192];
	    int bytesRead;
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    while ((bytesRead = t.read(buffer)) != -1)
	    {
	        output.write(buffer, 0, bytesRead);
	    }
	    byte [] blob =  output.toByteArray();
	    t.close();

		TrackerResponseParser reader = new TrackerResponseParser();
		return reader.parseResponse(blob);
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
	public static void main(String[] args) throws IOException{
		
		TorrentParser test = new TorrentParser();
		Torrent t = test.parseTorrent("ubuntustudio-18.04-dvd-amd64.iso.torrent");
		TrackerRequestHandler handler = new TrackerRequestHandler(t);
		TrackerResponse res = handler.getTrackerResponse();
		Messenger m = new Messenger();
		for(Peer peer:res.getPeerList()){
			System.out.println(peer);

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


	}
}