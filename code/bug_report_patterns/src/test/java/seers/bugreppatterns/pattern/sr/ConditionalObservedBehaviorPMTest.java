package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/26/16.
 */
public class ConditionalObservedBehaviorPMTest extends BaseTest {

	{
		pm = new ConditionalObservedBehaviorPM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = {
				"If you go to http://www.new.facebook.com/developers/ at bottom right is a tab called  Status _ which show the Status of the Platform to developers.",
				"While running 1.8-dev in a virtual appliance, Shazin and I ran into this bug.",
				"If I try to add this image to a gallery, the app crashes.",
				"When I visit stats, the progress indicator at the top of the screen below the title bar sometimes doesn't stop.",
				"If you click the little x to hide the report_ then you are given the following message:",
				"If I show a comment, then hide it, it will still be there when I open the document again.",
				"I get a can't instantiate class exception when I run the query \"select o from Site o\" when I have the following",
				"I just did apt-get docker-engine on my ubunbu machine, but when I checked the version, I got this  docker version",
				"However if I delete a comment, save the file and re-open it.",
				"The problem appears when I call entityManager.merge(myPerson) - there is an information that minimum child number is 1.",
				"Any call to Graph API over album or photo never returns the comment list_ and if you try to get it explicitly_ the answer is an empty data list."
				
//				"When I call \nnavigate(boolean next) with next = true, selection jump to first element(great).",
//				"But if i have selected first and i call it with next = false, it stay on first.",
//				"However, if you remove a listener when the node is removed, its throws an",
//				"Both times when i uninstalled it i used windows uninstaller, and didnt touch the files apache leaves behind.",
//				"when I go to selected sites which display properly within IE, many of the gif images does not seem to download.",
//				"When I installed apache and try to start the server - I get an error saying \nthat Apache2 service is not found.",
//				"When trying to add new application_ FB platform display a warning message  You are using Facebook as XXXXXX.",
//				"When building firefox with places and --with-libxul-sdk, there is an error in browser/browser/components/build:",
//				"The usual firestorm of discussion broke out ... anyway, as I noted, it's\nimportant to me that even if I issue an ungraceful restart or shutdown that the\nMPM manages, if at all possible, to call apr_pool_destroy(pchild).",
//				"When calling the authorize method in the iphone ios sdk (https://github.com/facebook/facebook-ios-sdk/blob/master/src/Facebook.m#L211) an intermittent kError 1349045 error sometimes occurs with the error description  An invalid Platform session was found.",
//				"When trying to RPMBuild -tb on httpd-2.2.11 source, the following error is thrown:",
//				"I have upgraded  PHP to PHP 5.1.0RC1  and  I get  this nice error whenever I \ntry to put a file into a Webdav folder.",
//				"If I disable the php module in Apache config file everything is back to normal.",
//				"Whenever I use my Mozilla Firefox-browser and log into my Hotmail-account, my automated signature when composing a new email, do not work."
		};
		TestUtils.testSentences(txts, pm, 1);
		
