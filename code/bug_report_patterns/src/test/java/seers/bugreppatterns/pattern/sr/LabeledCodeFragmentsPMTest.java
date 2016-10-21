package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class LabeledCodeFragmentsPMTest extends BaseTest {

	public LabeledCodeFragmentsPMTest() {
		pm = new LabeledCodeFragmentsPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"commands run:\n" + "```shell\n" + "docker tag hcg-uwsgi docker.example.com/hcg-uwsgi:latest\n"
						+ "docker --debug=true push docker.example.com/hcg-uwsgi\n" + "````" };
		TestUtils.testParagraphs(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
				"While merging changes from my stream into HEAD, I had to merge a couple of conflicts. When I was prompted to save, it took &gt;15 seconds to perform the save. The icon did not change to an hourglass and for a second I thought that Eclipse was dying a slow death.",

				"If i run this code:\n" + "sudo docker run alexl/test /usr/sbin/httpd -D FOREGROUND\n" + "\n"
						+ "and then press ctrl-c, then the docker daemon dies with:\n"
						+ "2013/09/24 14:28:38 Received signal 'terminated', exiting",
						
				"{code}. Caused by: org.hibernate.PropertyAccessException: IllegalArgumentException occurred calling getter of nl.msw.compraventa.model.location.Country.countryCode. 	at org.hibernate.property.BasicPropertyAccessor$BasicGetter.get(BasicPropertyAccessor.java:187). 	at org.hibernate.tuple.entity.AbstractEntityTuplizer.getPropertyValue(AbstractEntityTuplizer.java:630). 	at org.hibernate.persister.entity.AbstractEntityPersister.getPropertyValue(AbstractEntityPersister.java:4535). 	at or. 	... 87 more. {code}. ",
				
				"(Note: this is filed as a meta bug as part of the â€œPaper Cutâ€? bugs since we assume that there are multiple existing bugs related to this behavior.. Please make them block this bug.). ",
				
				"Method. --. ",
				
				"DetachedCriteria subselect = DetachedCriteria.forClass(EntityThree.class);\nsubselect.add(Restrictions.like(\name\",\"test\",MatchMode.START)).. setProjection(Projections.id())"

		};
		TestUtils.testParagraphs(txts, pm, 0);

		@SuppressWarnings("unused")
		String[] failingTxts = { "When I do a HQL query as:\n" + "\n"
				+ "SELECT count(*) FROM table WHERE property = 'value'\n" + "\n" + "and right afterwards I do:\n" + "\n"
				+ "FROM table WHERE property = 'value' ORDER BY other.property\n" + "\n"
				+ "the resulting rows won't be ordered if I activate cashing and use the same query-cash-region. If I include the ORDER BY clause to the first statement, everything is fine." };
	}

}
