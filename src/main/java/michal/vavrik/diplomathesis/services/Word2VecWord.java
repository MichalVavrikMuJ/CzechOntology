package michal.vavrik.diplomathesis.services;

import java.util.Arrays;
import java.util.Collection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @Setter @Getter @Builder
public class Word2VecWord {

	private String keyWord;
	
	private double[] wordVector;
	
	private Collection<String> wordsNearest;
	
	public String wordVectorToString() {
		return Arrays.toString(this.wordVector);
	}
}
