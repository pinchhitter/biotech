package cdac.in;

import java.io.*;
import java.util.*;

class Data{

	String cluster;
	Double deltaG;
	String fileContent;

	Data(String fileContent, String cluster, String deltaG ){
		this.fileContent = fileContent;
		this.cluster = cluster;
		this.deltaG = Double.parseDouble( deltaG );
	}
}

class SortByDeltaG implements Comparator<Data> {

    @Override
	    public int compare(Data d1, Data d2) {
		    if( d1.deltaG > d2.deltaG )
			    return 1;
		    if( d1.deltaG < d2.deltaG )
			    return -1;

		    long de1 = Double.doubleToLongBits( d1.deltaG );
		    long de2 = Double.doubleToLongBits( d2.deltaG );

		    return (de1 == de2 ?  0 : // Values are equal
				    (de1 > de2 ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
				     1));                          // (0.0, -0.0) or (NaN, !NaN)
	    }	
}


public class SortData{

	List<Data> datas = null;

	SortData(){
		datas = new ArrayList<Data>();
	}

	void write(int size, String directory, boolean cluster){
		try{
			File theDir = new File( directory );

			if (!theDir.exists()) {
				try{
					theDir.mkdir();
				} 
				catch(SecurityException se){
					se.printStackTrace();
					System.exit(0);	
				}        
			}

			int count = 0;

			List<String> clusterList = new ArrayList<String>();
			for(Data data: datas){	

				if( clusterList.contains( data.cluster ) && cluster ){
					continue;
				}

				count++;

				clusterList.add( data.cluster );
				String filename = "./"+directory+"/cluster"+count+"-"+data.cluster+".dock.pdb";	
				PrintWriter writer = new PrintWriter(filename, "UTF-8");
				writer.println( data.fileContent.trim() );
				writer.close();	
				if( count == size){
					break;
				}		
			}

		}catch(Exception e){
			e.printStackTrace();
		}	
	}	

	void read(String filename){

		BufferedReader br = null;

		try{
			br = new BufferedReader(new FileReader(new File( filename ) ) );
			String line = null;
			String fileContent = "";
			String cluster = null;
			String deltaG = null;
			while( (line = br.readLine()) != null ){

				String[] token = line.split("\\s+");

                                if( token.length > 2 && token[0].equals("ATOM") ){
                                        fileContent = fileContent+"\n"+( line.trim()+"          ").substring(0,77)+""+token[2].trim().charAt(0);
                                }else{
                                        fileContent = fileContent+"\n"+line.trim();
				}

				if( line.indexOf("REMARK  Cluster: ") >= 0 ){
					cluster = line.split(":")[1].trim();
				}else if ( line.indexOf("REMARK  deltaG:") >= 0 ){
					deltaG = line.split(":")[1].trim();
				}else if ( line.trim().equals("TER") ){

					if( cluster != null && deltaG != null ){
						Data data = new Data( fileContent, cluster, deltaG);
						datas.add( data );
					}
					fileContent = "";
					cluster = null;
					deltaG = null;
				}
			}
			Collections.sort( datas, new SortByDeltaG() );
			
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

	public static void main(String[] args){
		String filename = null;
		String directory = null;	
		boolean cluster = false;	
		int size = -1;
		int i = 0;
		while( i < args.length ){
			if( args[i].equalsIgnoreCase("-filename") || args[i].equalsIgnoreCase("-f") ){
				filename = args[i+1].trim();
				i++;
			}else if ( args[i].equalsIgnoreCase("-size") || args[i].equalsIgnoreCase("-s") ){
				size = Integer.parseInt( args[i+1].trim() );
				i++;
			}else if ( args[i].equalsIgnoreCase("-directory") || args[i].equalsIgnoreCase("-d")  ){
				directory = args[i+1].trim();
				i++;
			}else if ( args[i].equalsIgnoreCase("-cluster") || args[i].equalsIgnoreCase("-c")  ){
				cluster = true;
			}
			i++;
		}
		if( filename ==  null || directory == null || size == -1){
			System.out.println( "-filename/f/F <filename> -size/s/S <size> -directory/d/D <directory> [-cluster/c] ");	
			System.exit( 0 );	
		}
		SortData sd = new SortData();
		sd.read( filename );
		sd.write( size, directory, cluster);
	}
}

