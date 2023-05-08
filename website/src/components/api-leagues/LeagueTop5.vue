<template>
    <div class="table-container">
      <select class="select-container" v-model="selectedLeague" @change="fetchLeagueTable(selectedLeague,getClubs())">
        <option value="4328">Premier League</option>
        <option value="4335">LaLiga</option>
        <option value="4332">Serie A</option>
        <option value="4334">Ligue 1</option>
        <option value="4422">Ekstraklasa</option>
      </select>      
      <div v-if="clubs && clubs.length">
          <table class="scores-table">
            <thead>
              <tr>
                <th>Pozycja</th>
                <th>Herb</th>
                <th>Drużyna</th>
                <th>Punkty</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(club, index) in clubs.slice(0,5)" :key="index">
                <td>{{ club.intRank }}</td>
                <td><img class="team-badge" :src="club.strTeamBadge" alt="Team Logo" /></td>
                <td>{{ club.strTeam }}</td>
                <td><div class="points">{{ club.intPoints }}</div></td>
              </tr>
            </tbody>
          </table>
          <button id="hide-table" class="button">
            <router-link :to="{ name:'leagueTable',path: '/sport', params: { league: clubs[0].strLeague } }">Zobacz więcej</router-link>
          </button>
          
      </div>
    </div>
  </template>
  
  <script>
import { ref } from "vue";
import {fetchLeagueTable,fetchAllLeagues} from "@/components/api-leagues/GetLeagues.vue"
export default {

  setup() {
    var league=localStorage.getItem("league");
    const clubs = ref([]);
    if(!league)
      fetchAllLeagues();
    const selectedLeague = ref(league || "4422");

    // to pass RefImpl instead of Proxy(array) (inside of template section) which takes no effect when updating
    function getClubs() {
      return clubs;
    }

    fetchLeagueTable(selectedLeague.value,clubs);
    return {
      clubs,
      selectedLeague,
      getClubs,
      fetchLeagueTable,
    };
  }
};
</script>
  
  <style scoped>
  img {
    max-width: 35px;
    filter: drop-shadow(1px 1px 1px #222);
  }

  .table-container {
    position: relative;
    width: fit-content;
    background: white;
    padding: 1%;
    border-radius: 10px;
    float: right;
    min-width: 20%;
  }
  
  .scores-table {
    border-collapse: collapse;
    font-size: smaller;
    width: 100%;
  }
  
  .scores-table th,
  .scores-table td {
    padding: 0.5rem;
    text-align: center;
  }
  
  .scores-table td {
    border-bottom: 0.0625rem solid #ddd;
    border-right: none;
  }
  
  .scores-table th {
    background-color: #f9fafb;
    border-bottom: 3px solid #ddd;
    color: #333;
    font-weight: bold;
    padding: 12px;
    text-transform: uppercase;
    font-size: 0.7rem;
  }
  .scores-table th:first-child {
    border-top-left-radius: 4px;
  }
  
  .scores-table th:last-child {
    border-top-right-radius: 4px;
  }
  .select-container {
  background-color: #eaeaea;
  cursor: pointer;
  }
  .scores-table td .points {
    display: inline-block;
    color: rgb(0, 0, 0);
    font-size: 0.9rem;
    font-weight: bold;
  }
  
  .select-container {
    background-color: #f2f2f2;
    border: none;
    border-radius: 0.25rem;
    color: #333;
    font-size: 0.9rem;
    font-weight: 550;
    padding: 0.5rem 1rem;
    margin-bottom: 0.5rem;
    width: 100%;
  }
  
  .select-container:focus {
    outline: none;
    box-shadow: 0 0 0.125rem rgba(0, 0, 0, 0.2);
  }

  a:hover {
    color: white;
  }
  #show-table{
    background-color: #f2f2f2;
    border: none;
    border-radius: 0.25rem;
    color: #333;
    font-size: 1rem;
    font-weight: bold;
    padding: 0.5rem 1rem;
    width: 80%;
    margin-bottom: 0.5rem;
    min-width: 338px;
  }
  #show-table:hover{
    background-color: #eaeaea;
  }


  @media screen and (max-width: 1000px) {
    .table-container {
      width: 100%;
    }
    .scores-table th,
    .scores-table td {
      width: 25%;
    }

    .scores-table{
      width: 100%;
    }
  }

  @media screen and (max-width: 600px) {
    .table-container {
      margin-top: 7.5rem;
    }
  }
</style>
