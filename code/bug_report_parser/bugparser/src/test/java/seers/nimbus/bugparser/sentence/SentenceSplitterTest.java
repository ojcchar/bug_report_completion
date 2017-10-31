package seers.nimbus.bugparser.sentence;

import java.io.File;
import java.util.List;

import org.junit.Test;

import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.BugReport;
import seers.nimbus.bugparser.sentence.SentenceSplitter;

public class SentenceSplitterTest {

	@Test
	public void testParseText() throws Exception {

		String inFile = "test_data" + File.separator + "sentence_splitting" + File.separator + "steps2repro.xml";
		BugReport bugRep = XMLHelper.readXML(BugReport.class, inFile);

		String bugDescription = bugRep.getDescription();
		List<List<String>> parseDescr = SentenceSplitter.splitIntoParagraphsAndSentences(bugDescription);

		for (List<String> paragraph : parseDescr) {
			System.out.println("*** New paragraph ***");
			for (String sentence : paragraph) {
				System.out.print("S: ");
				System.out.println(sentence);
			}
			System.out.println(" ");
		}
	}

	@Test
	public void testIsStackTrace() {

		String[] lines = {
				"W/dalvikvm(28294): Exception Ljava/lang/NullPointerException; thrown while initializing La/a/b/k;",
				"W/dalvikvm(28294): Exception Ljava/lang/ExceptionInInitializerError; thrown while initializing La/a/b/l;",
				"W/dalvikvm(28294): threadid=11: thread exiting with uncaught exception (group=0x40a2a1f8)",
				"E/AndroidRuntime(28294): FATAL EXCEPTION: AsyncTask #1",
				"E/AndroidRuntime(28294): java.lang.RuntimeException: An error occured while executing doInBackground()",
				"E/AndroidRuntime(28294): at android.os.AsyncTask$3.done(AsyncTask.java:278)",
				"E/AndroidRuntime(28294): at java.util.concurrent.FutureTask$Sync.innerSetException(FutureTask.java:273)",
				"E/AndroidRuntime(28294): at java.util.concurrent.FutureTask.setException(FutureTask.java:124)",
				"E/AndroidRuntime(28294): at java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java:307)",
				"E/AndroidRuntime(28294): at java.util.concurrent.FutureTask.run(FutureTask.java:137)",
				"E/AndroidRuntime(28294): at android.os.AsyncTask$SerialExecutor$1.run(AsyncTask.java:208)",
				"E/AndroidRuntime(28294): at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1076)",
				"E/AndroidRuntime(28294): at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:569)",
				"E/AndroidRuntime(28294): at java.lang.Thread.run(Thread.java:856)",
				"E/AndroidRuntime(28294): Caused by: java.lang.ExceptionInInitializerError",
				"E/AndroidRuntime(28294): at a.a.b.f.<init>(Unknown Source)",
				"E/AndroidRuntime(28294): at a.a.b.e.<init>(Unknown Source)",
				"E/AndroidRuntime(28294): at a.a.c.di.b(Unknown Source)",
				"E/AndroidRuntime(28294): at a.a.c.di.a(Unknown Source)",
				"E/AndroidRuntime(28294): at a.a.c.b.a(Unknown Source)",
				"E/AndroidRuntime(28294): at a.a.c.ae.a(Unknown Source)",
				"E/AndroidRuntime(28294): at a.a.a.a.a(Unknown Source)",
				"E/AndroidRuntime(28294): at a.a.a.j.e(Unknown Source)",
				"E/AndroidRuntime(28294): at a.a.a.e.b(Unknown Source)",
				"E/AndroidRuntime(28294): at hu.vsza.a.b.searchByPartName(Unknown Source)",
				"E/AndroidRuntime(28294): at hu.vsza.adsdroid.c.a(Unknown Source)",
				"E/AndroidRuntime(28294): at hu.vsza.adsdroid.c.doInBackground(Unknown Source)",
				"E/AndroidRuntime(28294): at android.os.AsyncTask$2.call(AsyncTask.java:264)",
				"E/AndroidRuntime(28294): at java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java:305)",
				"E/AndroidRuntime(28294): ... 5 more",
				"E/AndroidRuntime(28294): Caused by: java.lang.ExceptionInInitializerError",
				"E/AndroidRuntime(28294): at a.a.b.l.<clinit>(Unknown Source)", "E/AndroidRuntime(28294): ... 19 more",
				"E/AndroidRuntime(28294): Caused by: java.lang.NullPointerException",
				"E/AndroidRuntime(28294): at java.util.Properties.load(Properties.java:246)",
				"E/AndroidRuntime(28294): at a.a.b.k.d(Unknown Source)",
				"E/AndroidRuntime(28294): at a.a.b.k.<clinit>(Unknown Source)" };

		for (String line : lines) {
			boolean is = SentenceSplitter.isStackTraceLine(line);
			System.out.println(is + " " + line);
		}

	}

}
