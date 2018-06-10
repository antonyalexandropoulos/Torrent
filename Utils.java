
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.math.BigInteger;
import java.io.*;
import java.net.*;

public class Utils{
  public static byte[] byteInfoHash(byte[] input){
    try
    {
      MessageDigest md=MessageDigest.getInstance("SHA-1");
      return md.digest(input);
    }
    catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    return null;
    
  
  }
	public static String SHAsum(byte[] input)throws UnsupportedEncodingException
    {
        
        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance("SHA-1");
            //System.out.println(bytesToHex(md.digest(input)));
            //byte [] s= hexStringToByteArray("36234f25405fa31127d61c29954a4352e44e8797");
            //System.out.println(byteArray2Hex(s));
            return byteArray2Hex(md.digest(input));
            //%124Vx%9a%bc%de%f1%23Eg%89%ab%cd%ef%124Vx%9a
           // %124Vx%9A%BC%DE%F1%23Eg%89%AB%CD%EF%124Vx%9A
            //%124Vx%9A%BC%DE%F1%23Eg%89%AB%CD%EF%124Vx%9A
            //%84%e6%b5%aa%9fhG%7b%e3F%bav-%e1%a6%c8L%ae%d4%ee
            //%84%e6%b5%aa%9fhG%7b%e3F%bav-%e1%a6%c8L%ae%d4%ee
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

     private static String byteArray2Hex(final byte[] bytes) throws UnsupportedEncodingException
    {
        //System.out.println(URLEncoder.encode(new String(bytes),"UTF-8"));
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter();
        for (byte b : bytes)
        { 

            if((b>='a'&& b<='z') || (b>='A'&& b<='Z') || (b>='0'&& b<='9') ||(b=='.' || b=='-' || b=='_' || b=='~'))
            {
      
                  int temp = Integer.valueOf(Integer.toString(b), 10);
                 // System.out.println(temp);
                  sb.append((char)b);
                
            }
            else{
             
              sb.append("%");
              sb.append(String.format("%02x", b));
            }
        }
        //System.out.println(String.format(formatter.toString(), 1));
        return sb.toString();
    }

	public static String bytesToHex(byte[] bytes) {
      char[] hexArray = "0123456789ABCDEF".toCharArray();
      char[] hexChars = new char[bytes.length * 2];
      for ( int j = 0; j < bytes.length; j++ ) {
          int v = bytes[j] & 0xFF;
          hexChars[j * 2] = hexArray[v >>> 4];
          hexChars[j * 2 + 1] = hexArray[v & 0x0F];
      }
      return new String(hexChars);
  }
  public static byte[] IntToByte(int n,int len){
      byte[] result = new byte[4];
      int div = 8*(len-1);
      for(int i=0;i<len;++i){
          result[i] = (byte) (n>>div);
          div-=8;
      }
      //result[0] = (byte) (i >> 24);
      //result[1] = (byte) (i >> 16);
      //result[2] = (byte) (i >> 8);
      //result[3] = (byte) (i /*>> 0*/);
      return result;
  }

  public static  int bytesToInt(byte[] bytes){
      
      int res = 0;
      int div = 8 * (bytes.length-1);
      for(int i=0;i<bytes.length;++i){
        res = res | ((bytes[i]& 0xFF) <<div);
        div-=8;
      }
      return res;
      
      // return new BigInteger(bytes).intValue();
      //return (bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF);
      
  }

  
}