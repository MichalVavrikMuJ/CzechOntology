package michal.vavrik.diplomathesis.database.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Czwiki {
	
	@Id
	public BigDecimal id;
	
	public String url;
	
	public String title;
	
	public String text;

}