		@SuppressWarnings("unused")
		String[] trickyOnes = {
				"When saving a new (transient) entity by cascading from a JPA merge() operation on its parent, Hibernate will generate two INSERT statements for the new entity when it is held in a UserCollectionType."};

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = {
				
				" if it searches for a user and gets back more than one entry, it does not work",
				"Feel free to close either of these issues if they're dupes.",
				"will create a picture post_ but if you change the picture url to:",
				"However, I'll understand it if you feel this isn't that important.",
				"But I think there should be a better solution, or at least a warning in the docs not to use % characters while setting HTTP headers from a .",
				"The existing util_ldap.c considers it an error in util_ldap_cache_checkuserid if it searches for a user and gets back more than one entry.",
				"The mod_reqtimeout module is not dropping connections and returning 408 when dealing with \"slow http header\" or \"slow http body\" requests.",
				"It would be nice if each about: page has a favicon :\\nabout:config\\nabout:support\\nabout:buildconfig\\nabout:plugins\\nabout:cache\\nabout:memory\\nabout:blank\\nabout:mozilla",
				
		
//		
//				"If I go to check the tmp0 deployed copy manually, I have found all cases where a single file was being moved to be bogus.",
//				"While looking at it I found what I\nwould consider to be some serious problems with the code:",
//				"While porting mod_watch to Apache 2.0, I looked at mod_auth_digest as an example\nof a module using shared memory and mutexes.",
//				"If the default value of zero is\nused, the code works, but if a non-zero value is supplied, it is not respected\nand a non-graceful shutdown begins immediately.",
//				"If i call FQL:",
//				"If I call:",
//				"When viewing this patient again, no input box and no date appears as options.",
//				"Please tell me if you need more infomation or help on this problem.",
//				"* no matter if you use annotation or mapping files, test will fail using javassist (it used to fail with previous version too)",
//				"Not being a GDK guru, I don't know if unconditionally calling the flush works on all platforms, but I think something like that is needed on X at least.",
//				"When EnableSendFile is enabled (the default) file transfers can be slow.",
//				"Observation:\n  If the clients cache is cleared, the next request comes from the upstream \nserver, then subsequent requests come from the proxy's cache.",
//				"This would be especially useful if the .class file is present in multiple .jar\nfiles because it then would bring to the one implementation that gets really used.",
//				"While for all the other source files, a double click brings me into the right\nplace in the source file, understandably this doesn't happen for the \"Unknown\nSource\" case.",
//				"However, it would be great if upon a double click there, the Project\nproperties-Java Build Path-Libraries would open the concerned library where I\ncould attach the source.",
//				"In the test case, when loading it, you should not have spellcheck entries in the context menu in the first input.",
//				"When processing a \"GET /.../file.html\", httpd attempts to open the file\n\"docroot/.../file.html/.",
//				"According to that page, empty brigades should be suppressed; upon seeing an empty brigade, the filter should simply return APR_SUCCESS rather than calling ap_pass_brigade.",
//				"In order to let the module trap the code, we need, when the module is loaded, to\naccept to establish the connection in case of a certificate validation error:",
//				"In order to check if the module is loaded, I need a few lines at the beginning",
//				"If this is normal then it would be helpful if the documentation reflected it and also cascade would only take an \"update\" or \"delete\".",
//				"if you use the form example from the mod_autoindex docs, opera (5&6 \ntested) escapes a \"*\" to %2A (yes: 42 ;-)",
//				"The \"docker run\" command flag \"device\" does not preserve the localhost device group when copied into the docker container.",
//				"1 http://server/balancer-manager?xml=1 gives the html output if parameter nonce is not provided",
//				"In addition hostname only shows the ip address and not ip:port if a port is configured",
//				"Several (maybe 6) months ago\nI noticed problems but wasn't sure if it was a configuration problem.",
//				"As I said, this works if apache is started as a console application, and it used to work with an earlier version of apache.",
//				"4 If no language attribute was found in the page rendered, then the default behavior comes back.",
//				"when and only when the spell checker is called (maybe some kind of icon with a \"more information\" bubble?)",
//				"3 If the dictionary is not installed, it will display a small message along the line of \"this form seems to ask for French text but you don't have the French dictionnary on your Firefox, do you want to install it?\"",
//				"If you google for ap_mpm_pod_check, it seems there's a whole collection of other",
//				"So in your script you must chech if apache_fib eq '0' or 'NO' \nthen return 0 \nelse \ncommand=\"setfib -F ${apache22_fib} ${command}\"",
//				"[subant] The following error occurred while executing this line:",
//				"[subant] /home/akurtakov/work/fedora/eclipse-mylyn/devel/org.eclipse.mylyn/build/plugins/org.eclipse.mylyn.wikitext.ui/customBuildCallbacks.xml:31: The following error occurred while executing this line:",
//				"because if I work on a task and I deactivate the",
//				"# enable if the mod_perl 1.0 compatibility is needed\n# use Apache::compat ();",
//				"Here is the definition if the davfs in the apache config file:",
//				"Even if I reconnect this user to facebook ie get the access token again_ the same error persists.",
//				"I spent a long time trying to figure out why my sticksessions were not working, when I finally found in an email in the archives that I needed a route parameter set in the  BalancerMember line.",
//				"When at the catalogue there are files which names in other encodings, the apache incorrectly processes names of files and the file-list spoils.",
//				"For some weird reasons_ my application s logo is not rendered into the application dashboard... When I inspect the HTML the  src  attribute if the  img  tag is empty..."
		
		};

		TestUtils.testSentences(txts, pm, 0);

		@SuppressWarnings("unused")
		String[] trickyOnes = {
				"However_ what if instead_ for determining whether to aggregate feed stories_ it first parsed it for multiple actors and any title_data attributes that are not used with multiple actors are cast out." };

	}

}