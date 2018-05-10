import java.util.Arrays;

public class BInt implements BElement{

	private final Long value;

	public BInt(Long n){

		this.value = n;
		System.out.println(n);
		System.out.println(this.value);
	}

	public Long getValue(){
		return value;
	}
	
	public byte [] bencode(){
		
		byte [] digits = lenToBytes(value.toString());

		byte [] output = new byte[digits.length + 2];
		int index = 0;
		output[index++]=(byte) 'i';
		for(int i=0;i<digits.length;++i){
			output[index++] = digits[i];
		}
		output[index]=(byte) 'e';
		return output;
	}

	public String B_to_String(){

		return "i" + value + "e" ;

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
 
        BInt that = (BInt) o;
 
        return value.equals(that.value);
    }

    @Override
    public int hashCode(){
        return value.hashCode();
    }
    public String toString(){
    	return String.valueOf(value);
    }

    public static void main(String[] args){
    	String turd = "whatever";
    	BInt test = new BInt (new Long(434123123132L));
    	System.out.println(new String(test.bencode()));
    	System.out.println(test.B_to_String());
    }
}