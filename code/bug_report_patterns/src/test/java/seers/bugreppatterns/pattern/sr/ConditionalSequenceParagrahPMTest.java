package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ConditionalSequenceParagrahPMTest extends BaseTest {

	public ConditionalSequenceParagrahPMTest() {
		pm = new ConditionalSequenceParagrahPM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
				"What is happening is that even though that file is being updated with a new CRL file every hour, only a full restart (stop, start) of Apache makes it re-read the CRL!\n"
						+ "If we send a HUP, it doesn't re-read it.",
				"I was able to checkout the code install go and run make in the root directory to make the build a few days ago. Now when I run make in the root of the repo I get the error in [1]. I believe this commit is the source of the problem: https://github.com/dotcloud/docker/commit/37a78902db9e968d307d0c0325612e8bef20bc69",
				"If I call removeConceptName on a concept and then try to save the concept, I get a foreign key constraint violation on ConceptWord.",
				
				
				"Observation:   If the clients cache is cleared, the next request comes from the upstream  server, then subsequent requests come from the proxy's cache.",
				"If this is normal then it would be helpful if the documentation reflected it and also cascade would only take an \"update\" or \"delete\".",
				"4 If no language attribute was found in the page rendered, then the default behavior comes back.",
				"when I tag the espn.go.com home page with \"sports\" and then search for \"sports\" in organize bookmarks.",
				"if the bucket has only one element and the element is not match, cause entry_p is not null and entry_p->te_next_p is null, then the loop is not excuted, then we   get to judge whether we find it.",
				"For example if a person was called Sarah Lee Smith and her first name is \"Sarah Lee\" then if you searched for Sarah Lee Smith you would not find her, you would need to leave out the Lee or have made Lee a middle name when it was created.",
				"when trying to add error bars from the sheet when clicking on the icon where one is normally able to then select the data from the sheet, one is not taken back to the spread sheet to select the range of cells by highlighting them.",
				"- if you open the list then press down arrow you skip over all items above the",
				"When working with a plugin, right clicking on the project and selecting \"Update Classpath\" (and then doing the update) causes the source folder to be changed to what used to be the output folder) - while the output folder stays the same.",
				"If a user is logged out when requesting extended permissions_ they are prompted to log in_ then approve the permissions_ but then it gets stuck here:",
				"If I create a DetachedCriteria object, then call \"createCriteria\" to on it, then pass a Subquery to the new Criteria, I get this exception:",
				"If a person is voided, then all of their names, addresses, etc are voided.",
				"However, with Docker 1.3.0, the `stdin` connection is kept alive and never closed when running this same command -- ie, `hi` is printed and then the stdin connection is locked.",
				"* Bonus points if you make it possible to create several toolbar folders which then all appear separately under \"View => Toolbars\".",
				
				"If you could make the tag linked= false  available_ then I could access the network name without the link.", 
				"While looking at it I found what I would consider to be some serious problems with the code:",
				"While porting mod_watch to Apache 2.0, I looked at mod_auth_digest as an example of a module using shared memory and mutexes.",
				"If the default value of zero is used, the code works, but if a non-zero value is supplied, it is not respected and a non-graceful shutdown begins immediately.",
				"If i call FQL:",
				"If I call:",
				"Please tell me if you need more infomation or help on this problem.",
				"* no matter if you use annotation or mapping files, test will fail using javassist (it used to fail with previous version too)",
				"Not being a GDK guru, I don't know if unconditionally calling the flush works on all platforms, but I think something like that is needed on X at least.",
				"When EnableSendFile is enabled (the default) file transfers can be slow.",
				"Observation:  If the clients cache is cleared, the next request comes from the upstream  server, then subsequent requests come from the proxy's cache.",
				"This would be especially useful if the class file is present in multiple .jar files because it then would bring to the one implementation that gets really used.",
				"While for all the other source files, a double click brings me into the right place in the source file, understandably this doesn't happen for the \"Unknown Source\" case.",
				"However, it would be great if upon a double click there, the Project properties-Java Build Path-Libraries would open the concerned library where I could attach the source.",
				"When processing a \"GET /.../file.html\", httpd attempts to open the file \"docroot/.../file.html/.",
				"In order to let the module trap the code, we need, when the module is loaded, to accept to establish the connection in case of a certificate validation error:",
				"In order to check if the module is loaded, I need a few lines at the beginning",
				"If this is normal then it would be helpful if the documentation reflected it and also cascade would only take an \"update\" or \"delete\".",
				"if you use the form example from the mod_autoindex docs, opera (5&6  tested) escapes a \"*\" to %2A (yes: 42 ;-)",
				"The \"docker run\" command flag \"device\" does not preserve the localhost device group when copied into the docker container.",
				"1 http://server/balancer-manager?xml=1 gives the html output if parameter nonce is not provided",
				"In addition hostname only shows the ip address and not ip:port if a port is configured",
				"Several (maybe 6) months ago I noticed problems but wasn't sure if it was a configuration problem.",
				"As I said, this works if apache is started as a console application, and it used to work with an earlier version of apache.",
				"4 If no language attribute was found in the page rendered, then the default behavior comes back.",
				"when and only when the spell checker is called (maybe some kind of icon with a \"more information\" bubble?)",
				"3 If the dictionary is not installed, it will display a small message along the line of \"this form seems to ask for French text but you don't have the French dictionnary on your Firefox, do you want to install it?\"",
				"So in your script you must chech if apache_fib eq '0' or 'NO'  then return 0  else  command=\"setfib -F ${apache22_fib} ${command}\"",
				"# enable if the mod_perl 1.0 compatibility is needed # use Apache::compat ();",
				"Here is the definition if the davfs in the apache config file:",
				"I spent a long time trying to figure out why my sticksessions were not working, when I finally found in an email in the archives that I needed a route parameter set in the BalancerMember line.",
				"When at the catalogue there are files which names in other encodings, the apache incorrectly processes names of files and the file-list spoils.",
				"If you need the \"live\" snapshot, please let me know.",
				"However, when that time expires  or the port 389 connection is closed, the apache child does not reap the connection and it remains  stuck in close_wait.",
				"I'm not sure this information needs to be logged at all (maybe left over from when this code was developed?)",
				"When building the test program, I'm using xdctools 3.15.03.67.",
				"Also if I want to access the container from outside, I have to delete nat rule within DOCKER statement in iptables -L -t nat.",
				"So it would be really super if the layout and images also proportionally enlarged (since otherwise I have to squint to read any text the might be on the image).",
				"When user is not logged in to Facebook green signup button with a message to the right of it is shown on top of the plugin.",
				"While the Apache Group has claimed that Apache HTTPD 2.0.39 is immune to the  apache-scalp.",
				"c exploit, users on several mailing lists say that these symptoms  are similar to those which are seen when the exploit is used against one of the  vulnerable Apache versions.",
				"This occurs when the reachable class cannot not be found by the class loader assigned to the JAXBContext.",
				"When built for linux, these functions always return success.",
				"Thus when it fails, we will be confident that it signifies a defect in the code being tested.",
				"Even if your policy about mime types is restrictive about adding new mime types, please consider  adding this, as it's a new extension for a file format that is already in your official mime types list.",
				"All I did was add the two switches to that line so that when the main  configure script call the apr-util configure, the values got passed.",
				"Scrolling to the end of the document causes undesirable effects when long lines are written to the  console.",
				"Right now_ when an application tab is render on a fan page_ we don t get the fb_sig_is_admin parameter (and thus cannot render any admin-specific content like moderation links).",
				"If GOPATH environment variable contains more than one \"workspace\" separated by ':' character(s) then docker will fail to build with the following error from Make:",
				"when I tag the espn.go.com home page with \"sports\" and then search for \"sports\" in organize bookmarks.",
				"Using our desktop application (id:83703757285) when the user is not logged in to Facebook triggers the login to facebook.",
				"IF apache needs the username/groupname for something else-- just let it notice the leading tildes and ignore them.",
				"This is quite common configuration when tests are placed into separate source folder, but using the same package in order to allow access to package and protected methods.",
				"So, Mylar does not remove filter for second class from some interesting package when that class is actually sitting in different source folder.",
				"When a directory or a file is requested in mod_proxy_ftp, and the user has not hinted what kind of resource it is(usual), the ftp client in mod_proxy_ftp tries the command SIZE first, and depending on its output, CWD or RETR.",
				"if the bucket has only one element and the element is not match, cause entry_p is not null and entry_p->te_next_p is null, then the loop is not excuted, then we get to judge whether we find it.",
				"Profiling has demonstrated that the first attempt to append something to the plugin log can result in a severe performance penalty if the log is large.",
				"It would be nice if each about: page has a favicon : about:config about:support about:buildconfig about:plugins about:cache about:memory about:blank about:mozilla",
				"In pt-BR (my lang) or in en-US (I suppose) Line 7 SE(E6;E6-0,01;1-D7) IF(E6;E6-0.01;1-D7) Line 8 SE(E7;E7-0,01;1-D8) IF(E7;E7-0.01;1-D8)",
				"When we are set to DavDepthInfinity Off (the default) and a PROPFIND request is made with Depth:Infinity the response is a 403, but there is nothing written to the error log.",
				"Upon refresh the page returns to normal function.",
				"Complex queries (queries which are not simple bookmark folder lookups) add \"visit_count > 0\" to the sql select statement (except when the query uses an annotation).",
				"When the content length of the script tag is below some threshold_ the xFBML code uses a GET request.",
				"When the content length is beyond some threshold_ the xFBML code uses a POST request.",
				"If the user has secure browsing enabled for Facebook_ the POST is redirected to an https GET request and the parameters are lost.",
				"Am not sure if the Problem is on the PHP or the Apache side....",
				"If that is the case, then check_enabled and  check_disabled shouldn't be optional hooks either.",
				"If this is the case, I would be happy to remove/alter certain portions (and I'll mention some discomforts I have below as well).",
				"Strangely, when a friend of mine pulled the code from git and mounted the volume... it worked.",
				"Further more, when executing a shell inside the container, the files existed and the user could write to the mounted directory.",
				"Now this was causing OS X to refuse access when boot2docker wanted to access the files.",
				"When reporting back to the facebook connect site_ the parameter that passes the location of XD_receiver.",
				"For example_ Facebook s DOM parsing routines cannot find head when content type is set properly to XHTML_ because they are looking for upper-case HEAD as would be expected in an HTML document.",
				"This behavior also happens from time to time when you are editing a plugin.xml file.",
				"Error message is `Error: Untar exit status 1 exec: \"xz\": executable file not found in $PATH`, even if the xz command is installed as `/usr/bin/xz`.",
				"So as you can see, the bug is that if you have a default font language set to \"None\" in my example, using \"This Text\" should override it or outright change the default to English (Canada).",
				"Currently it is not possible to use one of the installed JREs when  I want to use (i.e. specify on the build path) another JRE than the  one specified by JRE_LIB for a specific project.",
				"If it does, then that would at least build a little variation into everyone's clocks so that every 20 minutes the server does not get nailed again.",
				"If a person is voided, then all of their names, addresses, etc are voided.",
				"When running the test install setup wizard, clicking the back button should take you to the previous step.",
				"htaccess file (After modifying Allowoverride to value All), but still when I am accessing a page apache uses \"ISO-8859-1\" in response charset.",
				"Not sure if this could be due to images on-disk getting corrupted ??",
				"If the DatabaseMetadata.getTableMetadata could take a Table object as a parameter instead of the name, schema and catalog, it could check the Table.isQuoted() method before converting any names to upper or lower case.",
				"* if there's an intersection of the error list and L, push the manifest file",
				"`if (getActivity() == null)` should be moved into the `run()`method",
				"It should hook into the ltk refactoring support to update the header when the component file is moved.",
				"If the HTTP response line is malformed, mod_proxy_http acts as if ProxyBadHeader was set to StartBody, i.e. it returns the entire backend response as the body of the proxy's response.",
				"/usr/bin/env bash if [[ $1 = \"-d\" ]]; then  while true; do sleep 1000; done fi if [[ $1 = \"-bash\" ]]; then  echo \"Using Bash\"  /bin/bash fi java -Xmx1g -jar org.apache.stanbol.launchers.stable-1.0.0-SNAPSHOT.jar -p 9080",
				"However, if an adopter contributes a group starting with \"Ma\" for example, it will be placed right after Entity Beans.",
				"If a automated tool is used to scan a website it can start to crawl upwards in  file structure.",
				"This happens if the start-URL points to a location without an index.html If mod_autoindex generates a index.html, it will contain a link to a directory  level one level up i.e. Parent Directory.",
				"However a little while ago I noticed that the image seems to now be blocked by facebook.",
				"If dataset is null, NPE will throw in DataID.append() method.",
				"if test -n \"/usr/source/httpd-2.0.48/srclib/apr\"; then cp -p",
				"While Publishing review to facebook using facebook application_after publishing review the page could not redirect properly.the page opening in same popup window.I have used apache redirect url.",
				"It calls it  once at the beginning of the handler in order to set the content-type to the default (usually text/html)  and then calls it again if the php script sends a Content-Type header.",
				"ap_set_content_type attaches filters configured by AddOutputFilterByType when it is called.",
				"When I try to log in with my account maryseg@g the message your account is temporarily unavailable for maintenance for the next few hours .",
				"If the project contains a combination of builders:",
				"Start: if c, err := container.runtime.volumes.Create(nil, container, \"\", \"\", nil); err !",
				"ContainerStart: if err := container.Start(); err !",
				"Hi, the pivot filter doesn't work if the file is read only (for example opening a xls file from email), the menu appears and I can select a value but clicking on OK it says that the file is read only.",
				"To know if a user has given certain extended permission to our application_ there is this users.hasAppPermission method in the REST API.",
				"If this is modified to surround both tables in from clause with () then it is parsed correctly.",
				"If I load the document in OpenOffice.org 3.2.1, the second line of the table shows:",
				"If I load the document in LibreOffice 3.3.2, the second line of the table shows:",
				"If a user is browsing in secure mode and they hit an app that uses XFBML_ the image is referenced via http instead of https.",
				"After an HTTPS page  is loaded, if Apache is restared or stoped the following message will appear,  then Apache exits.",
				"If you need more info or test I can help.",
				"This occurs only when \"Allow Multiple\" answers is checked in the form schema in an obs group and multiple answers were then entered."

		};

		TestUtils.testParagraphs(txts, pm, 0);

	}

}