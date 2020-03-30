package DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import beans.Monster;
import beans.Stuff;
import beans.Terrain;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"stuffs",
	"terrains",
	"monsters",
	"others"
})
public class RessourcesFull {

	@JsonProperty("stuffs")
	private List<Stuff> stuffs = null;
	@JsonProperty("terrains")
	private List<Terrain> terrains = null;
	@JsonProperty("monsters")
	private List<Monster> monsters = null;
	@JsonProperty("others")
	private List<Integer> others = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	public ArrayList<Integer> getAllPlacables(){
		ArrayList<Integer> allPlacables = new ArrayList<Integer>();
		allPlacables.addAll(terrains.stream().map(e -> e.getId()).collect(Collectors.toList()));
		allPlacables.addAll(monsters.stream().map(e -> e.getId()).collect(Collectors.toList()));
		allPlacables.addAll(others);
		return allPlacables;
	}
	
	
	
	@JsonProperty("stuffs")
	public List<Stuff> getStuffs() {
		return stuffs;
	}

	@JsonProperty("stuffs")
	public void setStuffs(List<Stuff> stuffs) {
		this.stuffs = stuffs;
	}

	@JsonProperty("terrains")
	public List<Terrain> getTerrains() {
		return terrains;
	}

	@JsonProperty("terrains")
	public void setTerrains(List<Terrain> terrains) {
		this.terrains = terrains;
	}

	@JsonProperty("monsters")
	public List<Monster> getMonsters() {
		return monsters;
	}

	@JsonProperty("monsters")
	public void setMonsters(List<Monster> monsters) {
		this.monsters = monsters;
	}

	@JsonProperty("others")
	public List<Integer> getOthers() {
		return others;
	}

	@JsonProperty("others")
	public void setOthers(List<Integer> others) {
		this.others = others;
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


