<template>
  <div>
    <div v-if="cards && cards.length&&prevCards&&prevCards.length">
      <SlidingArray :cards="cards" :prevCards="prevCards" :duration=60></SlidingArray>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import SlidingArray from "./SlidingArray.vue";
import { useCurrencies } from '@/scripts/CurrenciesService.ts'

export default {
  components: {
    SlidingArray
  },
  setup() {
    const { exchangeTable, fetchCurrencies } = useCurrencies();
    const cards = ref([]);
    const prevCards = ref([]);
    const fetchExchangeTable = async () => {
      await fetchCurrencies();
      cards.value = exchangeTable.value[0]?.rates;
      prevCards.value=exchangeTable.value[1]?.rates;
    };

    fetchExchangeTable();
    
    return {
      cards,
      prevCards
    };
  }
};
</script>