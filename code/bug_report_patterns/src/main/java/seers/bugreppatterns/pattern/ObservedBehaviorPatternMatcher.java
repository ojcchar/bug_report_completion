package seers.bugreppatterns.pattern;

import java.util.List;

import seers.textanalyzer.entity.Token;

public abstract class ObservedBehaviorPatternMatcher extends PatternMatcher {

	public String getType() {
		return OB;
	}

	protected boolean isEBModal(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token tok = tokens.get(i);
			if (tok.getGeneralPos().equals("MD") && (tok.getLemma().equals("must") || tok.getLemma().equals("need")
					|| tok.getLemma().equals("should"))) {
				return true;
			}
		}
		return false;
	}
	
	
}
