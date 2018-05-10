import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import java.io.File;
import java.io.IOException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TrackerResponseParser{

	
	public TrackerResponse parseTorrent(String filepath) throws IOException{

		BReader reader = new BReader(new File(filepath));
		BElement[] out = reader.Decode();
		if(out.length>1){
			throw new Error("Response contains more than one object");
		}

		BDict dictionary = (BDict) out[0];
		TrackerResponse response= new TrackerResponse();

		response.setFailureReason(parseByteString("failure reason",dictionary));
		response.setWarningMessage(parseByteString("warning message",dictionary));
		response.setInterval(parseLong("interval",dictionary));
		response.setMinInterval(parseLong("minInterval",dictionary));
		response.setTrackerId(parseByteString("tracker id",dictionary));
		response.setComplete(parseLong("complete",dictionary));
		response.setIncomplete(parseLong("incomplete",dictionary));
		//response.setPeerList(parsePeerList(dictionary));
		return response;
	}

	private String parseByteString(String s,BDict dictionary){
		BString query =(BString) dictionary.find(new BString(s));
		if(query!=null){
			return new String(query.getData());
		}
		return null;
	}
	
	private Integer parseLong(String s,BDict dictionary){
		BInt query =(BInt) dictionary.find(new BString(s));
		if(query!=null){
			return query.getValue()!=null? query.getValue().intValue():null;
		}
		return null;
	}
	
	private List<Peer> parsePeerList(BDict dictionary) throws UnknownHostException{
		BElement query = (BElement) dictionary.find(new BString("peers"));
		List<Peer> peers = new LinkedList<>();
		if(query != null){

			//dictionary model peer list
			if(query instanceof BList){
				BList dicts = (BList) query;
				Iterator<BElement> it = dicts.getIterator();
				
				while(it.hasNext()){
					Object o = it.next();
					if(o instanceof BDict){
						BDict dict = (BDict) o;
						BInt bencodedPort= (BInt) dict.find(new BString("port"));
						BString bencodedIp = (BString) dict.find(new BString("ip"));
						Integer port = bencodedPort.getValue().intValue();
						InetAddress ip = InetAddress.getByAddress(bencodedIp.getData());
						Peer peer = new Peer(port,ip);
						peers.add(peer);
					}
				}
			}//binary model peer list 4 bytes ip address and 2 bytes port
			else if(query instanceof BString){
				BString bstring = (BString) query;
				byte [] data = bstring.getData();
				if(data.length%6 !=0)
					throw new Error("Binary model peer list incorrect");
				int numPeers = data.length / 6;
				for(int i=0;i<numPeers;++i){
					//split byte array into sixths
					byte[] curr = Arrays.copyOfRange(data, 6 * i, (6 * (i + 1)));
					byte[] ip   = Arrays.copyOfRange(curr, 0, 4);
					byte[] port = Arrays.copyOfRange(curr, 4, 6);
					Peer peer = new Peer(port,ip);
					peers.add(peer);


				}
			}

			return peers;
		}
		return null;
	}
	

	public static void main(String[] args)throws IOException{
		

	}
}