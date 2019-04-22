package seers.bugrepcompl.taggedtext;

import org.junit.Test;
import seers.appcore.utils.FilePathUtils;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class AllSentencesExtractorTest {

    @Test
    public void testGetCodedText() throws Exception {
        AllSentencesExtractor extractor = new AllSentencesExtractor();

        File baseFolder = new File("test_data/bug_coding");
        String bugId = "CASSANDRA-285";

        List<List<CodedTextFragment>> codedText = getCodedTextFragments(extractor, baseFolder, bugId);

        assertNotNull(codedText);
        assertFalse(codedText.isEmpty());

        printFragments(codedText);


        //----------------------------------------------

        bugId = "CASSANDRA-285-2";

        codedText = getCodedTextFragments(extractor, baseFolder, bugId);

        assertNotNull(codedText);
        assertFalse(codedText.isEmpty());

        printFragments(codedText);

        //----------------------------------------------

        bugId = "CASSANDRA-1787";

        codedText = getCodedTextFragments(extractor, baseFolder, bugId);

        assertNotNull(codedText);

        printFragments(codedText);

        assertEquals(9, codedText.size());

        //----------------------------------------------

        System.out.println();

        bugId = "KeePassDroid-146";

        codedText = getCodedTextFragments(extractor, baseFolder, bugId);

        assertNotNull(codedText);
        printFragments(codedText);
        assertEquals(5, codedText.size());

        //----------------------------------------------

        System.out.println();

        bugId = "KeePassDroid-146-2";

        codedText = getCodedTextFragments(extractor, baseFolder, bugId);

        assertNotNull(codedText);
        printFragments(codedText);
        assertEquals(5, codedText.size());

        //----------------------------------------------


    }

    private void printFragments(List<List<CodedTextFragment>> codedText) {
        for (List<CodedTextFragment> fragment : codedText) {

            for (CodedTextFragment codedTextFragment : fragment) {

                System.out.print("\t");
                System.out.println(codedTextFragment);
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    private List<List<CodedTextFragment>> getCodedTextFragments(AllSentencesExtractor extractor, File baseFolder,
                                                                String bugId) throws Exception {
        File bugAnnFile = FilePathUtils.getFile(baseFolder, bugId + ".ann");
        File bugTextFile = FilePathUtils.getFile(baseFolder, bugId + ".txt");
        return extractor.getCodedText2(bugAnnFile, bugTextFile);
    }
}