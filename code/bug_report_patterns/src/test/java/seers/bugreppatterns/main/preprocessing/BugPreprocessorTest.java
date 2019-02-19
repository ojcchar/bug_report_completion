package seers.bugreppatterns.main.preprocessing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class BugPreprocessorTest {

	@Test
	public void testNumber() {
		System.out.println("-------------------------------------------");
		String[] txts = { "0.3.0", "3.8.11", ":592", "9,21" };
		for (String txt : txts) {
			boolean isNum = BugPreprocessor.isNumber(new Token(txt, null, null, null, null, -1, -1));
			System.out.println(txt + " -> " + isNum);
			assertTrue(isNum);
		}
	}

	@Test
	public void testURL() {
		System.out.println("-------------------------------------------");
		String[] txts = { "http://java.sun.com/javase/6/docs/api/java/io/InputStream.html#available",
				//"hoohoo.ncsa.uiuc.edu",
				"https://hibernate.atlassian.net/browse/hhh-10693]",
				"http://dev.mysql.com/doc/refman/5.7/en/string-type-overview.html]:"

				};
		for (String txt : txts) {
			boolean isUrl = BugPreprocessor.isURL(new Token(txt, null, null, null, null, -1, -1));
			System.out.println(txt + " -> " + isUrl);
			assertTrue(isUrl);
		}
	}

	@Test
	public void testSpecialChar() {
		System.out.println("-------------------------------------------");
		String[] txts = { "``", "####", ":[", "='{", "--", "*****", "," };
		for (String txt : txts) {
			boolean isChar = BugPreprocessor.isSpecialChar(new Token(txt, null, null, null, null, -1, -1));
			System.out.println(txt + " -> " + isChar);
			assertTrue(isChar);
		}
		System.out.println("-------------------------------------------");
		String[] txts2 = { "FILESAVE", "VIEWING" };
		for (String txt : txts2) {
			boolean isChar = BugPreprocessor.isSpecialChar(new Token(txt, null, null, null, null, -1, -1));
			System.out.println(txt + " -> " + isChar);
			assertFalse(isChar);
		}

	}

	@Test
	public void testIsJavaStackTraceLine() {
		System.out.println("-------------------------------------------");
		String[] txts = { "	at org.hibernate.hql.internal.ast.HqlSqlWalker.lookupProperty(HqlSqlWalker.java:592)",
				"at com.android.internal.policy.PhoneWindow$DecorView.onLayout(PhoneWindow.java:3193)",
				"org.openmrs.module.sync.web.controller.ImportListController.processFormSubmission(ImportListController.java:306)",
				"org.apache.jasper.compiler.ErrorDispatcher.dispatch(ErrorDispatcher.java:407)",
				"org.apache.tomcat.util.net.JIoEndpoint$Worker.run(JIoEndpoint.java:447)",
				"org.springframework.orm.hibernate3.support.OpenSessionInViewFilter.doFilterInternal(OpenSessionInViewFilter.java:198)"};
		for (String txt : txts) {
			boolean isTrace = BugPreprocessor.isJavaStackTraceLine(txt);
			System.out.println(txt + " -> " + isTrace);
			assertTrue(isTrace);
		}

		String[] txts2 = { "	at org.hibernate.hql.internal.ast.HqlSqlWalker.lookupProperty(HqlSqlWalker.java:)",
				"at com.android.internal.policy.PhoneWindow$DecorView.onLayout()",
				"at com.android.internal.policy.PhoneWindow$DecorView.onLayout",
				"Created attachment Screenshot comparison" };
		for (String txt : txts2) {
			boolean isTrace = BugPreprocessor.isJavaStackTraceLine(txt);
			System.out.println(txt + " -> " + isTrace);
			assertFalse(isTrace);
		}



	}

	@Test
	public void testPreprocessSentence() {
		System.out.println("-------------------------------------------");

		String[] txts = {

				"/docker version\n Version: 0.3.0\n Git Commit: 4f202cd\n Kernel: 3.8.11-\n ```",
				"It's committing in 1m-1m40s now.", "docker commit has become slow since 0.3.0",
				"| 00000 50 4f 53 54 20 2f 76 31 2e 39 2f 63 6f 6d 6d 69 POST /v1.9/commi |",
				"#### API Request with `docker commit`",
				"In this example the `strace` output shows that `\"Entrypoint\": [\"/bin/bash\",\"-l\"]` was sent to the server, but inspecting the resulting image shows `\"Entrypoint\": null`.",
				"samhanes@samhanes-dev $ strace -e trace=write -e write=3 docker commit -run='{\"Entrypoint\":[\"/bin/bash\", \"-l\"]}' 0ba9980c64e1",
				"Loading a gzip-compressed version of the same image still works, so the breakage is xz-specific.",
				"tar.xz: XZ compressed data\n vagrant@docker-ubuntu-1404:~$ docker --version\n Docker version 1.3.2, build 39fa2fa\n vagrant@docker-ubuntu-1404:~$ sudo docker load --input=/docker-images/busybox-trusty.",
				"User can still pass mount option explicitly using dm.mountopt=discard to",
				"However, when launching the run-time instance of the \n Workbench from the debugger (i.e., open PDE, select plugin.xml, select Debug \n button), the run-time instance comes up in English.",
				"boolean showMenu (int x, int y) {\n 	Event event = new Event ();\n 	event.x = x;\n 	event.y = y;\n 	sendEvent (SWT.MenuDetect, event);\n 	if (!",
				"php\n echo '&lt;pre&gt;';\n for ($i=1;;$i++)\n { echo $i.\"\n\";\n   flush();ob_flush();\n   $contents = file_get_contents('http://localhost/test.htm');\n }",
				"The documentation for mod_cgi at http://httpd.apache.org/docs/current/mod/mod_cgi.html still refers to the CGI specification which was hosted at hoohoo.ncsa.uiuc.edu.",
				"See also bugs #47455 and #49152.",
				"public static Blob createBlob(InputStream stream) throws IOException {\n return new SerializableBlob( new BlobImpl( stream, stream.available() ) );\n}",
				"That's explicitly noted in the Java-API (http://java.sun.com/javase/6/docs/api/java/io/InputStream.html#available):\n\"Note that while some implementations of InputStream will return the total number of bytes in the stream (on calling available() ), many will not.",
				"CREATE TABLE info.rmbtest_course2\n(\n  fee integer,\n  id bigserial NOT NULL,\n  starttime timestamp without time zone,\n  title character varying(100) NOT NULL,\n  CONSTRAINT rmbtest_course2_pkey PRIMARY KEY (id)\n)",
				"	at org.hibernate.hql.internal.ast.HqlSqlWalker.lookupProperty(HqlSqlWalker.java:592)",
				"Commit: https://github.com/hibernate/hibernate-orm/commit/17de173cb5334d866f1886d3e057f49a6c987bad",
				"[screen shot 2014-08-02 at 10 12 17](https://cloud.githubusercontent.com/assets/40213/3787481/c599c8a2-1a1c-11e4-8503-26c58e34d53d.png)",
				"* Some should be replaced by translatable strings (e.g. `Start Date` in `res/layout/date_range_dialog.",
				"2 .... Open (right click =&gt; Open =&gt; LibreOffice)", "&lt;snip&gt;", "<snip>",
				"Apache 2.0.35 returns garbage in PHP4 CGI requests. *Not* PHP's fault.",
				"1 3.22 and Windows XP networking.",
				"FILESAVE causes lots of artifacts and destroys VIEWING for embedded .svg"

		};
		for (String txt : txts) {
			Sentence sentence = BugPreprocessor.preprocessSentence("0", txt);
			String terms = TextProcessor.getStringFromTermsAndPos(sentence, true);
			System.out.println(txt + " -> " + terms);
		}
	}

}
