package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import utils.ComputerType;
import utils.Coordinates;
import utils.PlayerState;
import utils.ShipDirection;

/**
 * Class that represents the A.I. of a Battleship opponent. According to its {@link ComputerType},
 * its hit choices can be made pseudo-wisely or pseudo-randomly.
 * @author 20027017 & 20031485
 *
 */
public class Computer extends Player implements Serializable{
	//attributes
	private static final long serialVersionUID = 1L;
	private ComputerType difficulty;
	//lista di tutte le coordinate che il Computer può colpire
	private ArrayList<Coordinates> coordinatesList;
	//lista delle prossime coordinate che il Computer colpirà
	private ArrayList<Coordinates> nextHits;
	//coordinate dell'ultimo colpo effettuato
	private Coordinates lastHit;
	//coordinate dell'ultimo colpo andato a segno
	private Coordinates lastSuccessfulHit;
	
	/**
	 * Constructor for the class {@link Computer}.
	 * @param gameSize The size of the Battleship game grid.
	 * @param difficulty The "intelligence" of the {@link Computer}.
	 */
	public Computer(int gameSize, ComputerType difficulty) {
		super(gameSize);
		setName("Computer");
		this.difficulty = difficulty;
		initCoordinatesList();
		nextHits = new ArrayList<>();
		this.lastHit = null;
		this.lastSuccessfulHit = null;
	}

	/**
	 * A method that initializes the list of the coordinates a {@link Computer}
	 * can hit during a Battleship match, according to the size of the game grid.
	 */
	private void initCoordinatesList() {
		coordinatesList = new ArrayList<>();
		for(int i = 0; i < this.getGameSize(); ++i) {
			for(int j = 0; j < this.getGameSize(); ++j) {
				Coordinates coordinates = new Coordinates(i, j);
				coordinatesList.add(coordinates);
			}
		}
	}

	/**
	 * Returns the difficulty set for the {@link Computer}.
	 * @return The difficulty of the {@link Computer}.
	 */
	public ComputerType getDifficulty() {
		return difficulty;
	}

	/**
	 * Returns the ArrayList of the coordinates the {@link Computer}.
	 * can hit.
	 * @return ArrayList of {@link Coordinates}.
	 */
	public ArrayList<Coordinates> getCoordinatesList() {
		return coordinatesList;
	}

	/**
	 * Returns the ArrayList of the possible coordinates the 
	 * {@link Computer} will hit in its next move.
	 * @return ArrayList of {@link Coordinates}.
	 */
	public ArrayList<Coordinates> getNextHits() {
		return nextHits;
	}
	
	/**
	 * Returns the Coordinates of the last hit the {@link Computer} fired.
	 * @return {@link Coordinates} of {@link Computer}'s last hit.
	 */
	public Coordinates getLastHit() {
		return lastHit;
	}

	/**
	 * Utility method that prints the {@link Coordinates}.
	 * of the last hit the {@link Computer} fired.
	 */
	public void printLastHit() {
		System.out.println("lastHit: ["+lastHit.getRow()+", "+lastHit.getColumn()+"]");
	}
	
	/**
	 * Returns the {@link Coordinates} of the last successful hit the
	 * {@link Computer} fired.
	 * @return Coordinates of {@link Computer}'s last successful hit.
	 */
	public Coordinates getLastSuccessfulHit() {
		return lastSuccessfulHit;
	}

	/**
	 * Utility method that prints the {@link Coordinates}
	 * of the last successful hit the {@link Computer} fired.
	 */
	public void printLastSuccessfulHit() {
		System.out.println("lastSuccessfulHit: ["+lastSuccessfulHit.getRow()+", "+lastSuccessfulHit.getColumn()+"]");
	}
	
	/**
	 * Utility method that prints the coordinates a smart {@link Computer} is 
	 * going to aim in its next turns.
	 * 
	 */
	public void printNextHits() {
		System.out.println("\nnextHits: ");
		for(int i = 0; i < nextHits.size(); ++i) {
			System.out.print("["+nextHits.get(i).getRow()+","+nextHits.get(i).getColumn()+"] ");
		}
		System.out.println("\n");
	}
	
