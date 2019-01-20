
# quiz

GET serverip:serverport/ - server running

GET serverip:serverport/api/game/regular/get_questions - get 30 randomly chosen questions from db (10 easy, 10 medium, 10 hard)

GET serverip:serverport/api/game/custom/query - get questions that match given parameters (20)

 	example: serverip:serverport/api/game/custom/query?tezavnost=hard&kategorija=sports&tip_vprasanj=multiple
  tip_vprasanj: multiple, boolean
  
  kategorija: sport, general knowledge, science: computers, entertainment: music, entertainment: film, geography
  
  tezavnost: hard, medium, easy
  
 GET api/core/check_data - fill database

