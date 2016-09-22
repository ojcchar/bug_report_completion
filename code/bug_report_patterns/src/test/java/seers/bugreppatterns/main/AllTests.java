package seers.bugreppatterns.main;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import seers.bugreppatterns.pattern.eb.AllEBTests;
import seers.bugreppatterns.pattern.ob.AllOBTests;
import seers.bugreppatterns.pattern.sr.AllSRTests;

@RunWith(Suite.class)
@SuiteClasses({ AllEBTests.class, AllSRTests.class, AllOBTests.class })
public class AllTests {

}
