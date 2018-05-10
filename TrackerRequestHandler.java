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

		//R%5b%c4%a5Pwg%26%1c%03%a0w%1b%c9%3b%dd%a5K%e2%ec
		//R%5b%c4%a5Pwg%26%1c%03%a0w%1b%c9%3b%dd%a5K%e2%ec
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
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		while((inputLine=in.readLine())!=null)
			System.out.println(inputLine);
		in.close(); 
		System.out.println(test);
		return this.response;
	}

	public static void main(String[] args) throws IOException{
		
		TorrentParser test = new TorrentParser();
		Torrent t = test.parseTorrent("t.torrent");
		TrackerRequestHandler handler = new TrackerRequestHandler(t);
		handler.getTrackerResponse();

	}
}