import java.io.*;

public class BugReportClassifier{
	
	public static void main(String[] args){
		try{
			String path="/shared/mlrdir3/disk1/ljwinnie/bug_report/sentence-classifier-nn/final_model/bug_report_completion/code/S2R_neural_classifier/";
			String pypath = path+"/evaluate.py";
			String config= path+"/decode.config";
			String datapath = path+"euler_evaluation/euler_evaluation/1_parsed_bug_reports_format2_prep";
			String outputpath = path+"/out.csv";

			ProcessBuilder pb = new ProcessBuilder("python", pypath, "--config",config,"--path",datapath,"--outpath",outputpath);

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