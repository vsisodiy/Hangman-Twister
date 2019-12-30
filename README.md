# Hangman-Twister

Hangman rules:
1. The game displays a clue with number of alphabets between MIN_WORD_LENGTH and MAX_WORD_LENGTH in clueLabels. The player has to guess the letters for missing alphabets in HANGMAN_TRIALS.
2. The player starts Hangman by clicking at Hangman button in opening scene which starts a new round.
3. Hangman view displays a new clue on top. The wordTimer starts from HANGMAN_GAME_TIME counting down to 0.
4. The player can make guesses using keyboardButtons. To start with, keyboardButtons has those buttons disabled that have letters that are part of the clue. As the player clicks on more buttons, they get disabled to indicate that they have been clicked, irrespective of whether they are correct guesses or not.
5. Every right guess shows the image at THUMBS_UP_INDEX in smileyButton and every incorrect guess shows the image at THUMB_DOWN_INDEX. If time runs out, it shows image at SADLY_INDEX, and if user is able to complete the word, it shows the image at SMILEY_INDEX.
6. If the player clicks on New Word button, the game starts a new round.
7. If the timer runs out before the player could guess the word, the entire keyboardGrid gets disabled to indicate that the round is over.
8. If the player clicks on Exit button, the game goes back to opening scene

Twister rules:
1. The challenge in Twister game is to wring out a given number of words from a clue. This number is indicated in topMessageText.
2. The words must be between TWISTER_MIN_WORD_LENGTH and the clue length. So if the clue is 'abcde' then the length of solution words must be between the value of TWISTER_MIN_WORD_LENGTH, which is 3, and 5.
3. The wordLengthLabels indicate the length of words to be found, and wordScoreLabels show the number of possible words of each length based on the words in the source WORDS_FILE_NAME. For example, the example above shows that there are 30 words of length 3, out of which six words have been found by the player. The words found by the player are displayed in solutionListViews in sorted order, as shown.
4. The round starts with a clue displayed in clueButtons and a topMessageText displaying the number of words to be found. It also starts the wordTimer from TWISTER_GAME_TIME, counting down to 0.
5. The letters in clueButtons are to be moved to answerButtons to form a word. The player can click any of the clueButtons. This moves the alphabet from the clicked clueButton to the first free answerButton from the left. The player can keep moving alphabets from clueButtons to answerButtons to form a word.
6. If the player clicks on any of the answerButtons that has an alphabet, then the alphabet moves back up to the first free space available in clueButtons from the left
7. If the player clicks on Twist button, then the game shuffles the letters in clueButtons. This is to help the player look at the letters in different ways.
8. If the player clicks on Clear button, then the game moves the alphabets in answerButtons, if any, to empty slots in clueButtons.
9. When the player clicks on Submit button,
   a. the game takes the word from answerButtons and checks if it is of TWISTER_MIN_WORD_LENGTH. If not, the smileyButton displays the      image at THUMBS_DOWN_INDEX.
   b. If the submitted word's length is at least TWISTER_MIN_WORD_LENGTH, it checks if it is one of the solutionWords (loaded from words    file) that has not been entered before. If yes, then the smileyButton displays the image at THUMBS_UP_INDEX. The game adds the word      to one of the solutionlistViews that has words of submitted word length. The submitted word is displayed in the position of its     
   sorted order. The game also updates the wordScoreLabel on the right of the listView as well as the scoreString at the top in
   topMessageText.
   c. If the submittedWord is correct but was entered before, then smileyButton displays image at REPEAT_INDEX
   d. If it is not a correct word (i.e. not in words file), then the smileyButton displays image at THUMBS_DOWN_INDEX
   e. If the player completes all words in solutionWordList, then smileyButton displays image at SMILEY_INDEX.
10. If the timer runs out, then the smileyButton displays image at SADLY_INDEX
11. If the player clicks on New word, the game starts a new round
12. If the player clicks on Exit button, the game goes back to opening screen.

The ScoreBoard shows line charts for scores earned in the two games. The scores are computed as soon as a round is completed (won or lost) and stored in a .csv file. The scores are computed as:
• Hangman score = number of hits / number of misses
• Twister score = total number of correct words submitted / total number of solution words
