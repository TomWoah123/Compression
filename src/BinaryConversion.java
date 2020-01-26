import java.io.*;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class BinaryConversion
{
    public static void main( String[] args )
    {
//        Scanner scan = new Scanner( System.in );
//        System.out.println( "Type a word" );
//        binaryConvert( "helloworld" );
//        String str = scan.nextLine();
//        lzwConvert( str );
//        lzwToFile();
//        decompressLZW( "00011010000001100001001000000100100000000010000011000110000100100001000010000000", 10 );
//        findBestBitLevel( "dalalalalaladalala" );
        decompressFromFile();
    }

    public static void binaryConvert( String string )
    {
        StringBuilder result = new StringBuilder();
        for ( int i = 0; i < string.length(); i++ )
        {
            char c = string.charAt( i );
            StringBuilder binary = new StringBuilder( Integer.toBinaryString( (int) c) );
            while ( binary.length() < 10 )
            {
                binary.insert( 0, "0" );
            }
            result.append( binary + " " );
        }
        System.out.println( string + " in binary is " + result );
    }

    public static String lzwConvert( String string )
    {
        int dictValue = 128;
        String binary = "";
        Map< String, Integer > dictionary = new TreeMap<>();
        int i = 0;
        int numOutputs = 0;
        System.out.print( "Output String: " );
        if ( string.length() == 1 )
        {
            String curr = "" + string.charAt( 0 );
            binary = "0" + Integer.toBinaryString( (int) string.charAt( 0 ) );
            while ( binary.length() <= 10 )
            {
                binary = "0" + binary;
            }
            System.out.println( curr );
            System.out.println( "Dictionary: " + dictionary.toString() );
            System.out.println( "Binary: " + binary );
            System.out.println( "Compression Ratio: 0.00" );
            return binary;
        }
        if ( string.length() == 2 )
        {
            String bin = Integer.toBinaryString( (int) ( string.charAt( 0 ) ) ) + " ";
            while( bin.length() <= 10 )
            {
                bin = "0" + bin;
            }
            binary += bin;
            String bin2 = Integer.toBinaryString( (int) ( string.charAt( 0 ) ) );
            while( bin2.length() <= 10 )
            {
                bin2 = "0" + bin2;
            }
            dictionary.put( string, 128 );
            binary += bin2;
            System.out.println( string );
            System.out.println( "Dictionary: " + dictionary.toString() );
            System.out.println( "Binary: " + binary );
            System.out.println( "Compression Ratio: 1.00" );
            return binary;
        }
        while( i < string.length() - 2 )
        {
            String current = "" + string.charAt( i );
            String next = "" + string.charAt( i + 1 );
            String key = current + next;
            while ( dictionary.containsKey( key ) )
            {
                current = key;
                i++;
                if ( i < string.length() - 1 )
                {
                    next = "" + string.charAt( i + 1 );
                    key = current + next;
                }
                else
                {
                    dictValue--;
                    break;
                }
            }
            if ( current.length() == 1 )
            {
                String bin = Integer.toBinaryString( (int) ( current.charAt( 0 ) ) ) + " ";
                while( bin.length() <= 10 )
                {
                    bin = "0" + bin;
                }
                binary += bin;
            }
            else
            {
                String bin = Integer.toBinaryString( dictionary.get( current ) );
                while( bin.length() < 10 )
                {
                    bin = "0" + bin;
                }
                binary += bin + " ";
            }
            Integer value = dictValue;
            dictionary.put( key, value );
            dictValue++;
            numOutputs++;
            i++;
            String output = current;
            System.out.print( output );
        }
        if ( i == ( string.length() - 2 ) )
        {
            String c = "" + string.charAt( i );
            String n = "" + string.charAt( i + 1 );
            String k = c + n;
            if ( !dictionary.containsKey( k ) )
            {
                dictionary.put( k, dictValue );
            }
            binary += "00" + Integer.toBinaryString( dictionary.get( k ) );
            numOutputs++;
            System.out.println( k );
        }
        else if ( i == string.length() - 1 )
        {
            String c = "" + string.charAt( i );
            System.out.println( c );
            String bin = Integer.toBinaryString( (int) c.charAt( 0 ) );
            while( bin.length() < 10 )
            {
                bin = "0" + bin;
            }
            binary += bin;
            numOutputs++;
        }
        else
        {
            System.out.println();
        }
        System.out.println( "Dictionary: " + dictionary.toString() );
        System.out.println( "Binary: " + binary );
        double stringbits = string.length() * 10;
        System.out.println( "Original: " + (int) stringbits );
        double lzwbits = numOutputs * 10;
        System.out.println( "Compressed: " + (int) lzwbits );
        double ratio = stringbits / lzwbits;
        DecimalFormat df = new DecimalFormat( "0.00" );
        System.out.println( "Compression Ratio: " + df.format( ratio ) );
        return binary;
    }

    public static void lzwToFile()
    {
        File file = new File( "./src/string.txt" );
        File f = new File( "./src/string_compressed.txt" );
        FileWriter fr = null;
        try {
            Scanner scanner = new Scanner( file );
            String compress = "";
            while ( scanner.hasNext() )
            {
                fr = new FileWriter( f, true );
                compress += scanner.nextLine();
            }
            fr.write( lzwConvert( compress ) );
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decompressLZW( String binary, int bitlevel )
    {
        int[] nums = new int[ binary.length() / bitlevel ];
        int index = 0;
        for ( int i = 0; i < binary.length(); i+=bitlevel )
        {
            nums[index] = Integer.valueOf( binary.substring( i, i + bitlevel ), 2 );
            index++;
        }
        Map< Integer, String > dictionary = new TreeMap<>();
        int j = 0;
        int dictValue = 128;
        String result = "";
        while( j < nums.length - 1 )
        {
            int current = nums[ j ];
            int next = nums[ j + 1 ];
            if ( current < 128 )
            {
                char output = (char) current;
                result += output;
                if ( next < 128 )
                {
                    String dict = output + "" + (char) next;
                    dictionary.put( dictValue, dict );
                    dictValue++;
                }
                else
                {
                    if ( dictionary.get( next ) == null )
                    {
                        String dict = output + "" + output;
                        dictionary.put( dictValue, dict );
                        dictValue++;
                    }
                    else
                    {
                        String dict = output + dictionary.get( next ).substring(0,1);
                        dictionary.put( dictValue, dict );
                        dictValue++;
                    }
                }
            }
            else
            {
                String output = dictionary.get( current );
                result += output;
                if ( next < 128 )
                {
                    String dict = output + (char) next;
                    dictionary.put( dictValue, dict );
                    dictValue++;
                }
                else
                {
                    if ( dictionary.get( next ) == null )
                    {
                        String dict = output + output.substring( 0, 1 );
                        dictionary.put( dictValue, dict );
                        dictValue++;
                    }
                    else
                    {
                        String dict = output + dictionary.get( next ).substring(0,1);
                        dictionary.put( dictValue, dict );
                        dictValue++;
                    }
                }
            }
            j++;
        }
        int last = nums[ nums.length - 1 ];
        if ( last < 128 )
        {
            result += (char) last;
        }
        else
        {
            result += dictionary.get( last );
        }
        System.out.println( "String Decompressed: " + result );
    }

    public static void decompressFromFile()
    {
        File file = new File( "./src/binary_values.txt" );
        try
        {
            Scanner scanner = new Scanner( file );
            String bit = scanner.nextLine();
            int bitlevel = Integer.valueOf( bit, 2 );
            String binary = "";
            while ( scanner.hasNext() )
            {
                binary += scanner.nextLine();
            }
            decompressLZW( binary, bitlevel );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public static double lzwReturnDouble(String string, int bitlevel )
    {
        int dictValue = 128;
        String binary = "";
        Map< String, Integer > dictionary = new TreeMap<>();
        int i = 0;
        int numOutputs = 0;
//        System.out.print( "Output String: " );
        if ( string.length() == 1 )
        {
            String curr = "" + string.charAt( 0 );
            binary = "0" + Integer.toBinaryString( (int) string.charAt( 0 ) );
            while ( binary.length() <= bitlevel )
            {
                binary = "0" + binary;
            }
//            System.out.println( curr );
//            System.out.println( "Dictionary: " + dictionary.toString() );
//            System.out.println( "Binary: " + binary );
//            System.out.println( "Compression Ratio: 0.00" );
            return 0.00;
        }
        if ( string.length() == 2 )
        {
            String bin = Integer.toBinaryString( (int) ( string.charAt( 0 ) ) ) + " ";
            while( bin.length() <= bitlevel )
            {
                bin = "0" + bin;
            }
            binary += bin;
            String bin2 = Integer.toBinaryString( (int) ( string.charAt( 0 ) ) );
            while( bin2.length() <= bitlevel )
            {
                bin2 = "0" + bin2;
            }
            dictionary.put( string, 128 );
            binary += bin2;
//            System.out.println( string );
//            System.out.println( "Dictionary: " + dictionary.toString() );
//            System.out.println( "Binary: " + binary );
//            System.out.println( "Compression Ratio: 1.00" );
            return 1.00;
        }
        while( i < string.length() - 2 )
        {
            String current = "" + string.charAt( i );
            String next = "" + string.charAt( i + 1 );
            String key = current + next;
            while ( dictionary.containsKey( key ) )
            {
                current = key;
                i++;
                if ( i < string.length() - 1 )
                {
                    next = "" + string.charAt( i + 1 );
                    key = current + next;
                }
                else
                {
                    dictValue--;
                    break;
                }
            }
            if ( current.length() == 1 )
            {
                String bin = Integer.toBinaryString( (int) ( current.charAt( 0 ) ) ) + " ";
                while( bin.length() <= bitlevel )
                {
                    bin = "0" + bin;
                }
                binary += bin;
            }
            else
            {
                String bin = Integer.toBinaryString( dictionary.get( current ) );
                while( bin.length() < bitlevel )
                {
                    bin = "0" + bin;
                }
                binary += bin + " ";
            }
            Integer value = dictValue;
            dictionary.put( key, value );
            dictValue++;
            numOutputs++;
            i++;
            String output = current;
            System.out.print( output );
        }
        if ( i == ( string.length() - 2 ) )
        {
            String c = "" + string.charAt( i );
            String n = "" + string.charAt( i + 1 );
            String k = c + n;
            if ( !dictionary.containsKey( k ) )
            {
                dictionary.put( k, dictValue );
            }
            binary += "00" + Integer.toBinaryString( dictionary.get( k ) );
            numOutputs++;
            System.out.println( k );
        }
        else if ( i == string.length() - 1 )
        {
            String c = "" + string.charAt( i );
            System.out.println( c );
            String bin = Integer.toBinaryString( (int) c.charAt( 0 ) );
            while( bin.length() < bitlevel )
            {
                bin = "0" + bin;
            }
            binary += bin;
            numOutputs++;
        }
        else
        {
            System.out.println();
        }
//        System.out.println( "Dictionary: " + dictionary.toString() );
//        System.out.println( "Binary: " + binary );
        double stringbits = string.length() * 10;
//        System.out.println( "Original: " + (int) stringbits );
        double lzwbits = numOutputs * bitlevel;
//        System.out.println( "Compressed: " + (int) lzwbits );
        double ratio = stringbits / lzwbits;
//        DecimalFormat df = new DecimalFormat( "0.00" );
//        System.out.println( "Compression Ratio: " + df.format( ratio ) );
        return ratio;
    }

    public static void findBestBitLevel( String string )
    {
        double ratio = lzwReturnDouble( string, 8 );
        int i = 9;
        while( ratio < lzwReturnDouble( string, i ) )
        {
            i++;
        }
        System.out.println( "The best bitlevel is " + i );
    }

}
