<script>
import { ref } from 'vue';
import { useLeagues } from "../../scripts/LeagueService";
import {setWithExpiry,getWithExpiry} from "@/scripts/HandleItems.ts"
const { leagueScores, fetchLeagues } = useLeagues();

export async function  fetchLeagueTable(leagueId,data)  {
    const clubs=ref(data);
    var leagueValues=getWithExpiry(leagueId);
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

export async function fetchAllLeagues(){
var allIds=[4328,4335,4332,4334,4422];
allIds.forEach(async (leagueId) => {
    await fetchLeagues(leagueId);
    setWithExpiry(leagueId, leagueScores.value, 180000);
});
}
</script>