	/**
	 * Utility method that prints the coordinates {@link Computer} still has
	 * to hit in its next turns.
	 * 
	 */
	public void printCoordinatesList() {
		System.out.println("\ncoordinatesList: ");
		for(int i = 0; i < coordinatesList.size(); ++i) {
			if(i >= 1 && coordinatesList.get(i-1).getRow() != coordinatesList.get(i).getRow())
				System.out.println();
			System.out.print("["+coordinatesList.get(i).getRow()+","+coordinatesList.get(i).getColumn()+"] ");
			
		}
		System.out.println("\n");
	}
	
	/**
	 * According to the {@link Player}'s state, the {@link Computer}
	 * decides where to aim for its next hit. If the {@link Computer} is
	 * "stupid", it will aim randomly. 
	 * @param state The current {@link Player}'s state.
	 * @return A bidimensional integer array containing the two coordinates
	 * the {@link Computer} has chosen to hit
	 */
	public int[] computerHits(PlayerState state) {
		//istanzio array di coordinate
		int[] coordinates = new int[2];
		switch(this.difficulty) {
			//estrae casualmente due coordinate
			case STUPID:
				coordinates = randomHit();
				break;
				
			//estrae intelligentemente due coordinate in funzione dello stato del giocatore
			case SMART:
				//se il giocatore è stato mancato o se una sua nave è stata affondata, miro a caso
				//se il giocatore è stato colpito, miro vicino alla cella colpita
				coordinates = smartHit(state);
				lastHit = new Coordinates(coordinates[0], coordinates[1]);
				break;
				
			//non dovrebbe mai andare qui
			default:
				System.err.println("ERROR@Computer::computerHits()");
				break;
		}
		//ritorna array bidimensionale con le coordinate estratte
		return coordinates;
	}
	
	/**
	 * Randomly extract a couple of {@link Coordinates} from the list of
	 * coordinates {@link Computer} still has not hit.
	 * @return A bidimensional integer array containing the two coordinates.
	 */
	public int[] randomHit() {
		int[] coordinates = new int[2];
		//crea intero tra 0 e coordinatesList.size()
		Random r = new Random();
		int randomIndex = r.nextInt(coordinatesList.size());
		
		//recupera le coordinate a quell'indice
		coordinates[0] = coordinatesList.get(randomIndex).getRow();
		coordinates[1] = coordinatesList.get(randomIndex).getColumn();
		
		//assegno l'ultimo colpo a lastHits
		lastHit = new Coordinates(coordinates[0], coordinates[1]);
		
		//annullo l'ultimo colpo andato a segno
		lastSuccessfulHit = null;
		
		//rimuove quelle coordinate
		coordinatesList.remove(randomIndex);
		return coordinates;
	}
	
	/**
	 * Only if the {@link Computer} is set as "smart", it chooses
	 * a couple of coordinates more wisely.
	 * @param state The current {@link Player}'s state.
	 * @return A bidimensional integer array containing the coordinates chosen.
	 */
	public int[] smartHit(PlayerState state) {
		//istanzio array di due coordinate
		int[] coordinates = new int[2];
		
		//controllo se il computer aveva colpito allo scorso tiro
		//questo metodo permette di mettere delle coordinate in nextHits, così
		//che il Computer le possa colpire ai prossimi turni
		didComputerHit(state);
		
		//stampo quello che ho in nextHits
		//printNextHits();
		
		//se non ci sono elementi in nextHits
		if(nextHits.size() == 0) {
			//estraggo casualmente una coppia di coordinate e le assegno alle coordinate colpite
			coordinates = randomHit();
			
			//assegno le nuove coordinate a lastHit
			lastHit = new Coordinates(coordinates[0], coordinates[1]);
		}
		//se nextHits ha elementi
		else {
			//colpo random tra nextHits
			Random r = new Random();
			
			//creo un indice casuale tra 0 e la dimensione di nextHits
			int randomIndex = r.nextInt(nextHits.size());
			
			//prendo le coordinate all'indice randomIndex e le metto nell'array coordinates, da restituire
			coordinates = hits(nextHits.get(randomIndex).getRow(), nextHits.get(randomIndex).getColumn());
			
			//tolgo le coordinate scelte dalla lista nextHits
			nextHits.remove(randomIndex);
			
			//assegno le nuove coordinate a lastHit
			lastHit = new Coordinates(coordinates[0], coordinates[1]);
		}
		//restituisco l'array di due coordinate
		return coordinates;
	}
	
