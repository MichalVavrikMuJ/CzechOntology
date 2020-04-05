package michal.vavrik.diplomathesis.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.database.entity.Czwiki;
import michal.vavrik.diplomathesis.database.repository.CzWikiRepository;
import michal.vavrik.diplomathesis.rest.model.DeriNetRowDTO;

@Service
@Slf4j
public class WikiDerinetLinkerService {
	
	@Autowired
	private CzWikiRepository wikiRepository;
	
	public void linkDerinetRowWithWiki(DeriNetRowDTO derinetRow) {
		List<Czwiki> findByTitleContaining = linkKeyWordWithWiki(derinetRow.getLemma());
		findByTitleContaining.forEach(w -> log.info("Wiki article with title: {} and url: {}", w.getTitle(), w.getUrl()));
	}
	
	public List<Czwiki> linkKeyWordWithWiki(String keyWord) {
		return wikiRepository.findByTitleContaining(keyWord);
	}

}
