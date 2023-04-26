<template>
    <div>
      <div class="button-group">
        <button class="button" @click="fetchLeagueTable(4328)">Premier League</button>
        <button class="button" @click="fetchLeagueTable(4335)">LaLiga</button>
        <button class="button" @click="fetchLeagueTable(4332)">Serie A</button>
        <button class="button" @click="fetchLeagueTable(4334)">Ligue 1</button>
        <button class="button" @click="fetchLeagueTable(4422)">Ekstraklasa</button>
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
                    <span v-for="letter in club.strForm.split('')" :key="index" :class="{ 'win': letter === 'W', 'draw': letter === 'D', 'loss': letter === 'L' }">{{ letter }}</span>
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
import { ref } from "vue";
import { useLeagues } from "../../scripts/LeagueService";
import {setWithExpiry,getWithExpiry} from "@/scripts/HandleItems.ts"
export default {

  setup() {
    var league=localStorage.getItem("league");
    const { leagueScores, fetchLeagues } = useLeagues();
    const clubs = ref([]);

    if(!league)
      league=4422;
    
    // clubs.value=getWithExpiry(league);
   


    const fetchLeagueTable = async (leagueId) => {
      var leagueValues=getWithExpiry(leagueId);
      console.log(leagueValues)
      if(leagueValues==null){
        await fetchLeagues(leagueId);
        clubs.value = [];
        clubs.value=leagueScores.value;

        if(getWithExpiry(leagueId)==null)
          setWithExpiry(leagueId,leagueScores.value,180000);
      }
      else
        clubs.value = leagueValues;
      localStorage.setItem("league",leagueId);
    }

      fetchLeagueTable(league);
    return {
      clubs,
      fetchLeagueTable
    };
  }
};
</script>

<style>
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
    width: 80px;
    
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
  }
  
  .scoresTable .form .win {
    color: green;
  }
  
  .scoresTable .form .draw {
    color: orange;
  }
  
  .scoresTable .form .loss {
    color: red;
  }
  
  .button-group {
    display: flex;
    justify-content: center;
    margin-bottom: 1rem;
  }
  
  .button {
    background-color: #4CAF50;
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
    background-color: #3e8e41;
  }
  
  .button:focus {
    outline: none;
    box-shadow: 0 0 0 3px rgba(0, 0, 0, 0.4);
  }
  
  .button:active {
    background-color: #2e7d32;
  }
  
  @media screen and (max-width: 600px) {
    .button-group {
      flex-wrap: wrap;
    }
  
    .button {
      width: 100%;
    }
  }

  
</style>