	/**
	 * Given a cell's coordinates, the {@link Computer} aims to the 
	 * (at most) four cells vertically and horizontally surrounding 
	 * the chosen cell.
	 * @param row The row coordinate of the chosen cell.
	 * @param col The col coordinate of the chosen cell.
	 */
	public void crossCheck(int row, int col) {
		int i = 0;
		//se ci sono, aggiungo le caselle a nextHits
		while(i < coordinatesList.size()) {
			//se esiste una coppia di coordinate che corrispondono ai parametri di ricerca, la metto in nextHits
			//(dovrebbero essere automaticamente escluse le caselle "fuori dalla griglia" e quelle già scelte
			//se una casella ha riga = row-1 e colonna = col (stessa colonna ma una riga prima)
			if(	(coordinatesList.get(i).getRow() == row - 1 && coordinatesList.get(i).getColumn() == col) ||
				//oppure se ha riga = row+1 e colonna = col (stessa colonna ma una riga dopo)
				(coordinatesList.get(i).getRow() == row + 1 && coordinatesList.get(i).getColumn() == col) ||
				//oppure se ha riga = row e colonna = col-1 (stessa riga ma una colonna prima)
				(coordinatesList.get(i).getRow() == row && coordinatesList.get(i).getColumn() == col - 1) ||
				//oppure se ha riga = row e colonna = col+1 (stessa riga ma una colonna dopo)
				(coordinatesList.get(i).getRow() == row && coordinatesList.get(i).getColumn() == col + 1)) {
		
				//metto la coordinata scelta in nextHits
				nextHits.add(coordinatesList.get(i));
				//rimuovo la coordinata scelta da coordinatesList
				coordinatesList.remove(i);
				--i;
			}
			++i;
		}
		//controllo di aver messo le coordinate in nextHits
		//printNextHits();
	}
	
	/**
	 * Given two couples of aligned {@link Coordinates}, the {@link Computer} 
	 * chooses at most two other cells, aligned to the parameters, to be
	 * aimed to in its next hits.
	 * @param lastHit A {@link Coordinates} object containing the coordinates of the first cell.
	 * @param lastSuccessfulHit A {@link Coordinates} object containing the coordinates of the second cell.
	 * @param direction The alignment of the two cells.
	 */
	public void lineCheck(Coordinates lastHit, Coordinates lastSuccessfulHit, ShipDirection direction) {
		switch(direction) {
			case HORIZONTAL:
				int i = 0;
				while(i < coordinatesList.size()) {
					//se trovo una coordinata sulla stessa riga dei due colpi
					if(		coordinatesList.get(i).getRow() == lastHit.getRow() &&
							coordinatesList.get(i).getRow() == lastSuccessfulHit.getRow()) {
						//cerco nella colonna prima e nella colonna dopo e, se esiste, la aggiungo
						if(		(coordinatesList.get(i).getColumn() == lastHit.getColumn() - 1) ||
								(coordinatesList.get(i).getColumn() == lastHit.getColumn() + 1) ||
								(coordinatesList.get(i).getColumn() == lastSuccessfulHit.getColumn() - 1) ||
								(coordinatesList.get(i).getColumn() == lastSuccessfulHit.getColumn() + 1)) {
							//aggiunge la coordinata cercata a nextHits
							nextHits.add(coordinatesList.get(i));
							
							//rimuove tale coordinata da coordinatesList
							coordinatesList.remove(i);
							--i;
						}
					}	
					++i;
				}
				
				//remove coordinates which are not on the same row
				i = 0;
				while(i < nextHits.size()) {
					if(nextHits.get(i).getRow() != lastHit.getRow() && nextHits.get(i).getRow() != lastSuccessfulHit.getRow()){
						coordinatesList.add(nextHits.get(i));
						nextHits.remove(i);
						--i;
					}
					++i;
				}
				//controllo le coordinate che ho messo in nextHits
				//printNextHits();
				break;
				
			case VERTICAL:
				//cerco tra le coordinate in coordinatesList
				int j = 0;
				while(j < coordinatesList.size()) {
					//se trovo una coordinata sulla stessa colonna dei due colpi
					if(		coordinatesList.get(j).getColumn() == lastHit.getColumn() && 
							coordinatesList.get(j).getColumn() == lastSuccessfulHit.getColumn()) {
						//cerco nella riga prima e nella riga dopo e, se esiste, la aggiungo
						if(		(coordinatesList.get(j).getRow() == lastHit.getRow() - 1) ||
								(coordinatesList.get(j).getRow() == lastHit.getRow() + 1) ||
								(coordinatesList.get(j).getRow() == lastSuccessfulHit.getRow() - 1) ||
								(coordinatesList.get(j).getRow() == lastSuccessfulHit.getRow() + 1)) {
							//aggiunge la coordinata cercata a nextHits
							nextHits.add(coordinatesList.get(j));
							
							//rimuove tale coordinata da coordinatesList
							coordinatesList.remove(j);
							--j;
						}
					}	
					++j;
				}
				//remove coordinates which are not on the same column
				j = 0;
				while(j < nextHits.size()) {
					if(nextHits.get(j).getColumn() != lastHit.getColumn() && nextHits.get(j).getColumn() != lastSuccessfulHit.getColumn()){
						coordinatesList.add(nextHits.get(j));
						nextHits.remove(j);
						--j;
					}
					++j;
				}
				//controllo le coordinate che ho messo in nextHits
				//printNextHits();
				break;
				
			default:
				System.err.println("ERROR@Computer::linearCheck()");
				break;
		}
		//printNextHits();
	}
	
