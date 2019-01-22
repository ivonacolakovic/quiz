
# quiz

GET serverip:serverport/ - server running

GET serverip:serverport/api/game/regular/get_questions - get 30 randomly chosen questions from db (10 easy, 10 medium, 10 hard)

GET serverip:serverport/api/game/custom/query - get questions that match given parameters (20)

 	example: serverip:serverport/api/game/custom/query?tezavnost=hard&kategorija=sports&tip_vprasanj=multiple
  tip_vprasanj: multiple, boolean
  
  kategorija: sport, general knowledge, science: computers, entertainment: music, entertainment: film, geography
  
  tezavnost: hard, medium, easy
  
 GET api/core/check_data - fill database
 
 PRAVILA IGRE: Uporabnik na začetku ima možnost da izbere kategorijo, nivo (easy, medium, hard), tip vprašanj (multiple, true/false). V primeru, da uporabnik ne izbere nič od zgoraj navedenega, odgovarja na mešana vprašanja in se sprehaja med nivoji - na naslednji nivo lahko gre, če odgovori točno na 10 vprašanj. Ko uporabnik odgovori točno na vprašanje, pridobi določeno število kovancev (število kovancev je odvisno od nivoja na katerem je uporabnik trenutno). Pridobljene kovance uporabnik lahko uporabi, da pridobi točen odgovor.

