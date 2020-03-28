package DTO;
/*
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Ressources {

	private List<Integer> stuffs = null;
	private List<Integer> terrains = null;
	private List<Integer> monsters = null;
	private List<Integer> others = null;

	public Ressources(List<Integer> stuffs, List<Integer> terrains, List<Integer> monsters, List<Integer> others) {
		super();
		this.stuffs = stuffs;
		this.terrains = terrains;
		this.monsters = monsters;
		this.others = others;
	}

	public ArrayList<Integer> getAllPlacables(){
		ArrayList<Integer> allPlacables = new ArrayList<Integer>();
		allPlacables.addAll(terrains);
		allPlacables.addAll(monsters);
		allPlacables.addAll(others);
		return allPlacables;
	}

	public List<Integer> getStuffs() {
		return stuffs;
	}

	public void setStuffs(List<Integer> stuffs) {
		this.stuffs = stuffs;
	}

	public List<Integer> getTerrains() {
		return terrains;
	}

	public void setTerrains(List<Integer> terrains) {
		this.terrains = terrains;
	}

	public List<Integer> getMonsters() {
		return monsters;
	}

	public void setMonsters(List<Integer> monsters) {
		this.monsters = monsters;
	}

	public List<Integer> getOthers() {
		return others;
	}

	public void setOthers(List<Integer> others) {
		this.others = others;
	}



}

 */


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
	"stuffs",
	"terrains",
	"monsters",
	"others"
})
public class Ressources {

	@JsonProperty("stuffs")
	private List<Integer> stuffs = null;
	@JsonProperty("terrains")
	private List<Integer> terrains = null;
	@JsonProperty("monsters")
	private List<Integer> monsters = null;
	@JsonProperty("others")
	private List<Integer> others = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("stuffs")
	public List<Integer> getStuffs() {
		return stuffs;
	}

	@JsonProperty("stuffs")
	public void setStuffs(List<Integer> stuffs) {
		this.stuffs = stuffs;
	}

	@JsonProperty("terrains")
	public List<Integer> getTerrains() {
		return terrains;
	}

	@JsonProperty("terrains")
	public void setTerrains(List<Integer> terrains) {
		this.terrains = terrains;
	}

	@JsonProperty("monsters")
	public List<Integer> getMonsters() {
		return monsters;
	}

	@JsonProperty("monsters")
	public void setMonsters(List<Integer> monsters) {
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

	public ArrayList<Integer> getAllPlacables(){
		ArrayList<Integer> allPlacables = new ArrayList<Integer>();
		allPlacables.addAll(terrains);
		allPlacables.addAll(monsters);
		allPlacables.addAll(others);
		return allPlacables;
	}

}

