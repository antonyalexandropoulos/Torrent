
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;


import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;


public class TorrentParser{

	private long multiFileLength=0;
	public Torrent parseTorrent(String filepath) throws IOException{
		BReader reader = new BReader(new File(filepath));
		

		BElement[] out = reader.Decode();
		if(out.length>1){
			throw new Error("Invalid torrent file given");
		}

		BDict dictionary = (BDict) out[0];
		BDict info =(BDict) dictionary.find(new BString("info"));
		Torrent torrent = new Torrent();

		//System.out.println(info.find(new BString("piece length")));
		
		//Main dictionary
	
		torrent.setInfoHash(Utils.byteInfoHash(info.bencode()));
		torrent.setUrlInfoHash(Utils.SHAsum(info.bencode()));
		torrent.setAnnounce(parseByteString("announce",dictionary));
		torrent.setName(parseByteString("name",dictionary));
		torrent.setCreatedBy(parseByteString("created by",dictionary));
		torrent.setComment(parseByteString("comment",dictionary)); 
		torrent.setCreationDate(parseLong("creation date",dictionary));

		//Info dictionary
		torrent.setPieceLength(parseLong("piece length",info));
		torrent.setPieceData(parseByteString("pieces",info).getBytes());
		torrent.setFileList(parseFileList(info));
        torrent.setPieces(parsePieceHashes(info));

        torrent.setSingleFile(info.find(new BString("length"))!=null);

		if(torrent.isSingleFile())
			torrent.setTotalSize(parseLong("length",info));
		else
			torrent.setTotalSize(this.multiFileLength);

		
		return torrent;
	}

	private String parseByteString(String s,BDict dictionary){
		BString query =(BString) dictionary.find(new BString(s));
		if(query!=null){
			return new String(query.getData());
		}
		return null;
	}
	
	private Long parseLong(String s,BDict dictionary){
		BInt query =(BInt) dictionary.find(new BString(s));
		if(query!=null){
			return query.getValue();
		}
		return null;
		}

	private List<TorrentFile> parseFileList(BDict info){
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
					this.multiFileLength += length.getValue();
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

	private List<Piece> parsePieceHashes(BDict info){
		BString query = (BString) info.find(new BString("pieces"));
		if(query != null){
			byte [] data = query.getData();
			List <Piece>  hashes = new ArrayList<>();
			if(data.length%20!=0)
				throw new Error("Piece data not a multiple of 20");
			int hashcount = data.length / 20;
			for(int currHash=0;currHash<hashcount;++currHash){
				byte[] curr = Arrays.copyOfRange(data, 20 * currHash, (20 * (currHash + 1)));
				String sha1 = Utils.bytesToHex(curr);
				Long pieceLength = parseLong("piece length",info);
				if (currHash==hashcount-1){
					if(parseLong("length",info)!=null)
						pieceLength = parseLong("length",info) % pieceLength==0 ? pieceLength : parseLong("length",info) % pieceLength;
					else
						pieceLength = multiFileLength % pieceLength==0 ? pieceLength : multiFileLength % pieceLength;

				}
				Piece currPiece = new Piece(sha1,pieceLength,currHash,curr);
				hashes.add(currPiece);
			}
			
			return hashes;
			
		}
		else
			throw new Error("No piece data found in the file");
	}


	public static void main(String[] args)throws IOException{
		TorrentParser test = new TorrentParser();
		Torrent t = test.parseTorrent("kubuntu-18.04-desktop-amd64.iso.torrent");
		System.out.println(t.getComment());
		System.out.println(t.getCreationDate());
		System.out.println(t.getCreatedBy());
		System.out.println(t.getTotalSize());
		System.out.println(t.getPieceLength());
		
		System.out.println(t.getFileList());
		System.out.println(t.getInfoHash());
		System.out.println(t.isSingleFile());
		System.out.println(t.getPieces());

	}
}