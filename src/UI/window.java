package UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import AI.Dijkstra;
import DTO.Dungeon;
import DTO.RessourcesFull;
import beans.CreateApiRequest;
import beans.Monster;
import beans.Stuff;
import beans.Terrain;



public class window {

	private JFrame frame;
	
	private JPanel rightPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel leftPanel = new JPanel();
	private JPanel infosPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	private JPanel centerTopPanel = new JPanel();
	private JPanel centerBottomPanel = new JPanel();
	//disabled
	private JPanel legendPanel = new JPanel();

	private JButton previousDungeonBtn = new JButton("<");
	private JButton nextDungeonBtn = new JButton(">");

	private ArrayList<JButton> dungeonMapButtons = new ArrayList<>();
	private int mapWidth = 10, mapHeight = 10, currentDungeonId = 0;

	private JTextField dungeonNameTf;
	private JTextField dungeonIndex;
	private JTextField creatorTxtValue;
	private JTextField timeFailedTxtValue;
	private JTextField difficultyTxtValue;
	private JTextField errorTextField;
	
	//datas
	private ImageIcon imgNotFound = new ImageIcon("ressources/notFound.png");
	private ArrayList<ImageIcon> terrainIcon = new ArrayList<>();
	private ArrayList<ImageIcon> monsterIcon = new ArrayList<>();
	private ArrayList<ImageIcon> otherIcon = new ArrayList<>();
	private ArrayList<Dungeon> dungeonList = new ArrayList<>();

	private ArrayList<Integer> terrainIds = new ArrayList<>();
	private ArrayList<Integer> monsterIds = new ArrayList<>();
	private ArrayList<Integer> otherIds = new ArrayList<>();
	
	private Dungeon currentDungeon;
	
	//DTO
	private RessourcesFull ressources;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window window = new window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public window() {
		initialize();
		loadDatas();
		installMap();
	}
	
	private void simulate() {
		if(ressources == null) {
			return;
		}
		int entranceTerrainId = 1; //default wall id is 1
		for(Terrain t : ressources.getTerrains()) {
			if(t.getName().equals("entrance")) {
				entranceTerrainId = t.getId();
				break;
			}
		}
		int entranceCaseId = -1;
		for(int i = 0; i < currentDungeon.getCases().size(); i++) {
			if(currentDungeon.getCases().get(i) == entranceTerrainId) {
				entranceCaseId = i;
				break;
			}
		}
		int throneTerrainId = 3; //default wall id is 1
		for(Terrain t : ressources.getTerrains()) {
			if(t.getName().equals("throne")) {
				throneTerrainId = t.getId();
				break;
			}
		}
		if(entranceCaseId == -1)
			return;
		
		//create AI and start
		new Dijkstra(
			entranceCaseId,
			throneTerrainId,
			currentDungeon.getSize(),
			(ArrayList<Integer>) currentDungeon.getStuff(),
			(ArrayList<Integer>) currentDungeon.getCases(),
			dungeonMapButtons,
			(ArrayList<Terrain>) ressources.getTerrains(),
			(ArrayList<Monster>) ressources.getMonsters(),
			(ArrayList<Stuff>) ressources.getStuffs(),
			timeFailedTxtValue,
			errorTextField,
			currentDungeon.getId()
		);
	}
	
	private void changeDungeon(int change) {
		if(dungeonList.size() <= 0)
			return;
		currentDungeonId += change;
		if(currentDungeonId < 0) currentDungeonId = 0;
		if(currentDungeonId > dungeonList.size()-1) currentDungeonId = dungeonList.size() -1;

		currentDungeon = dungeonList.get(currentDungeonId);
		int dungeonSize = currentDungeon.getSize();
		if(dungeonSize > mapWidth || dungeonSize > mapHeight)
			return;

		List<Integer> map = currentDungeon.getCases();
		for(int x = 0; x < dungeonSize; x++) {
			for(int y = 0; y < dungeonSize; y++) {
				int placable = map.get(x * dungeonSize + y);
				JButton btn = dungeonMapButtons.get(x * dungeonSize + y);
				if(placable < 1000) {
					btn.setIcon(terrainIds.contains(placable) ? terrainIcon.get(terrainIds.indexOf(placable)) : imgNotFound);
				}else if(placable < 3000) {
					btn.setIcon(monsterIds.contains(placable) ? monsterIcon.get(monsterIds.indexOf(placable)) : imgNotFound);
				}else {
					btn.setIcon(otherIds.contains(placable) ? otherIcon.get(otherIds.indexOf(placable)) : imgNotFound);
				}
			}
		}
		
		//update dungeon labels
		dungeonNameTf.setText(currentDungeon.getName());
		creatorTxtValue.setText(currentDungeon.getCreatorId().toString());
		timeFailedTxtValue.setText(currentDungeon.getTimeFailed().toString());
		difficultyTxtValue.setText(currentDungeon.getDifficulty().toString());
		
		//repaint
		centerPanel.revalidate();
		centerPanel.repaint();
	}

