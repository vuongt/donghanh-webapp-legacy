package org.donghanh;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class     LocalFunctions {
	
	static String tabs(String base, int nb_tabs){
		if (nb_tabs == 0)
			return new String("");
		return
			new String( base + tabs(base, nb_tabs - 1));
	}
	
    static void downloadFromUrl(URL url, String localFilename) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            URLConnection urlConn = url.openConnection();//connect

            is = urlConn.getInputStream();               //get connection inputstream
            fos = new FileOutputStream(localFilename);   //open outputstream to local file

            byte[] buffer = new byte[4096];              //declare 4KB buffer
            int len;

            //while we have available data, continue downloading and storing to local file
            while ((len = is.read(buffer)) > 0) {  
                fos.write(buffer, 0, len);
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }
    
    public static int nb_Juges(int nb_docs, int nb_juges_by_copy, int max_docs, String evaluated_by){
    	// Công thức cho cách chia xen kẽ, 
    	if (evaluated_by.equals("FR")){
        	if (nb_docs <= 0){
        		return 0;
        	}
        	
        	else if (nb_docs <= max_docs){
        		return nb_juges_by_copy;
        	}
        	
//        	else if (nb_docs > max_docs * nb_juges_by_copy){
//        		return nb_juges_by_copy * 3;
//        	}
        	
        	else return (nb_docs * nb_juges_by_copy - 1)/max_docs + 1;
    	}
    	
    	//Công thức cho cách chia nhóm tách rời, áp dụng tại ĐH Taiwan và Singapore
    	else if ((evaluated_by.equals("SG")) || (evaluated_by.equals("TW"))){
    		if (nb_docs <= 0){
        		return 0;
        	}
        
        	else return ((nb_docs - 1)/max_docs + 1) * nb_juges_by_copy;
    	}
    	
    	else return 0;
    }
    
    public static String[] Distribution(int nb_docs, int nb_juges_by_copy, int max_docs, String evaluated_by){
    	int nb_Juges =  nb_Juges(nb_docs, nb_juges_by_copy, max_docs, evaluated_by);
    	if (evaluated_by.equals("FR") && nb_juges_by_copy == 3){
    		if (nb_Juges == 3){
        		return new String[]{"G0G1G2G3G", "G0G1G2G3G", "G0G1G2G3G"};
        	}
        	
        	else if (nb_Juges == 4){
        		return new String[]{"G0G1G2G4G", "G0G2G3G1G", "G0G3G4G2G", "G0G4G1G3G"};
        	}
        	
        	else if (nb_Juges == 5){
        		return new String[]{"G0G1G2G4G", "G0G2G3G5G", "G0G3G4G1G", "G0G4G5G2G", "G0G5G1G3G"};
        	}
        	
        	else if (nb_Juges == 6){
        		return new String[]{"G0G1G2G4G", "G0G2G3G5G", "G0G3G4G6G", "G0G4G5G1G", "G0G5G6G2G", "G0G6G1G3G"};
        	}
        	
        	else if (nb_Juges == 7){
        		return new String[]{"G0G1G2G4G", "G0G2G3G5G", "G0G3G4G6G", "G0G4G5G7G", "G0G5G6G1G", "G0G6G7G2G", "G0G7G1G3G"};
        	}
    		
        	else if (nb_Juges == 8){
        		return new String[]{"G0G1G2G4G", "G0G2G3G5G", "G0G3G4G6G", "G0G4G5G7G", "G0G5G6G8G", "G0G6G7G1G", "G0G7G8G2G", "G0G8G1G3G"};
        	}
    		
        	else if (nb_Juges == 9){
        		return new String[]{"G0G1G2G4G", "G0G2G3G5G", "G0G3G4G6G", "G0G4G5G7G", "G0G5G6G8G", "G0G6G7G9G", "G0G7G8G1G", "G0G8G9G2G", "G0G9G1G3G"};
        	}
    		
        	else{
        		return null;
        	}
    	}
    	
    	else if (evaluated_by.equals("FR") && nb_juges_by_copy == 4){
        	if (nb_Juges == 4){
        		return new String[]{"G0G1G2G3G4G", "G0G1G2G3G4G", "G0G1G2G3G4G", "G0G1G2G3G4G"};
        	}
        	
        	else if (nb_Juges == 5){
        		return new String[]{"G0G1G2G3G4G", "G0G2G3G4G5G", "G0G3G4G5G1G", "G0G4G5G1G2G", "G0G5G1G2G3G"};
        	}
        	
        	else if (nb_Juges == 6){
        		return new String[]{"G0G1G2G3G4G", "G0G1G2G5G6G", "G0G3G4G5G6G", "G0G1G2G4G5G", "G0G1G3G4G6G", "G0G2G3G5G6G"};
        	}
        	
        	else if (nb_Juges == 7){
        		return new String[]{"G0G1G2G3G5G", "G0G2G3G4G6G", "G0G4G5G6G1G", "G0G3G4G5G7G", "G0G5G6G7G2G", "G0G6G7G1G3G", "G0G7G1G2G4G"};
        	}
    		
        	else if (nb_Juges == 8){
        		return new String[]{"G0G1G2G3G4G", "G0G5G6G7G8G", "G0G1G2G5G6G", "G0G3G4G7G8G", "G0G1G3G5G7G", "G0G2G4G6G8G", "G0G1G4G5G8G", "G0G2G3G6G7G"};
        	}
    		
        	else if (nb_Juges == 9){
        		return new String[]{"G0G1G2G4G8G", "G0G2G3G5G9G", "G0G3G4G6G1G", "G0G4G5G7G2G", "G0G5G6G8G3G", "G0G6G7G9G4G", "G0G7G8G1G5G", "G0G8G9G2G6G", "G0G9G1G3G7G"};
        	}
        	
        	else if (nb_Juges == 10){
        		return new String[]{"G0G1G2G4G6G", "G0G2G3G5G7G", "G0G3G4G6G8G", "G0G4G5G7G9G", "G0G5G6G8G10G", "G0G6G7G9G1G", "G0G7G8G10G2G", "G0G8G9G1G3G", "G0G9G10G2G4G", "G0G10G1G3G5G"};
        	}
        	
        	else if (nb_Juges == 11){
        		return new String[]{"G0G1G2G4G8G", "G0G2G3G5G9G", "G0G3G4G6G10G", "G0G4G5G7G11G", "G0G5G6G8G1G", "G0G6G7G9G2G", "G0G7G8G10G3G", "G0G8G9G11G4G", "G0G9G10G1G5G", "G0G10G11G2G6G", "G0G11G1G3G7G"};
        	}
        	
        	else if (nb_Juges == 12){
        		return new String[]{"G0G1G2G4G8G", "G0G2G3G5G9G", "G0G3G4G6G10G", "G0G4G5G7G11G", "G0G5G6G8G12G", "G0G6G7G9G1G", "G0G7G8G10G2G", "G0G8G9G11G3G", "G0G9G10G12G4G", "G0G10G11G1G5G", "G0G11G12G2G6G", "G0G12G1G3G7G"};
        	}
    		
        	else{
        		return null;
        	}
    	}
    	
    	else if ((evaluated_by.equals("SG")) || (evaluated_by.equals("TW"))){
    		int nb_Group = nb_Juges/nb_juges_by_copy ;
    		
    		String[] distribution = new String[nb_Group];
    		
    		for (int index = 0; index < nb_Group ; index++){
    			String s = "G0G";
    			for (int subidx = 0; subidx < nb_juges_by_copy; subidx++){
    				int current_juge = index * nb_juges_by_copy + subidx + 1;
    				s += current_juge + "G";
    			}
    			distribution[index] = s;
    		}
    		
    		return distribution;
    	}
    	
    	return null;
    }
    
}
