import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

public class BDict implements BElement{

	private final Map<BString,BElement> map;

	public BDict(){
		this.map = new LinkedHashMap<>();
	}

	public void put(BString key, BElement value){
		this.map.put(key,value);
	}

	public Object find(BString key){
		return this.map.get(key);
	}
	
	public byte [] bencode(){
		ArrayList<Byte> bytes = new ArrayList<>();

		bytes.add((byte) 'd');

		for (Map.Entry<BString, BElement> entry : this.map.entrySet()){
			for(byte b:entry.getKey().bencode())
				bytes.add(b);
			for(byte b:entry.getValue().bencode())
				bytes.add(b);
		}

		bytes.add((byte) 'e');

		byte[] output = new byte[bytes.size()];
		for(int i=0;i<bytes.size();++i)
			output[i]= bytes.get(i);

		return output;
	}

	public String B_to_String(){

		StringBuilder sb = new StringBuilder();

		sb.append("d");
		for (Map.Entry<BString, BElement> entry : this.map.entrySet()){
			sb.append(entry.getKey().B_to_String());
			sb.append(entry.getValue().B_to_String());
		}
		sb.append("e");
		return sb.toString();

	}

    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
        sb.append("\n[\n");
        for (Map.Entry<BString, BElement> entry : this.map.entrySet())
        {
            sb.append(entry.getKey()).append(" :: ").append(entry.getValue()).append("\n");
        }
        sb.append("]");

        return sb.toString();
    }

    public static void main(String[] args){
    	BString turd = new BString("whatever");
    	BInt test = new BInt (new Long(434123123132L));
    	BDict temp = new BDict();
    	temp.put(turd,test);

    	System.out.println(new String(temp.bencode()));
    	System.out.println(temp.B_to_String());
    }
}