import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class BReader {

    private int index=0;
    private byte [] data;

    public BReader(File f) throws IOException{
        Path path = Paths.get(f.getAbsolutePath());
        this.data = Files.readAllBytes(path);
    }

    public BReader(String s){
        data = s.getBytes();
    }
   
    public BElement[] Decode(){
        final ArrayList<BElement> items= new ArrayList<>();
        while(index<data.length){
          items.add(DecodeOne());
        }

        return items.toArray(new BElement[items.size()]);
    }
    private BElement DecodeOne(){

        if(data[index]>='0' && data[index]<='9'){
          return DecodeString();
        }
        else if(data[index]=='i'){
          return DecodeInt();

        }
        else if(data[index]=='l'){
          return DecodeList(); 
        }
        else if(data[index]=='d'){
          return DecodeMap();
        }

        throw new Error("Parser in invalid state at byte " + data[index]);
    }

    private BString DecodeString(){

        int length = parseNum(':') ;
        byte [] value = new byte [length];
        for(int i=0;i<length;++i){
            value[i] = data[index++];
        }
        return new BString(value);
        }

    private BInt DecodeInt(){

        index++;
        Long out = new Long(parseNum('e'));
        return new BInt(out);
    }

    private BList DecodeList(){
        index++;
        final BList items= new BList();
        while(data[index]!='e'){
          items.add(DecodeOne());
          
        }
        index++;
       
        return items;
    }

    private BDict DecodeMap(){
        index++;
        BDict  map= new BDict();
        while(data[index]!='e'){
          BString key =(BString) DecodeOne();
          BElement value = DecodeOne();
          map.put(key,value);
          
        }
        index++;
        return map;
    }

    private int parseNum(char end){

        int num = 0 ;
        Boolean neg = false;
        if(data[index]=='-'){
          neg = true;
          index++;
        }
        while(data[index]!=end){
          num = num*10 + (data[index] -'0');
          index++;
        }
        index++;
        return (neg)? -num:num;
    }
    private static String readFile(String pathname) throws IOException {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");
        System.out.println(file);
        try {
            while(scanner.hasNextLine()) {

                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    public static void main(String[] args) throws IOException {
    
      String temp = "d9:publisher3:bob17:publisher-webpage15:www.example.com18:publisher.location4:homee";
      
      BReader test = new BReader(temp);
      BElement t = test.Decode()[0];
      System.out.println(t.B_to_String().equals(temp));
      BReader filereader = new BReader(new File("KNOPPIX_V7.7.1DVD-2016-10-22-EN.torrent"));
      BDict dict = (BDict) filereader.Decode()[0];
      BString c = new BString("info");
      BDict info = (BDict) dict.find(c);
      try (FileOutputStream fos = new FileOutputStream("turd.torrent")) {
         fos.write(info.bencode());
         //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
      }
      BString piece = (BString) info.find(new BString("pieces"));
      System.out.println(piece.getData().length%4);
     // String encoded = new String(info.bencode());
      //System.out.println(encoded);
      

    }

};