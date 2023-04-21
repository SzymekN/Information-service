<template>
<div v-for="daily in weather?.daily" :key="daily.dt">
    <div class="weather">
        <h3>{{ formatTemperature(daily.temp.day, 'C') }}</h3>
        <p>{{ daily.weather[0].description }}</p>
        <p>{{ new Date(daily.dt * 1000).toLocaleDateString(undefined, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }) }}</p>
        <hr>
    </div>
</div>
</template>
<script>
import { defineComponent, onMounted } from 'vue'

import { useWeather } from '@/scripts/WeatherService'
export default defineComponent({
  setup () {
    const { weather, fetchWeather, getWeatherImageUrl, formatTemperature } = useWeather();
    onMounted(fetchWeather);
    return {
      weather,
      getWeatherImageUrl,
      formatTemperature
    }
  }
})
</script>

<style>
.weather{
  color: #282828;
  font-size: 0.8rem;
}

h3 {
  font-weight: 500;
  margin-bottom: -5%;
}

p{
  margin-bottom: -2%;
}
</style>