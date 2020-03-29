package beans;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"id",
	"name",
	"price",
	"type",
	"attack",
	"armor",
	"range",
	"difficulty"
})
public class Stuff /*extends Ressources*/ {

	@JsonProperty("id")
	private Integer id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("price")
	private Integer price;
	@JsonProperty("type")
	private String type;
	@JsonProperty("attack")
	private Integer attack;
	@JsonProperty("armor")
	private Integer armor;
	@JsonProperty("range")
	private Integer range;
	@JsonProperty("difficulty")
	private Integer difficulty;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("price")
	public Integer getPrice() {
		return price;
	}

	@JsonProperty("price")
	public void setPrice(Integer price) {
		this.price = price;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("attack")
	public Integer getAttack() {
		return attack;
	}

	@JsonProperty("attack")
	public void setAttack(Integer attack) {
		this.attack = attack;
	}

	@JsonProperty("armor")
	public Integer getArmor() {
		return armor;
	}

	@JsonProperty("armor")
	public void setArmor(Integer armor) {
		this.armor = armor;
	}

	@JsonProperty("range")
	public Integer getRange() {
		return range;
	}

	@JsonProperty("range")
	public void setRange(Integer range) {
		this.range = range;
	}

	@JsonProperty("difficulty")
	public Integer getDifficulty() {
		return difficulty;
	}

	@JsonProperty("difficulty")
	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}