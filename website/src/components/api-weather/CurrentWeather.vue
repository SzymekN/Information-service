<template>
    <div class="card" v-if="weather?.current">
        <h1>POGODA NA DZIŚ</h1>
        <h2>{{ formatTemperature(weather?.current.temp, 'C') }}</h2>
        <h3>{{weather.current.weather[0].description}}</h3><h3><span>{{weather.current.wind_speed}}km/h </span> <span>Wilgotność {{weather.current.humidity}}%</span></h3>
        <div>
            <h4> Wschód {{ new Date(weather.current.sunrise * 1000).toLocaleTimeString([], { timeStyle: 'short' }) }}</h4>
            <h4> Zachód {{ new Date(weather.current.sunset * 1000).toLocaleTimeString([], { timeStyle: 'short' }) }}</h4>
        </div>
    </div>

</template>
<script>
import { defineComponent } from 'vue'

import { useWeather } from '@/scripts/WeatherService';


export default defineComponent({
  setup () {
    const { weather, fetchWeather, getWeatherImageUrl, formatTemperature } = useWeather();
    if (!weather?.value?.current) {
      fetchWeather();
    }
    return {
      weather,
      getWeatherImageUrl,
      formatTemperature
    }
  }
})
</script>
<style scoped>
.card{
  color: #282828;
  font-size: 0.7rem;
}

h1 {
  font-size: 1.5rem;
  font-weight: 500;
  margin-bottom: -1%;
}

h2{
  font-size: 1.3rem;
  margin-bottom: -5%;
}

h3{
  font-size: 0.9rem;
  margin-bottom: -5%;
}

h4{
  font-size: larger;
  margin-bottom: -2%;
}
</style>
