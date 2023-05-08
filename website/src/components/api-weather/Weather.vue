<template>
    <div>
      <CurrentWeather v-if="weather?.current" :weather="weather" :formatTemperature="formatTemperature" :getWeatherImageUrl="getWeatherImageUrl"/>
      <hr>
      <Forecast v-if="weather?.daily" :weather="weather" :formatTemperature="formatTemperature" :getWeatherImageUrl="getWeatherImageUrl"/>
    </div>
  </template>

<script>
  import { defineComponent } from 'vue';
  import CurrentWeather from './CurrentWeather.vue';
  import Forecast from './Forecast.vue';
  import { useWeather } from '@/scripts/Scripts.ts';
  import { getWithExpiry } from "@/scripts/Scripts.ts";
  
  export default defineComponent({
    components: {
      CurrentWeather,
      Forecast,
    },
    setup() {
      const { weather, fetchWeather, getWeatherImageUrl, formatTemperature } = useWeather();
  
      const loadWeather = async () => {
        try {
          const weatherValues=getWithExpiry("weather");
          if(weatherValues==null){
            await fetchWeather();
            console.log("weather")
          }
          else{
            weather.value=weatherValues;
          }
        } catch (error) {
          console.error(error);
        }
      };
  
      if (!weather?.value) {
        loadWeather();
      }
  
      return {
        weather,
        formatTemperature,
        getWeatherImageUrl,
      };
    },
  });
</script>
  