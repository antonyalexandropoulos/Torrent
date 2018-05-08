import java.net.InetAddress;

public class Peer{
	private int port;
	private InetAddress ip;

	public Peer(int port,InetAddress ip){
		this.port = port;
		this.ip = ip;
	}

	@Override
	public String toString(){
		return "( " +String.valueOf(port) +this.ip.toString() + " )";
	}
}