<template>
    <div>
      <div class="button-container">
      <button class="back-button" > <router-link :to="{ name:'leagueTop5',path: '/sport'}">&#x2039;</router-link></button>
      <div class="button-group">
        <button class="button" @click="handleClick(PremierLeagueId)">Premier League</button>
        <button class="button" @click="handleClick(LaLigaId)">LaLiga</button>
        <button class="button" @click="handleClick(SerieAId)">Serie A</button>
        <button class="button" @click="handleClick(League1Id)">Ligue 1</button>
        <button class="button" @click="handleClick(EkstraklasaId)">Ekstraklasa</button>
      </div>
    </div>
      <div v-if="clubs && clubs.length">
        <div class="scoresTableWrapper">
          <table class="scoresTable">
            <thead>
              <tr>
                <th>Pozycja</th>
                <th>Herb</th>
                <th>Drużyna</th>
                <th>Forma</th>
                <th>Zagrane</th>
                <th>Wygrane</th>
                <th>Remisy</th>
                <th>Przegrane</th>
                <th>Bramki zdobyte</th>
                <th>Bramki stracone</th>
                <th>Bilans bramkowy</th>
                <th>Punkty</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(club, index) in clubs" :key="index">
                <td>{{ club.intRank }}</td>
                <td><img :src="club.strTeamBadge" alt="Team Logo" /></td>
                <td>{{ club.strTeam }}</td>
                <td>
                  <div class="form">
                    <span v-for="letter in club.strForm.split('')" :key="index" :class="{ 'win': letter === 'W', 'draw': letter === 'D', 'loss': letter === 'L' }"></span>
                  </div>
                </td>
                <td><div>{{ club.intPlayed }}</div></td>
                <td><div>{{ club.intWin }}</div></td>
                <td><div>{{ club.intDraw }}</div></td>
                <td><div>{{ club.intLoss }}</div></td>
                <td><div>{{ club.intGoalsFor }}</div></td>
                <td><div>{{ club.intGoalsAgainst }}</div></td>
                <td><div>{{ club.intGoalDifference }}</div></td>
                <td><div>{{ club.intPoints }}</div></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </template>
  
  
  <script>
import { ref,onMounted} from "vue";
import {fetchLeagueTable,fetchAllLeagues} from "@/scripts/Scripts.ts";
import { useRouter} from 'vue-router'

export default {

  setup() {
    const PremierLeagueId = 4328;
    const LaLigaId = 4335;
    const SerieAId = 4332;
    const Ligue1Id = 4334;
    const EkstraklasaId = 4422;

    const router = useRouter();
    var league=localStorage.getItem("league");
    const clubs = ref([]);
    
    if(!league)
      fetchAllLeagues();
    const selectedLeague = ref(league || EkstraklasaId.toString);
    onMounted(() => {
      fetchAllLeagues();
    });
   
    function handleClick(id) {
      // Perform some action before navigating to the route
      fetchLeagueTable(id,clubs);
      
      // Navigate to the route
      router.push({name:'leagueTable',path: '/sport', params: { league: clubs.value[0].strLeague } });
    }
  
      
  
    fetchLeagueTable(selectedLeague.value,clubs);
    return {
      clubs,
      selectedLeague,
      handleClick,
      fetchLeagueTable,
      PremierLeagueId,
      LaLigaId,
      SerieAId,
      Ligue1Id,
      EkstraklasaId,
    };
  }
};
</script>

<style>
.button-container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.scoresTable {
    border-collapse: collapse;
    width: 100%;
    margin-bottom: 1rem;
  }
  
  .scoresTable th,
  .scoresTable td {
    padding: 0.5rem;
    text-align: center;
    white-space: nowrap;
    box-sizing: border-box;
  }
  
  .scoresTable th {
    background-color: #eee;
    font-weight: bold;
    text-transform: uppercase;
    letter-spacing: 0.1em;
  }
  
  .scoresTable td {
    font-size: 1rem;
    line-height: 1.5;
    margin: 0;
    border: 0;
  }
 
  
  .scoresTable tbody tr:nth-of-type(odd) {
    background-color: #f9f9f9;
  }
  
  .scoresTable tbody tr:hover {
    background-color: #f5f5f5;
  }
  
  .scoresTable td:first-child {
    font-weight: bold;
  }
  
  .scoresTable .form {
    display: flex;
    justify-content: space-between;
    width: 100%;
    align-items: center;
  }
  
  .scoresTable .form span {
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 20px;
    height: 20px;
    border-radius: 50%;
    border: 1px solid #ccc;
    margin-right: 5px;
    font-size: 12px;
  }
  
  .scoresTable .form .win {
    border-color: green;
    background-color: green;
    color: white;
  }
  
  .scoresTable .form .win::before {
    content: '\2713';
  }
  
  .scoresTable .form .loss {
    border-color: red;
    background-color: red;
    color: white;
  }
  
  .scoresTable .form .loss::before {
    content: '\2717';
  }
  
  .scoresTable .form .draw {
    border-color: gray;
    background-color: gray;
    color: white;
  }
  
  .scoresTable .form .draw::before {
    content: '\2212';
    font-weight: bold;
    position: relative;
    top: -1px;
  }

  .back-button {
    background-color: transparent;
    border: none;
    font-size: 3.5rem;
    cursor: pointer;
    height:3rem;
    margin-right: 2rem;
    margin-bottom: 1.75rem;
  }
  
  .back-button:hover {
    color: #0d47a1;
  }

  .button-group {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 0rem;
  }
  
  .button {
    background-color: #0074D9;
    border: none;
    color: white;
    padding: 0.5rem 1rem;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 1rem;
    margin: 0.25rem;
    cursor: pointer;
    border-radius: 0.25rem;
    transition: background-color 0.3s ease;
    }
    
  .button:hover {
    background-color: #004E8E;
  }
  
  .button:focus {
    outline: none;
    box-shadow: 0 0 0 3px rgba(0, 0, 0, 0.4);
  }
  
  .button:active {
    background-color: #002A5E;
  }
  
  @media screen and (max-width: 600px) {
    .button-group {
      flex-wrap: wrap;
    }
  
    .button {
      width: 100%;
    }
  }

  .scoresTable td:nth-child(2){
    min-width: 60px;
  }

  .scoresTable td:nth-child(3) {
    min-width: 160px;
  }
  
</style>