# quiz

GET / - server running

GET serverip:serverport/api/game/regular/get_questions - play regular quiz with 3 levels

GET serverip:serverport/api/game/custom/query
  example: serverip:serverport/api/game/custom/query?tezavnost=hard&kategorija=sport&tip_vprasanj=multiple
  tip_vprasanj: multiple, boolean
  kategorija: sport, general knowledge, science: computers, entertainment: music, entertainment: film, geography
  tezavnost: hard, medium, easy
  
GET api/core/check_data - fill database