	/**
	 * Empties the list of the next {@link Coordinates} the 
	 * {@link Computer} wants to hit.
	 */
	public void clearNextHits() {
		int i = 0;
		while(i < nextHits.size()) {
			coordinatesList.add(nextHits.get(i));
			nextHits.remove(i);
		}
	}
	
	/**
	 * The {@link Computer} checks if its last hit was successful or not.
	 * according to the state the {@link Player} is in.
	 * @param state The current state of the {@link Player}.
	 */
	public void didComputerHit(PlayerState state) {
		//leggo lo stato del giocatore
		if(state == null)
			state = PlayerState.WATER;
		switch(state){
			//se mancato
			case WATER:
				//per ora non fa niente
				break;
			
			//se colpito
			case HIT:
				//se sto colpendo una nuova nave
				if(lastSuccessfulHit == null) {
					//controllo incrociato attorno alla cella colpita
					crossCheck(lastHit.getRow(), lastHit.getColumn());
				}
				
				//se invece un colpo è andato a segno "di recente"
				else {
					//controllo se le due celle sono sulla stessa riga
					if(lastHit.getRow() == lastSuccessfulHit.getRow()) {
						//controllo in linea - aggiungo a nextHits le celle immediatamente accanto alle due colpite
						lineCheck(lastHit, lastSuccessfulHit, ShipDirection.HORIZONTAL);
					}
					
					//controllo se le due celle sono sulla stessa colonna
					else if(lastHit.getColumn() == lastSuccessfulHit.getColumn()) {
						//controllo in linea- aggiungo a nextHits le celle immediatamente sopra e sotto alle due colpite
						lineCheck(lastHit, lastSuccessfulHit, ShipDirection.VERTICAL);
					}
				}
				
				//ho colpito, quindi ridefinisco lastSuccessfulHit
				lastSuccessfulHit = new Coordinates(lastHit.getRow(), lastHit.getColumn());
				break;
			
			//se colpito e affondato
			case HITANDSUNK:
				//cancello nextHits, torno a mirare a caso
				clearNextHits();
				
				//metto l'ultimo colpo andato a segno a null (cambio bersaglio)
				lastSuccessfulHit = null;
				break;
			
			default:
				System.err.println("ERROR@Computer::didComputerHit()");
				break;
		}
	}
}
