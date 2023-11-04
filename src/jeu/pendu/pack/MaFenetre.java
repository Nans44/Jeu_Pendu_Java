package jeu.pendu.pack;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class MaFenetre extends JFrame {

	private JLabel motAffiche; //permet d'écrire le mot
	private JLabel tentativesLabel; //permet d'écrire le nombre de tentatives restantes
	private JTextField saisie; //permet d'écrire la saisie
	private JButton entree; //permet de soumettre une proposition de lettre
	
	private String motADeviner;
	private char[] motCache; //creation d'un tableau de char qui va contenir le mot caché
	private int tentativesRestantes = 7; //nombre de tentatives restantes
	private boolean finDuJeu =false;
	private List<String> motsPossibles;
	
	//Constructeur
	public MaFenetre(){
		super("Jeu du Pendu");//titre de la fenetre
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);//fermer le processus à la fermeture de la fenetre
		this.setSize(600, 400);//taille de la fenetre
		this.setLocationRelativeTo(null);//centrer la fenetre
		
		JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new GridLayout(4,1)); //creation d'un layout en grille de 4 lignes et 1 colonne
		
		motAffiche = new JLabel();
		motAffiche.setFont(new Font("SANS_SERIF", Font.PLAIN, 24));
		motAffiche.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(motAffiche); 
		
		tentativesLabel = new JLabel("Tentatives restantes : "+tentativesRestantes, SwingConstants.CENTER);
		contentPane.add(tentativesLabel);
		
		saisie = new JTextField();
		contentPane.add(saisie);
		
		entree = new JButton("Soumettre la proposition");
		entree.setBackground(Color.LIGHT_GRAY);
		contentPane.add(entree);
		
		initMot();
		
		entree.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!finDuJeu) {
					devinerLettre();
				}
				
			}
		});		
	}
	
	//fonction qui va servir à initialiser le motCache et afficher les '_'
	private void initMot() {
		//choix d'un mot au hasard dans le fichier texte noms_pendu.txt
		motsPossibles = lireMotsDepuisFichier("noms_pendu.txt");
		Random motRandom = new Random();
		int indiceMotAleatoire = motRandom.nextInt(motsPossibles.size());
		motADeviner = motsPossibles.get(indiceMotAleatoire);
		
		//on initialise motCache comme un tableau de char de la taille du mot à deviner
		motCache = new char[motADeviner.length()];
		for(int i = 0; i<motADeviner.length();i++) {
			motCache[i] = '_';
		}
		afficherMotCache();
	}
	
	//Fonction qui lit un fichier texte et retourne une liste de String
	public static List<String>lireMotsDepuisFichier(String nomFichier){
		List<String> listeMots = new ArrayList<>();
		
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                listeMots.add(ligne);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listeMots;
	}
	
	//permet d'afficher le mot
	private void afficherMotCache() {
		StringBuffer motVide = new StringBuffer();
		for(char c : motCache) {
			//on ajoute chaque lettre c au StringBuffer
			motVide.append(c).append(" ");
		}
		
		//affiche sur le JLabel motAffiche le mot vide
		motAffiche.setText(motVide.toString());
	}
	
	
	private void devinerLettre() {
		String lettre = saisie.getText(); //lettre String car getText retourne un String
		boolean flagCorrect = false;
		//la lettre doit être un caractère, donc un String de longueur 1
		if(lettre.length() == 1) {
			//on a bien un caractère
			char lettreChar = lettre.charAt(0);
			
			for(int i = 0; i < motADeviner.length(); i++) {
				//si la lettre est dans le mot et qu'elle n'a pas encore été devinée
				if(motADeviner.charAt(i) == lettreChar && motCache[i] == '_') {
					motCache[i] = lettreChar;
					flagCorrect = true;
				}
			}
			if(flagCorrect == false) {
				tentativesRestantes--;
			}
			//MAJ du nombre de tentatives restantes
			tentativesLabel.setText("Tentatives restantes : " + tentativesRestantes);
			afficherMotCache();
			
			//si le mot est complet :
			if(motADeviner.equals(String.valueOf(motCache))) {
				finDuJeu = true;
				JOptionPane.showMessageDialog(this, "Bravo, vous avez trouvé le bon mot !");
			}
			//s'il ne reste plus aucune tentative
			else if (tentativesRestantes == 0){
				finDuJeu = true;
				JOptionPane.showMessageDialog(this, "Perdu, le mot était "+motADeviner);
			}
			
		}
		saisie.setText("");
	}
	
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(new NimbusLookAndFeel()); //on applique le look Nimbus à la fenêtre
		
		
		MaFenetre maFenetre = new MaFenetre();
		maFenetre.setVisible(true);

	}

}
