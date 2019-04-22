package seers.bugrepcompl.taggedtext;

import org.junit.Test;
import seers.appcore.utils.FilePathUtils;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class SelectedSentencesExtractorTest {

    @Test
    public void testGetCodedText() throws Exception {
        SelectedSentencesExtractor extractor = new SelectedSentencesExtractor();

        File baseFolder = new File("test_data/bug_coding");
        String bugId = "CASSANDRA-285";

        List<CodedTextFragment> codedText = getCodedTextFragments(extractor, baseFolder, bugId);

        assertNotNull(codedText);
        assertTrue(codedText.isEmpty());

        //----------------------------------------------

        bugId = "CASSANDRA-285-2";

        codedText = getCodedTextFragments(extractor, baseFolder, bugId);

        assertNotNull(codedText);
        assertTrue(codedText.isEmpty());

        //----------------------------------------------

        bugId = "CASSANDRA-1787";

        codedText = getCodedTextFragments(extractor, baseFolder, bugId);

        assertNotNull(codedText);

        for (CodedTextFragment fragment : codedText) {

            System.out.println(fragment);
        }

        assertEquals(3, codedText.size());

        //----------------------------------------------

        System.out.println();

        bugId = "KeePassDroid-146";

        codedText = getCodedTextFragments(extractor, baseFolder, bugId);

        assertNotNull(codedText);

        for (CodedTextFragment fragment : codedText) {

            System.out.println(fragment);
        }

        assertEquals(6, codedText.size());

        //----------------------------------------------


    }

    private List<CodedTextFragment> getCodedTextFragments(SelectedSentencesExtractor extractor, File baseFolder,
                                                          String bugId) throws Exception {
        File bugAnnFile = FilePathUtils.getFile(baseFolder, bugId + ".ann");
        File bugTextFile = FilePathUtils.getFile(baseFolder, bugId + ".txt");
        return extractor.getCodedText(bugAnnFile, bugTextFile);
    }
}