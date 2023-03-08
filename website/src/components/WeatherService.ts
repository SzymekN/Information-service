import { ref } from "vue";

import { WeatherModel } from './WeatherModel';

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

  console.log("Your current position is:");
  console.log(`Latitude : ${coords.latitude}`);
  console.log(`Longitude: ${coords.longitude}`);
  console.log(`More or less ${coords.accuracy} meters.`);

  const response = await fetch(`${weatherUrl}&lat=${coords.latitude}&lon=${coords.longitude}&appid=81b526afdb9b8e5cbfb248526813c344&units=metric&lang=pl`);
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