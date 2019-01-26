import java.io.*;

public class BugReportClassifier{
	
	public static void main(String[] args){
		try{
			String path="/shared/mlrdir3/disk1/ljwinnie/bug_report/sentence-classifier-nn/final_model/final_NN_classifier/";
			String pypath = path+"/evaluate.py";
			String config= path+"/decode.config";
			String datapath = path+"sample_data/argouml";

			ProcessBuilder pb = new ProcessBuilder("python", pypath, "--config",config,"--path", datapath);

			Process p = pb.start();

			BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = bfr.readLine();
			System.out.println(line);
			while((line = bfr.readLine()) != null){
				System.out.println("Python Output: " + line);
			}


		}catch(Exception e){
			System.out.println(e);
		}
	}
}