	private boolean loadDatas() {
		try {
			// GET ressources
			HttpURLConnection con = new CreateApiRequest("GET", "http://localhost:8080/ressources", new HashMap<>()).getCon();
			int status = con.getResponseCode();
			if(status != 200) {
				errorTextField.setText("Cannot get ressources");
				return false;
			}
			InputStream iStream = con.getInputStream();
			InputStreamReader reader = new InputStreamReader(iStream);
			BufferedReader in = new BufferedReader(reader);
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			}
			in.close();
			con.disconnect();
			ObjectMapper objectMapper = new ObjectMapper();
			ressources = objectMapper.readValue(content.toString(), RessourcesFull.class);

			// GET dungeons
			HttpURLConnection con2 = new CreateApiRequest("GET", "http://localhost:8080/dungeons/", new HashMap<>()).getCon();
			int status2 = con2.getResponseCode();
			if(status2 < 200 || status2 >= 300) {
				errorTextField.setText("Cannot get dungeons");
				return false;
			}
			InputStream iStream2 = con2.getInputStream();
			InputStreamReader reader2 = new InputStreamReader(iStream2);
			BufferedReader in2 = new BufferedReader(reader2);
			String inputLineDungeons;
			StringBuffer contentDungeons = new StringBuffer();
			while ((inputLineDungeons = in2.readLine()) != null) {
				contentDungeons.append(inputLineDungeons);
			}
			in2.close();
			con2.disconnect();
			dungeonList = objectMapper.readValue(contentDungeons.toString(), new TypeReference<List<Dungeon>>(){});
			
		} catch (Exception e) {
			errorTextField.setText("API not responding");
			return false;
		}
		
		ArrayList<Integer> ids = ressources.getAllPlacables();
		for(int i = 0; i < ids.size(); i++) {
			String path = "ressources/icon" + ids.get(i) + ".png";
			File f = new File(path);
			if(ids.get(i) < 1000) {
				terrainIds.add(ids.get(i));
				terrainIcon.add(f.exists() ? new ImageIcon(path) : imgNotFound);
			}else if(ids.get(i) < 3000) {
				monsterIds.add(ids.get(i));
				monsterIcon.add(f.exists() ? new ImageIcon(path) : imgNotFound);
			}else {
				otherIds.add(ids.get(i));
				otherIcon.add(f.exists() ? new ImageIcon(path) : imgNotFound);
			}
		}
		
