import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.math.BigInteger;

public class Messenger{


	public byte[] HandShake(byte[] infoHash){
		byte[] pstrlen = {(byte)19};
		byte[] pstr    = "BitTorrent protocol".getBytes();
		byte[] reserved = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};

		byte[] msg = new byte[pstrlen.length+pstr.length+reserved.length+40];
		int index  = 0;

		for(byte b:pstrlen) msg[index++]= b;
		for(byte b:pstr) msg[index++]= b;
		for(byte b:reserved) msg[index++]= b;
		for(byte b:infoHash) msg[index++]= b;	

		return msg;
	}

	public byte[] KeepAlive(){
		byte [] msg = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
		return msg;
	}
	
	public byte[] Choke(){
		byte [] msg = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x01};
		return msg;
	}

	public byte[] UnChoke(){
		byte [] msg = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x02};
		return msg;
	}

	public byte[] Interested(){
		byte [] msg = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x02};
		return msg;
	}

	public byte[] NotInterested(){
		byte [] msg = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x03};
		return msg;
	}

	public byte[] Request(int index,int begin,int length){
		byte[] idx = Utils.IntToByte(index,4);
		byte[] s   = Utils.IntToByte(begin,4);
		byte[] l   = Utils.IntToByte(length,4);
		byte [] msg = {(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x03,(byte)0x06};
		byte[] res = new byte[17];
		int current = 0 ;
		for(byte b:msg)
			res[current++]=b;
		for(byte b:idx)
			res[current++]=b;
		for(byte b:s)
			res[current++]=b;
		for(byte b:l)
			res[current++]=b;
		return res;
	}

	public byte[] Have(int index){
		byte[] idx = Utils.IntToByte(index,4);
		byte [] msg = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x05,(byte)0x03,idx[0],idx[1],idx[2],idx[3]};
		return msg;
	}

	public static void main(String [] args){
		Messenger m = new Messenger();

		for(Byte b:m.Request(55,101,2147483647))
			System.out.println(b & 0xFF);
		/*
		for(Byte b:m.Choke())
			System.out.println(b);
		for(Byte b:m.UnChoke())
			System.out.println(b);
		*/
		byte [] tmp = m.Request(55,101,2147483647);
		byte []test = {tmp[7],tmp[8]};
		System.out.println(Utils.bytesToInt(test));
		System.out.println(((tmp[13] & 0xFF)<<24) | ((tmp[14] & 0xFF)<<16 )| ((tmp[15] & 0xFF)<<8 )| ((tmp[16] & 0xFF) ));
		
	}
}