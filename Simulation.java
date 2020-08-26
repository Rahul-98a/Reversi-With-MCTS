import java.lang.reflect.Array;
import java.util.*;


/**
 * Java implementation of Reversi with Monte-Carlo Tree Search
 */

public class Simulation
{
  // User for input
  static Scanner sc = new Scanner(System.in);

  // Game board and state represented using map
  //static HashMap<Integer, Object> board = new HashMap<Integer, Object>();
  static String[] board= new String[64];
  
  // Defining players, X will always be human, O will always be AI
  static String ai = "O";        // MCTS
  static String human = "X";     // Heuristic MCTS
  static String blank = " ";
  
  

  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * Initialize board pieces to null, except middle 4 squares, which will be
   * either black or white
   */
  public static String[] initialize_board()
  {
    for (int i = 0; i < 8 * 8; i++)
    {
      board[i] = null;
      if (i == 27 || i == 36)
      {
        board[i] = human;
      }
      else if (i == 28 || i == 35)
      {
        board[i] = ai;
      }
      else
      {
        board[i] = blank;
      }
    }

    return board;
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  
  
  
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * Get all the valid moves for the current board instance
   */
  public static ArrayList<Integer> get_valid_moves(String[] current_board, String current_player)
  {
    String opposing_player;
    if (current_player == human)
    {
      opposing_player = ai;
    }
    else
    {
      opposing_player = human;
    }

    // List of current_players tiles
    ArrayList<Integer> current_tiles = new ArrayList<Integer>();
    ArrayList<Integer> possible_moves = new ArrayList<Integer>();

    // Get all positions for the current player
    for (int i = 0; i < 64; i++)
    {
      if (current_board[i] == current_player)
      {
        current_tiles.add(i);
      }
    }

    // Get the number of currently placed tiles to iterate over
    int size = current_tiles.size();

    /*
     * Check if there are possible vertical placements
     */
    for (int i = 0; i < size; i++)
    {
      int tile = current_tiles.get(i);

      // Check above placements
      int x = 1;
      int num = (tile - (8*x));
      boolean valid = true;
      try {
        if (num >= 0 && num <= 63) {
          if (current_board[num] == opposing_player && num >= 0 && valid)
          {
            while (current_board[num] == opposing_player && num >= 0 && valid)
            {
              x += 1;
              num = (tile - (8*x));
              if (num < 0 && num > 63)
              {
                valid = false;
              }
            }
            if(current_board[num] == blank && num >= 0 && valid) {
              possible_moves.add(num);
            }
          }
        }
      }
      catch(Exception e){
      }

      // Check below placements
      x = 1;
      num = (tile + (8*x));
      valid = true;
      try {
        if (num >= 0 && num <= 63) {
          if (current_board[num] == opposing_player && num >= 0 && valid)
          {
            while (current_board[num] == opposing_player && num >= 0 && valid)
            {
              x += 1;
              num = (tile + (8*x));
              if (num < 0 && num > 63)
              {
                valid = false;
              }
            }
            if(current_board[num] == blank && num >= 0 && valid) {
              possible_moves.add(num);
            }
          }
        }
      }
      catch (Exception e)
      {
      }
    }

    /*
     * Check if there are possible horizontal placements
     */
    for (int i = 0; i < size; i++)
    {
      int tile = current_tiles.get(i);
      int current_position = tile % 8;
      int tiles_to_right = 7 - current_position;
      int tiles_to_left = 7 - tiles_to_right;

      // Check placements to the left
      int x = 1;
      int count = 0;
      int num = tile - (1*x);
      boolean valid = true;
      try
      {
        if (num >= 0 && num <= 63)
        {
          if (current_board[num] == opposing_player && count < tiles_to_left && valid)
          {
            while (current_board[num] == opposing_player && count < tiles_to_left && valid)
            {
              x += 1;
              count += 1;
              num = tile - (1*x);
              if(num < 0 || num > 63)
              {
                valid = false;
              }
            }
            if(current_board[num] == blank && count < tiles_to_left && valid) {
              possible_moves.add(num);
            }
          }
        }
      }
      catch (Exception e){
        
      }

      // Check placements to the right
      x = 1;
      count = 0;
      num = tile + (1*x);
      valid = true;
      try {
        if (num >= 0 && num <= 63)
        {
          if (current_board[num] == opposing_player && count < tiles_to_right && valid)
          {
            while (current_board[num] == opposing_player && count < tiles_to_right && valid)
            {
              x += 1;
              count += 1;
              num = tile + (1*x);
              if(num < 0 || num > 63)
              {
                valid = false;
              }
            }
            if(current_board[num] == blank && count < tiles_to_right && valid) {
              possible_moves.add(num);
            }
          }
        }
      }
      catch (Exception e)
      {
        
      }
    }

    /*
     * Check if there are possible diagonal placements from left to right
     */
    for (int i = 0; i < size; i++)
    {
      int tile = current_tiles.get(i);
      int current_position = tile % 8;
      int tiles_to_right = 7 - current_position;
      int tiles_to_left = 7 - tiles_to_right;

      // Check diagonal from above
      int x = 1;
      int count = 0;
      int num = tile - (9*x);
      boolean valid = true;
      try {
        if(num >= 0 && num <= 63)
        {
          if (current_board[num] == opposing_player && count < tiles_to_left && valid)
          {
            while (current_board[num] == opposing_player && count < tiles_to_left && valid)
            {
              x += 1;
              count += 1;
              num = tile - (9*x);
              if(num < 0 || num > 63) {
                valid = false;
              }
            }
            if(current_board[num] == blank && count < tiles_to_left && valid) {
              possible_moves.add(num);
            }
          }
        }
      }
      catch (Exception e)
      {
        
      }


      // Check diagonal from below
      x = 1;
      count = 0;
      num = tile + (9*x);
      valid = true;
      try
      {
        if(num >= 0 && num <= 63) 
        {
          if (current_board[num] == opposing_player && count < tiles_to_right && valid)
          {
            while (current_board[num] == opposing_player && count < tiles_to_right && valid)
            {
              x += 1;
              count += 1;
              num = tile + (9*x);
              if(num < 0 || num > 63) {
                valid = false;
              }
            }
            if(current_board[num] == blank && count < tiles_to_right && valid) {
              possible_moves.add(num);
            }
          }
        }
      }
      catch (Exception e)
      {
        
      }

    }

    /* 
     * Check if there are possible diagonal placements from right to left
     */
    for (int i = 0; i < size; i++)
    {
      int tile = current_tiles.get(i);
      int current_position = tile % 8;
      int tiles_to_right = 7 - current_position;
      int tiles_to_left = 7 - tiles_to_right;

      // Check diagonal from above
      int x = 1;
      int count = 0;
      int num = tile - (7 * x);
      boolean valid = true;
      try {
        if(num >= 0 && num <= 63) 
        {
          if (current_board[num] == opposing_player && count < tiles_to_right && valid)
          {
            while (current_board[num] == opposing_player && count < tiles_to_right && valid)
            {
              x += 1;
              count += 1;
              num = tile - (7 * x);
              if(num < 0 || num > 63)
              {
                valid = false;
              }
            }
            if(current_board[num] == blank && count < tiles_to_right && valid) {
              possible_moves.add(num);
            }
          }
        }
      }
      catch (Exception e)
      {
        
      }


      // Check diagonal from below
      x = 1;
      count = 0;
      num = tile + (7 * x);
      valid = true;
      try
      {
        if(num >= 0 && num <= 63) 
        {
          if (current_board[num] == opposing_player && count < tiles_to_left && valid)
          {
            while (current_board[num] == opposing_player && count < tiles_to_left && valid)
            {
              x += 1;
              count += 1;
              num = tile + (7 * x);
              if(num < 0 || num > 63)
              {
                valid = false;
              }
            }
            if(current_board[num] == blank && count < tiles_to_left && valid) {
              possible_moves.add(num);
            }
          }
        }
      }
      catch (Exception e)
      {
        
      }
    }

    // Return the possible moves
    possible_moves.removeAll(current_tiles); // current tiles should not be valid moves
    Collections.sort(possible_moves); // sort from least to greatest
    LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(possible_moves); //remove duplicates
    ArrayList<Integer> possible_moves_without_duplicates = new ArrayList<>(hashSet);
    // Remove elements out of range (probably no such element, but just in case)
    Iterator<Integer> itr = possible_moves_without_duplicates.iterator(); 
    while (itr.hasNext()) 
    { 
        int x = (Integer)itr.next(); 
        if (x < 0) 
            itr.remove(); 
        if (x > 63) 
          itr.remove(); 
    } 
   
    return possible_moves_without_duplicates;
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


  
  

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * Prints the given board
   */
  public static void print_board(String[] current_board)
  {
    System.out.print(" ---------------------------------");
    System.out.println();

    for (int i = 0; i < 64; i++)
    {
      if ((i + 1) % 8 == 0)
      {
        System.out.print(current_board[i] + " | ");
        System.out.println();
        System.out.print(" ---------------------------------");
        System.out.println();
      }
      else if (i % 8 == 0)
      {
        System.out.print(" | " + current_board[i] + " | ");
      }
      else
      {
        System.out.print(current_board[i] + " | ");
      }
    }

  }
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  

  

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * The player has selected a move to make, flip the appropriate tiles
   */
  private static String[] make_move(String current_player, String[] current_board, int tile)
  {
    // Set who the opposing player is
    String opposing_player;
    if (current_player == human)
    {
      opposing_player = ai;
    }
    else
    {
      opposing_player = human;
    }

    // Keep track of which files to flip, separate lists to avoid cascading tile flips
    ArrayList<Integer> final_tiles_to_flip = new ArrayList<Integer>();
    ArrayList<Integer> vertical_above_tiles_to_flip = new ArrayList<Integer>();
    ArrayList<Integer> vertical_below_tiles_to_flip = new ArrayList<Integer>();
    ArrayList<Integer> horizontal_left_tiles_to_flip = new ArrayList<Integer>();
    ArrayList<Integer> horizontal_right_tiles_to_flip = new ArrayList<Integer>();
    ArrayList<Integer> diagonal_left_right_above_tiles_to_flip = new ArrayList<Integer>();
    ArrayList<Integer> diagonal_left_right_below_tiles_to_flip = new ArrayList<Integer>();
    ArrayList<Integer> diagonal_right_left_above_tiles_to_flip = new ArrayList<Integer>();
    ArrayList<Integer> diagonal_right_left_below_tiles_to_flip = new ArrayList<Integer>();

    // Flip the tile the user chose
   // board.replace(tile, current_player);
    current_board[tile] = current_player;
    
    /*
     * Check if there are tiles to flip on the vertical (above)
     */
    String current_tile = opposing_player;
    int x = 1;
    while(current_tile != current_player)
    {
      int tile_spot = tile - (8 * x);
      if(tile_spot >= 0 && tile_spot <= 63)
      {
        current_tile = (String) current_board[tile_spot];
        if (current_tile == opposing_player) {
          vertical_above_tiles_to_flip.add(tile_spot);
          x += 1;
        }
        else if (current_tile == current_player){
          current_tile = current_player;
        }
        else {
          vertical_above_tiles_to_flip.clear();
          current_tile = current_player;
        }
      }
      else {
        vertical_above_tiles_to_flip.clear();
        current_tile = current_player;
      }
    }    
    
    /*
     * Check if there are tiles to flip on the vertical (below)
     */
    current_tile = opposing_player;
    x = 1;
    while(current_tile != current_player)
    {
      int tile_spot = tile + (8 * x);
      if(tile_spot >= 0 && tile_spot <= 63)
      {
        current_tile = (String) current_board[tile_spot];
        if (current_tile == opposing_player) {
          vertical_below_tiles_to_flip.add(tile_spot);
          x += 1;
        }
        else if (current_tile == current_player){
          current_tile = current_player;
        }
        else {
          vertical_below_tiles_to_flip.clear();
          current_tile = current_player;
        }
      }
      else
      {
        vertical_below_tiles_to_flip.clear();
        current_tile = current_player;

      }
    }    
    
    /*
     * Check if there are tiles to flip on the horizontal to the right
     */
    current_tile = opposing_player;
    x = 1;
    int count = 0;
    while(current_tile != current_player)
    {
      int tile_spot = tile + (1 * x);
      if(tile_spot >= 0 && tile_spot <= 63)
      {
        current_tile = (String) current_board[tile_spot];
        int current_position = tile % 8;
        int tiles_to_right = 7 - current_position;

        if (current_tile == opposing_player && count < tiles_to_right) {
          horizontal_right_tiles_to_flip.add(tile_spot);
          x += 1;
          count += 1;
        }
        else if (current_tile == current_player && count < tiles_to_right){
          current_tile = current_player;
        }
        else {
          horizontal_right_tiles_to_flip.clear();
          current_tile = current_player;
        }
      }
      else {
        horizontal_right_tiles_to_flip.clear();
        current_tile = current_player;
      }

    }    
    
    /*
     * Check if there are tiles to flip on the horizontal to the left
     */
    current_tile = opposing_player;
    x = 1;
    count = 0;
    while(current_tile != current_player)
    {
      int tile_spot = tile - (1 * x);
      if(tile_spot >= 0 && tile_spot <= 63)
      {
        current_tile = (String) current_board[tile_spot];
        int current_position = tile % 8;
        int tiles_to_right = 7 - current_position;
        int tiles_to_left = 7 - tiles_to_right;

        if (current_tile == opposing_player && count < tiles_to_left) {
          horizontal_left_tiles_to_flip.add(tile_spot);
          x += 1;
          count += 1;
        }
        else if (current_tile == current_player && count < tiles_to_left){
          current_tile = current_player;
        }
        else {
          horizontal_left_tiles_to_flip.clear();
          current_tile = current_player;
        }
      }
      else
      {
        horizontal_left_tiles_to_flip.clear();
        current_tile = current_player;
      }
    }    
    
    /*
     * Check if there are tiles to flip on the diagonal left-right from above
     */
    current_tile = opposing_player;
    x = 1;
    count = 0;
    while(current_tile != current_player)
    {
      int tile_spot = tile - (9 * x);
      if(tile_spot >= 0 && tile_spot <= 63)
      {
        current_tile = (String) current_board[tile_spot];
        int current_position = tile % 8;
        int tiles_to_right = 7 - current_position;
        int tiles_to_left = 7 - tiles_to_right;
        
        if (current_tile == opposing_player && count < tiles_to_left) {
          diagonal_left_right_above_tiles_to_flip.add(tile_spot);
          x += 1;
          count += 1;
        }
        else if (current_tile == current_player && count < tiles_to_left){
          current_tile = current_player;
        }
        else {
          diagonal_left_right_above_tiles_to_flip.clear();
          current_tile = current_player;
        }
      }
      else
      {
        diagonal_left_right_above_tiles_to_flip.clear();
        current_tile = current_player;
      }
    }    
    
    /*
     * Check if there are tiles to flip on the diagonal left-right from below
     */
    current_tile = opposing_player;
    x = 1;
    count = 0;
    while(current_tile != current_player)
    {
      int tile_spot = tile + (9 * x);
      if(tile_spot >= 0 && tile_spot <= 63)
      {
        current_tile = (String) current_board[tile_spot];
        int current_position = tile % 8;
        int tiles_to_right = 7 - current_position;
        
        if (current_tile == opposing_player && count < tiles_to_right) {
          diagonal_left_right_below_tiles_to_flip.add(tile_spot);
          x += 1;
          count += 1;
        }
        else if (current_tile == current_player && count < tiles_to_right){
          current_tile = current_player;
        }
        else {
          diagonal_left_right_below_tiles_to_flip.clear();
          current_tile = current_player;
        }
      }
      else
      {
        diagonal_left_right_below_tiles_to_flip.clear();
        current_tile = current_player;
      }
    }    

    /*
     * Check if there are tiles to flip on the diagonal right-left from above
     */
    current_tile = opposing_player;
    x = 1;
    count = 0;
    while(current_tile != current_player)
    {
      int tile_spot = tile - (7 * x);
      if(tile_spot >= 0 && tile_spot <= 63)
      {
        current_tile = (String) current_board[tile_spot];
        int current_position = tile % 8;
        int tiles_to_right = 7 - current_position;
        
        if (current_tile == opposing_player && count < tiles_to_right) {
          diagonal_right_left_above_tiles_to_flip.add(tile_spot);
          x += 1;
          count += 1;
        }
        else if (current_tile == current_player && count < tiles_to_right){
          current_tile = current_player;
        }
        else {
          diagonal_right_left_above_tiles_to_flip.clear();
          current_tile = current_player;
        }
      }
      else
      {
        diagonal_right_left_above_tiles_to_flip.clear();
        current_tile = current_player;
      }
    }    
    
    /*
     * Check if there are tiles to flip on the diagonal right-left from below
     */
    current_tile = opposing_player;
    x = 1;
    count = 0;
    while(current_tile != current_player)
    {
      int tile_spot = tile + (7 * x);
      if(tile_spot >= 0 && tile_spot <= 63)
      {
        current_tile = (String) current_board[tile_spot];
        int current_position = tile % 8;
        int tiles_to_right = 7 - current_position;
        int tiles_to_left = 7 - tiles_to_right;

        if (current_tile == opposing_player && count < tiles_to_left) {
          diagonal_right_left_below_tiles_to_flip.add(tile_spot);
          x += 1;
          count += 1;
        }
        else if (current_tile == current_player && count < tiles_to_left){
          current_tile = current_player;
        }
        else {
          diagonal_right_left_below_tiles_to_flip.clear();
          current_tile = current_player;
        }
      }
      else
      {
        diagonal_right_left_below_tiles_to_flip.clear();
        current_tile = current_player;
      }
    }    

    // Add to our array the tiles that need to be flipped
    final_tiles_to_flip.addAll(vertical_above_tiles_to_flip);
    final_tiles_to_flip.addAll(vertical_below_tiles_to_flip);
    final_tiles_to_flip.addAll(horizontal_right_tiles_to_flip);
    final_tiles_to_flip.addAll(horizontal_left_tiles_to_flip);
    final_tiles_to_flip.addAll(diagonal_left_right_above_tiles_to_flip);
    final_tiles_to_flip.addAll(diagonal_left_right_below_tiles_to_flip);
    final_tiles_to_flip.addAll(diagonal_right_left_above_tiles_to_flip);
    final_tiles_to_flip.addAll(diagonal_right_left_below_tiles_to_flip);

    // Flip all the tiles that need to be flipped
    int flip_tiles_size = final_tiles_to_flip.size();
    for (int i = 0; i < flip_tiles_size; i++)
    {
      //current_board.replace(final_tiles_to_flip.get(i), current_player);
      current_board[final_tiles_to_flip.get(i)] = current_player;

    }
  
  
    return current_board;
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  
  
  
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * Check the winner of the game
   */
  private static String check_winner(String[] current_board)
  {
    int size = current_board.length;
    int ai_points = 0;
    int human_points = 0;
    String winner;
    
    // Count the number of black and white tiles
    for(int i = 0; i < size; i++)
    {
      if(current_board[i] == human) {
        human_points += 1;
      }
      else if(current_board[i] == ai) {
        ai_points += 1;
      }
    }
    
    // Check who won
    if(human_points > ai_points) {
      winner = human;
    }
    else if(ai_points > human_points) {
      winner = ai;
    }
    else {
      winner = "Tie";
    }

    return winner;
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  
  
  
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * Simulate a random play-out for both players until end of game
   */
  private static String simulate_game(String[] current_board, String opposing_player, String current_player)
  {
    boolean opposing_player_has_move = false;
    boolean current_player_has_move = false;
    boolean game_over = false;
    String winner = null;
    Random r = new Random();

    while(game_over == false) 
    {
      // Simulate opposing player making move
      ArrayList<Integer> possible_moves = get_valid_moves(current_board, opposing_player);
      if(possible_moves.size() > 0) 
      {
        opposing_player_has_move = true;
        int rand_index = r.nextInt(((possible_moves.size()-1) - 0) + 1) + 0;
        int move = possible_moves.get(rand_index);
        current_board = make_move(opposing_player, current_board, move);  
      }
      else
      {
        opposing_player_has_move = false;
      }
      
      // Simulate current_player
      possible_moves = get_valid_moves(current_board, current_player);
      if(possible_moves.size() > 0) 
      {
        current_player_has_move = true;
        int rand_index = r.nextInt(((possible_moves.size()-1) - 0) + 1) + 0;
        int move = possible_moves.get(rand_index);
        current_board = make_move(current_player, current_board, move);  
      }
      else
      {
        current_player_has_move = false;
      }
      
      // If no valid moves left, game is over, check who the winner is
      if(current_player_has_move == false && opposing_player_has_move == false)
      {
        game_over = true;
        winner = check_winner(current_board);
      }
    }
    
    return winner;

  }

  /**
   * Choosing a move using pure MCTS
   */
  private static int mcts_get_move(String[] current_board, ArrayList<Integer> legal_moves)
  {
    int size = legal_moves.size();
    int possible_moves_scores[]  = new int[size]; 
        
    // Iterate through all legal moves
    for (int init_move = 0; init_move < size; init_move++) 
    {
      // Reset board and legal moves
      String[] temp_board = new String[64];      
      System.arraycopy(current_board, 0, temp_board, 0, 64); 
      ArrayList<Integer> temp_legal_moves = get_valid_moves(temp_board, ai);
      int move = temp_legal_moves.get(init_move);
      temp_board = make_move(ai, temp_board, move);  

      // Do the N random play-outs
      int N = 1000;
      int wins = 0, losses = 0, ties = 0;
      String winner;
      for(int i = 0; i < N; i++)
      {
        // Reset the board
        String[] updated_temp_board = new String[64];      
        System.arraycopy(temp_board, 0, updated_temp_board, 0, 64); 

        // Simulate game based on random moves to completion
        winner = simulate_game(updated_temp_board, human, ai);
        
        // Increment wins/losses/ties
        if (winner == ai) {
          wins += 1;
        }
        if (winner == human) {
          losses += 1;
        }
        if (winner == "Tie") {
          ties += 1;
        }
      }
      
      // Calculate score based on random play-outs and add to list
      int score = (2*wins) + (1*ties) + (-1*losses);
      possible_moves_scores[init_move] = score;
    }
    
    // Get the index of the largest score
    int largest = 0;
    for (int i = 0; i < possible_moves_scores.length; i++ )
    {
        if (possible_moves_scores[i] > possible_moves_scores[largest]) 
        { 
          largest = i;
        }
    }

    // Return highest scored move
    //System.out.println(possible_moves_scores.toString());
    return legal_moves.get(largest);
  }
  
  /**
   * Choosing a move using MCTS with additional heuristics
   * 
   * Before we perform the N random play-outs, we will check if key positions are available, if a key position is available
   * we will choose it, and skip the random play-outs
   * 
   * Reference: https://guides.net4tv.com/games/how-win-reversi#:~:text=The%20basic%20moves%20of%20Reversi,your%20stone%20in%20that%20square.
   */
  private static int mcts_heuristic_get_move(String[] current_board, ArrayList<Integer> legal_moves)
  {
    int size = legal_moves.size();
    int possible_moves_scores[]  = new int[size]; 
    
    for(int i = 0; i < legal_moves.size(); i++)
    {
      /*
       * Moves with the highest priority
       */
      // The corners
      if(legal_moves.get(i) == 0 || legal_moves.get(i) == 7 || legal_moves.get(i) == 56 || legal_moves.get(i) == 63) {
        return legal_moves.get(i);
      }
      
      /*
       * Moves with the second highest priority
       */
      // Spots controlling the corners
      if(legal_moves.get(i) == 2 || legal_moves.get(i) == 5 || legal_moves.get(i) == 16 || legal_moves.get(i) == 23 ||
          legal_moves.get(i) == 40 || legal_moves.get(i) == 47 || legal_moves.get(i) == 58 || legal_moves.get(i) == 61)
      {
        return legal_moves.get(i);
      }
      
      /*
       * Moves with the third highest priority
       */
      // Spots controlling the spots that control the corners
      if(legal_moves.get(i) == 18 || legal_moves.get(i) == 21 || legal_moves.get(i) == 42 || legal_moves.get(i) == 45)
      {
        return legal_moves.get(i);
      }      
    }
        
    // Iterate through all legal moves
    for (int init_move = 0; init_move < size; init_move++) 
    {
      // Reset board and legal moves
      String[] temp_board = new String[64];      
      System.arraycopy(current_board, 0, temp_board, 0, 64); 
      ArrayList<Integer> temp_legal_moves = get_valid_moves(temp_board, human);
      int move = temp_legal_moves.get(init_move);
      temp_board = make_move(human, temp_board, move);  

      // Do the N random play-outs
      int N = 1000;
      int wins = 0, losses = 0, ties = 0;
      String winner;
      for(int i = 0; i < N; i++)
      {
        // Reset the board
        String[] updated_temp_board = new String[64];      
        System.arraycopy(temp_board, 0, updated_temp_board, 0, 64); 

        // Simulate game based on random moves to completion
        winner = simulate_game(updated_temp_board, ai, human);
        
        // Increment wins/losses/ties
        if (winner == human) {
          wins += 1;
        }
        if (winner == ai) {
          losses += 1;
        }
        if (winner == "Tie") {
          ties += 1;
        }
      }
      
      // Calculate score based on random play-outs and add to list
      int score = (2*wins) + (1*ties) + (-1*losses);
      possible_moves_scores[init_move] = score;
    }
    
    // Get the index of the largest score
    int largest = 0;
    for (int i = 0; i < possible_moves_scores.length; i++ )
    {
        if (possible_moves_scores[i] > possible_moves_scores[largest]) 
        { 
          largest = i;
        }
    }

    // Return highest scored move
    //System.out.println(possible_moves_scores.toString());
    return legal_moves.get(largest);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  

  
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * Simulate MCTS vs MCTS with heuristics
   */
  private static String play_game() 
  {
    boolean ai_has_move;
    boolean player_has_move;

    // Print the initial board
    board = initialize_board();

    while(true) 
    {
      String current_player = ai;
      print_board(board);
      ArrayList<Integer> possible_moves = get_valid_moves(board, current_player);
      
      // If there are possible moves, make the move
      if(possible_moves.size() == 0) 
      {
        ai_has_move = false;
        System.out.println("Player " + current_player + " has no moves to place. Skipping to next player.");
      }
      else 
      {
        ai_has_move = true;
        System.out.println("Possible Moves for Player O: " + possible_moves);
        System.out.println("Player O making move... ");
        String[] temp_board = new String[64]; // pass to the function a copy of the current boards
        System.arraycopy(board, 0, temp_board, 0, 64); 
        int new_tile = mcts_get_move(temp_board, possible_moves);
        System.out.println("Player O choosing move at tile " + new_tile);
        board = make_move(current_player, board, new_tile);   
        System.out.println();
        System.out.println();
      }
      
      current_player = human;
      print_board(board);
      possible_moves = get_valid_moves(board, current_player);
      
      // If there are possible moves, make the move
      if(possible_moves.size() == 0)
      {
        player_has_move = false;
        System.out.println("Player " + current_player + " has no moves to place. Skipping to next player.");
      }
      else 
      {
        player_has_move = true;
        System.out.println("Possible Moves for Player X: " + possible_moves);
        System.out.println("Player X making move... ");
        String[] temp_board = new String[64]; // pass to the function a copy of the current boards
        System.arraycopy(board, 0, temp_board, 0, 64); 
        int new_tile = mcts_heuristic_get_move(temp_board, possible_moves);
        System.out.println("Player X choosing move at tile " + new_tile);
        board = make_move(current_player, board, new_tile);   
        System.out.println();
        System.out.println();
      }
      
      // Check that both players have valid moves, else they are deadlocked and game is over
      if(ai_has_move == false && player_has_move == false)
      {
        String winner = check_winner(board);
        System.out.println("Game over, no valid moves remaining!");
        System.out.println("------------------------------------------------");
        if(winner == "Tie") 
        {
          System.out.println("It is a tie!");
        }
        else 
        {
          System.out.println("Player " + winner + " is the winner!");
        }
        return winner;
      } 
    }
  }
    
  /**
   * Play a simulated MCTS vs Modified MCTS game N times, and show results
   */
  public static void main(String[] args)
  { 
    int heuristic_wins = 0;
    int mcts_wins = 0;
    int ties = 0;
    int matches = 100;

    for(int i = 0; i < matches; i++)
    {
      String winner = play_game();
      System.out.println("Winner is " + winner);
      
      if(winner == ai) {
        heuristic_wins += 1;
      }
      if(winner == human) {
        mcts_wins += 1;
      }
      if(winner == "Tie"){
        ties += 1;
      }
    }
    
    System.out.println("--------------- SIMULATIONS OVER ---------------");
    System.out.println("MCTS won " + mcts_wins + "/" + matches + " matches");
    System.out.println("MCTS with Heuristics won " + heuristic_wins + "/" + matches + " matches");
    System.out.println("Total ties " + ties + "/" + matches + " matches");
    System.out.println("--------------- PROGRAM EXITED ---------------");

  }
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////






  
  
  
  
  
}
