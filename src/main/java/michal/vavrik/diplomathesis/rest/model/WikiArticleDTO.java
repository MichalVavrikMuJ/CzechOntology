package michal.vavrik.diplomathesis.rest.model;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class WikiArticleDTO {
	
	public BigDecimal id;
	
	public String url;
	
	public String title;
	
	public String text;

}
