<template>
<div v-for="daily in weather?.daily" :key="daily.dt">
    <div class="weather">
        <div class="temperature-wrapper">
          <h2>{{ formatTemperature(daily.temp.day, 'C') }}</h2>
          <img id="weatherDailyImage" :src="getWeatherImageUrl(daily.weather[0].icon, '2x')" alt="Weather icon" />
        </div>
        <p>{{ daily.weather[0].description }}</p>
        <p>{{ new Date(daily.dt * 1000).toLocaleDateString(undefined, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }) }}</p>
        <hr>
    </div>
</div>
</template>
<script>
import { defineComponent } from 'vue'

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
  }
})
</script>

<style>
.weather{
  color: #282828;
  font-size: 0.8rem;
}
h2 {
  margin-bottom: 0.5rem;
}
h3 {
  font-weight: 500;
  margin-bottom: -5%;
}

p{
  margin-bottom: -2%;
}

#weatherDailyImage{
  width: 25px;
  height: 25px;
}
</style>