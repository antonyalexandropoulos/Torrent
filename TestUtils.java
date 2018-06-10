
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.math.BigInteger;
import java.io.*;
import java.net.*;
import java.util.Random;

public class TestUtils{
  private static  void TestByteConversions(){
    int max= 99999,min = 1;
    for(int i=0;i<1000000;i++){
      Random rand= new Random();
      int randomNum = rand.nextInt((max - min) + 1) + min;
      //System.out.println(randomNum);
      byte []test = Utils.IntToByte(randomNum,4);
      assert (Utils.bytesToInt(test)==randomNum);
    }
    
  }


  public static  void main (String[] args){
      
      TestByteConversions();
      
  }
}