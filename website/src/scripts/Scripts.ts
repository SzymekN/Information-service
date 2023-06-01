interface Weather {
    id: number;
    main: string;
    description: string;
    icon: string;
}

interface FeelsLike {
  day: number;
  eve: number;
  morn: number;
  night: number;
}

interface Temperature extends FeelsLike {
  max: number;
  min: number;
}

interface CurrentWeather {
  feels_like: number;
  humidity: number;
  sunrise: number;
  sunset: number;
  temp: number;
  weather: Weather[];
  wind_speed: number;
}

interface DailyWeather {
  dt:number,
  feels_like: FeelsLike;
  humidity: number;
  sunrise: number;
  sunset: number;
  temp: Temperature;
  weather: Weather[];
  wind_speed: number;
}

export interface WeatherModel {
  current: CurrentWeather;
  daily: DailyWeather[];
}


interface Rate {
currency: string;
code: string;
mid: number;
}

export interface ExchangeTable {
  table: string;
  no: string;
  effectiveDate: string;
  rates: Rate[];
}


interface Scores{
    idStanding:number;
    intRank:number;
    idTeam:number;
    strTeam:string;
    strTeamBadge:string;
    idLeague:number;
    strLeague:string;
    strSeason:string;
    strForm:string;
    strDescription:string;
    intPlayed:number;
    intWin:number; 
    intLoss :number;
    intDraw:number;
    intGoalsFor:number;
    intGoalsAgainst:number;
    intGoalDifference:number;
    intPoints:number;
    dateUpdated:string;
}
export interface LeagueScores{
    table: Scores[];
}

import { ref, Ref} from "vue";

import { weatherApiKey } from '../../Keys'


const weatherUrl = 'https://api.openweathermap.org/data/2.5/onecall?exclude=minutely,hourly';
const weather = ref<WeatherModel>();

export function useWeather() {
  return {
    weather,
    fetchWeather,
    getWeatherImageUrl,
    formatTemperature,
  }
}
const options = {
  enableHighAccuracy: true,
  timeout: 5000,
  maximumAge: 0,
};

async function success(pos) {
  const coords = pos.coords;
  const response = await fetch(`${weatherUrl}&lat=${coords.latitude}&lon=${coords.longitude}&appid=${ weatherApiKey }&units=metric&lang=pl`);
  weather.value = await response.json();
}

function error(err) {
  console.warn(`ERROR(${err.code}): ${err.message}`);
}

async function fetchWeather(): Promise<void> {
  navigator.geolocation.getCurrentPosition(success, error, options);
}

function getWeatherImageUrl(iconName: string, size: '2x' | '4x') {
  return `http://openweathermap.org/img/wn/${iconName}@${size ? size : '1x'}.png`;
}

function formatTemperature(value: number, format: 'F' | 'C') {
  return `${Math.round(value)}° ${format}`;
}


const nbpUrl = 'https://api.nbp.pl/api/exchangerates/tables/a/last/2';
const exchangeTable = ref<ExchangeTable[]>([]);

export function useCurrencies() {
  return {
    exchangeTable,
    fetchCurrencies,
  }
}
async function fetchCurrencies(): Promise<Ref<ExchangeTable[]>> {
  try {
    const response = await fetch(nbpUrl);
    const exchangeTableArray = await response.json();
    exchangeTable.value = exchangeTableArray;
    console.log(exchangeTable.value)
    return exchangeTable;
  } catch (error) {
    console.error(error);
    return exchangeTable;
  }
}

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

export function setWithExpiry(key, value, ttl) {
	const now = new Date()
    const item = {
		value: value,
		expiry: now.getTime() + ttl,
	}
	localStorage.setItem(key, JSON.stringify(item))
}

export function getWithExpiry(key) {
	const itemStr = localStorage.getItem(key)

    if (!itemStr) {
		return null
	}
	const item = JSON.parse(itemStr)
	const now = new Date()

    if (now.getTime() > item.expiry) {

        localStorage.removeItem(key)
		return null
	}
	return item.value
}

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

export function getArticleById(data,id) {
  return data.filter(
   function(data){ return data.id == id }
 );
 }
 
export async function fetchAllLeagues(){
var allIds=[4328,4335,4332,4334,4422];
allIds.forEach(async (leagueId) => {
    await fetchLeagues(leagueId);
    setWithExpiry(leagueId, leagueScores.value, 180000);
});
}

