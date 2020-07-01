/*
Jonathan Slack
Assignment 08 Cheating Word Guessing Game
CIS 1068 Summer I 2020
Professor John Fiore, Temple University
 */

import java.util.Random;
import java.io.*;
import java.util.*;
import java.util.ArrayList;

public class hangmanGame{
    public static int hangmanGuessCount; //Maintains count of user's incorrect guesses.

    public static int wordLength(){ // Determines the length of the word for guessing game.
        int RAND_MIN = 2;
        int RAND_MAX = 14;
      Random rnd = new Random();
      int wordlength = rnd.nextInt(RAND_MAX - RAND_MIN)+RAND_MIN;
      return wordlength;
    }

    public static ArrayList<String> wordFile() { //Reads dictionary word text attached to this file
        ArrayList<String> dictionary = new ArrayList<String>();
        try {
            Scanner in = new Scanner(new File("dictionary.txt"));
            while (in.hasNextLine()) {
                dictionary.add(in.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading dictionary.txt");
        }
        return dictionary;
    }

    public static ArrayList<String> trimWordList(ArrayList<String> dictionary){ //trims the list of possible words to those matching the length determined by wordLength()
        int wordLength = wordLength();
        for (int i = 0; i < dictionary.size(); i++) {
            if (dictionary.get(i).length() != wordLength) {
                dictionary.remove(i);
                i--;
            }
        }
        return dictionary;
    }
    
    public static boolean winConditions(int hangmanGuessCount, int MaxGuesses, ArrayList<String> lettersToGuess){ //Sets win conditions for game, called by checkWinLose function
        if(hangmanGuessCount < MaxGuesses && lettersToGuess.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
    
    public static boolean loseConditions(int hangmanGuessCount, int MaxGuesses){ //Sets lose conditions for game, called by checkWinLose function
        if(hangmanGuessCount >= MaxGuesses){
            return true;
        }
        else{
            return false;
        }
    }

    public static void checkWinLose(int hangmanGuessCount, int MaxGuesses, String word, ArrayList<String> lettersToGuess){ //checks win/lose parimeters in functions above. Called by main function before each normal turn. Prints a win/lose messages if parimeters are met and exits program.
        if (winConditions(hangmanGuessCount, MaxGuesses, lettersToGuess) == true){
            System.out.print("\nRejoice! A man has been spared his appointment with the reaper and embraces his family in relief. The word is " + word + ".\n");
            System.exit(0);
        }
        else if (loseConditions(hangmanGuessCount, MaxGuesses) == true){
            System.out.print("\nAs dusk settles on our word game, a widow crys on the arid earth below as a man swings from the gallows. The word was " + word+ ".\n");
            System.exit(0);
        }
        else{}
    }

    public static void correctGuess(String userGuess) { //prints message if user guesses a character correctly. Called by main function.
        System.out.println("\nCorrect! Word does contain " + userGuess + ".");
    }

    public static void incorrectGuess(String userGuess){//prints message if user guess a character incorrectly, and adds to incorrect Guess Count. Called by hangmanCheat and hangmanNormal functions.
        System.out.println("\nSorry! Word does not contain " + userGuess + ".");
        hangmanGuessCount++;
    }

    public static boolean continueCheat(String userGuess, ArrayList<String> dictionary){ //Determines whether program continues to cheat, based on whether it can trim the list of possible words without getting caught. Called by main function.
        int wordsRemainingWithoutLetterGuessed = 0;
        for (int i = 0; i < dictionary.size(); i++) {
            if (dictionary.get(i).contains(userGuess) == false){
                wordsRemainingWithoutLetterGuessed++;
            }

        }
        if(wordsRemainingWithoutLetterGuessed ==0){
            return false;
        }
        else{
            return true;
        }
    }

    public static String letterGuessInput(){ //Allows user to input a guess. Called by main function each turn.
        Scanner input = new Scanner(System.in);
        System.out.println("\nPlease enter a letter: ");
        String userGuess = input.next();
        //input.close();
        return userGuess;
    }
    
    public static ArrayList<String> hangmanCheat(ArrayList<String> dictionary, String userGuess){ //Function used to progress the game before a word is chosen. All words containing user guess are removed from a list of possible words for game. Called by main function.
        for (int i = 0; i < dictionary.size(); i++) {
            if (dictionary.get(i).contains(userGuess)) {
                dictionary.remove(i);
                i--;
            }
        }
        incorrectGuess(userGuess);
        return dictionary;
    }
    
    public static String pickWord(ArrayList<String> dictionary){ // Once continueCheat becomes false, picks a word from remaining possible words at random. Called by main function.
        Random wordIndex = new Random();
        String word = dictionary.get(wordIndex.nextInt(dictionary.size()));
        return word;
    }

    public static ArrayList<String> convertWordToList(String hangmanWord){ //Converts word to List, so that correct guesses may be removed and win conditions may be met. Called by main function.
        String letter_to_add;
        ArrayList<String> lettersToGuess = new ArrayList<String>();
        for(int i = 0; i < hangmanWord.length()-1; i++){
            letter_to_add = hangmanWord.substring(i, i+1);
            lettersToGuess.add(letter_to_add);
        }
        lettersToGuess.add(hangmanWord.substring(hangmanWord.length()-1));
    return lettersToGuess;
    }
    
    public static ArrayList<String> hangmanNormal(String hangmanWord, String userGuess, ArrayList<String> hangmanWordList){//Function used to progress game once a word has been picked. Called by main function
        if (hangmanWord.contains(userGuess)) {
            correctGuess(userGuess);
            removeCorrectGuess(userGuess, hangmanWordList); 
        } else {
            incorrectGuess(userGuess);
        }
        return hangmanWordList;
    }
   
    public static ArrayList<String> removeCorrectGuess(String userGuess, ArrayList<String> hangmanWordList){ //removes correct guesses from hangmanWordList, so that win conditions may be met. Called my hangmanNormal and by main function.
        for(int i = 0; i < hangmanWordList.size(); i++){
            hangmanWordList.remove(userGuess);
        }
        return hangmanWordList;
    }
    public static void main(String args[]){
        int maxGuesses = 12; //maximum incorrect guesses.
        boolean continueCheat = true; //games start with computer cheating by default.
        String correctGuess = "";
        ArrayList<String> dictionary = trimWordList(wordFile());
        System.out.print("A man's life is at stake. He will live another day if you are able to guess all the letters making up a hidden word.\nThe word is " + dictionary.get(0).length() + " letters long. If you guess incorrectly "+ maxGuesses + " times, the man's punishment will commence.\n");
        for(int i = 0; i < maxGuesses; i++){//loop breaks once the user runs out of guesses, or when no more cheating guesses can be made.
            if (continueCheat == true){ //Once continueCheat becomes false, rest of function becomes unavailable and program moves on.
            System.out.print((maxGuesses - hangmanGuessCount) + " guesses remaining.\n");
            String userGuess = letterGuessInput();
                if (continueCheat(userGuess, dictionary) == true) {
                dictionary = hangmanCheat(dictionary, userGuess); 
                 }
                else{
                    continueCheat = false;
                    correctGuess(userGuess);
                    correctGuess = userGuess;//saved so that first correct guess is removed from word once word is chosen by pickword function.
                } 
            }
        }     
        String hangmanWord = pickWord(dictionary);
        ArrayList<String> hangmanWordList= convertWordToList(hangmanWord);
        checkWinLose(hangmanGuessCount, maxGuesses, hangmanWord, hangmanWordList); //first win/lose condition check, as user has either broken cheat loop or run out of guesses. If user has first run out of guesses, a word is first picked and presented as the word all along.
        removeCorrectGuess(correctGuess, hangmanWordList);
        for(int i = 0; i < maxGuesses+hangmanWord.length(); i++){//loop continues until win/lose conditions are met and program exits.
            checkWinLose(hangmanGuessCount, maxGuesses, hangmanWord, hangmanWordList);
            System.out.print((maxGuesses - hangmanGuessCount) + " guesses remaining.\n");
            String userGuess = letterGuessInput();
            hangmanNormal(hangmanWord, userGuess, hangmanWordList);
            removeCorrectGuess(userGuess, hangmanWordList);
        }
    }
}