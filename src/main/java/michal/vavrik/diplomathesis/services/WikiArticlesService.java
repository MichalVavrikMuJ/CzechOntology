package michal.vavrik.diplomathesis.services;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.database.entity.Czwiki;
import michal.vavrik.diplomathesis.database.repository.CzWikiRepository;
import michal.vavrik.diplomathesis.rest.controller.model.WikiArticleDTO;

@Slf4j
@Service
public class WikiArticlesService {
	
	@Autowired
	private CzWikiRepository wikiRepo;
	
	public void saveArticles(List<WikiArticleDTO> articles) {
		articles.forEach(article -> {
			try {
				String text = article.text.getBytes().length < 3800 ? article.text : new String(article.text.getBytes("UTF-8"), 0, 3000 - 2, "UTF-8");
				log.info("Saving wiki article with id: {}", article.id);
				wikiRepo.save(Czwiki.builder()
						.id(article.id)
						.url(article.url)
						.title(article.title)
						.text(text)
						.build()
						);
			} catch (UnsupportedEncodingException e) {
				log.error("could not truncate text {}", e.getMessage());
			}
		});
	}

}
