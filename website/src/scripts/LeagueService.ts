import { ref, Ref } from "vue";
import { LeagueScores } from "./LeagueModel";

const leagueScores = ref<LeagueScores[]>([]);

async function fetchLeagues(leagueId: string): Promise<Ref<LeagueScores[]>> {
  try {
    const date = new Date();
    const apiUrl =
      "https://www.thesportsdb.com/api/v1/json/3/lookuptable.php?l=" +
      leagueId +
      "&s=" +
      (date.getFullYear() - 1) +
      "-" +
      date.getFullYear();
      console.log(apiUrl)
    const response = await fetch(apiUrl);
    const leagueScoresArray = await response.json();

    leagueScores.value = leagueScoresArray.table;
   
    return leagueScores;
  } catch (error) {
    console.error(error);
    return leagueScores;
  }
}

export function useLeagues() {
  return {
    leagueScores,
    fetchLeagues,
  };
}
