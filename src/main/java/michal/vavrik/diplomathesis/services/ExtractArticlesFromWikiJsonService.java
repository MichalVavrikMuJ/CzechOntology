package michal.vavrik.diplomathesis.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import lombok.extern.slf4j.Slf4j;
import michal.vavrik.diplomathesis.rest.controller.model.WikiArticleDTO;

@Slf4j
@Service
public class ExtractArticlesFromWikiJsonService {
	
	private static final String splitBy = "\\{\"id\":";
	
	private static final String prefix = "{\"id\":";
	
	@Value("${wiki.dirPattern}")
	private String wikiDirPattern;
	
	@Autowired
	private ApplicationContext appContext;
	
	public List<WikiArticleDTO> getArticles() throws IOException {
		log.info("Parsing start");
		List<String> articlesInString = new ArrayList<>();
		Arrays.stream(getArticleResources()).map(fileWithArticles -> {
			try {
				log.info("file with articles named: {}", fileWithArticles.getFilename());
				return oneArticleJsonObject(fileWithArticles);
			} catch (IOException e) {
				log.error("parsing json articles failed: {}",e.getMessage());
			}
			return null;
		}).forEach(articlesInString::addAll);
		
		ObjectMapper mapper = new ObjectMapper();
		
		return articlesInString.stream().map(x -> {
			try {
//				log.info("{}", x);
				return mapper.readValue(x, WikiArticleDTO.class);
			} catch (IOException e) {
				log.error("{}", x);
				return null;
			}
			//return null;
		})
				.filter(wikiArticle -> wikiArticle != null)
				.collect(Collectors.toList());
	}

	private Resource[] getArticleResources() throws IOException {
		return appContext.getResources(wikiDirPattern);
	}
	
	private List<String> oneArticleJsonObject(Resource file) throws IOException {
		InputStream inputStream = file.getInputStream();
		 
	    ByteSource byteSource = new ByteSource() {
	        @Override
	        public InputStream openStream() throws IOException {
	            return inputStream;
	        }
	    };
	 
	    String fileInString = byteSource.asCharSource(Charsets.UTF_8).read();
	    String[] articles = fileInString.split(splitBy);
	    return Arrays.stream(articles).map(article -> prefix + article).collect(Collectors.toList());
	}
	
}