		return true;
	}
	
	private void installMap() {
		//Load walls, to be sure nothing wil happen
		ImageIcon img = new ImageIcon("ressources/icon1.png");
		for(int x = 0; x < mapHeight; x++) {
			for(int y = 0; y < mapWidth; y++) {
				JButton btn = new JButton("", img);
				btn.setActionCommand("0");
				dungeonMapButtons.add(btn);
				centerPanel.add(btn);
			}
		}
		changeDungeon(0);
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// INIT Frame
		frame = new JFrame();
		frame.setBounds(100, 100, 1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		leftPanel.setBounds(0, 0, 248, 681);
		mainPanel.add(leftPanel);
		leftPanel.setLayout(null);
		
		infosPanel.setBounds(1, 0, 245, 340);
		leftPanel.add(infosPanel);
		infosPanel.setLayout(null);
		
		JTextField creatorTxt = new JTextField();
		creatorTxt.setText("Creator");
		creatorTxt.setEditable(false);
		creatorTxt.setColumns(10);
		creatorTxt.setBounds(0, 65, 100, 20);
		infosPanel.add(creatorTxt);
		
		JTextField timeFailedTxt = new JTextField();
		timeFailedTxt.setText("Time failed");
		timeFailedTxt.setEditable(false);
		timeFailedTxt.setColumns(10);
		timeFailedTxt.setBounds(0, 90, 100, 20);
		infosPanel.add(timeFailedTxt);
		
		JTextField difficultyTxt = new JTextField();
		difficultyTxt.setText("Difficulty");
		difficultyTxt.setEditable(false);
		difficultyTxt.setColumns(10);
		difficultyTxt.setBounds(0, 115, 100, 20);
		infosPanel.add(difficultyTxt);
		
//		JTextField txtStuff = new JTextField();
//		txtStuff.setText("Stuff");
//		txtStuff.setEditable(false);
//		txtStuff.setColumns(10);
//		txtStuff.setBounds(0, 140, 100, 20);
//		infosPanel.add(txtStuff);
		
		creatorTxtValue = new JTextField();
		creatorTxtValue.setEditable(false);
		creatorTxtValue.setColumns(10);
		creatorTxtValue.setBounds(105, 65, 100, 20);
		infosPanel.add(creatorTxtValue);
		
		timeFailedTxtValue = new JTextField();
		timeFailedTxtValue.setEditable(false);
		timeFailedTxtValue.setColumns(10);
		timeFailedTxtValue.setBounds(105, 90, 100, 20);
		infosPanel.add(timeFailedTxtValue);
		
		difficultyTxtValue = new JTextField();
		difficultyTxtValue.setEditable(false);
		difficultyTxtValue.setColumns(10);
		difficultyTxtValue.setBounds(105, 115, 100, 20);
		infosPanel.add(difficultyTxtValue);
		
//		stuffsTxtValue = new JTextField();
//		stuffsTxtValue.setEditable(false);
//		stuffsTxtValue.setColumns(10);
//		stuffsTxtValue.setBounds(105, 140, 100, 20);
//		infosPanel.add(stuffsTxtValue);
		
		
		
		// Panel Center
		centerPanel.setBounds(344, 72, 550, 550);
		centerPanel.setLayout(new GridLayout(mapWidth, mapHeight, 0, 0));
		
		
		
		
		// Panel Center TOP
		centerTopPanel.setBounds(262, 0, 750, 60);
		mainPanel.add(centerTopPanel);
		centerTopPanel.setLayout(null);
		
		dungeonNameTf = new JTextField();
		dungeonNameTf.setText("Dungeon's name");
		dungeonNameTf.setBounds(0, 0, 750, 60);
		centerTopPanel.add(dungeonNameTf);
		dungeonNameTf.setColumns(10);
		dungeonNameTf.setEditable(false);
		
		
		mainPanel.add(centerPanel);
		
		
		
		// Panel Center Bottom
		centerBottomPanel.setBounds(262, 635, 750, 35);
		mainPanel.add(centerBottomPanel);
		centerBottomPanel.setLayout(null);
		
		previousDungeonBtn.setBounds(270, 0, 45, 35);
		previousDungeonBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeDungeon(-1);
			}
		});
		centerBottomPanel.add(previousDungeonBtn);
		
		JButton btnSaveLevel = new JButton("Send AI");
		btnSaveLevel.setBounds(325, 0, 100, 35);
		btnSaveLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulate();
			}
		});
		centerBottomPanel.add(btnSaveLevel);
		
		nextDungeonBtn.setBounds(435, 0, 45, 35);
		nextDungeonBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeDungeon(1);
			}
		});
		centerBottomPanel.add(nextDungeonBtn);
		

		errorTextField = new JTextField();
		errorTextField.setText("");
		errorTextField.setEditable(false);
		errorTextField.setColumns(10);
		errorTextField.setBounds(486, 0, 260, 35);
		centerBottomPanel.add(errorTextField);
		
		
		
		
		
		// Panel Right
		// Legend Panel
		rightPanel.setBounds(1028, 0, 235, 681);
		mainPanel.add(rightPanel);
		rightPanel.setLayout(null);
		

		
		

	}
	

}
