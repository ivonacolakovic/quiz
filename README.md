# quiz

GET / - server running

GET serverip:serverport/api/game/regular/get_questions - get questions with for 3 levels

GET serverip:serverport/api/game/custom/query - get questions based on parameters given

  `example: serverip:serverport/api/game/custom/query?tezavnost=hard&kategorija=sport&tip_vprasanj=multiple`
  
    tip_vprasanj: multiple, boolean
    tezavnost: hard, medium, easy
    kategorija: Sports, Science: Computers, Entertainment: Film, Entertainment: Music, Geography, General Knowledge
  
GET serverip:serverport/api/core/check_data - fill database
