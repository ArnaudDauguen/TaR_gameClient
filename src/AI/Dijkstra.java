package AI;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

import DTO.Dungeon;
import beans.Monster;
import beans.Stuff;
import beans.Terrain;

public class Dijkstra {
	
	private int entranceCaseId, weight = 0, size = 10, throneTerrainId = 3, totalDamage = 0, framePerSecond = 30, aiHp, defaultAiHp = 100;
	private ArrayList<Integer> path, map, weightMap, aiStuff;
	private ArrayList<Terrain> terrains;
	private ArrayList<Monster> monsters;
	private ArrayList<Stuff> stuffs;
	private ArrayList<JButton> btnMap;
	private Predicate<Integer> byNotAWall = caseId -> map.get(caseId) != 1;
	private Predicate<Integer> byHaventWalkedBefore = caseId -> !path.contains(caseId);
	private ImageIcon playerIcon;
	private JTextField nbFails;
	
	
	public Dijkstra(int entranceCaseId, int throneTerrainId, int size, ArrayList<Integer> aiStuff, ArrayList<Integer> map, ArrayList<JButton> btnMap, ArrayList<Terrain> terrains, ArrayList<Monster> monsters, ArrayList<Stuff> stuffList, JTextField nbFails) {
		this.entranceCaseId = entranceCaseId;
		this.throneTerrainId = throneTerrainId;
		this.size = size;
		this.map = map;
		this.weightMap = new ArrayList<>();
		this.aiStuff = aiStuff;
		this.terrains = terrains;
		this.monsters = monsters;
		this.stuffs = stuffList;
		this.btnMap = btnMap;
		this.nbFails = nbFails;

		initialize();
		
		//Main AI loop
		new Thread(new Runnable() {
			public void run() {
				boolean ended = false;
				while(!ended) {
					//reset path to entrance
					path = new ArrayList<Integer>();
					path.add(entranceCaseId);
					weight = 0;
					try {
						ended = generateRoad();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}
	
	private void initialize() {
		for(int i = 0; i < size * size; i++)
			weightMap.add(0);
		
		String path = "ressources/player.png";
		String pathNotFound = "ressources/notFound.png";
		File f = new File(path);
		playerIcon = f.exists() ? new ImageIcon(path) : new ImageIcon(pathNotFound);
		
		//calculate damage dealt
		//TODO check why total = 0 ??
		String[] damageMultiplier = {"sword", "bow", "magic"};
		for(int stuffId : aiStuff) {
			for(Stuff stuff : stuffs) {
				if(stuff.getId() == stuffId) {
					totalDamage += stuff.getAttack() * (Arrays.asList(damageMultiplier).indexOf(stuff.getType()) +1);
				}
			}
		}
		if(totalDamage == 0) {
			System.out.println("Some equipements are missing, AI will figth in godmode");
			totalDamage = Integer.MAX_VALUE;
		}
	}
	
	// create a path
	private boolean generateRoad() throws Exception {
		aiHp = defaultAiHp;
		boolean response = false;
		boolean pathComplete = false;
		Icon lastCaseIcon = btnMap.get(path.get(0)).getIcon();
		while(!pathComplete) {
			int lastPathSize = path.size();
			
			//mooving
			int lastCaseTraveled = path.get(path.size() -1);
			ArrayList<Integer> neighbour = Dungeon.getNeighbourCaseIds(lastCaseTraveled, size, size);
			neighbour = (ArrayList<Integer>) neighbour.stream().filter(byNotAWall).filter(byHaventWalkedBefore).collect(Collectors.toList());
			neighbour.sort((o1, o2) -> weightMap.get(o1).compareTo(weightMap.get(o2)));
			if(neighbour.size() == 0) {
				//end of road
				weight += 1000;
				pathComplete = true;
				break;
			}
			int lowestWeight = weightMap.get(neighbour.get(0));
			neighbour = (ArrayList<Integer>) neighbour.stream().filter(caseId -> weightMap.get(caseId) <= lowestWeight).collect(Collectors.toList());
			int newCaseIdToTravel = neighbour.get((int) Math.floor(Math.random() * neighbour.size()));
			
			
			path.add(newCaseIdToTravel);
			lastCaseIcon = moovePlayerIcon(lastCaseIcon, newCaseIdToTravel);
			
			//fight
			if(map.get(newCaseIdToTravel) > 999 && map.get(newCaseIdToTravel) < 3000) {
				//start new path is enemy never met
				if(weightMap.get(newCaseIdToTravel) == 0) {
					pathComplete = true;
				}
				ArrayList<Monster> potentialEnemies = (ArrayList<Monster>) monsters.stream().filter(m -> {
					if(m.getId() >= map.get(newCaseIdToTravel) && m.getId() <= map.get(newCaseIdToTravel)) // to check if equals
						return true;
				return false;}
				).collect(Collectors.toList());
				
				int enemyHp = 100;
				int enemyAttack = 10;
				if(potentialEnemies.size() != 0) {
					enemyHp = potentialEnemies.get(0).getHp();
					enemyAttack = potentialEnemies.get(0).getDamage();
				}

				//combat
				do{
					weight += (Math.floor(enemyHp /10)); //weigth +1 per 10 hp
					enemyHp -= totalDamage;
					//check if enemy died before taking damage
					if(enemyHp > 0) {
						aiHp -= enemyAttack;
						if(aiHp <= 0) {
							pathComplete = true;
							weight += 10000;
							break;
						}
					}
				}while(enemyHp > 0);
				
			}
			

			//check for throne
			if(map.get(newCaseIdToTravel) == throneTerrainId) {
				weight = -1;
				pathComplete = true;
				response = true;
				break;
			}
			
			
			
			
			
			Thread.sleep(1000/framePerSecond);
			
			//check if path did not change
			if(path.size() == lastPathSize) {
				pathComplete = true;
			}
		}
		
		//apply weigth
		if(weight != -1) {
			for(int caseId : path) {
				weightMap.set(caseId, weightMap.get(caseId) + weight);
			}
			Thread.sleep(500);
			updateUi(lastCaseIcon);
		}else {
			for(int caseId : path) {
				weightMap.set(caseId, weight);
			}
		}
		return response;
	}
	
	
	
	private Icon moovePlayerIcon(Icon lastCaseIcon, int newCaseIdToTravel) {
		Icon tmpIcon = btnMap.get(newCaseIdToTravel).getIcon();
		btnMap.get(newCaseIdToTravel).setIcon(playerIcon);
		btnMap.get(path.get(path.size()-2)).setIcon(lastCaseIcon);
		lastCaseIcon = tmpIcon;
		
		btnMap.get(newCaseIdToTravel).revalidate();
		btnMap.get(newCaseIdToTravel).repaint();
		
		return lastCaseIcon;
	}
	
	
	
	private void updateUi(Icon lastCaseIcon) {
		nbFails.setText(String.valueOf(Integer.parseInt(nbFails.getText()) +1));
		// rerender lastCase
		btnMap.get(path.get(path.size()-1)).setIcon(lastCaseIcon);
	}
	
	
	
	public void printWeightMap() {
		System.out.println("new weight : " + weight);
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				System.out.print(weightMap.get(x*size +y) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
	public ArrayList<Integer> getPath() {return path;}
	public void setPath(ArrayList<Integer> path) {this.path = path;}
	public ArrayList<Integer> getWeightMap() {return weightMap;}
	public void setWeightMap(ArrayList<Integer> weightMap) {this.weightMap = weightMap;}
	public int getWeigth() {return weight;}
	public void setWeigth(int weigth) {this.weight = weigth;}
	public ArrayList<Terrain> getTerrains() {return terrains;}
	public void setTerrains(ArrayList<Terrain> terrains) {this.terrains = terrains;}
	public ArrayList<Monster> getMonsters() {return monsters;}
	public void setMonsters(ArrayList<Monster> monsters) {this.monsters = monsters;}
	
}
