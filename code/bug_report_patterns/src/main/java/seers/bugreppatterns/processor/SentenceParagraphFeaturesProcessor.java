package seers.bugreppatterns.processor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReport;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReportDescription;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionParagraph;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionSentence;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.PredictionOutput;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class SentenceParagraphFeaturesProcessor extends TextInstanceProcessor {

    private List<PatternMatcher> paragraphPatterns;

    public SentenceParagraphFeaturesProcessor(ThreadParameters params) {
        super(params);
        paragraphPatterns = patterns.stream().filter(p -> p.getName().startsWith("P")).collect(Collectors.toList());
    }

    @Override
    public void executeJob() {

        for (File file : files) {

            try {
                PatternLabeledBugReport bugRep = XMLHelper.readXML(PatternLabeledBugReport.class, file);

                PatternLabeledBugReportDescription description = bugRep.getDescription();
                if (description == null) {
                    continue;
                }
                List<PatternLabeledDescriptionParagraph> paragraphs = description.getParagraphs();
                if (paragraphs == null) {
                    continue;
                }

                for (PatternLabeledDescriptionParagraph paragraph : paragraphs) {

                    if (paragraph == null)
                        continue;

                    LinkedHashMap<PatternMatcher, Integer> paragraphPattenMatches = new LinkedHashMap<>();

                    ImmutablePair<Paragraph, HashMap<Sentence, String>> paragraphAndSentCodedPatterns =
                            buildParagraph(paragraph);
                    Paragraph parsedParagraph = paragraphAndSentCodedPatterns.getLeft();

                    if (parsedParagraph.isEmpty()) {
                        continue;
                    }

                    //---------------------------

                    final List<PatternMatcher> paragraphCodedPatterns = getCodedPatterns(paragraph.getPatterns());
                    for (PatternMatcher codedPattern : paragraphCodedPatterns) {
                        updatePattern(paragraphPattenMatches, codedPattern, 1);
                    }

                    //-------------------------


                    for (PatternMatcher patternMatcher : paragraphPatterns) {
                        int numMatches = patternMatcher.matchParagraph(parsedParagraph);
                        if (numMatches > 0) {
                            updatePattern(paragraphPattenMatches, patternMatcher, numMatches);
                        }
                    }

                    //-------------------------

                    final HashMap<Sentence, String> sentenceCodedPatterns = paragraphAndSentCodedPatterns.getRight();

                    for (Sentence sentence : parsedParagraph.getSentences()) {


                        LinkedHashMap<PatternMatcher, Integer> sentencePatternMatches =
                                new LinkedHashMap<>(paragraphPattenMatches);
                        //-------------------------

                        final String sentPatts = sentenceCodedPatterns.get(sentence);
                        final List<PatternMatcher> sentCodedPatterns = getCodedPatterns(sentPatts);
                        for (PatternMatcher codedPattern : sentCodedPatterns) {
                            updatePattern(sentencePatternMatches, codedPattern, 1);
                        }

                        //-------------------------

                        for (PatternMatcher patternMatcher : this.patterns) {
                            int matchSentence = patternMatcher.matchSentence(sentence);
                            if (matchSentence > 0) {
                                updatePattern(sentencePatternMatches, patternMatcher, matchSentence);
                            }
                        }

                        //-------------------------

                        PredictionOutput predictionOutput = predictor.predictLabels(bugRep.getId(), sentence.getId(),
                                sentencePatternMatches);

                        writePreFeatures(bugRep.getId(), sentence.getId(), predictionOutput.getFeatures());
                        writePrediction(bugRep.getId(), sentence.getId(), predictionOutput.getLabels());
                    }
                }
            } catch (Exception e) {
                LOGGER.error("[" + system + "] Error for file: " + file, e);
            }
        }

    }

    private void updatePattern(LinkedHashMap<PatternMatcher, Integer> patternMatches,
                               PatternMatcher codedPattern,
                               int matches) {
        Integer numMatches = patternMatches.get(codedPattern);
        if (numMatches == null) {
            numMatches = matches;
        } else {
            numMatches = Math.max(numMatches, matches);
        }
        patternMatches.put(codedPattern, numMatches);
    }

    private List<PatternMatcher> getCodedPatterns(String patterns) {

        List<PatternMatcher> codedPatterns = new ArrayList<>();
        if (StringUtils.isEmpty(patterns)) {
            return codedPatterns;
        }

        patterns = patterns.trim();

        final String[] patts = patterns.split(",");

        if (patts.length == 0)
            return codedPatterns;

        codedPatterns = Arrays.stream(patts).map(p -> {

            if ("S_SR_GERUND_ACTION".equals(p))
                return null;

            final Optional<PatternMatcher> patternFound = this.patterns.stream()
                    .filter(pm -> pm.getName().equals(p))
                    .findFirst();
            if (patternFound.isPresent())
                return patternFound.get();

            LOGGER.warn("Could not find pattern: " + p);
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return codedPatterns;
    }

    private ImmutablePair<Paragraph, HashMap<Sentence, String>> buildParagraph(
            PatternLabeledDescriptionParagraph paragraph) {
        HashMap<Sentence, String> sentenceCodedPatterns = new HashMap<>();
        final Paragraph paragraph1 = new Paragraph(paragraph.getId());
        paragraph1.setSentences(buildSentences(paragraph.getSentences(), sentenceCodedPatterns));
        return new ImmutablePair<>(paragraph1, sentenceCodedPatterns);
    }


    private List<Sentence> buildSentences(List<PatternLabeledDescriptionSentence> sentences,
                                          HashMap<Sentence, String> sentenceCodedPatterns) {
        List<Sentence> sentences2 = new ArrayList<>();
        for (PatternLabeledDescriptionSentence sentence : sentences) {
            final Sentence sent = SentenceUtils.parseSentence(sentence.getId(), sentence.getValue());
            sentenceCodedPatterns.put(sent, sentence.getPatterns());
            sentences2.add(sent);
        }
        return sentences2;
    }

}
