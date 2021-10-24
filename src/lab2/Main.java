package lab2;

import java.util.*;
import java.io.*;
import java.lang.*;
public class Main {

	public class Transition {
		public int to;
		public char edge;

		public Transition(int to, char edge) {
			this.to = to;
			this.edge = edge;
		}
	}

	private ArrayList<Transition>[] automata;
	private int[] terminationStates;
	private int s0;

	public static void main(String[] args) {
		new Main().run();
	}

	public void run() {
		try {
			readAutomata("/Users/whisper-/Documents/java/lab2/src/files/file.txt");

			String[] words = readW0("/Users/whisper-/Documents/java/lab2/src/files/w0.txt");
			for(String item : words) {
				System.out.println("Answer for " + item + " is " + solve(item));
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
	}

	public boolean solve(String word) {
		int length = automata.length;
		for(int state = 0; state < length; ++state) {
			if(!isReachable(s0, state, new boolean[length])) { continue; }

			int st1 = getStateAfterW0(word, state);
			for(int terminationState : terminationStates) {
				if(isReachable(st1, terminationState, new boolean[length])) {
					return true;
				}
			}
		}

		

		return false;
	}
	
	public int getStateAfterW0(String word, int state) {
		if(word.length() == 0) {
			return state;
		}
		char letter = word.charAt(0);
		word = word.substring(1, word.length());
		for(Transition transition : automata[state]) {
			if(transition.edge == letter) {
				return getStateAfterW0(word, transition.to);
			}
		}

		return -1;
	}

	private boolean isReachable(int start, int stop, boolean[] visited) {
		if(start == stop) {
			return true;
		}
		if(start < 0 || start > visited.length) {
			return false;
		}

		visited[start] = true;

		for(Transition transition : automata[start]) {
			if(!visited[transition.to]) {
				if(isReachable(transition.to, stop, visited)) { return true; }
			}
		}
		return false;
	}

	public String[] readW0(String filepath) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(filepath));

		int total = scanner.nextInt();
		String[] words = new String[total];
		for(int i = 0; i < total; ++i) {
			words[i] = scanner.next();
		}
		return words;
	}

	public void readAutomata(String filepath) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(filepath));

		int a = scanner.nextInt();

		int s = scanner.nextInt();
		automata = new ArrayList[s];
		for(int i = 0; i < s; ++i) {
			automata[i] = new ArrayList<Transition>();
		}

		s0 = scanner.nextInt();

		int f = scanner.nextInt();
		terminationStates = new int[f];
		for(int i = 0; i < f; ++i) {
			terminationStates[i] = scanner.nextInt();
		}

		while(scanner.hasNext()) {
			int from = scanner.nextInt();
			char edge = scanner.next().charAt(0);
			int to = scanner.nextInt();

			automata[from].add(new Transition(to, edge));
		}
	}
}