import java.util.Arrays;

public class BString implements BElement{
	byte [] data;

	public BString(String s){
		data = s.getBytes();
	}

	public BString(byte [] b){
		data = b;
	}

	public byte[] getData(){
		return data;
	}
	public byte [] bencode(){
		byte [] len = lenToBytes(Long.toString(data.length));
		byte [] output = new byte[len.length + data.length + 1];
		System.arraycopy(len, 0, output, 0, len.length);
		output[len.length] = ':';
		for(int i=0;i<data.length;++i){
			output[len.length+i+1] = data[i];
		}
		return output;
	}

	public String B_to_String(){

		return data.length + ":" + new String(data);

	}
	public byte[] lenToBytes(String len){
		byte [] out = new byte[len.length()];
		for(int i=0;i<len.length();++i){
			out[i] = (byte) len.charAt(i);
		}
		return out;
	}
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        BString that = (BString) o;
 
        return Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode(){
        return data != null ? Arrays.hashCode(data) : 0;
    }
    @Override
    public String toString(){
    	return new String(this.data);
    }
    public static void main(String[] args){
    	String turd = "whatever";
    	BString test = new BString (turd);
    	System.out.println(new String(test.bencode()));
    	System.out.println(test.B_to_String());
    }
}