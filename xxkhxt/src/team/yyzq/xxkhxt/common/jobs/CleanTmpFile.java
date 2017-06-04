package team.yyzq.xxkhxt.common.jobs;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CleanTmpFile implements Job {
	private static File tmpFileDir = null;
	
	public static File getTmpFileDir(){
		if(tmpFileDir == null){
	    	String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
	    	path = path.replaceAll("classes.{1,2}$", "").replaceAll("file:/", "");
	    	tmpFileDir = new File(path+"tmpFile");
	    	if(!tmpFileDir.exists())
	    		tmpFileDir.mkdirs();
		}
		
		return tmpFileDir;
	}
	
	public static String getTmpFileDirPath(){
		return getTmpFileDir().getAbsolutePath();
	}
	
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
    	File tmpFile = CleanTmpFile.getTmpFileDir();
    	if(tmpFile.exists()){
    		File[] files = tmpFile.listFiles();
    		for(File file : files){
    			try {
    				FileUtils.forceDelete(file);
    			} catch (IOException e1) {
    				
    			}
    		}
    	}
    }

}