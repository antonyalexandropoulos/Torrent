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
		request.add("info_hash",new String(this.torrent.getInfoHash().getBytes(),"ISO-8859-1"));
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

	public static void main(String[] args) throws IOException{
		
		TorrentParser test = new TorrentParser();
		Torrent t = test.parseTorrent("ubuntustudio-18.04-dvd-amd64.iso.torrent");
		TrackerRequestHandler handler = new TrackerRequestHandler(t);
		TrackerResponse res = handler.getTrackerResponse();
		for(Peer peer:res.getPeerList()){
			System.out.println(peer);
		}


	}
}