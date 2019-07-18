package cdac.in;

import java.util.*;
import java.io.*;

class ProtineExtractor{
	
	String virus = null;
	String start;
	Map<String, String> ends;
	Map<String, String> aminoacid;

	ProtineExtractor(){

		ends = new TreeMap<String, String>();
		aminoacid = new TreeMap<String, String>();

		/* Start Sequence */
		start = "ATG";

		/* End Sequences */
		ends.put("TAA","yes");
		ends.put("TAG","yes");
		ends.put("TGA","yes");

		/* Amino Acide Sequences */

		aminoacid.put("TTT","Phe[F]");
		aminoacid.put("TTC","Phe[F]");
		aminoacid.put("TTA","Leu[L]");
		aminoacid.put("TTG","Leu[L]");
		aminoacid.put("TCT","Ser[S]");
		aminoacid.put("TCC","Ser[S]");
		aminoacid.put("TCA","Ser[S]");
		aminoacid.put("TCG","Ser[S]");
		aminoacid.put("TAT","Tyr[Y]");
		aminoacid.put("TAC","Tyr[Y]");
		aminoacid.put("TAA","Ter[Z]");
		aminoacid.put("TAG","Ter[Z]");
		aminoacid.put("TGT","Cys[C]");
		aminoacid.put("TGC","Cys[C]");
		aminoacid.put("TGA","Ter[Z]");
		aminoacid.put("TGG","Trp[W]");
		aminoacid.put("CTT","Leu[L]");
		aminoacid.put("CTC","Leu[L]");
		aminoacid.put("CTA","Leu[L]");
		aminoacid.put("CTG","Leu[L]");
		aminoacid.put("CCT","Pro[P]");
		aminoacid.put("CCC","Pro[P]");
		aminoacid.put("CCA","Pro[P]");
		aminoacid.put("CCG","Pro[P]");
		aminoacid.put("CAT","His[H]");
		aminoacid.put("CAC","His[H]");
		aminoacid.put("CAA","Gln[Q]");
		aminoacid.put("CAG","Gln[Q]");
		aminoacid.put("CGT","Arg[R]");
		aminoacid.put("CGC","Arg[R]");
		aminoacid.put("CGA","Arg[R]");
		aminoacid.put("CGG","Arg[R]");
		aminoacid.put("ATT","Ile[I]");
		aminoacid.put("ATC","Ile[I]");
		aminoacid.put("ATA","Ile[I]");
		aminoacid.put("ATG","Met[M]");
		aminoacid.put("ACT","Thr[T]");
		aminoacid.put("ACC","Thr[T]");
		aminoacid.put("ACA","Thr[T]");
		aminoacid.put("ACG","Thr[T]");
		aminoacid.put("AAT","Asn[N]");
		aminoacid.put("AAC","Asn[N]");
		aminoacid.put("AAA","Lys[K]");
		aminoacid.put("AAG","Lys[K]");
		aminoacid.put("AGT","Ser[S]");
		aminoacid.put("AGC","Ser[S]");
		aminoacid.put("AGA","Arg[R]");
		aminoacid.put("AGG","Arg[R]");
		aminoacid.put("GTT","Val[V]");
		aminoacid.put("GTC","Val[V]");
		aminoacid.put("GTA","Val[V]");
		aminoacid.put("GTG","Val[V]");
		aminoacid.put("GCT","Ala[A]");
		aminoacid.put("GCC","Ala[A]");
		aminoacid.put("GCA","Ala[A]");
		aminoacid.put("GCG","Ala[A]");
		aminoacid.put("GAT","Asp[D]");
		aminoacid.put("GAC","Asp[D]");
		aminoacid.put("GAA","Glu[E]");
		aminoacid.put("GAG","Glu[E]");
		aminoacid.put("GGT","Gly[G]");
		aminoacid.put("GGC","Gly[G]");
		aminoacid.put("GGA","Gly[G]");
		aminoacid.put("GGG","Gly[G]");
	}
	
	class Genom{
		StringBuffer genomMapSeq;
		StringBuffer genomSeq;
		int seqCount = 0;
		
		Genom(){

			genomMapSeq = new StringBuffer("");
			genomSeq = new StringBuffer("");
		}
		
		boolean add(String seq){
			genomSeq.append( seq );
			String protine = aminoacid.get( seq );
			if( protine != null ){
				genomMapSeq.append( protine.substring(4,5) );
				seqCount++;
			}
			
			if( ends.containsKey( seq ) ){
				return false;
			}
		
		return true;
		}
		
		boolean write(String directory, int count, int startIndex, int lastIndex){

			try{
				new File("./"+directory).mkdir();
				String filename = "./"+directory+"/G"+count+".gnom";	
				PrintWriter writer = new PrintWriter(filename, "UTF-8");
				writer.println( "Total Aminoacid: "+( seqCount - 2 ) );
				writer.println( "Start Index: "+startIndex );
				writer.println( "End Index: "+lastIndex );
				//writer.println( genomSeq.toString().trim() );
				writer.println( genomMapSeq.toString().trim() );
				writer.close();	
			}catch(Exception e){
				e.printStackTrace();
			}
		
		return true;	
		}
	}

	void read(String filename, boolean header){
		try{		
			BufferedReader br = new BufferedReader( new FileReader( new File(filename) ));
			String line = null;
			StringBuffer sb = new StringBuffer(""); 
			while( (line = br.readLine()) != null ){
				if( header ){
					header = false;
					continue;
				}
				sb.append(line.trim() );		
			}
			virus = sb.toString();
			System.err.println("Reading Completed: "+virus.length() );
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	void extract(String directory){
		String copy = virus;
		int count = 1;
		int startIndex = 0;	
		int lastIndex = 0;
		while( copy.length() > 0 ){
		
			int index = copy.indexOf( start );
			if( index >= 0 ){

				startIndex = lastIndex + index;
				copy = copy.substring( index );
				Genom genom = new Genom();
				int i = 0;
				for( ; (i + 3) < copy.length(); i += 3){
					String sequence = copy.substring(i, i + 3);
					if ( ! genom.add( sequence  ) ){
						break;
					}
				}

				lastIndex = ( (startIndex + i ) + 1 );

				if( genom.write( directory , count, startIndex, lastIndex) ){
					count++;	
				}

				copy = copy.substring( i );

			}else{
				break;
			}
		}	
	}

	public static void main(String[] args){

		ProtineExtractor pe = new ProtineExtractor();

		String filename = null;
		String directory = null;
		int i = 0 ;

		while( i < args.length ){

			if( args[i].equals("-f") || args[i].equals("-F") ){
				filename = args[i+1].trim();
				i++;
			}else if( args[i].equals("-d") || args[i].equals("-D") ){
				directory = args[i+1].trim();
				i++;
			}
			i++;
		}

		if( filename == null || directory == null ){
			System.out.println("-Uses:");
			System.out.println("-f <virus-filename>");
			System.out.println("-d <genom-directory>");
			System.exit(0);
		}
		pe.read( filename, true);
		pe.extract( directory );
	}
}
