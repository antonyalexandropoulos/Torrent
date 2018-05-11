import java.net.InetAddress;
import java.net.UnknownHostException;

public class Peer{
	private Integer port;
	private InetAddress ip;

	public Peer(Integer port,InetAddress ip){
		this.port = port;
		this.ip = ip;
	}

	public Peer(byte[] port ,byte[] ip) throws UnknownHostException{
		this.port = Utils.bytesToInt(port);
		this.ip = InetAddress.getByAddress(ip);
	}

	@Override
	public String toString(){
		return "( " +String.valueOf(port) +" : " +this.ip.toString() + " )";
	}
}