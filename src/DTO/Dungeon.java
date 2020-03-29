package DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"name",
	"creatorId",
	"timeFailed",
	"size",
	"difficulty",
	"id",
	"cases",
	"stuff",
	"paths"
})
public class Dungeon {

	@JsonProperty("name")
	private String name;
	@JsonProperty("creatorId")
	private Integer creatorId;
	@JsonProperty("timeFailed")
	private Integer timeFailed;
	@JsonProperty("size")
	private Integer size;
	@JsonProperty("difficulty")
	private Integer difficulty;
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("cases")
	private List<Integer> cases = null;
	@JsonProperty("stuff")
	private List<Integer> stuff = null;
	@JsonProperty("paths")
	private List<Object> paths = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	public static ArrayList<Integer> getNeighbourCaseIds(int posCaseId, int mapWidth, int mapHeight) {
		ArrayList<Integer> positions = new ArrayList<>();

		if(posCaseId +1 < mapHeight * mapWidth && (posCaseId +1) % mapWidth != 0) 
			positions.add(posCaseId +1);
		if(posCaseId -1 >= 0 && (posCaseId -1) % mapWidth != mapWidth -1) 
			positions.add(posCaseId -1);
		if(posCaseId + mapWidth < mapHeight * mapHeight)
			positions.add(posCaseId + mapWidth);
		if(posCaseId - mapWidth >= 0)
			positions.add(posCaseId - mapWidth);
		
		return positions;
	}
	
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("creatorId")
	public Integer getCreatorId() {
		return creatorId;
	}

	@JsonProperty("creatorId")
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	@JsonProperty("timeFailed")
	public Integer getTimeFailed() {
		return timeFailed;
	}

	@JsonProperty("timeFailed")
	public void setTimeFailed(Integer timeFailed) {
		this.timeFailed = timeFailed;
	}

	@JsonProperty("size")
	public Integer getSize() {
		return size;
	}

	@JsonProperty("size")
	public void setSize(Integer size) {
		this.size = size;
	}

	@JsonProperty("difficulty")
	public Integer getDifficulty() {
		return difficulty;
	}

	@JsonProperty("difficulty")
	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("cases")
	public List<Integer> getCases() {
		return cases;
	}

	@JsonProperty("cases")
	public void setCases(List<Integer> cases) {
		this.cases = cases;
	}

	@JsonProperty("stuff")
	public List<Integer> getStuff() {
		return stuff;
	}

	@JsonProperty("stuff")
	public void setStuff(List<Integer> stuff) {
		this.stuff = stuff;
	}

	@JsonProperty("paths")
	public List<Object> getPaths() {
		return paths;
	}

	@JsonProperty("paths")
	public void setPaths(List<Object> paths) {
		this.paths = paths;
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
