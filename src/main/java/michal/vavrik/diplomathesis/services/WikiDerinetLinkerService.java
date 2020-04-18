package michal.vavrik.diplomathesis.services;

import java.util.List;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.database.entity.Czwiki;
import michal.vavrik.diplomathesis.database.entity.DeriNetRow;
import michal.vavrik.diplomathesis.database.repository.CzWikiRepository;
import michal.vavrik.diplomathesis.database.repository.DeriNetRepository;
import michal.vavrik.diplomathesis.rest.model.DeriNetRowDTO;

@Service
@Slf4j
public class WikiDerinetLinkerService {
	
	@Autowired
	private Word2Vec word2Vec;
	
	@Autowired
	private CzWikiRepository wikiRepository;
	
	@Autowired
	private DeriNetRepository deriNetRepository;
	
	public void linkDerinetRowWithWiki(DeriNetRowDTO derinetRow) {
		List<Czwiki> findByTitleContaining = linkKeyWordWithWiki(derinetRow.getLemma());
		findByTitleContaining.forEach(w -> log.info("Wiki article with title: {} and url: {}", w.getTitle(), w.getUrl()));
	}
	
	public List<Czwiki> linkKeyWordWithWiki(String keyWord) {
		return wikiRepository.findByTitleContaining(keyWord);
	}
	
	public List<DeriNetRow> linkKeyWordWithDeriNetRow(String keyWord) {
		return deriNetRepository.findByLemmaContaining(keyWord);
	}

	public Word2VecWord getWord2Vec(String keyWord) {
		log.info("Started loading word2vec model.");
		return Word2VecWord.builder().keyWord(keyWord).wordVector(word2Vec.getWordVector(keyWord)).wordsNearest(word2Vec.wordsNearest(keyWord, 100)).build();
	}
	
	public String getWordsSimilarity(String keyWord1, String keyWord2) {
		return Double.toString(word2Vec.similarity(keyWord1, keyWord2));
	}

}
