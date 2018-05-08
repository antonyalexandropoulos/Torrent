import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import java.io.File;
import java.io.IOException;


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
	/*
	private List<TorrentFile> parsePeerList(BDict dictionary){
		BList query = (BList) info.find(new BString("files"));
		if(query != null){
			List<TorrentFile> fileList = new ArrayList<>();
			Iterator<BElement> it = query.getIterator();
			while(it.hasNext()){
				Object fileObject = it.next();
				if(fileObject instanceof BDict){
					BDict dict  = (BDict) fileObject;
					BList filepaths = (BList) dict.find(new BString("path"));
					BInt length = (BInt) dict.find(new BString("length"));
					List<String> paths = new LinkedList<String>();
					Iterator<BElement> filePathsIterator = filepaths.getIterator();
                    
                    while (filePathsIterator.hasNext()){
                    	 BString temp = (BString)filePathsIterator.next();
                         paths.add(new String(temp.getData()));
                    }

                    fileList.add(new TorrentFile(length.getValue(), paths));
				}
			}
			return fileList;
		}
		return null;
	}
	*/

	public static void main(String[] args)throws IOException{
		

	}
}