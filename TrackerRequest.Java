
import java.util.LinkedHashMap;
import java.util.Map;

public class TrackerRequest{

	private String announce;
	private LinkedHashMap<String,String> payload;

	public TrackerRequest(){
		this.payload = new LinkedHashMap<String,String>();
	}
	public void setAnnounce(String announce){
		this.announce = announce;
	}
	public void add(String key,String value){
		this.payload.put(key,value);
	}
	public String find(String key){
		return this.payload.get(key);
	}
	public void clearPayload(){
		this.payload.clear();
	}

	public void showPayload(){
		for(Map.Entry<String, String> entry : this.payload.entrySet()){
			System.out.printf(entry.getKey(),":",entry.getValue());
			System.out.printf(":");
			System.out.println(entry.getValue());
		}
	}
	public String getRequest(){
		StringBuilder sb = new StringBuilder();
		if(this.announce==null){
			return null;
		}
		sb.append(this.announce);
		sb.append("?");

		for(Map.Entry<String, String> entry : this.payload.entrySet()){
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("&");
		}
		sb.setLength(sb.length()-1);
		return sb.toString();
	}

	@Override
	public String toString(){
		return this.getRequest();
	}

	public static void main(String[] args){
		TrackerRequest t = new TrackerRequest();
		t.setAnnounce("http://google.com");
		t.add("test","ddd");
		t.add("temp","www");
		t.showPayload();
		System.out.println(t.getRequest());
		System.out.println(t);
	}
}