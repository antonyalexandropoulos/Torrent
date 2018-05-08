import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class BList implements BElement{

	private final List<BElement> list;
	public byte[] data;

	public BList(){
		this.list = new LinkedList<BElement>();
	}
	 public Iterator<BElement> getIterator(){
        return list.iterator();
    }

    public void add(BElement o){
        this.list.add(o);
    }

	
	public byte [] bencode(){
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		bytes.add((byte)'l');
		for(BElement e : this.list){
			for(byte b:e.bencode()){
				bytes.add(b);
			}
		}
		bytes.add((byte)'e');
		byte [] output = new byte[bytes.size()];
		for(int i=0;i<bytes.size();++i){
			output[i] = bytes.get(i);
		}
		
		
		return output;
	}

	public String B_to_String(){

		StringBuilder sb = new StringBuilder();
		sb.append("l");
		for(BElement item: this.list){
			sb.append(item.B_to_String());
		}
		return sb.toString();
	}

    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("[");
    	for(Object e: this.list){
    		sb.append("e.toString()");

    	}
    	sb.append("]");
    	return sb.toString();
    }

    public static void main(String[] args){
    	BString turd = new BString("whatever");
    	BInt test = new BInt (new Long(434123123132L));
    	BList temp = new BList();
    	temp.add(turd);
    	temp.add(test);
    	System.out.println(temp.bencode());
    	System.out.println(temp.B_to_String());
    }
}