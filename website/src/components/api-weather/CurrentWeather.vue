<template>
  <div class="card">
    <h1>POGODA NA DZIŚ</h1>
    <div class="temperature-wrapper">
      <h2>{{ formatTemperature(weather?.current.temp, 'C') }}</h2>
      <img :src="getWeatherImageUrl(weather?.current.weather[0].icon, '2x')" alt="Weather icon" />
    </div>
    <h3>{{ weather.current.weather[0].description }}</h3>
    <h4>
      <span>Wiatr {{ weather.current.wind_speed }}km/h </span>
      <span>Wilgotność {{ weather.current.humidity }}%</span>
    </h4>
    <div>
      <h4> Wschód {{ new Date(weather.current.sunrise * 1000).toLocaleTimeString([], { timeStyle: 'short' }) }}</h4>
      <h4 id="sunset"> Zachód {{ new Date(weather.current.sunset * 1000).toLocaleTimeString([], { timeStyle: 'short' }) }}</h4>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
import {setWithExpiry,getWithExpiry} from "@/scripts/HandleItems.ts"
export default defineComponent({
  props: {
    weather: {
      type: Object,
      required: true
    },
    formatTemperature: {
      type: Function,
      required: true
    },
    getWeatherImageUrl: {
      type: Function,
      required: true
    }
  },
  setup(props){
    var weatherTemp=getWithExpiry("weather");
    if(weatherTemp==null)
      setWithExpiry("weather",props.weather,86400000);
}
})
</script>
<style scoped>
.card {
  color: #282828;
  font-size: 0.7rem;
}

h1 {
  font-size: 1.5rem;
  font-weight: 500;
  margin-bottom: -1%;
}

h2 {
  font-size: 1.3rem;
  margin-bottom: -5%;
  margin-right: 5%;
  float: left;
}

h3 {
  font-size: 0.9rem;
  margin-bottom: -5%;
}

h4 {
  font-size: larger;
  margin-bottom: -5%;
}
#sunset{
  margin-bottom: 0.5rem;
}

img {
  width: 50px;
  height: 50px;
  margin-bottom: -5%;
  margin-top: 5%;
}
</style>
